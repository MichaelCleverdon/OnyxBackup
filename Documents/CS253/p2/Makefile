all: match

CFLAGS= -Wall -std=c99

match: match.o
	gcc match.o -o match

match.o: match.c
	gcc $(CFLAGS) -c match.c -o match.o

clean:
	rm -f *.o match
