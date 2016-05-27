#include "protocolMulticast.h"

//ecoute sur le port UDP de l'entité
int serverMulticast(entity *ent, int i){
  int sock  = socket(PF_INET, SOCK_DGRAM,0);
  if(sock == -1){
    fprintf(stderr,"Erreur lors de la creation de la socket\n");
    return -1;      
  }
  int ok = 1;
  //ATTENTION : pour les salles de TP mettre SO_REUSEADDR au lieu de SO_REUSEPORT
  //if(setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&ok,sizeof(ok)) == -1){
  if(setsockopt(sock,SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok)) == -1){
    //fprintf(stderr,"Multicast-Erreur: setsockopt SO_REUSEPORT\n");
    perror("Multicast-Erreur (setsockopt)");
    close(sock);
    return -1;
  }
  struct sockaddr_in address_sock;
  address_sock.sin_family = AF_INET;
  if(i == 1){
    address_sock.sin_port = htons(atoi(ent->port_diff));
  }else if(i == 2){
    address_sock.sin_port = htons(atoi(ent->port_diff2));
  }
  address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
  if(bind(sock,(struct sockaddr *)&address_sock,
	  sizeof(struct sockaddr_in)) == -1){
    //fprintf(stderr,"Multicast-Erreur: bind\n");
    perror("Multicast-Erreur (bind)");
    close(sock);
    return -1;
  }
  struct ip_mreq mreq;
  if(i == 1){
    printf("1\n");
    mreq.imr_multiaddr.s_addr = inet_addr(ent->ip_diff);
  }else if(i == 2){
    printf("2 -%s-\n",ent->ip_diff2);
    mreq.imr_multiaddr.s_addr = inet_addr(ent->ip_diff2);
  }
  mreq.imr_interface.s_addr = htonl(INADDR_ANY);
  if(setsockopt(sock,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq)) == -1){
    //fprintf(stderr,"ServerMulticast-Erreur: setsockopt IP_ADD_MEMBERSHIP\n");
    perror("ServerMulticast-Erreur (setsockopt)");   
    close(sock);
    return -1;
  }
  char mess[M_SIZE_MAX];
  while(1){
    if(recv(sock,mess,M_SIZE_MAX,0) == -1){
      //fprintf(stderr,"ServerMulticast-Erreur: recv\n");
      perror("ServerMulticast-Erreur (recv)");
      close(sock);
      return -1;
    }
    if(strcmp(mess,"DOWN")){
      envoiMulticast(ent,i,mess);
      freeEntity(ent);
      printf("\nServerMulticast-MessageRecu: %s",mess);
      printf("\n\t-----L'anneau est casser-----\n\n");
      exit(0);
    }else{
      //TODO ce n'est pas le message DOWN qui est recu 
    }
  }
  return 0;
}

//envoi un message en Multicast
int envoiMulticast(entity * ent, int i, char * mess){ 
  printf("envoi2\n");
  int sock;
  if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
    //fprintf(stderr,"EnvoiMulticast-Erreur: socket\n");
    perror("EnvoiMulticast-Erreur (socket)");
    return -1;      
  }
  struct addrinfo * first_info;
  int r;
  if(i == 1){
    r = getaddrinfo(ent->ip_diff,ent->port_diff,NULL,&first_info);
  }else if(i == 2){
    r = getaddrinfo(ent->ip_diff2,ent->port_diff2,NULL,&first_info);
  }
  if(r == 0){
    if(first_info!=NULL){
      struct sockaddr *saddr=first_info->ai_addr;
      printf("multi mess envoi : -%s-\n",mess);
      sendto(sock,mess,strlen(mess),0,saddr,(socklen_t)sizeof(struct sockaddr_in));
    }else{
      printf("EnvoiMulticast-Erreur: firt_info == NULL\n");
    }
  }else{
    fprintf(stderr,"EnvoiUDP-Erreur: getaddrinfo\n");
    close(sock);
    return -1;
  }
  close(sock);
  return 0;
}
