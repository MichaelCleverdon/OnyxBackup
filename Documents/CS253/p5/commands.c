//Returns exit status of the command
int executeExternalCommand(char* args[]){
	int pid = fork();
	//Child
	if(pid == 0){
		execvp(args[0], args);
		exit(1);
	}
	//Parent
	else if(pid > 0){
		int retrievedExitStatus;
		wait(&retrievedExitStatus);
		return WEXITSTATUS(retrievedExitStatus);
	}
	else{
		perror("Fork Failed");
	}
	return -1;



}

void executeCommand(char *str){
	int sizeOfArgs = 0;
	char *args[MAXLINE];
	char* token;
	token = strtok(str, " ");
	while(token != NULL){
		args[sizeOfArgs] = token;
		//printf("%s\n", token);
		token = strtok(NULL, " ");
		sizeOfArgs++;
	}
	
	//IF exit, then run exit()
	if(strcmp(args[0], "exit") == 0|| strcmp(args[0], "quit") == 0){
		clear_history();
		exit(0);
	}

	//cd command
	else if(strcmp(args[0], "cd") == 0){

		//Only continue if there is a directory
		//if(strcmp(args[1], "") == 0){

			//Only continue if there are only 2 arguments
			if(sizeOfArgs < 3){
		
				//If the directory exists
				if(chdir(args[1]) == 0){
					char currentDirectory[1024];
					getcwd(currentDirectory, sizeof(currentDirectory));	
					fprintf(stdout, "%s\n", currentDirectory);
					char *commandStringForHistory = strncat(strncat(args[0], " ", MAXLINE), args[1], MAXLINE);
					add_history(commandStringForHistory, 0);
					strncpy(currentDirectory, "", 1024);
				}
				//Directory doesn't exist
				else{
					fprintf(stderr, "%s: No such file or directory\n", args[1]);
					char *commandStringForHistory = strncat(strncat(args[0], " ", MAXLINE-sizeof(args[0]) -1 ), args[1], MAXLINE - sizeof(args[0]) - sizeof(args[1]) - 1);
					add_history(commandStringForHistory, 1);
					strncpy(commandStringForHistory, "", 1024);
				}
			}
			else{
				char commandStringForHistory[MAXLINE];
				for(int i = 0; i < sizeOfArgs; i++){
					strncat(commandStringForHistory, args[i], MAXLINE - strlen(commandStringForHistory));
					if(i != sizeOfArgs-1){
						strncat(commandStringForHistory, " ", MAXLINE - strlen(commandStringForHistory));
					}
				}
				fprintf(stderr, "%s: Too many arguments\n", commandStringForHistory);
				add_history(commandStringForHistory, 1);
				strncpy(commandStringForHistory, "", 1024);
			}
		//}
	
		
	}

	//History command 
	else if(strcmp(args[0], "history") == 0){
		add_history("history", 0);
		print_history(0);
		
		
	}
	else{
		/*char *externalArgsArray[MAXLINE];
		for(int i = 0; i < sizeOfArgs; i++){
			externalArgsArray[i] = args[i];
			if(i == sizeOfArgs--){
				//strncpy(externalArgsArray[i++], '\0', MAXLINE);
				externalArgsArray[sizeOfArgs] = '\0';
			}
		}*/

		//char* nullPointer = '\0';
		//strncpy(args[sizeOfArgs], nullPointer, MAXLINE);
		args[sizeOfArgs] = '\0';
		int exitStatusEC = executeExternalCommand(args);
		char commandStringForHistory[MAXLINE];
		for(int i = 0; i < sizeOfArgs; i++){
			strncat(commandStringForHistory, args[i], MAXLINE);
			if(i != sizeOfArgs - 1){
				strncat(commandStringForHistory, " ", MAXLINE);
			}
		}
		add_history(commandStringForHistory, exitStatusEC);
		strncpy(commandStringForHistory, "", sizeof(commandStringForHistory));
	}

	//After you're done, reset the args array
	for(int i = 0; i < sizeOfArgs; i++){
		strncpy(args[i], "", 1024);	
	}
}

//void cleanArgs;
