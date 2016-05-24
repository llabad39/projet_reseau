#define M_SIZE_MAX 512        /***< maximum size for a message (octets) */

#include <netdb.h>
#include <sys/types.h>   
#include <stdio.h>
#include <string.h>
#include <pthread.h>


#include "protocolTCP.h"
#include "protocolUDP.h"
#include "protocolMulticast.h"


int r_create(entity *ent);
int r_connect(entity *ent);
int r_duplicate(entity *ent);
int r_quit_ring(entity *ent);
int r_help();
int r_c_help();
int r_command(entity * ent);
