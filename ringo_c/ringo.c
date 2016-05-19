#include "protocolRINGO.h"

int main() {
  while(1){
    char buff[M_SIZE_MAX];
    scanf("%s",buff);
    entity *ent = (entity *)malloc(sizeof(entity));
    if(!strcmp(buff,"help") || !strcmp(buff,"--help")){
      r_help();
    }else if(!strcmp(buff,"create")){
      r_create(ent);
    }else if(!strcmp(buff,"connect")){
      r_connect(ent);
    }else if(!strcmp(buff,"duplicate")){
      r_duplicate(ent);
    }else if(!strcmp(buff,"quit_ring")){
      printf("quit_ring\n");      
    }else if(!strcmp(buff,"quit") || !strcmp(buff,"q")){
      return 0;
    }else{
      fprintf(stderr,"\n\tErreur d'utilisation de la commande ringo\n");
      r_help();
    }
  }
}
