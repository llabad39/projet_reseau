#include "utils.h"
#include "protocolTCP.h"

int main(int argc, char* argv[]){
  if(argc > 1){  
    if(!strcmp(argv[1],"--help")){
      printf("Permet d'ecouter sur le port TCP voulu\n");
      printf("Utilisation : serverTCP port\n");
      return 0;
    }
    //int port = atoi(argv[1]);
    entity *ent;
    ent = malloc(sizeof(entity));
    strncpy(ent->port_tcp,argv[1],4);
    if(serverTCP(atoi(ent->port_tcp),*ent) != 0){
      fprintf(stderr,"Erreur dans l'execution du serveur TCP\n");
      free(ent);
      return -1;
    }
    free(ent);
  }else{
    fprintf(stderr,"Veillez rentrez un numéro de port valable\n");
    fprintf(stderr,"Utilisation : serverTCP port\n");
    return -1;
  }
  return 0;
}
