#define _POSIX_C_SOURCE 200112L

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/socket.h>
#include <netdb.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <semaphore.h>
#include <arpa/inet.h>
#include <signal.h>
#include <time.h>

#include <libxml/encoding.h>
#include <libxml/xmlwriter.h>

#include "logidefs.h"
#include "dx/dxudefs.h"

#define INET_ADDRLEN_MAX  32
#define MAX_CLIENTS 5


static void *map; // pointer to shared memory mapping
static int   servfd; // server socker descriptor
static timer_t hbeatTimer; /* periodic timer */

/* period of hbeatTimer */
static struct itimerspec hbeatPeriod = {
	.it_interval.tv_sec = 0,
	.it_interval.tv_nsec = 0,
	.it_value.tv_sec = 1,
	.it_value.tv_nsec = 0,
};

static xmlDocPtr doc = NULL;
static xmlNodePtr rootNode = NULL;


void *get_in_addr( struct sockaddr *sa)
{
	if ( sa->sa_family == AF_INET)
	{
		return &(((struct sockaddr_in *)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}



static void loghbeat( union sigval unused)
{
	int err;
	xmlNodePtr entryNode = NULL;
	time_t secSinceEpoch;
	struct tm timeStamp;
	char timeStampBuf[64];
	char dataBuf[32];


	/* enter critical section */
	err = sem_wait( &((dxshmem_t *)map)->sem);
	if ( err == -1)
	{
		// unable to get semaphore
		fprintf(stderr, "sem_wait failed: %s\n", strerror(errno));
	}
	else
	{
		// generate time stamp
		time( &secSinceEpoch);
		(void) localtime_r( &secSinceEpoch, &timeStamp);
		(void) asctime_r( &timeStamp, timeStampBuf);
		*(strchr(timeStampBuf, '\n')) = '\0';

		/* TBD: periodic work */
		entryNode = xmlNewNode( NULL, BAD_CAST "data");
		if ( entryNode != NULL) // if successful
		{
			xmlSetProp( entryNode, BAD_CAST "time", BAD_CAST timeStampBuf);
			xmlAddChild( rootNode, entryNode);

			snprintf( dataBuf, sizeof(dataBuf)-1, "%f", ((dxshmem_t *)map)->throttleInput_V);
			xmlNewChild( entryNode, NULL, BAD_CAST "ThrotInVolt", BAD_CAST dataBuf);

				snprintf( dataBuf, sizeof(dataBuf)-1, "%f", ((dxshmem_t *)map)->batteryVoltage_V);
			xmlNewChild( entryNode, NULL, BAD_CAST "BattVolt", BAD_CAST dataBuf);

			xmlSaveFormatFileEnc( "LOG.xml", doc, "UTF-8", 1);
		}


		/* leave critical section */
		sem_post( &((dxshmem_t *)map)->sem);
	}

	// reload the timer (ONE SHOT MODE)
	err = timer_settime( hbeatTimer, 0, &hbeatPeriod, NULL);
	if ( err == -1)
	{
		// failed to reload timer
		fprintf(stderr, "timer_settime failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}
}



int main( int argc, char *argv[], char *envp[])
{
	int fd, err;
	struct addrinfo hints, *res;
	client_t client_list[MAX_CLIENTS];
	fd_set readfds;
	int maxfd, peersd;
	struct sockaddr_storage peeraddr;
	socklen_t peeraddrlen;
	struct timeval tv;
	int yes = 1;

	struct sigevent sev;
	time_t secSinceEpoch;
	struct tm timeStamp;
	char timeStampBuf[128];

	printf("Hello, logtask!\n");

	/* open shared memory object and map to this process's address space */
	fd = shm_open( DX_SHM_NAME, O_RDWR, 0);
	if ( fd == -1)
	{
		// handle error
		fprintf(stderr, "shm_open failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	map = mmap( NULL, sizeof(dxshmem_t), PROT_WRITE | PROT_READ, MAP_SHARED, fd, 0);
	if ( map == MAP_FAILED)
	{
		// handle error
		fprintf(stderr, "mmap failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// if mmap was successful we can close the descriptor to the shared memory object
	close(fd);


	/* create a new XML log file */
	doc = xmlNewDoc(BAD_CAST "1.0");
	rootNode = xmlNewNode( NULL, BAD_CAST "RDU_log");

	time( &secSinceEpoch);
	(void) localtime_r( &secSinceEpoch, &timeStamp);
	(void) asctime_r( &timeStamp, timeStampBuf);
	*(strchr(timeStampBuf, '\n')) = '\0';

	xmlSetProp( rootNode, BAD_CAST "start_time", BAD_CAST timeStampBuf);
	xmlDocSetRootElement( doc, rootNode);


	/* create and arm (start) one shot timer to trigger loghbeat */
	memset( &sev, 0, sizeof(sev));
	sev.sigev_notify = SIGEV_THREAD;
	sev.sigev_notify_function = loghbeat;

	// create timer
	err = timer_create( CLOCK_REALTIME, &sev, &hbeatTimer);
	if ( err == -1)
	{
		// failed to create timer
		fprintf(stderr, "timer_create failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// start timer (ONE SHOT MODE)
	err = timer_settime( hbeatTimer, 0, &hbeatPeriod, NULL);
	if ( err == -1)
	{
		// failed to start timer
		fprintf(stderr, "timer_settime failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}


	/* server socket setup */

	// clear the hints struct
	memset( &hints, 0, sizeof(hints));
	hints.ai_family = AF_UNSPEC; // any address family OK
	hints.ai_socktype = SOCK_STREAM; // TCP socket
	hints.ai_flags = AI_PASSIVE; // mark socket as passice; accept connections

	/* TBD: replace hard-coded port num */
	err = getaddrinfo( NULL, "3490", &hints, &res);
	if ( err != 0) // getaddrinfo failed
	{
		fprintf(stderr, "getaddrinfo failed: %s\n", gai_strerror(err));
		exit(EXIT_FAILURE);
	}
	
	// create server socket
	while( res) // try all addrinfo structs created by getaddrinfo
	{
		servfd = socket( res->ai_family, res->ai_socktype, res->ai_protocol);
		if ( servfd == -1)
		{
			// handle error
			res = res->ai_next; // try next struct
			continue;
		}


		// set socket option: reuse local addresses for AF_INET sockets
		err = setsockopt( servfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int));
		if ( err == -1)
		{
			// handle error
			close( servfd);
			exit( EXIT_FAILURE);
		}

		// bind socket to address
		err = bind( servfd, res->ai_addr, res->ai_addrlen);
		if ( err == -1)
		{
			// handle error
			close( servfd);
			res = res->ai_next;
			continue;
		}

		break;
	}

	// if server socket was not created
	if ( res == NULL)
	{
		fprintf(stderr, "socket failed: %s\n", strerror(errno));
		/* TBD: might be OK to continue without this */
		exit(EXIT_FAILURE);
	}


	freeaddrinfo( res);


	// zero the client list array
	memset( &client_list[0], 0, sizeof(client_list));


	/* TBD: replace hard-coded 'backlog' value (second argument) */
	// mark socket as passive
	err = listen( servfd, MAX_CLIENTS);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "listen failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}


	while(1)
	{
		// clear socket set to be monitored by select()
		FD_ZERO( &readfds);

		// add server socket to socket set
		FD_SET( servfd, &readfds);
		maxfd = servfd;

		// add client sockets to set (if active)
		for (int i = 0; i < MAX_CLIENTS; i++)
		{
			if ( client_list[i].clientfd == 0) // IF not active
				continue;

			FD_SET( client_list[i].clientfd, &readfds);

			if ( client_list[i].clientfd > maxfd)
				maxfd = client_list[i].clientfd;
		}

		/* TBD: replace hardcoded interval values */
		tv.tv_sec = 5;
		tv.tv_usec = 0;
		// wait for a socket to become I/O ready
		err = select( maxfd + 1, &readfds, NULL, NULL, &tv);
		if ( err == -1 && err != EINTR)
		{
			// handle error
			fprintf(stderr, "select failed: %s\n", strerror(errno));
			continue;
		}


		// check for i/o on server socket
		if ( FD_ISSET( servfd, &readfds))
		{
			peeraddrlen = sizeof(peeraddr);
			peersd = accept( servfd, &peeraddr, &peeraddrlen);
			if ( peersd == -1)
			{
				// handle error
				fprintf(stderr, "accept failed: %s\n", strerror(errno));
				exit(EXIT_FAILURE);
			}


			/* TBD: handle case where client list is full and connection is recv'd */
			// add new peer socket to client list
			for (int i = 0; i < MAX_CLIENTS; i++)
			{
				if ( client_list[i].clientfd == 0)
				{
					client_list[i].clientfd = peersd;

					fprintf(stdout, "[%s][socket %d]: CONNECTION ACCEPTED\n", 
						inet_ntop(peeraddr.ss_family, 
							      get_in_addr( (struct sockaddr *)&peeraddr), 
						    	  client_list[i].clientIP, 
						      	  INET6_ADDRSTRLEN), 
								  peersd);
					
					break;
				}

			}


		}

#if 1 /* TBD: define variables at start of function */
		char readbuf[128];
		ssize_t nread;
#endif

		// check for i/o on client sockets
		for( int i = 0; i < MAX_CLIENTS; i++)
		{
			if ( FD_ISSET( client_list[i].clientfd, &readfds))
			{
				/* TBD: do something */
				nread = recv( client_list[i].clientfd, readbuf, sizeof(readbuf) - 1, 0);
				if ( nread <= 0)
				{
					// error or connection closed
					fprintf(stdout, "[%s][socket %d]: CONNECTION CLOSED\n", client_list[i].clientIP, client_list[i].clientfd);
					close( client_list[i].clientfd);
					FD_CLR( client_list[i].clientfd, &readfds);
					client_list[i].clientfd = 0;
				}
				else
				{
					// strip the newline character (formatting)
					*( strchr( readbuf, '\n')) = '\0';

					fprintf(stdout, "[%s][socket %d]: MESSAGE RECEIVED\n\t %s\n", 
						client_list[i].clientIP, 
						client_list[i].clientfd,
						readbuf);
				}
			}
		}


	}

	return 0;
}



