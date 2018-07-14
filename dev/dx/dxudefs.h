/* TBD: add file header comment */

// DX module universal defintions
#ifndef _DXUDEFS_H_
#define _DXUDEFS_H_


#include <semaphore.h>


#define DX_SHM_NAME       "/dx-shm"


// structure used to cast shared memory object created by DX
// when it its locally mapped by a client process
typedef struct
{
	sem_t sem; // sempahore (must be acquired by process before read/write to shared memory)
	
	/* TBD: fields below will contain VCU sensor values */
	int val;
} dxshmem_t;



#endif