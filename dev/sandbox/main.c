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
	time_t secSinceEpoch;
	struct tm dateTime;
	char buf[128];

	time( &secSinceEpoch);
	(void) localtime_r( &secSinceEpoch, &dateTime);
	(void) asctime_r( &dateTime, buf);

	*(strchr(buf, '\n')) = '\0';


	xmlDocPtr doc = NULL;
	xmlNodePtr rootNode = NULL;
	xmlNodePtr entry  = NULL;
	LIBXML_TEST_VERSION

	doc = xmlNewDoc(BAD_CAST "1.0");
	rootNode = xmlNewNode( NULL, BAD_CAST "RDU_log");
	xmlSetProp( rootNode, (xmlChar *)"start_time", BAD_CAST buf);
	xmlDocSetRootElement( doc, rootNode);

	entry = xmlNewNode( NULL, BAD_CAST "data");
	xmlSetProp( entry, BAD_CAST "time_stamp", BAD_CAST buf);
	xmlAddChild( rootNode, entry);

	xmlNewChild(entry, NULL, BAD_CAST "ThrottleInputVoltage", BAD_CAST "0.0");
	xmlNewChild(entry, NULL, BAD_CAST "BatteryVoltage", BAD_CAST "0.0");
	xmlNewChild(entry, NULL, BAD_CAST "BatteryDischargeCurrent", BAD_CAST "0.0");
	xmlNewChild(entry, NULL, BAD_CAST "State", BAD_CAST "UNKNOWN");
	xmlNewChild(entry, NULL, BAD_CAST "EnableSignal", BAD_CAST "UNKNOWN");
	xmlNewChild(entry, NULL, BAD_CAST "RunSignal", BAD_CAST "UNKNOWN");

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
