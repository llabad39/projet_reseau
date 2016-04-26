CFLAGS = -Wall
CC = gcc
EXEC = serverTCP clientTCP


all: $(EXEC)

serverTCP: 
	$(CC) $(CFLAGS) serverTCP.c protocolTCP.c utils.c serverTCP

clientTCP: 
	$(CC) $(CFLAGS) clientTCP.c protocolTCP.c utils.c clientTCP

clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC)
