#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include "clientTCP.h"

int main(int argc, char* argv[]){
  if(argc > 1 && !strcmp(argv[1],"--help")){
    printf("Permet de se connecter a une machine en TCP\n");
    printf("Utilisation : clientTCP addresse port\n");
    return 0;
  }
  if(argc > 2){
    char * ip = argv[1];
    int port = atoi(argv[2]);
    printf("debut\n");
    int s = connectTCP(ip,port,ip,port);
    printf("fin avec %d\n",s);
  }else{
    printf("Veillez rentrez une adresse et un numéro de port\n");
    return -1;
  }
  return 0;
}
