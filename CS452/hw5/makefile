CC = gcc
CFLAGS = -Wall -g -lm
prog = test
all: test
.PRECIOUS = test.c
test: test.c balloc.o bitmap.o utils.o freelist.o
	$(CC) $(CFLAGS) -o test test.c balloc.o bitmap.o utils.o freelist.o

balloc.o: balloc.c bitmap.h freelist.h utils.h error.h
	$(CC) $(CFLAGS) -c balloc.c

bitmap.o: bitmap.c utils.h
	$(CC) $(CFLAGS) -c bitmap.c

utils.o: utils.c
	$(CC) $(CFLAGS) -c utils.c

freelist.o: freelist.c utils.h error.h
	$(CC) $(CFLAGS) -c freelist.c

clean:
	rm *.o