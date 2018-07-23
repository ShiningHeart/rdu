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

#include <libxml/encoding.h>
#include <libxml/xmlwriter.h>

int main( int argc, char *argv[])
{


	xmlDocPtr doc = NULL;
	xmlNodePtr rootNode = NULL;

	LIBXML_TEST_VERSION

	doc = xmlNewDoc(BAD_CAST "1.0");
	rootNode = xmlNewNode( NULL, BAD_CAST "rdu_log");
	xmlSetProp( rootNode, "date", "23/07/2018");
	xmlDocSetRootElement( doc, rootNode);


	xmlSaveFormatFileEnc("TEST.xml", doc, "UTF-8", 1);
	xmlFreeDoc(doc);

	// time_t secSinceEpoch;
	// struct tm bdTime;

	// time( &secSinceEpoch);
	// (void) localtime_r ( &secSinceEpoch, &bdTime);

	// printf("The date is: %s\n", asctime( &bdTime));


	// printf("Hello, World!\n");

	return 0;
}
