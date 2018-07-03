#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <semaphore.h>
#include <signal.h>

#include "dx/dxudefs.h"


static void *map; // pointer to shared memory mapping


int main( int argc, char *argv[], char *env[])
{
	int fd;

	printf("Hello, lcdtask!\n"); /* for testing */

	/* TBD: hardware/driver initialization */

	/* open shared memory object and map to this processes address space */
	fd = shm_open(DX_SHM_NAME, O_RDWR, 0);
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


	while(1)
	{
		// loop forever
		sleep(1); /* TBD: placeholder until initial implementation is finished */
	}


	return 0;
}
