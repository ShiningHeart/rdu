/* TBD: add file header comment */

// DX module internal definitions
#ifndef _DXIDEFS_H_
#define _DXIDEFS_H_

#include <unistd.h>


#define BUF_MAX_LEN 32


struct dxchild_t
{
	const char fname[BUF_MAX_LEN];
	pid_t childPID;
};


#endif
