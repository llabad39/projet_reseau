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
    //creation de la liste de idm
    struct lidm * l = NULL;
    l = malloc(sizeof(struct lidm));
    l->idm = "00000000\0";
    l->next = NULL;
    while(1){
      char * idm = malloc(sizeof(char)*9);
      memset(idm,0,(size_t)sizeof(idm));
      int rec = recv(sock,mess,M_SIZE_MAX,0);
      mess[rec] = '\0';
      char mess_c[M_SIZE_MAX];
      strcpy(mess_c,mess);
      type = strtok(mess," ");
      strncpy(idm,strtok(NULL," "),9);
      memset(&idm[8],0,1);
      //on verifie si on a pas deja vu le message
      if(contains(l,idm) == 0){
	printf("SERVER UDP - Message recu : %s\n",mess_c);
	add(l,idm);
	if(strcmp(type,"WHOS") == 0){
	  //on transmet le message
	  envoiUDP(ent,mess_c);
	  //on envoi le message MEMB
	  envoiUDP(ent,memb(getIdm(),ent->id,getIp(),ent->port_udp));
	}else if(strcmp(type,"GBYE") == 0){
	  char * ip = malloc(sizeof(char)*15);
	  strncpy(ip,strtok(NULL," "),15);
	  char * port = malloc(sizeof(char)*4);
	  strncpy(port,strtok(NULL," "),8);
	  char * ip_succ = malloc(sizeof(char)*15);
	  strncpy(ip_succ,strtok(NULL," "),15);
	  char * port_succ = malloc(sizeof(char)*4);
	  strncpy(port_succ,strtok(NULL," "),8);
	  if(strcmp(ent->port_udp_next,port) == 0 && strcmp(getIp(),ip) == 0){
	    printf("avant : ip -%s- port -%s- \n",ent->ip_next,ent->port_udp_next);
	    envoiUDP(ent,eybg(getIdm()));
	    printf("apres : ip -%s- port -%s- \n",ent->ip_next,ent->port_udp_next);
	    strncpy(ent->ip_next,ip_succ,15);
	    strncpy(ent->port_udp_next,port_succ,4);
	    getInfo(*ent);

	  }else{
	    printf("mess -%s-\n",mess_c);
	    envoiUDP(ent,mess_c);
	  }
	}else if (strcmp(type,"EYBG") == 0){
	  printf("Vous avez quitter l'anneau ! \n");
	  freeEntity(ent);
	  getInfo(*ent);
	  return 0;
	}else{
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
    printf("envoi : ip -%s- port -%s- \n",ent->ip_next,ent->port_udp_next);
    fprintf(stderr,"Erreur lors de getaddrinfo\n");
    close(sock);
    return -1;
  }
  close(sock);
  
  return 0;
}
