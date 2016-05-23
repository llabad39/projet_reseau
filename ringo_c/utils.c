#define M_SIZE_MAX 512        /***< maximum size for a message (octets) */

#include "utils.h"

//demande a l'utilisateur d'entrer ces infos de base
//a savoir son id, port tcp et port udp
void askInfo(entity * ent){
  char buff[M_SIZE_MAX];

  //id
  printf("Veillez entrer un id (8 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 8){
    printf("id trop long : max 8 char, reessayer\n");
    scanf("%s",buff);
  }
  strcpy(ent->id,buff);
  memset(buff,0,sizeof(buff));

  //port TCP
  printf("Veillez entrer un port tcp libre (4 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 4){
    printf("port tcp trop long : max 4 char, reessayer\n");
    scanf("%s",buff);
  } 
  strcpy(ent->port_tcp,buff);
  memset(buff,0,sizeof(buff));

  //port UDP
  printf("Veillez entrer un port udp libre (4 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 4){
    printf("port udp trop long : max 4 char, reessayer\n");
    scanf("%s",buff);
  } 
  strcpy(ent->port_udp,buff);
  strcpy(ent->port_udp_next,buff);
  memset(buff,0,sizeof(buff));
}

//demande a l'utilisateur d'entrer ces infos de diffusion 
//a savoir l'ip et le port de diffusion 
void askInfoDiff(entity * ent){
  char buff[M_SIZE_MAX];

  //ip_diff
  printf("Veillez entrer une addresse de multidiffusion (15 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 15){
    printf("addresse de multidiffusion trop long : max 15 char, reessayer\n");
    scanf("%s",buff);
  } 
  strcpy(ent->ip_diff,buff);
  memset(buff,0,sizeof(buff));

  //port_diff
  printf("Veillez entrer un port de diffusion libre (4 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 4){
    printf("port de diffusion trop long : max 4 char, reessayer\n");
    scanf("%s",buff);
  } 
  strcpy(ent->port_diff,buff);
  memset(buff,0,sizeof(buff));
}

//demande a l'utilisateur d'entrer l'ip de la machine sur laquelle il veut
//se connecter
void askIpDest(char * ip){
  char buff[M_SIZE_MAX];

  //ip destination
  printf("Veillez entrer une addresse IP ou vous connecter (15 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 15){
    printf("addresse IP trop long : max 15 char, reessayer\n");
    scanf("%s",buff);
  }
  strcpy(ip,buff);
  memset(buff,0,sizeof(buff));

  return;
}

//demande a l'utilisateur d'entrer le port TCP de la machine sur laquelle 
//il veut se connecter
void askPortDest(char * port_tcp_dest){
  char buff[M_SIZE_MAX];

  //port TCP destination
  printf("Veillez entrer un port tcp ou vous connecter (4 char max)\n");
  scanf("%s",buff);
  while(strlen(buff) > 4){
    printf("port tcp trop long : max 4 char, reessayer\n");
    scanf("%s",buff);
  } 
  strcpy(port_tcp_dest,buff);
  
  return ;
}

//transforme un int en char *
char * itos(int i){
  char * result = malloc(sizeof(char)*15);
  sprintf(result,"%d",i);
  return result;
}

//renvoi l'addresse ip de la machine courante
char * getIp(){
  struct ifaddrs *myaddrs, *ifa;
  struct sockaddr_in *s4;
  int status;
  char *ip=(char *)malloc(64*sizeof(char));
  status = getifaddrs(&myaddrs);
  if (status != 0){
    fprintf(stderr,"Probleme de recuperation d'adresse IP");
    exit(1);
  }
  for (ifa = myaddrs; ifa != NULL; ifa = ifa->ifa_next){
    if (ifa->ifa_addr == NULL) continue;
    if ((ifa->ifa_flags & IFF_UP) == 0) continue;
    if ((ifa->ifa_flags & IFF_LOOPBACK) != 0) continue;
    if (ifa->ifa_addr->sa_family == AF_INET){
      s4 = (struct sockaddr_in *)(ifa->ifa_addr);
      if (inet_ntop(ifa->ifa_addr->sa_family, (void *)&(s4->sin_addr),
		    ip, 64*sizeof(char)) != NULL){
	return ip;
      }
    }
  }
  freeifaddrs(myaddrs);
  return ip;
}

//affiche les info de l'entité ent
void getInfo(entity ent){
  printf("\n%-14s : %s\n","id",ent.id);
  printf("%-14s : %s\n","port_udp",ent.port_udp);
  printf("%-14s : %s\n","port_tcp",ent.port_tcp);
  printf("%-14s : %s\n","ip_next",ent.ip_next);
  printf("%-14s : %s\n","port_udp_next",ent.port_udp_next);
  printf("%-14s : %s\n","ip_diff",ent.ip_diff);
  printf("%-14s : %s\n","port_diff",ent.port_diff);
  printf("%-14s : %s\n","ip_next2",ent.ip_next2);
  printf("%-14s : %s\n","port_udp_next2",ent.port_udp_next2);
  printf("%-14s : %s\n","ip_diff2",ent.ip_diff2);
  printf("%-14s : %s\n\n","port_diff2",ent.port_diff2);
}

//demande a l'utilisateur si il veut voir les info de ent
void askGetInfo(entity ent){
  char buff[M_SIZE_MAX];

  printf("\nVoulez vous voir vos informations ? (y/n)\n\n");
  scanf("%s",buff);
  while(1){
    if(!strcmp(buff,"y") || !strcmp(buff,"yes")){
      getInfo(ent);
      return;
    }else if(!strcmp(buff,"n") || !strcmp(buff,"no")){
      return;
    }else{
      printf("Repondez par y/yes ou par n/no\n");
      scanf("%s",buff);
    }
  }
}

//renvoi un identifiant unique pour les messages
char * getIdm(){
  
  char * idm = malloc(sizeof(char)*9);
  time_t rawtime;
  struct tm * timeinfo;
  memset(idm,0,1); //permet d'enlever '\0' qui est dans idm
  
  //time ( &rawtime );
  timeinfo = localtime ( &rawtime );
  srand(clock());
  int a = rand() %(timeinfo->tm_sec*10+1);//*10 pour augmenter le nombre
  int b = rand() %(timeinfo->tm_min*10)+1;//et +1 pour eviter les divisions par 0
  int c = rand() %(timeinfo->tm_mday*10+1);
  int d = rand() %timeinfo->tm_year;
  //on prend juste le premier byte de chaques nombre aléatoire
  strncat(idm,itos(a),1);
  strncat(idm,itos(b),1);
  strncat(idm,itos(c),1);
  strncat(idm,itos(d),1);
  a = rand() %(timeinfo->tm_sec*10+1);
  b = rand() %(timeinfo->tm_min*10+1);
  c = rand() %(timeinfo->tm_mday*10+1);
  d = rand() %timeinfo->tm_year;
  strncat(idm,itos(a),1);
  strncat(idm,itos(b),1);
  strncat(idm,itos(c),1);
  strncat(idm,itos(d),1);
  
  return idm;
}

