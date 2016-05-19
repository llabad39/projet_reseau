/**
 * A structure to represent an entity
 */
typedef struct entity entity;
struct entity {
  char id [8];
  char port_udp[5];
  char port_tcp[5];
  char ip_next[16];
  char port_udp_next[5];
  char ip_diff[16];
  char port_diff[5];

  char ip_next2[16];
  char port_udp_next2[5];
  char ip_diff2[16];
  char port_diff2[5];
};

entity createEntity(char * _ip_diff, char * _port_diff, char * _port_tcp,char* _port_udp);
