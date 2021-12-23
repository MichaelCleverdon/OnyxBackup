#include <stdio.h>
#include <unistd.h>
#include <string.h>

#include "bitmap.h"
#include "utils.h"

static const int bitsperbyte=8;

static int divup(int n, int d) {
  return (n+d-1)/d;
}

//Bitmap search goes from low to high in terms of order. That way the value found in the bitmap would be 

static int mapsize(unsigned int size, int e) {
  int blocksize=e2size(e);
  int blocks=divup(size,blocksize);
  int buddies=divup(blocks,2);
  return divup(buddies,bitsperbyte);
}

//Computes an index to a bitmap for either of the buddy pair's two addresses
//Left buddy address, normal
//Right buddy address, converted to the left one by shiftinging a bit to the left by 1?
static int bitaddr(void *base, void *mem, int e) {
  int addr=buddyclr(base,mem,e)-base;
  int blocksize=e2size(e);
  return addr/blocksize/2;
}

//Bitmap has a 1 if one or both buddies is allocated on that level
//Creates a new bitmap for a specific size and exponent
extern BitMap bitmapnew(unsigned int size, int e) {
  int ms=mapsize(size,e);
  BitMap b=mmalloc(ms);
  if ((long)b==-1)
    return 0;
  memset(b,0,ms); //0's out the entire bitmap
  return b;
}

//Set a certain bit in the bitmap to 1
extern void bitmapset(BitMap b, void *base, void *mem, int e) {
  int offset=bitaddr(base,mem,e); //Get the address
  bitset(((unsigned char *)b)+offset/bitsperbyte,offset%bitsperbyte); //Set the bit to 1
}

//0 out a bit in the bitmap
extern void bitmapclr(BitMap b, void *base, void *mem, int e) {
  int offset=bitaddr(base,mem,e);//Get the address
  bitclr(((unsigned char *)b)+offset/bitsperbyte,offset%bitsperbyte); //Set the bit to 0
}

//Return the bit in the bitmap
extern int bitmaptst(BitMap b, void *base, void *mem, int e) {
  int offset=bitaddr(base,mem,e); //Get the address
  return bittst(((unsigned char *)b)+offset/bitsperbyte,offset%bitsperbyte); //Return the bit's value
}

//Print out the bitmap
extern void bitmapprint(BitMap b, unsigned int size, int e) {
  int ms=mapsize(size,e);//Get the size of the map
  int byte;
  for (byte=ms-1; byte>=0; byte--) //For each character in the bitmap (8 bits)
    printf("%02x%s",((unsigned char *)b)[byte],(byte ? " " : "\n")); //Print out the 2 digits into hex then add either a space or space between
}
