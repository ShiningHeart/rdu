/*H**********************************************************
* FILENAME :          dxcan.c
*
* DESCRIPTION :
*       CAN methods to write and read data to/from the RDU 
*
* NOTES :
*       
*
*       Copyright (c) 2018 Team Phantom
*
* AUTHOR :     Soroush Jafary               START DATE : 13 July 2018
*
*H*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>

#include <net/if.h>
#include <sys/ioctl.h>

#include <linux/can.h>
#include <linux/can/raw.h>
#include <linux/if.h>

#include "dxidefs.h"
#include "dxudefs.h"
#include "dxproto.h"
#include "lib.h"
#include "dxcan.h"


static int s;


int fxCanSocketInit(){

	struct sockaddr_can addr; //socket structure for the CAN address family 
	struct ifreq ifr; //low level access to linux CAN device 
	struct can_filter canFilter;



	canFilter.can_id = CAN_EFF_MASK;
	canFilter.can_mask = 0;
	/* 
	// open socket 
	// function shall create an unbound socket in a communications domain, and return a file descriptor 
	// CAN_RAW = Raw protocol of PF_CAN, applicable to socket type SOCK_RAW
	*/
	if ((s = socket(PF_CAN, SOCK_RAW, CAN_RAW)) < 0) {
		perror("Failed to open socket!");
		return -1;
	}
	// Set the family 
	addr.can_family = AF_CAN;

	strncpy(ifr.ifr_name, "can0", IFNAMSIZ);
	//strncpy(ifr.ifr_name,ifname,IFNAMSIZ);

	// SIOCGIFINDEX is an macro, which retrieve the interface index of the interface into the socket file 
	if (ioctl(s, SIOCGIFINDEX, &ifr) < 0) {
		perror("SIOCGIFINDEX");
		return -1;
	}
	// set the received index from the ioctrl 
	addr.can_ifindex = ifr.ifr_ifindex;

	/* disable default receive filter on this RAW socket */
	/* This is obsolete as we do not read from the socket at all, but for */
	/* this reason we can remove the receive list in the Kernel to save a */
	/* little (really a very little!) CPU usage.                          */
	setsockopt(s, SOL_CAN_RAW, CAN_RAW_FILTER, &canFilter, sizeof(canFilter));


	// bind socket 
	// The function shall assign a local socket address to a socket identified by descriptor socket 
	if (bind(s, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
		perror("Failed bind!");
		return -1;
	}

	return 0;

}


int fxCanDataTx(){

	int nbytes;
	struct can_frame frame; //central structure for receving and sending CAN frames 


	/* CAN message to be sent out */
	unsigned char buff[] = "00000002#0000000000000000";

	fprintf(stderr,"CAN testing\n");
	
	/* parse CAN frame */
	if (parse_canframe(buff, &frame)){
		fprintf(stderr, "\nWrong CAN-frame format!\n\n");
		return -1;
	}

		if ((nbytes = write(s, &frame, sizeof(frame))) != sizeof(frame)) {
			perror("write");
			return -1;
		}

	return 0;
}



int fxCanDataRx(dxshmem_t* VCUData){

	fd_set rdfs;	
	int err;
	int nbytes;
	struct can_frame frame; //central structure for receving and sending CAN frames 
	int i;
	int msgID;
	
	uint8_t inLSB_U8;
	uint8_t inMSB_U8;
	
	FD_ZERO(&rdfs);
	FD_SET(s,&rdfs);

	if ( (err = select(s+1,&rdfs,NULL,NULL,NULL)) < 0 ){
		perror("select");
		return -1;
	
	}

	
	if (FD_ISSET(s, &rdfs)) {
		nbytes = read(s, &frame, sizeof(frame));
	}

			

	if (nbytes < 0){
		fprintf(stderr, "\nCAN raw socket read!\n\n");
		return -1;
	}

	msgID = (frame.can_id & 0x1FFFFFFFu);
	//fprintf(stderr,"%d\n",msgID);

	// Throttle Input (scaling = 0.1 | offset = -5.0)
	VCUData->throttleInput_V = (frame.data[0] * 0.1) - 5.0;
	
	
	// Battery Voltage (scaling = 1.0 | offset = -500.0)
	inLSB_U8 = frame.data[1];
	inMSB_U8 = frame.data[2];
	VCUData->batteryVoltage_V = ((float)(inLSB_U8 + 256*inMSB_U8)) * 1.0 - 500.0;
	
	
	// Battery Current (scaling = 0.1 | offset = -500.0)
	inLSB_U8 = frame.data[3];
	inMSB_U8 = frame.data[4];
	VCUData->batteryDischargeCurrent_A = ((float)(inLSB_U8 + 256*inMSB_U8)) * 0.1 - 500.0;
	
	
	// State (scaling = 1 | offset = 0)
	VCUData->state = frame.data[5];


	// Enable (scaling = 1 | offset = 0)
	VCUData->enableSignal = frame.data[6];
	

	// Run (scaling = 1 | offset = 0)
	VCUData->runSignal = frame.data[7];
	

	fflush(stdout);
	

	return 0;

}

