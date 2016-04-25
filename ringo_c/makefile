CFLAGS = -Wall
CC = gcc
EXEC = serverTCP clientTCP envoiUDP
OBJECTS = protocolTCP.o utils.o entity.o
HEADERS = $(wildcard *.h)

all: $(EXEC)

serverTCP: serverTCP.o $(OBJECTS)

clientTCP: clientTCP.o $(OBJECTS)

envoiUDP: envoiUDP.o $(OBJECTS)

%.o: %.c $(HEADERS)
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f *.o
	rm -f *~

proper: 
	rm -f $(EXEC)
