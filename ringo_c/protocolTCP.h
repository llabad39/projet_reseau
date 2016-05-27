#define M_SIZE_MAX 512        /***< maximum size for a message (octets) */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <pthread.h>

#include "utils.h"
#include "protocolMulticast.h"

int serverTCP(entity * ent);

int connectTCP(char *ip_dest, int port_dest,entity * ent);

int connectTCPDupl(char *ip_dest, int port_dest,entity * ent);
