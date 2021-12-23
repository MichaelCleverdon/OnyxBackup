#define MAXLINE 4096


#define _GNU_SOURCE
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>   
#include "smash.h"
#include "history.c"
#include "commands.c"

char bfr[MAXLINE];
int main(void){
    setvbuf(stdout,NULL,_IONBF,0);  //Disable buffering in the stdout stream
    init_history();
    fputs("$ ",stderr);  //Output the first prompt//Loop reading commands until EOF or error

    //Loop reading commands until EOF or error
    while (fgets(bfr, MAXLINE, stdin) != NULL) {
        bfr[strlen(bfr) - 1] = '\0';    //Replace newline with NUL
        executeCommand(bfr);
        fputs("$ ",stderr);
    }
}
