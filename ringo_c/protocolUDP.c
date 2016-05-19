#include "protocolUDP.h"

int serverUDP(entity ent){
  int sock;
  if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
    fprintf(stderr,"Erreur lors de la creation de la socket\n");
    return -1;      
  }
  struct sockaddr_in address_sock;
  address_sock.sin_family = AF_INET;
  address_sock.sin_port = htons(atoi(ent.port_udp));
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
}
