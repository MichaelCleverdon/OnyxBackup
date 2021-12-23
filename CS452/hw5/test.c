#include <stdio.h>
#include <string.h>
#include "balloc.h"

//This way every separator is the same amount of slashes long
void printSeparator(){
    printf("\n/////////////////////\n");
}
void red(){
    printf("\033[1;31m");
}
void reset () {
  printf("\033[0m");
}

void yellow(){
  printf("\033[1;33m");
}

int main(){
    size_t s = 4000; //Should round to 4096
    int l = 0; //Should round to 3
    int u = 8; //2^u bytes for upper bound 
    red();
    printf("Test 1: Creating pool of right size\n");
    yellow();
    printf("Expected Result: Size Request: 4096.\nLower bound: 8 bytes\nUpperBound: 256 bytes\n16 Blocks in Order 5, each of size 256 bytes. \nNo other blocks in any other order level.\n");
    reset();
    Balloc b = bnew(s,l,u);
    bprint(b);
    printSeparator(); 
    red();
    printf("Test 2: Actually allocating some data and splitting all the blocks\n");
    yellow();
    printf("Expected Result: String with value '7 letrs' should be printed to the console.\nBlock of size 8 should be allocated (order 0)\nAll other orders should have a 1 besides the highest order which should have 15 blocks\n");
    reset();
    char* str = (char*)balloc(b, 8);
    strncpy(str, "7 letrs", 8);
    printf("OUTPUT: %s\n", str);
    bprint(b);

    printSeparator();

    red();
    printf("Test 3: Allocating a larger block\n"); 
    yellow();
    printf("Expected Result: String with value '31 letters long is this string!' should be printed to the console\nBlock of size 32 should be allocated (order 2)\nNo blocks should be split\n");
    reset();
    char* str1 = (char*)balloc(b, 32);
    strncpy(str1, "31 letters long is this string!", 32);
    printf("OUTPUT: %s\n", str1);
    bprint(b);

    printSeparator();

    red();
    printf("Test 4: Splitting a block from an order that's not order 5\n");
    yellow();
    printf("Expected result: Another string with value 'This sentence is 31 characters' should be printed to the console\nA block of size 32 should be alocated (order 2)\nAn order 3 block will have been split. This will make Order 3 have 0 blocks left and Order 2 have 1 block left\n");
    reset();
    char* str2 = (char*)balloc(b, 32);
    strncpy(str2, "This sentence is 31 characters!", 32);
    printf("OUTPUT: %s\n", str2);
    bprint(b);

    printSeparator();

    red();
    printf("Test 5: Rounding\n");
    yellow();
    printf("Expected result: A string of size 16 with value 'String' should be allocated despite the request being of size 10\nA block of size 16 should be allocated (order 1)\nNo splitting should be required\n");
    reset();
    char* str3 = (char*)balloc(b, 10);
    strncpy(str3, "String", 10);
    printf("OUTPUT: %s\n", str3);
    bprint(b);

    printSeparator();
    red();
    printf("Test 6: Too small\n");
    yellow();
    printf("Expected Result: The program should allocate 8 bytes to the 1 character string because that's the smallest it's allowed to be.\nShould allocate the free block at 8 bytes (order 0).\nNo splitting required\n");
    reset();
    char* str4 = (char*)balloc(b, 2);//1 + 1 for the NULL byte
    strncpy(str4, "1234567", 8); //Should be 8 bytes in here
    printf("OUTPUT: %s\n", str4);
    bprint(b);
    printSeparator();

    red();
    printf("Test 7: Free 1 block\n");
    yellow();
    printf("Expected Result: The program should free a string of 8 bytes.\nThis will create a new block of order 0 but no merging should occur\n");
    reset();
    bfree(b,str); //Str's size is 8 bytes
    bprint(b);
    printSeparator();

    red();
    printf("Test 8: Free + Merge\n");
    yellow();
    printf("Expected Result: The program shold free another string of 8 bytes.\nGiven that the previous test succeeded, this test should merge both blocks of size order 0 \nand result in the balloc pool having a block of order 1\n");
    reset();
    bfree(b, str4); //Str4's size should be 8 bytes as well
    bprint(b);
      
    printSeparator();

    red();
    printf("Test 9: BSize()\n");
    yellow();
    printf("Expected Result: The program should return the allocated size of the block that now contains a string (not actual size of the string).\nShould return 16\n");
    reset();
    printf("Size of Block: %d\n", bsize(b, str3));

    printSeparator();

    red();
    printf("Last Test: Too big\n");
    yellow();
    printf("Expected result: The program should ERROR and crash because the user is requesting too much memory from the predetermined pool\n");
    reset();
    char* strLast = (char*)balloc(b, 300);
    strncpy(strLast, "Biiiiiiig string", 300);
    printf("OUTPUT: %s\n", strLast);
    bprint(b);
}