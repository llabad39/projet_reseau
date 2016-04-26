#include "protocolTCP.h"

int serverTCP(int port, entity ent){
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
      strcat(mess,"\n");
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
      
      strtok(buff," ");
      strncpy(ent.ip_next,strtok(NULL," "),15);
      strncpy(ent.port_udp_next,strtok(NULL," "),4);

      char mess2[M_SIZE_MAX] = "ACKC\n";
      write(*sock2,mess2,strlen(mess2));

      close(*sock2);
    }
  }
  close(sock);
  return 0;
}


//Attention : a modifier pour envoyer le port udp et non pas le port tcp
//peut etre prendre en argument une entity ?
int connectTCP(char *ip, int port, char *ip_dest, int port_dest, entity * ent){
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
    char port_s[4];
    snprintf(port_s,4,"%d",port);
    strcat(mess,ip);
    strcat(mess," ");
    strcat(mess,itos(port));
    strcat(mess,"\n");
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
    if(strcmp(buff2,"ACKC\n") < 0){
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



