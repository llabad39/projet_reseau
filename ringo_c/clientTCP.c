#include "utils.h"
#include "protocolTCP.h"

int main(int argc, char* argv[]){
  if(argc > 1 && !strcmp(argv[1],"--help")){
    printf("Permet de se connecter a une machine en TCP\n");
    printf("Utilisation : clientTCP addresse port\n");
    return 0;
  }
  if(argc > 2){
    char * ip = argv[1];
    int port = atoi(argv[2]);
    if(connectTCP(ip,port,ip,port) == -1){
      fprintf(stderr,"Erreur lors de la connection TCP\n");
      return -1;
    }
  }else{
    fprintf(stderr,"Veillez rentrez une adresse et un numéro de port\n");
    fprintf(stderr,"Utilisation : clientTCP addresse port\n");
    return -1;
  }
  return 0;
}