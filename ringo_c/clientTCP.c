//#include "utils.h"
#include "protocolTCP.h"
#include <pthread.h>

int main(int argc, char* argv[]){
  if(argc > 1 && !strcmp(argv[1],"--help")){
    printf("Permet de se connecter a une machine en TCP\n");
    printf("Utilisation : clientTCP addresse port\n");
    return 0;
  }
  if(argc > 2){
    char * ip = argv[1];
    int port = atoi(argv[2]);
    entity *ent = (entity *)malloc(sizeof(entity));
    if(connectTCP(getIp(),port,ip,port,ent) == -1){
      fprintf(stderr,"Erreur lors de la connection TCP\n");
      return -1;
    }
    //TEST
    /*
    if(serverTCP(atoi(ent.port_tcp),ent) != 0){
      fprintf(stderr,"Erreur dans l'execution du serveur TCP\n");
      return -1;
    }
    */
    getInfo(*ent);
  }else{
    fprintf(stderr,"Veillez rentrez une adresse et un numéro de port\n");
    fprintf(stderr,"Utilisation : clientTCP addresse port\n");
    return -1;
  }
  return 0;
}
