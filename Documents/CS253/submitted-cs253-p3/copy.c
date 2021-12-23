int doCopy(void){  //Returns exit status
    char storageString[1024];
    while(fgets(storageString, 1024, stdin) != NULL){
        if(storageString[strlen(storageString)- 1] != '\n'){
            fputs("The operation you are trying to run is too long\nPlease make sure that overall length of each line does not exceed 1024 characters\nYou can do this by putting some of the text on other lines.\n", stdout);
            return(1);        
        }
        fputs(storageString, stdout);
    }
    return(0);
}
