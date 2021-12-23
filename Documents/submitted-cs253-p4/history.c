#include "history.h"
struct Cmd* commandHistoryArray[10];
int sizeOfCommandHistory;
int currentIndex;
void init_history(void){  //Builds data structures for recording cmd history
  *commandHistoryArray = malloc(10*sizeof(struct Cmd));
    // for(int i = 0; i < 10; i++){
        sizeOfCommandHistory = 0;
        currentIndex = 0;
    // }
}

void add_history(char *cmd, int exitStatus){  //Adds an entry to the history
    commandHistoryArray[currentIndex] = malloc(sizeof(cmd)+sizeof(struct Cmd));
    commandHistoryArray[currentIndex]->cmd = malloc(sizeof(cmd));
    commandHistoryArray[currentIndex]->cmd = strdup(cmd);
    commandHistoryArray[currentIndex]->exitStatus = exitStatus;
    if(sizeOfCommandHistory < 10){
        sizeOfCommandHistory++;
    }

    currentIndex++;
    if(currentIndex > 9){
        currentIndex -= 10;
    }
}

void clear_history(void){ //Frees all malloc’d memory in the history
    for(int i = 0; i < sizeOfCommandHistory; i++){
        free(commandHistoryArray[i]->cmd);
        free(commandHistoryArray[i]);
    }
}

void print_history(int firstSequenceNumber){ //Prints the history to stdout
    int loops = 0;
    //Since the print history command will never run with currentIndex = 0 unless the array is full, this allows us to make sure the right starting point is selected
    int startingIndex = (sizeOfCommandHistory > 9) ? currentIndex : 0; 
    while (loops < sizeOfCommandHistory){
        fprintf(stdout, "[%d] %s\n", commandHistoryArray[(startingIndex+loops)%10]->exitStatus, commandHistoryArray[(startingIndex+loops)%10]->cmd);
        loops++;
    }
}
