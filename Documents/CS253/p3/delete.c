int doDelete(char *line1, char *line2){
    char *tosserPointer;
    int line1Value = strtol(line1, &tosserPointer, 10);
    int line2Value = strtol(line2, &tosserPointer, 10);

    if(line1Value > line2Value){
        fputs("Please make sure your lines are in the proper order\n", stdout);
        return(1);
    }
    int counter = 1;
    char storageString[1024];

    while(fgets(storageString, 1024, stdin) != NULL){


        if(storageString[strlen(storageString) - 1] != '\n'){
            fputs("The operation you are trying to run is too long\nPlease make sure that overall length of each line will not exceed 1024 characters\nYou can do this by putting some of the text on other lines.\n", stdout);
            return(1);        
        }
        //Only print out the line if it's not being deleted :D
        if(counter < line1Value || counter > line2Value){
                    fputs(storageString, stdout);
        }
        counter++;
    }
    return(0);
}
