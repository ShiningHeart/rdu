#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>


int main( int argc, char *argv[])
{
	pid_t pid;
	int status, err;
	char *dxargs[] = {};

	printf("Hello sandbox!\n");

	pid = fork();
	if ( pid == 0) // child process
	{
		err = execve("../dx/dx", dxargs, NULL);
		if ( err == -1)
		{
			printf("Error: execve(), %s\r\n", strerror(errno));
			exit(EXIT_FAILURE);
		}
	}

	waitpid( pid, &status, 0);

	printf("Goodbye sandbox\n");

	return 0;
}
