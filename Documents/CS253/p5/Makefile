all: smash

CFLAGS = -Wall -std=c99 -g -o0

smash: smash.o
	gcc $(CFLAGS) smash.o -o smash

smash.o: $(wildcard *.c) $(wildcard *.h)
	gcc $(CFLAGS) -c smash.c -o smash.o

clean:
	rm -f *.o smash
