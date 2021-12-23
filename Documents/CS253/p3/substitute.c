int doSubstitute(char *pattern, char *replacement){
    int patternLength = strlen(pattern);
    char storageString[1024];
    int arrayOfPatternMatches[1024] = {};
    int patternMatchesCounter;
    if(strcmp(pattern, replacement) == 0){
        return(doCopy());
    }
    while(fgets(storageString, 1024, stdin) != NULL){
        if(storageString[strlen(storageString) - 1] != '\n'){
            fputs("The operation you are trying to run is too long\nPlease make sure that overall length of the input file does not exceed 1024 characters\nYou can do this by putting some of the text on other lines.\n", stdout);
            return(1);        
        }
        patternMatchesCounter = 0;
        //If it actually finds the pattern in the line
        if(strstr(storageString, pattern) != NULL){
            int storageStringLength = strlen(storageString);

            //Loop through the storageString to check for pattern matches
            for (int i = 0; i <= storageStringLength - patternLength; i++) { 
               int j; 
      
                //Loops through the entire pattern to make sure it is the same as the original text
                for (j = 0; j < patternLength; j++) {
                    if (storageString[i + j] != pattern[j]) {
                        break; 
                    }
                }

                //If it makes it all the way through without breaking, then the pattern was found
                if (j == patternLength){
                    arrayOfPatternMatches[patternMatchesCounter] = i;
                    patternMatchesCounter++;
                }
                
            }
            //Do some quick calculations to make sure the character count won't go over 1024
            if(patternMatchesCounter*strlen(replacement) + strlen(storageString) - (patternMatchesCounter*strlen(pattern)) > 1023){
                fputs("The operation you are trying to run is too long\nPlease make sure that overall length of the find and replace function will not exceed 1024 characters\nYou can do this by putting some of the text on other lines or picking a smaller replacement string.\n", stdout);
                return(1);
            }
            char newString[1024];
            for(int k = 0; k < patternMatchesCounter; k++){
                int indexCurrentlyWorkingOn = arrayOfPatternMatches[k];
                char tempString[1024];
                char* tempPointer;
                strncpy(tempString, storageString, 1022);
               // for(int l = 0; l < patternLength; l++){
                //Insert null character to display where the first part ends and where the repair begins
                tempString[indexCurrentlyWorkingOn] = '\0';
                //If it's not the first run, adjust the string to only read between the last substitution and the current one
                if(k != 0){
                    tempPointer = &tempString[arrayOfPatternMatches[k-1]+1];
                }
                else{
                    tempPointer = tempString;        
                }
                //TempPointer1 is the index of the null terminator separating the original new string 
                char *tempPointer1 = &tempPointer[indexCurrentlyWorkingOn];

                //If the pattern is found at the beginning, do different logic
                if(indexCurrentlyWorkingOn == 0){
                    strncat(newString, replacement, 1023-strlen(newString));
                }

                //If the pattern is found at the end, just replace the end with the replacement
                
                else if(indexCurrentlyWorkingOn == strlen(storageString)-1){

                    strncat(newString, strncat(tempPointer1 - 1, replacement, 1023), 1023-strlen(newString));
                }
                else{                
                    //Assign a new string to start after the pattern, used in recombination of string
                    strncat(newString, strncat(tempPointer, replacement, 1024-strlen(tempPointer)), 1023-strlen(newString));
                }   
            }   
            //If it didn't replace the end of the string, append the rest of the string
            if(arrayOfPatternMatches[patternMatchesCounter-1] != strlen(storageString)){
                char* finalPointer = &storageString[arrayOfPatternMatches[patternMatchesCounter-1]+strlen(pattern)];
                strncat(newString, finalPointer, 1023-strlen(newString));
            }
            fputs(newString, stdout);
            strncpy(newString, "", 1024);
        }
        else{
            fputs(storageString, stdout);
        }
        strncpy(storageString, "", 1024);
        memset(arrayOfPatternMatches, 0, sizeof(arrayOfPatternMatches));
    }
    return(0);
}
