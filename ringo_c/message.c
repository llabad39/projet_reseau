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
  strcat(mess," ");
  strcat(mess,id);
  strcat(mess," ");
  strcat(mess,ip);
  strcat(mess," ");
  strcat(mess,port);
  strcat(mess," \n");
  
  return mess;
}

//genere le message GBYE avec les bons parametres
char * gbye(char * idm, char * ip, char * port, char * ip_succ,char * port_succ){
  char * mess = malloc(sizeof(char)*M_SIZE_MAX);
  memset(mess,0,1);
  strncat(mess,"GBYE  ",5);
  strcat(mess,idm);
  strcat(mess," ");
  strcat(mess,ip);
  strcat(mess," ");
  strcat(mess,port);
  strcat(mess," ");
  strcat(mess,ip_succ);
  strcat(mess," ");
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

//ajoute idm a la liste
void add (lidm * head, char * idm){
  lidm * current = head;
  while(current->next != NULL){
    current = current->next;
  }
  current->next = malloc(sizeof(struct lidm));
  current->next->idm = malloc(sizeof(char)*9);
  strncpy(current->next->idm,idm,9);
  memset(&current->next->idm[8],0,1);
  current->next->next = NULL;
}

//renvoi 1 si idm est dans la liste l, 0 sinon
int contains (lidm * head , char * idm){
  lidm * l = head;
  while(l->next != NULL){
    if(strcmp(l->idm,idm) == 0)
      return 1;
    l = l->next;
  }
  if(strcmp(l->idm,idm) == 0){
    return 1;
  }
  return 0;
} 

//affiche tout les idm de la liste l
void show (lidm * head){
  lidm * l = head;
  while(l->next != NULL){
    printf("%s\n",l->idm);
    l = l->next;
  }
  printf("%s\n",l->idm);
}
