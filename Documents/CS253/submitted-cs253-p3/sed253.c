//-----------------------------------------------------------------------------
//
// NAME
//  sed253 -- Simplified editor
//
// SYNOPSIS
//  sed253
//  sed253 -s pattern string
//  sed253 -d line1 line2
//
// DESCRIPTION
//  Simplified editor.  Copies lines read from stdin to stdout.  Options:
//
//  -s Substitute every occurrence of pattern with string
//  -d Delete line1 through line2 inclusive
//
// ERRORS
//  Prints usage message and exits abnormally for invalid commands.  Prints an
//  error message and exits abnormally for other issues.
//
// LIMITATIONS
//  Lines of text are limited to a maximum of 1023 chars.
//
// AUTHORS
//  Epoch...................................................................jrc
//
//-----------------------------------------------------------------------------

//Bring in the definitions for our helper functions
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "copy.h"
#include "substitute.h"
#include "delete.h"
#include "copy.c"
#include "substitute.c"
#include "delete.c"
void usage(char* s) {
    
    //Print the usage message to stderr
    fprintf(stderr,"Usage: \"%s <inFile >outFile \"to copy\n",s);
    fprintf(stderr,"Usage: \"%s -s <pattern> <string> <inFile >outFile \"to copy inFile to outFile and substitute <pattern> for <string>\n",s);
    fprintf(stderr,"Usage: \"%s -d <number1> <number2> <inFile >outFile \"to copy inFile to outFile while deleting lines <line1> to <line2> inclusive where line numbers start at 1\n",s);
    
    //Force an exit.  This is equivalent to return 2 in main()
    exit(2);                //Exit status is 2
    
}

//-----------------------------------------------------------------------------
// main -- the main function
//-----------------------------------------------------------------------------
int main(int argc, char **argv) {
    
    // 0 is copy, 1 is substitute, 2 is delete
    int functionNumber = 0; //Start with copy
    //Too many arguments
    if(argc > 4){
        usage(argv[0]);
    }
    //Substitute
    if(argc > 1){
        char* argument = argv[1];
        if(strcmp(argument, "-s") == 0){
            if(argc != 4){
                usage(argv[0]);            
            }
            functionNumber = 1;
        }
        else if (strcmp(argv[1], "-d") == 0){
            if(argc != 4){
                usage(argv[0]);            
            }
            functionNumber = 2;
        }
    }
    switch(functionNumber){
        case 0:
            exit(doCopy());    
            break;
        case 1:
            exit(doSubstitute(argv[2], argv[3]));
            break;
        case 2:
            exit(doDelete(argv[2], argv[3]));
            break;
    }
}

