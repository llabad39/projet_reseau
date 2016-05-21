#include "message.h"

//genere le message WHOS avec le bon idm
char * whos(char * idm){
  char * mess = malloc(sizeof(char)*M_SIZE_MAX);
  memset(mess,0,1);
  strncat(mess,"WHOS ",5);
  strcat(mess,idm);
  strcat(mess," \n");
  
  return mess;
}

//genere le message MEMB avec les bons parametres
char * memb(char * idm, char * id, char * ip, char * port){
  char * mess = malloc(sizeof(char)*M_SIZE_MAX);
  memset(mess,0,1);
  strncat(mess,"MEMB ",5);
  strcat(mess,idm);
  strcat(mess,id);
  strcat(mess,ip);
  strcat(mess,port);
  strcat(mess," \n");
  
  return mess;
}

//genere le message GBYE avec les bons parametres
char * gbye(char * idm, char * ip, char * port, char * ip_succ,char * port_succ){
  char * mess = malloc(sizeof(char)*M_SIZE_MAX);
  memset(mess,0,1);
  strncat(mess,"MEMB ",5);
  strcat(mess,idm);
  strcat(mess,ip);
  strcat(mess,port);
  strcat(mess,ip_succ);
  strcat(mess,port_succ);
  strcat(mess," \n");
  
  return mess;
}

//genere le message EYBG avec le bon idm
char * eybg(char * idm){
  char * mess = malloc(sizeof(char)*M_SIZE_MAX);
  memset(mess,0,1);
  strncat(mess,"EYBG ",5);
  strcat(mess,idm);
  strcat(mess," \n");
  
  return mess;
}
