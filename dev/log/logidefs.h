/* TBD: add file header comment */

// DX module internal definitions
#ifndef _LOGIDEFS_H_
#define _LOGIDEFS_H_

#include <arpa/inet.h>
#include <sys/socket.h>
#include <netdb.h>



typedef struct client_t
{
	int         clientfd;            /* client socket file descriptor */
	char        clientIP[INET6_ADDRSTRLEN];            /* client IP address */
} client_t;


#endif
