#include "utils.h"

char * itos(int i){
  char * result = malloc(sizeof(char)*15);
  sprintf(result,"%d",i);
  return result;
}

char * getIp(){
  struct ifaddrs *myaddrs, *ifa;
  struct sockaddr_in *s4;
  int status;
  char *ip=(char *)malloc(64*sizeof(char));
  status = getifaddrs(&myaddrs);
  if (status != 0){
    perror("Probleme de recuperation d'adresse IP");
    exit(1);
  }
  for (ifa = myaddrs; ifa != NULL; ifa = ifa->ifa_next){
    if (ifa->ifa_addr == NULL) continue;
    if ((ifa->ifa_flags & IFF_UP) == 0) continue;
    if ((ifa->ifa_flags & IFF_LOOPBACK) != 0) continue;
    if (ifa->ifa_addr->sa_family == AF_INET){
      s4 = (struct sockaddr_in *)(ifa->ifa_addr);
      if (inet_ntop(ifa->ifa_addr->sa_family, (void *)&(s4->sin_addr),
		    ip, 64*sizeof(char)) != NULL){
	return ip;
      }
    }
  }
  freeifaddrs(myaddrs);
  return ip;
}

void getInfo(entity ent){
  if(ent.id != NULL){
    printf("id : %s\n",ent.id);
  }else{
    printf("id : NOP\n");
  }
  if(ent.port_udp != NULL){
    printf("port_udp : %s\n",ent.port_udp);
  }else{
    printf("port_udp : NOP\n");
  }
  if(ent.port_tcp != NULL){
    printf("port_tcp : %s\n",ent.port_tcp);
  }else{
    printf("port_tcp : NOP\n");
  }
  if(ent.ip_next != NULL){
    printf("ip_next : %s\n",ent.ip_next);
  }else{
    printf("ip_next : NOP\n");
  }
  if(ent.port_udp_next != NULL){
    printf("port_udp_next : %s\n",ent.port_udp_next);
  }else{
    printf("port_udp_next : NOP\n");
  }
  if(ent.ip_diff != NULL){
    printf("ip_diff : %s\n",ent.ip_diff);
  }else{
    printf("ip_diff : NOP\n");
  }
  if(ent.port_diff != NULL){
    printf("port_diff : %s\n",ent.port_diff);
  }else{
    printf("port_diff : NOP\n");
  }
  if(ent.ip_next2 != NULL){
    printf("ip_next2 : %s\n",ent.ip_next2);
  }else{
    printf("ip_next2 : NOP\n");
  }
  if(ent.port_udp_next2 != NULL){
    printf("port_udp_next2 : %s\n",ent.port_udp_next2);
  }else{
    printf("port_udp_next2 : NOP\n");
  }
}
