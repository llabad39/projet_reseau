#include "protocolUDP.h"
#include "protocolMulticast.h"

//ecoute sur le port UDP de l'entité
int serverUDP(entity * ent){
  int sock = socket(PF_INET, SOCK_DGRAM,0);
  if(sock == -1){
    perror("ServeurUDP-Erreur (socket)");
    return -1;      
  }
  struct sockaddr_in address_sock;
  address_sock.sin_family = AF_INET;
  address_sock.sin_port = htons(atoi(ent->port_udp));
  address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
  if(bind(sock,(struct sockaddr *)&address_sock,
	  sizeof(struct sockaddr_in)) == -1){
    perror("ServeurUDP-Erreur (bind)");
    return -1;
  }
  char mess[M_SIZE_MAX];
  char * type;
  //creation de la liste de idm
  struct lidm * l = NULL;
  l = malloc(sizeof(struct lidm));
  l->idm = "00000000\0"; //initialisation
  l->next = NULL;
  while(1){
    char * idm = malloc(sizeof(char)*9);
    memset(idm,0,(size_t)sizeof(idm));
    int rec = recv(sock,mess,M_SIZE_MAX,0);
    mess[rec] = '\0';
    char mess_c[M_SIZE_MAX];
    strcpy(mess_c,mess);
    type = strtok(mess," ");
    char * tmp = strtok(NULL," ");
    if(tmp != NULL){
      strncpy(idm,tmp,9);
      memset(&idm[8],0,1);
      //on verifie si on a pas deja vu le message
      if(contains(l,idm) == 0){
	add(l,idm);
	if(strcmp(type,"WHOS") == 0){
	  //on transmet le message
	  envoiUDP(ent,mess_c,1);
	  //on envoi le message MEMB
	  envoiUDP(ent,memb(getIdm(),ent->id,getIp(),ent->port_udp),1);
	  
	  if(strcmp(ent->port_udp_next2,"") != 0){
	    envoiUDP(ent,mess_c,2);
	  }
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
	    envoiUDP(ent,eybg(getIdm()),1);
	    strncpy(ent->ip_next,ip_succ,15);
	    strncpy(ent->port_udp_next,port_succ,4);
	  }else if(strcmp(ent->port_udp_next2,port) == 0 && strcmp(getIp(),ip) == 0){
	    envoiUDP(ent,eybg(getIdm()),2);
	    strncpy(ent->ip_next,ip_succ,15);
	    strncpy(ent->port_udp_next,port_succ,4);
	  }else{
	    //on transmet le message
	    envoiUDP(ent,mess_c,1);
	    if(strcmp(ent->port_udp_next2,"") != 0){
	      envoiUDP(ent,mess_c,2);
	    }
	  }
	}else if (strcmp(type,"EYBG") == 0){
	  printf("\n\t-----Vous avez quitter l'anneau !-----\n\n");
	  freeEntity(ent);
	  free(idm);
	  return 0;
	}else if (strcmp(type,"MEMB") == 0){
	  printf("\nServerUDP-MessageRecu: %s",mess_c);
	  envoiUDP(ent,mess_c,1);
	  if(strcmp(ent->port_udp_next2,"") != 0){
	    envoiUDP(ent,mess_c,2);
	  }
	  return 0;
	}
	/*	else if (strcmp(type,"TEST") == 0){
	  char * ip_diff = malloc(sizeof(char)*15);
	  strncpy(ip_diff,strtok(NULL," "),15);
	  char * port_diff = malloc(sizeof(char)*4);
	  strncpy(port_diff,strtok(NULL," "),8);
	  if(strcmp(ent->ip_diff,ip_diff) && strcmp(ent->port_diff,port_diff)){
	    envoiUDP(ent,mess_c,1);
	  }else if(strcmp(ent->ip_diff2,ip_diff) && strcmp(ent->port_diff2,port_diff)){
	    envoiUDP(ent,mess_c,2);
	  }
	  return 0;
}
*/
	else{
	  //on transmet le message
	  envoiUDP(ent,mess_c,1);
	  if(strcmp(ent->port_udp_next2,"") != 0){
	    envoiUDP(ent,mess_c,2);
	  }
	}
      }
    }else if(strcmp(mess_c,"DOWN\n") == 0){
      freeEntity(ent);
      printf("\nServerUDP-MessageRecu: %s",mess_c);
      printf("\n\t-----L'anneau est casser-----\n\n");
      exit(0);
    }
  }
  return 0;
}

//envoi un message en UDP
int envoiUDP(entity * ent, char * mess,int i){ 
  int sock;
  if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
    perror("EnvoiUDP-Erreur (socket)");
    return -1;      
  }
  struct addrinfo * first_info;
  int r;
  if(i == 1){
    r = getaddrinfo(ent->ip_next,ent->port_udp_next,NULL,&first_info);
  }else if(i == 2){
    r = getaddrinfo(ent->ip_next2,ent->port_udp_next2,NULL,&first_info);
  }
  if(r == 0){
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
