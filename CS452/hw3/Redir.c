#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

#include "Redir.h"

extern int execRedir(int isInput, char* file1, char* file2){
    if(isInput == 1){
        int fd = open(file1, O_RDONLY);
        dup2(fd, STDIN_FILENO);
        close(fd);
        if(file2){
            int fd2 = fileno(fopen(file2, "w"));
            dup2(fd2, STDOUT_FILENO);
            close(fd2);
        }
    }
    //Output
    else{
        //Check if the redir has something in it
        if(file1){
            int fd = fileno(fopen(file1, "w"));
            dup2(fd, STDOUT_FILENO);
            close(fd);
        }
        else
            return 0;
    }
    
    return 1;
}