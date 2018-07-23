#define _POSIX_C_SOURCE 1

#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <time.h>

#include <libxml2/libxml/tree.h>

int main( int argc, char *argv[])
{

	time_t secSinceEpoch;
	struct tm bdTime;

	time( &secSinceEpoch);
	(void) localtime_r ( &secSinceEpoch, &bdTime);

	printf("The date is: %s\n", asctime( &bdTime));


	printf("Hello, World!\n");

	return 0;
}
