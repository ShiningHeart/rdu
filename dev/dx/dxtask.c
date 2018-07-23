/*H**********************************************************
* FILENAME :          dxtask.c
*
* DESCRIPTION :
*       Main DX task source file.
*
* NOTES :
*       > DX module initiates data transfers between the RDU and the Vehicle Control Unit (VCU)
*       > Communication with the VCU is supported via CAN interface
*       > DX creates shared memory object to write VCU data and make it available to other processes
*       > Sequence:
*            - Initialization (hardware + shared memory + semaphore initialization)
*            - Timer configuration (configure interval timer to trigger periodic callback function
*              execution in a separate thread)
*            - fork() + execve() to start other processes if initialization is successful
*            - superloop (inside the loop, call wait() to monitor child process state changes; if
*              a child process terminates, for example, it can be restarted by DX dynamically)
*
*       Copyright (c) 2018 Team Phantom
*
* AUTHOR :     Milos Lazic               START DATE : 24 May 2018
*
*H*/

#define _XOPEN_SOURCE 500

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <signal.h>
#include <time.h>

#include "dxidefs.h"
#include "dxudefs.h"
#include "dxproto.h"
#include "dxcan.h"

/* ===== global variables ===== */

static void *map;   /* pointer to shared memory mapping */
static timer_t hbeatTimer; /* periodic timer */

/* period of the hbeatTimer */
static struct itimerspec hbeatPeriod = {
	.it_interval.tv_sec = 0,
	.it_interval.tv_nsec = 0,
	.it_value.tv_sec = 1,  /* TBD: period of DX heartbeat should be LCM of period of other applications */
	.it_value.tv_nsec = 0,
};

/* array of structures containg path to module exectables and default PIDs */
static 	struct dxchild_t childlist[] =
{
	{ "../lcd/lcdapp", -1 },
	{ "../log/logapp", -1 },
};
#define NUM_CHILD_PROCESSES (sizeof(childlist)/sizeof(childlist[0]))




static void dxSigIntHandler(int signo)
{
	int err;

	//fprintf(stdout, "\nSignal recvd: %d\n", signo);

#if 0 // not needed, SIGINT automatically delivered to child processes
	// kill all child processes that are currently executing
	for( int i = 0; i < NUM_CHILD_PROCESSES; i++)
	{
		if ( childlist[i].childPID != -1)
		{
			err = kill( childlist[i].childPID, SIGTERM);
			if( err == -1)
			{
				// handle error
				fprintf(stderr, "kill() failed [%s]: %s\n", childlist[i].fname, strerror(errno));
			}
		}
	}
#endif


	/* TBD: destroy timer with timer_delete? */


	// destroy semaphore store in shared memory
	err = sem_destroy( &((dxshmem_t *)map)->sem);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "sem_destroy failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// unmap shared memory
	err = munmap( map, sizeof(dxshmem_t));
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "munmap failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}


	// destroy shared memory object
	err = shm_unlink( DX_SHM_NAME);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "shm_unlink failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}


	exit(EXIT_SUCCESS);
}


static void dxhbeat( union sigval args)
{
	int err;

	/* enter critical section */
	err = sem_wait( &((dxshmem_t *)map)->sem);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "sem_wait failed: %s\n", strerror(errno));
	}
	else
	{

		/*================================================================*/
		/*===============*/ /* TBD: get data from VCU */ /*===============*/
		/*================================================================*/

		err = fxCanDataTx();
		if ( err == -1){
			// handle error
			fprintf(stderr, "CAN data tx failed: %s\n", strerror(errno));
		}
		else
		{
			// only execute this if fxCanDataTx succeeded otherwise it will hangup on select()
			// subroutine call
			err = fxCanDataRx( (dxshmem_t *)map );
			if ( err == -1){
				// handle error
				fprintf(stderr, "CAN data rx failed: %s\n", strerror(errno));
			}
		}


		fprintf(stderr,"Throttle Input: %0.3f \n",*(&((dxshmem_t *)map)->throttleInput_V));
		fprintf(stderr,"Battery Voltage: %0.2f\n",*(&((dxshmem_t *)map)->batteryVoltage_V));
		fprintf(stderr,"Battery Current: %0.2f\n",*(&((dxshmem_t *)map)->batteryDischargeCurrent_A));
		fprintf(stderr,"State: %d \n",*(&((dxshmem_t *)map)->state));
		fprintf(stderr,"Enable Signal: %d \n",*(&((dxshmem_t *)map)->enableSignal));
		fprintf(stderr,"Run Signal: %d \n",*(&((dxshmem_t *)map)->runSignal));

		/* leave critical section */
		sem_post( &((dxshmem_t *)map)->sem);
	}


	// reload the timer (ONE SHOT MODE)
	err = timer_settime( hbeatTimer, 0, &hbeatPeriod, NULL);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "timer_settime failed; %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}
}



int main( int argc, char *argv[])
{

	int fd, err, waitstatus;
	struct sigevent sev;
	pid_t pid;
	struct sigaction onSIGINT;
	char *chargv[] = { NULL };
	char *chenvp[] = { NULL };



	/* TBD: hardware/driver initialization */

	err = fxCanSocketInit();
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "CAN socket init failed: %s\n", strerror(errno));
	}


	/* TBD: shared memory creation and initialization subroutine call */	
	// delete existing shared memory object (if it was previously created)
	err = shm_unlink( DX_SHM_NAME);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "shm_unlink failed: %s\n", strerror(errno));
		// NOTE: it is OK if this call fails, it just means that the 
		//       shared memory object DX_SHM_NAME did not exist previously
		//       and will be created
	}

	// create (or open if it already exists) a POSIX shared memory object
	fd = shm_open( DX_SHM_NAME, O_RDWR | O_CREAT | O_TRUNC, S_IRWXU | S_IRWXG);
	if ( fd == -1)
	{
		// handle error
		fprintf(stderr, "shm_open failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// truncate shared memory object to size required to store VCU data buffer
	err = ftruncate( fd, sizeof(dxshmem_t));
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "ftruncate failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// map shared memory object into DX's address space
	map = mmap( NULL, sizeof(dxshmem_t), PROT_READ | PROT_WRITE,
		MAP_SHARED, fd, 0);
	if ( map == MAP_FAILED)
	{
		// handle error
		fprintf(stderr, "mmap failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// file descriptor to shared memeory object no longer required so close it
	close(fd);

	// initialize semaphore for shared memory access sychronization
	// semaphore is unnamed and embedded in shared memory at offset 0
	err = sem_init( &((dxshmem_t *)map)->sem, 1, 1);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "sem_init failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

#if 1
	// set SIGINT disposition (Ctrl+c keyboard interrupt)
	memset( &onSIGINT, 0, sizeof(struct sigaction));
	onSIGINT.sa_handler = dxSigIntHandler;
	err = sigaction( SIGINT, &onSIGINT, NULL);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "sigaction failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}
#else

#endif


	/* TBD: create and arm (start) periodic timer */
	// NOTE: child processes DO NOT inhert timers from parent */
	memset( &sev, 0, sizeof(sev)); // clear sigevent structure
	sev.sigev_notify = SIGEV_THREAD;
	sev.sigev_notify_function = dxhbeat;

	// creates timer
	err = timer_create( CLOCK_REALTIME, &sev, &hbeatTimer);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "timer_create failed: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}

	// start the timer (ONE SHOT MODE)
	err = timer_settime( hbeatTimer, 0, &hbeatPeriod, NULL);
	if ( err == -1)
	{
		// handle error
		fprintf(stderr, "timer_settime failed; %s\n", strerror(errno));
		exit(EXIT_FAILURE);

	}



	/* TBD: fork() + execve(); launch other RDU processes */
	for( int i = 0; i < (sizeof(childlist)/sizeof(childlist[0])); i++)
	{

		pid = fork();
		if ( pid == 0) // this is the child./dx
		{
			err = execve( childlist[i].fname, chargv, chenvp);
			if ( err == -1)
			{
				// handle error
				fprintf(stderr, "execve failed: %s\n", strerror(errno));
			}
		}
		else // this is the parent
		{
			childlist[i].childPID = pid;
		}
	}



	while(1)
	{
		/* loop forever */

		/* TBD: call wait() to monitor state changes in child processes */
		pid = wait( &waitstatus);
		// process (DX) does not have unwaited-for children
		if ( pid == -1 && errno == ECHILD)
		{
			/* TBD: maybe program should terminate here? */
			continue;
		}
		else
		{
			/* TBD: only do this if the child process was terminated normally */
			printf("child process (%d) terminated with exit code (%d)\n", pid, waitstatus);
			for( int i = 0; i < NUM_CHILD_PROCESSES; i++)
			{
				if (childlist[i].childPID == pid)
					childlist[i].childPID = -1;
			}
		}
	}
}
