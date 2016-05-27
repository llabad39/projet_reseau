#include "protocolRINGO.h"
#include <errno.h>
#include <pthread.h>


void * tcp(void * _ent){
  entity * ent = (entity *)_ent;
  serverTCP(ent);
  return(NULL);
}

void * udp(void * _ent){
  entity * ent = (entity *)_ent;
  serverUDP(ent);
  return(NULL);
}

void * multicast(void * _ent){
  entity * ent = (entity *)_ent;
  serverMulticast(ent->ip_diff,ent->port_diff);
  return(NULL);
}

void * multicast2(void * _ent){
  entity * ent = (entity *)_ent;
  serverMulticast(ent->ip_diff2,ent->port_diff2);
  return(NULL);
}

int r_create(entity *ent){
  printf("\n\t----------CREATE----------\n\n");

  askInfo(ent);

  //ip
  strcpy(ent->ip_next,getIp());

  askInfoDiff(ent);

  //lancement des serveurs TCP et UDP sur les port renseignes via des threads
  pthread_t th_tcp,th_udp,th_multidiff;
  if(pthread_create(&th_tcp, NULL,tcp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverTCP\n");
    return -1;
  }
  if(pthread_create(&th_udp, NULL,udp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverUDP\n");
    return -1;
  }
  while(pthread_create(&th_multidiff, NULL,multicast,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-Multidiffusion\n");
    askInfoDiff(ent);
  }

  askGetInfo(*ent);

  printf("\n\t--------FIN CREATE--------\n\n");
  return r_command(ent);
}

//permet de se connecter a une 
int r_connect(entity *ent){
  printf("\n\t----------CONNECT----------\n\n");

  askInfo(ent);
  char * ip = malloc(sizeof(char)*16);
  askIpDest(ip);

  char * port_tcp_dest = malloc(sizeof(char)*5);
  askPortDest(port_tcp_dest);

  if(connectTCP(ip,atoi(port_tcp_dest),ent) == -1){
    fprintf(stderr,"r_connect-Erreur: connectTCP\n");
    return -1;
  }
  
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
  
  printf("\n\t--------FIN CONNECT--------\n\n");
  
  free(ip);
  free(port_tcp_dest);
  
  return r_command(ent);
}

//permet de demander a une entité de devenir doubleur, et s'inserer
int r_duplicate(entity *ent){ 
  printf("\n\t----------DUPLICATE----------\n\n");

  askInfo(ent);
  char * ip = malloc(sizeof(char)*16);
  askIpDest(ip);

  char * port_tcp_dest = malloc(sizeof(char)*5);
  askPortDest(port_tcp_dest);

  askInfoDiff(ent);

  connectTCPDupl(ip,atoi(port_tcp_dest),ent);

  //lancement des serveurs TCP,UDP et Multicast sur les port 
  //renseignes via des threads
  pthread_t th_tcp,th_udp,th_multidiff;
  if(pthread_create(&th_tcp, NULL,tcp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverTCP\n");
    return -1;
  }
  if(pthread_create(&th_udp, NULL,udp,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-serverUDP\n");
    return -1;
  }
  while(pthread_create(&th_multidiff, NULL,multicast2,ent) != 0){
    fprintf(stderr,"Probleme lors de la creation du thread-Multidiffusion\n");
    askInfoDiff(ent);
  }
  
  askGetInfo(*ent);

  printf("\n\t--------FIN DUPLICATE--------\n\n");
  
  free(ip);
  free(port_tcp_dest);

  return r_command(ent);
}

//permet de quitter un anneau
int r_quit_ring(entity *ent){ 
  envoiUDP(ent,gbye(getIdm(),getIp(),
		    ent->port_udp,ent->ip_next,ent->port_udp_next));
  sleep(1);
  return 0;
}

//affiche l'aide des commandes RINGO
int r_help(){
  printf("\n%-10s : permet de creer un entité\n","create");
  printf("%-10s : permet de se connecter a une autre entité\n","connect");
  printf("%-10s : permet de demander a une entité de devenir doubleur\n","duplicate");
  printf("%-10s : permet de quitter l'application RINGO\n\n","quit/q");
  return 0;
}

//affiche l'aide des commandes lorsqu'on est connecter
int r_c_help(){
  printf("\n%-10s : permet de savoir qui est sur l'anneau\n","whos/WHOS");
  printf("%-10s : permet de quitter l'anneau dans laquelle on se trouve\n","quit_ring/q");
  return 0;
}

//attend des commandes ringo
int r_command(entity * ent){
  while(1){
    char buff[M_SIZE_MAX];
    scanf("%s",buff);
    if(!strcmp(buff,"help") || !strcmp(buff,"--help")){
      r_c_help();
    }else if(!strcmp(buff,"whos") || !strcmp(buff,"WHOS")){
      envoiUDP(ent,whos(getIdm()));
    }else if(!strcmp(buff,"quit") || !strcmp(buff,"q") 
      || !strcmp(buff,"quit_ring")){
      return r_quit_ring(ent);
    }else{
      fprintf(stderr,"\n\tErreur d'utilisation de la commande ringo\n");
      r_c_help();
    }
  }
  return 0;
}
