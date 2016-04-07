#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>

int connectTCP(char *ip, int port, char *ip_dest, int port_dest){
  
  struct sockaddr_in adress_sock;
  adress_sock.sin_family = AF_INET;
  adress_sock.sin_port = htons(port_dest);
  if(inet_aton(ip_dest,&adress_sock.sin_addr) == 0){
    printf("Veillez entrer une adresse correcte\n");
    return -1;
  }
  int sock = socket(PF_INET,SOCK_STREAM,0);
  if(connect(sock,(struct sockaddr *)&adress_sock,
	     sizeof(struct sockaddr_in)) != -1){
    /*char buff [100];
    int size_rec = recv(sock,buff,99*sizeof(char),0);
    if(size_rec < 1){
      printf("Probleme lors du recv\n");
      return -1;
    }
    buff[size_rec] = '\0';
    printf("Message : %s\n",buff);*/
    char mess[100] = "NEWC ";
    char port_s[4];
    sprintf(port_s,"%d",port);
    strcat(mess,ip);
    strcat(mess," ");
    strcat(mess,port_s);
    strcat(mess,"\n");
    int w = write(sock,mess,strlen(mess));
    printf("envoi : %d\n",w);
  }else{
    printf("Probleme lors du connect\n");
  }
  return 0;
}
