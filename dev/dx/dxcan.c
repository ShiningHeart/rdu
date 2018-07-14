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

#include <net/if.h>
#include <sys/ioctl.h>

#include <linux/can.h>
#include <linux/can/raw.h>

#include "lib.h"
#include "dxcan.h"


static int s;   


int fxCanSocketInit(){

	struct sockaddr_can addr; //socket structure for the CAN address family 
	struct ifreq ifr; //low level access to linux CAN device 

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

	strcpy(ifr.ifr_name, "can0");

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
	setsockopt(s, SOL_CAN_RAW, CAN_RAW_FILTER, NULL, 0);

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
	unsigned char buff[] = "7DF#0201050000000000";

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

