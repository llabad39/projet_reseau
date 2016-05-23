#include "utils.h"
#include <stdlib.h>
#include <string.h>

/*
entity * createEntity(char * _ip_diff, char * _port_diff,char * _port_tcp,char * _port_udp){

  entity *ent = (entity *)malloc(sizeof(entity));
  strncpy(ent->port_tcp,_port_tcp,4);
  strncpy(ent->port_udp,_port_udp,4);
  strncpy(ent->port_udp_next,_port_udp,4);
  strncpy(ent->ip_diff,_ip_diff,15);
  strncpy(ent->port_diff,_port_diff,4);
  strncpy(ent->ip_next,getIp(),15);

  return ent;
}
*/

void freeEntity(entity * ent){
  memset(ent->id,0,1);
  memset(ent->port_tcp,0,1);
  memset(ent->port_udp,0,1);
  memset(ent->port_udp_next,0,1);
  memset(ent->ip_diff,0,1);
  memset(ent->port_diff,0,1);
  memset(ent->ip_next,0,1);
}
