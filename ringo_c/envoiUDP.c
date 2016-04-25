#include "protocolTCP.h"

#include <netdb.h>
#include <sys/types.h>

int main(int argc, char* argv[]) { 
  if(argc > 1 && !strcmp(argv[1],"--help")){
    printf("Permet d'envoyer sur le port UDP voulu\n");
    printf("Utilisation : envoiUDP addresse port\n");
    return 0;
  }
  if(argc > 2){
    //    int sock = socket(PF_INET,SOCK_DGRAM,0);
    //struct sockaddr_in serverAddr;

    struct hostent *host_address;
    if((host_address = gethostbyname(argv[1])) == NULL){
      fprintf(stderr,"%s : cette addresse n'est pas valide.\nUtilisation : envoiUDP adresse port\n",argv[1]);
      return -1;
    }
    int sock;
    if((sock  = socket(PF_INET, SOCK_DGRAM,0)) == -1){
      fprintf(stderr,"Erreur lors de la creation de la socket\n");
      return -1;      
    }
    struct sockaddr_in s_socket;
    memset ((char *) &s_socket,0,sizeof(struct sockaddr_in));
    (&s_socket)->sin_family = AF_INET;
    (&s_socket)->sin_port = htons(atoi(argv[2]));
    (&s_socket)->sin_addr.s_addr = *((unsigned long *)(host_address->h_addr_list[0]));

    /*

    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(atoi(argv[2]));
    serverAddr.sin_addr.s_addr = inet_addr(argv[1]);
    memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);  
    
    
  
      struct addrinfo *first_info;
      struct addrinfo hints;
      memset(&hints, 0, sizeof(struct addrinfo));
      hints.ai_family = AF_INET;
      hints.ai_socktype = SOCK_DGRAM;
      int r = getaddrinfo(argv[1],argv[2],&hints,&first_info);
      struct sockaddr *saddr = first_info->ai_addr;
*/
    char mess[M_SIZE_MAX];
    while(1){
      printf("ENVOI UDP - Entrer votre message à envoyer :\n");
      fgets ( mess, sizeof mess, stdin ); 
      //le saut de ligne est ajouter a la fin du message par fgets
      if(strcmp(mess,"quit\n") == 0 
	 || strcmp(mess,"QUIT\n") == 0
	 || strcmp(mess,"q\n") == 0){
	close(sock);
	return 0;
      }else{
	int ss = sendto(sock,mess,strlen(mess),0,(struct sockaddr *)&s_socket,sizeof(s_socket));
	printf("Message envoyer : %d\n",ss);
      }
    }
    close(sock);
    return 0;
  }else{
  fprintf(stderr,"Veillez rentrez une adresse et un numéro de port\n");
  fprintf(stderr,"Utilisation : envoiUDP addresse port\n");
  return -1;
  }
}
