#ifndef HEADER_FILE
#define HEADER_FILE

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <net/if.h>
#include <sys/types.h>
#include <ifaddrs.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include "entity.h"

#endif

void askInfo(entity * ent);
void askInfoDiff(entity * ent);
char *  askIpDest();
char *  askPortDest();
char * itos(int i);
char * getIp();
void getInfo(entity ent);
void askGetInfo(entity ent);
