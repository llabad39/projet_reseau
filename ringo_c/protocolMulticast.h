#define M_SIZE_MAX 512        /***< maximum size for a message (octets) */


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <netdb.h>

#include "utils.h"


int serverMulticast(char * ip, char * port);

int envoiMulticast(char * ip, char * port, char * mess);
