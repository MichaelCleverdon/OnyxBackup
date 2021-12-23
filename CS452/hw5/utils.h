#ifndef UTILS_H
#define UTILS_H
#include <stddef.h>

//Wrapper around mmap that takes the same parameters as malloc(), just without using it
extern void *mmalloc(size_t size);
//exponent to size
//Returns 2^e
extern unsigned int e2size(int e);
//returns log base 2 of size
extern int size2e(unsigned int size);

extern void bitset(unsigned char *p, int bit);
extern void bitclr(unsigned char *p, int bit);
extern void bitinv(unsigned char *p, int bit);
extern int bittst(unsigned char *p, int bit);

extern void *buddyset(void *base, void *mem, int e);
extern void *buddyclr(void *base, void *mem, int e);
extern void *buddyinv(void *base, void *mem, int e);
extern int buddytst(void *base, void *mem, int e);

#endif
