#include "protocolRINGO.h"
#include <errno.h>
#include <pthread.h>


void * tcp(void * _ent){
  printf("thread tcp lancer\n");
  entity ent = *(entity *)_ent;
  serverTCP(ent);
  return(NULL);
}

void * udp(void * _ent){
  printf("thread udp lancer\n");
  entity ent = *(entity *)_ent;
  serverUDP(ent);
  return(NULL);
}

int r_create(entity *ent){
  printf("\n\t----------CREATE----------\n\n");

  askInfo(ent);

  //ip
  strcpy(ent->ip_next,getIp());

  askInfoDiff(ent);

  //lancement des serveurs TCP et UDP sur les port renseignes via des threads
  pthread_t th_tcp,th_udp;
  if(pthread_create(&th_tcp, NULL,tcp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverTCP\n");
    return -1;
  }
  if(pthread_create(&th_udp, NULL,udp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverUDP\n");
    return -1;
  }

  askGetInfo(*ent);

  printf("\t--------FIN CREATE--------\n\n");
  return 0;
}

int r_connect(entity *ent){
  printf("\n\t----------CONNECT----------\n\n");

  askInfo(ent);
  char * ip = malloc(sizeof(char)*16);
  askIpDest(ip);

  char * port_tcp_dest = malloc(sizeof(char)*5);
  askPortDest(port_tcp_dest);

  connectTCP(ip,atoi(port_tcp_dest),ent);
  
  //lancement des serveurs TCP et UDP sur les port renseignes via des threads
  pthread_t th_tcp,th_udp;
  if(pthread_create(&th_tcp, NULL,tcp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverTCP\n");
    return -1;
  }
  if(pthread_create(&th_udp, NULL,udp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverUDP\n");
    return -1;
  }

  askGetInfo(*ent);

  printf("\t--------FIN CONNECT--------\n\n");

  free(ip);
  free(port_tcp_dest);
  return 0;
}

int r_duplicate(entity *ent){ 
  printf("\n\t----------DUPLICATE----------\n\n");

  askInfo(ent);
  char * ip = malloc(sizeof(char)*16);
  askIpDest(ip);

  char * port_tcp_dest = malloc(sizeof(char)*5);
  askPortDest(port_tcp_dest);

  askInfoDiff(ent);

  connectTCPDupl(ip,atoi(port_tcp_dest),ent);

  //lancement des serveurs TCP et UDP sur les port renseignes via des threads
  pthread_t th_tcp,th_udp;
  if(pthread_create(&th_tcp, NULL,tcp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverTCP\n");
    return -1;
  }
  if(pthread_create(&th_udp, NULL,udp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverUDP\n");
    return -1;
  }
  
  askGetInfo(*ent);

  printf("\t--------FIN DUPLICATE--------\n\n");
  
  free(ip);
  free(port_tcp_dest);

  return 0;
}

int r_quit_ring(entity *ent){
  strcpy(ent->ip_next,"127.0.0.1");
  strcpy(ent->port_udp_next,"4242");
  envoiUDP(*ent,"yoyoy bla bla\n");
  return 0;
}

int r_help(){
  printf("\n%-10s : permet de creer un entité\n","create");
  printf("%-10s : permet de se connecter a une autre entité\n","connect");
  printf("%-10s : permet de demander a une entité de devenir doubleur\n","duplicate");
  printf("%-10s : permet de quitter l'anneau dans laquelle on se trouve\n","quit_ring");
  printf("%-10s : permet de quitter l'application RINGO\n\n","quit/q");
  return 0;
}
