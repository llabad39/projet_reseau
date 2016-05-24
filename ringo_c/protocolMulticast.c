#include "protocolMulticast.h"

//ecoute sur le port UDP de l'entité
int serverMulticast(char * ip, char * port){
  int sock  = socket(PF_INET, SOCK_DGRAM,0);
  if(sock == -1){
    fprintf(stderr,"Erreur lors de la creation de la socket\n");
    return -1;      
  }
  int ok = 1;
  //ATTENTION : pour les salles de TP mettre SO_REUSEADDR au lieu de SO_REUSEPORT
  //if(setsockopt(sock,SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok)) == -1){
  if(setsockopt(sock,SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok)) == -1){
    fprintf(stderr,"Multicast-Erreur: setsockopt SO_REUSEPORT\n");
    close(sock);
    return -1;
  }
  struct sockaddr_in address_sock;
  address_sock.sin_family = AF_INET;
  address_sock.sin_port = htons(atoi(port));
  address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
  if(bind(sock,(struct sockaddr *)&address_sock,
	  sizeof(struct sockaddr_in)) == -1){
    fprintf(stderr,"Multicast-Erreur: bind\n");
    close(sock);
    return -1;
  }
  struct ip_mreq mreq;
  printf("ip diff -%s-\n",ip);
  printf("port diff -%s-\n",port);
  mreq.imr_multiaddr.s_addr = inet_addr(ip);
  mreq.imr_interface.s_addr = htonl(INADDR_ANY);
  if(setsockopt(sock,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq)) == -1){
    fprintf(stderr,"ServerMulticast-Erreur: setsockopt IP_ADD_MEMBERSHIP\n");
    perror("Erreur :");
    return -1;
  }
  char mess[M_SIZE_MAX];
  while(1){
    if(recv(sock,mess,M_SIZE_MAX,0) == -1){
    fprintf(stderr,"ServerMulticast-Erreur: recv\n");
    close(sock);
    return -1;
    }
    if(strcmp(mess,"DOWN")){
      printf("envoi1\n");
      return envoiMulticast(ip,port,mess);
    }else{
      //TODO ce n'est pas le message DOWN qui est recu 
    }
  }
  return 0;
}

//envoi un message en Multicast
int envoiMulticast(char * ip, char * port, char * mess){ 
  printf("envoi2\n");
  int sock;
  if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
    fprintf(stderr,"envoiMulticast-Erreur: socket\n");
    return -1;      
  }
  struct addrinfo * first_info;
  if(getaddrinfo(ip,port,NULL,&first_info) == 0){
    if(first_info!=NULL){
      struct sockaddr *saddr=first_info->ai_addr;
      sendto(sock,mess,strlen(mess),0,saddr,sizeof(struct sockaddr_in));
    }
  }else{
    fprintf(stderr,"EnvoiUDP-Erreur: getaddrinfo\n");
    close(sock);
    return -1;
  }
  close(sock);
  
  return 0;
}
