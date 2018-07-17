// DX module internal definitions
#ifndef _DXCAN_H_
#define _DXCAN_H_

#include <unistd.h>

int fxCanSocketInit();
int fxCanDataTx();
int fxCanDataRx();

#endif
