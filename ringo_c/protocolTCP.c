#include "protocolTCP.h"

int serverTCP(entity ent){
  int sock = socket(PF_INET,SOCK_STREAM,0);
  struct sockaddr_in adress_sock;
  adress_sock.sin_family = AF_INET;
  adress_sock.sin_port = htons(atoi(ent.port_tcp));
  adress_sock.sin_addr.s_addr = htonl(INADDR_ANY);
  if(bind(sock,(struct sockaddr *)&adress_sock,
	  sizeof(struct sockaddr_in)) != 0){
    fprintf(stderr,"Probleme lors du bind\n");
    close(sock);
    return -1;
  }
  if(listen(sock,0) != 0){
    fprintf(stderr,"Probleme lors du listen\n");
    close(sock);
    return -1;
  }
  struct sockaddr_in caller;
  socklen_t si = sizeof(caller);
  while(1){
    int *sock2 = (int *)malloc(sizeof(int));
    *sock2 = accept(sock,(struct sockaddr *)&caller,&si);
    if(*sock2 >= 0){
      char mess[M_SIZE_MAX] = "WELC ";
      strcat(mess,ent.ip_next);
      strcat(mess," ");
      strcat(mess,ent.port_udp_next);
      strcat(mess," ");
      strcat(mess,ent.ip_diff);
      strcat(mess," ");
      strcat(mess,ent.port_diff);
      strcat(mess," ");
      strcat(mess," \n");
      write(*sock2,mess,strlen(mess));
      char buff[M_SIZE_MAX];
      int recv = read(*sock2,buff,(M_SIZE_MAX-1)*sizeof(char));
      if( recv < 0){
	fprintf(stderr,"Probleme lors du read\n");
	close(sock);
	return -1;
      }
      buff[recv]='\0';
      printf("SERVEUR TCP - Message recu : %s\n",buff);

      char * b1;
      char * b2;
      
      b1 = strtok(buff," ");
      //cas de demande d'insertion dans l'anneau
      if(strcmp(b1,"NEWC") == 0){
	if((b1 = strtok(NULL," ")) != NULL && (b2 = strtok(NULL," ")) != NULL){
	  strncpy(ent.ip_next,b1,15); 
	  strncpy(ent.port_udp_next,b2,4);
	  char mess2[M_SIZE_MAX] = "ACKC\n";
	  write(*sock2,mess2,strlen(mess2));
	}else{
	  fprintf(stderr,"Message erroné, insertion annulée\n\n");
	} 
	//cas de demande de duplication de l'anneau
      }else if(strcmp(b1,"DUPL") == 0){
	if(strcmp(ent.ip_next2,"") != 0){
	  char mess [M_SIZE_MAX] = "NOTC\n";
	  write(*sock2,mess,strlen(mess));
	  fprintf(stderr,"Duplication impossible, l'entite est deja doubleur\n");
	}else{
	  char * b3;
	  char * b4;
	  if((b1 = strtok(NULL," ")) != NULL 
	     && (b2 = strtok(NULL," ")) != NULL
	     && (b3 = strtok(NULL," ")) != NULL 
	     && (b4 = strtok(NULL," ")) != NULL){
	    strncpy(ent.ip_next2,b1,15); 
	    strncpy(ent.port_udp_next2,b2,4); 
	    strncpy(ent.ip_diff2,b3,15); 
	    strncpy(ent.port_diff2,b4,4);
	    char mess2 [M_SIZE_MAX] = "ACKD ";
	    strcat(mess2,ent.port_udp);
	    strcat(mess2,"\n");
	    write(*sock2,mess2,strlen(mess2));
	  }else{
	    fprintf(stderr,"Message erroné, duplication annulée\n\n");
	  } 
	}
      }
      close(*sock2);
      getInfo(ent);
    }
  }
  close(sock);
  return 0;
}


/*
 * fonction qui permet de se connecter en TCP sur une entite
 * et s'inserer dans l'anneau
 */
int connectTCP(char *ip_dest, int port_dest, entity * ent){
  struct sockaddr_in adress_sock;
  adress_sock.sin_family = AF_INET;
  adress_sock.sin_port = htons(port_dest);
  if(inet_aton(ip_dest,&adress_sock.sin_addr) == 0){
    fprintf(stderr,"Veillez entrer une adresse correcte\n");
    return -1;
  }
  int sock = socket(PF_INET,SOCK_STREAM,0);
  if(connect(sock,(struct sockaddr *)&adress_sock,
	     sizeof(struct sockaddr_in)) != -1){
    char buff [M_SIZE_MAX];
    int size_rec = recv(sock,buff,(M_SIZE_MAX-1)*sizeof(char),0);
    if(size_rec < 1){
      fprintf(stderr,"Probleme lors du recv\n");
      close(sock);
      return -1;
    }
    buff[size_rec] = '\0';
    printf("CLIENT TCP - Message recu : %s\n",buff);
    char mess[M_SIZE_MAX] = "NEWC ";
    strcat(mess,getIp());
    strcat(mess," ");
    strcat(mess,ent->port_udp);
    strcat(mess," \n");
    write(sock,mess,strlen(mess));

    char buff2 [M_SIZE_MAX];
    size_rec = recv(sock,buff2,(M_SIZE_MAX-1)*sizeof(char),0);
    if(size_rec < 1){
      fprintf(stderr,"Probleme lors du recv\n");
      close(sock);
      return -1;
    }
    buff2[size_rec] = '\0';
    printf("CLIENT TCP - Message recu : %s\n",buff2);
    if(strcmp(buff2,"ACKC\n") > 0){
      fprintf(stderr,"Probleme d'acceptation dans l'anneau\n");
      close(sock);
      return -1;      
    }
       
    strtok(buff," "); 
    strncpy(ent->ip_next,strtok(NULL," "),15);
    strncpy(ent->port_udp_next,strtok(NULL," "),4);
    strncpy(ent->ip_diff,strtok(NULL," "),15);
    strncpy(ent->port_diff,strtok(NULL," "),4);
    
  }else{
    fprintf(stderr,"Probleme lors du connect\nVerifier le port tcp indiqué\n");
    close(sock);
    return -1;
  }
  close(sock);
  return 0;
}

/*
 * fonction qui permet de se connecter en TCP sur une entite
 * et de demander sa duplication
 */
int connectTCPDupl(char *ip_dest, int port_dest, entity * ent){
  struct sockaddr_in adress_sock;
  adress_sock.sin_family = AF_INET;
  adress_sock.sin_port = htons(port_dest);
  if(inet_aton(ip_dest,&adress_sock.sin_addr) == 0){
    fprintf(stderr,"Veillez entrer une adresse correcte\n");
    return -1;
  }
  int sock = socket(PF_INET,SOCK_STREAM,0);
  if(connect(sock,(struct sockaddr *)&adress_sock,
	     sizeof(struct sockaddr_in)) != -1){
    char buff [M_SIZE_MAX];
    int size_rec = recv(sock,buff,(M_SIZE_MAX-1)*sizeof(char),0);
    if(size_rec < 1){
      fprintf(stderr,"Probleme lors du recv\n");
      close(sock);
      return -1;
    }
    buff[size_rec] = '\0';
    printf("CLIENT TCP - Message recu : %s\n",buff);
    char mess[M_SIZE_MAX] = "DUPL ";
    strcat(mess,getIp());
    strcat(mess," ");
    strcat(mess,ent->port_udp);
    strcat(mess," ");
    strcat(mess,ent->ip_diff);
    strcat(mess," ");
    strcat(mess,ent->port_diff);
    strcat(mess," \n");
    write(sock,mess,strlen(mess));

    char buff2 [M_SIZE_MAX];
    size_rec = recv(sock,buff2,(M_SIZE_MAX-1)*sizeof(char),0);
    if(size_rec < 1){
      fprintf(stderr,"Probleme lors du recv\n");
      close(sock);
      return -1;
    }
    buff2[size_rec] = '\0';
    printf("CLIENT TCP - Message recu : %s\n",buff2);
    if(strcmp(buff2,"NOTC\n") == 0){
      fprintf(stderr,"Impossible de dupliquer l'anneau, l'entite est deja doubleur\n");
      close(sock);
      return -1;      
    }
       
    strtok(buff," "); 
    strtok(NULL," ");
    strtok(NULL," ");
    strncpy(ent->ip_diff,strtok(NULL," "),15);
    strncpy(ent->port_diff,strtok(NULL," "),4);

    strncpy(ent->ip_next,ip_dest,15);

    strtok(buff2," ");
    strncpy(ent->port_udp_next,strtok(NULL," "),4);
    
  }else{
    fprintf(stderr,"Probleme lors du connect\nVerifier le port tcp indiqué\n");
    close(sock);
    return -1;
  }
  close(sock);
  return 0;
}


