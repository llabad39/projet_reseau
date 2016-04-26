#include "protocolTCP.h"

#include <netdb.h>
#include <sys/types.h>

int main(int argc, char* argv[]) { 
  if(argc > 1 && !strcmp(argv[1],"--help")){
    printf("Permet d'ecouter sur le port UDP voulu\n");
    printf("Utilisation : serverUDP port\n");
    return 0;
  }
  if(argc > 1){
    int sock;
    if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
      fprintf(stderr,"Erreur lors de la creation de la socket\n");
      return -1;      
    }
    struct sockaddr_in address_sock;
    address_sock.sin_family = AF_INET;
    address_sock.sin_port = htons(atoi(argv[1]));
    address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
    int r = bind(sock,(struct sockaddr *)&address_sock,
		 sizeof(struct sockaddr_in));
    if(r == 0){
      char mess[M_SIZE_MAX];
      while(1){
	int rec = recv(sock,mess,M_SIZE_MAX,0);
	mess[rec] = '\0';
	printf("SERVER UDP - Message recu : %s\n",mess);
      }
    }
    return 0;
  }else{
  fprintf(stderr,"Veillez rentrez un numéro de port\n");
  fprintf(stderr,"Utilisation : serverUDP port\n");
  return -1;
  }
}
