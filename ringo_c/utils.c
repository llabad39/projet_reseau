#include "utils.h"

char * itos(int i){
  char * result = malloc(sizeof(char)*15);
  sprintf(result,"%d",i);
  return result;
}
