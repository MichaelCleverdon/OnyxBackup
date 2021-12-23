#Phony target all to rebuild everything
all: rules.d sed253

CFLAGS= -Wall -std=c99 -g -o0

rules.d: $(wildcard *.c) $(wildcard *.h)
	gcc -MM $(wildcard *.c) > rules.d
	
-include rules.d

sed253: sed253.o
	gcc $(CFLAGS) sed253.o -o sed253

sed253.o: rules.d
	gcc $(CFLAGS) -c sed253.c -o sed253.o

clean:
	rm -f *.o sed253 rules.d
