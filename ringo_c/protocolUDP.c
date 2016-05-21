#include "protocolUDP.h"

//ecoute sur le port UDP de l'entité
int serverUDP(entity * ent){
  int sock;
  if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
    fprintf(stderr,"Erreur lors de la creation de la socket\n");
    return -1;      
  }
  struct sockaddr_in address_sock;
  address_sock.sin_family = AF_INET;
  address_sock.sin_port = htons(atoi(ent->port_udp));
  address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
  int r = bind(sock,(struct sockaddr *)&address_sock,
	       sizeof(struct sockaddr_in));
  if(r == 0){
    char mess[M_SIZE_MAX];
    char * type;
    while(1){
      int rec = recv(sock,mess,M_SIZE_MAX,0);
      mess[rec] = '\0';
      printf("SERVER UDP - Message recu : %s\n",mess);
      char mess_c[M_SIZE_MAX];
      strcpy(mess_c,mess);
      type = strtok(mess," ");
      if(strcmp(type,"WHOS") == 0){
	char * idm = malloc(sizeof(char)*8);
	strncpy(idm,strtok(NULL," "),8);
	envoiUDP(ent,whos(idm));
	free(idm);
      }else if(strcmp(type,"MEMB") == 0){
	//TODO
      }else if(strcmp(type,"GBYE") == 0){
	char * idm = malloc(sizeof(char)*8);
	strncpy(idm,strtok(NULL," "),8);
	char * ip = malloc(sizeof(char)*15);
	strncpy(ip,strtok(NULL," "),15);
	char * port = malloc(sizeof(char)*4);
	strncpy(port,strtok(NULL," "),8);
	char * ip_succ = malloc(sizeof(char)*15);
	strncpy(ip_succ,strtok(NULL," "),15);
	char * port_succ = malloc(sizeof(char)*4);
	strncpy(port_succ,strtok(NULL," "),8);
	if(strcmp(ent->port_udp,port_succ) == 0 && strcmp(getIp(),ip_succ) == 0){
	  envoiUDP(ent,eybg(getIdm()));
	}else{
	  printf("mess -%s-\n",mess_c);
	  envoiUDP(ent,mess_c);
	}
      }
    }
  }
  return 0;
}

//envoi un message en UDP
int envoiUDP(entity * ent, char * mess){ 
  int sock;
  if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
    fprintf(stderr,"Erreur lors de la creation de la socket\n");
    return -1;      
  }
  struct addrinfo * first_info;
  
  if(getaddrinfo(ent->ip_next,ent->port_udp_next,NULL,&first_info) == 0){
    if(first_info!=NULL){
      struct sockaddr *saddr=first_info->ai_addr;
      sendto(sock,mess,strlen(mess),0,saddr,sizeof(struct sockaddr_in));
    }
  }else{
    fprintf(stderr,"Erreur lors de getaddrinfo\n");
    close(sock);
    return -1;
  }
  close(sock);
  
  return 0;
}
