#define M_SIZE_MAX 512        /***< maximum size for a message (octets) */

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

/**
 * Linked list to id message (idm)
 */
typedef struct lidm lidm;
struct lidm {
  char * idm;
  struct lidm *next;
};


char * whos(char * idm);
char * memb(char * idm, char * id, char * ip, char * port);
char * gbye(char * idm, char * ip, char * port, char * ip_succ,char * port_succ);
char * eybg(char * idm);
char * test(char * idm, char * ip_diff, char * port_diff);
void add (lidm * l, char * idm);
int contains (lidm * l , char * idm);
void show (lidm * l);
