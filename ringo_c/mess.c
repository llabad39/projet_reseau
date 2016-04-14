#define M_SIZE_MAX 512        /***< maximum size for a message (octets) */

/**
 * A structure to represent a message
 */
typedef struct{
  char idm [8];
  char mess [M_SIZE_MAX];
} mess;

