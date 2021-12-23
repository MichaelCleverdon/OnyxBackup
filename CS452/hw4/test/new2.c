#include <string.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    char* s;
    int length; 
    int pos;
} Token;

typedef struct {
    Token** tokens;
    char* separators;
    int changeSeparators;
} *Data;

int read(Data d, char* buf, int max){
    Token** tokens = d->tokens;
    Token* token = *tokens;
    if(token != NULL){
    strncpy(buf, token->s + token->pos, max);
        if(max + token->pos >= token->length){
            d->tokens++;
            free(token);
            return 0;
        }
        else{
            token->pos = max;
            return 1;
        }
    }
    return -1;
}
int ioctl(Data d, int cmd, int command){
    if(!cmd && !command){
        d->changeSeparators = 1;
        return 1;
    }
    return 0;
}

int write(Data d, char* str, int len){
    if(d->changeSeparators == 1){
        free(d->separators);
        d->separators = strdup(str);
        return 0;
    }
    int tokenArrayLength = 0;
    int stringLength = 0;
    str[strlen(str)] = '\0'; //Ensure null termination for strtok
    // Token** tempTokenArray = (Token**)malloc(sizeof(Token**));
    char* stringTokens[len];
    stringTokens[tokenArrayLength] = NULL;
    char* token = strtok(str, d->separators);
    while(stringLength < len){
        stringTokens[tokenArrayLength] = token;
        tokenArrayLength++;
        stringLength += strlen(token) + 1; //+1 for the separator
        token = strtok(NULL, d->separators);
    }
    stringTokens[tokenArrayLength] = NULL;
    Token** tokenArray = malloc(sizeof(*tokenArray) * (tokenArrayLength+1));//+1 for the null termination entry
    int i = 0;
    while(i < tokenArrayLength){
        Token* t = (Token*)malloc(sizeof(*t));
        t->s = stringTokens[i];
        t->length = strlen(stringTokens[i]);
        t->pos = 0;
        tokenArray[i] = t;
        i++;
    }
    tokenArray[i] = NULL;
    d->tokens = tokenArray;
    return stringLength-1;
}

int main(){
//     char* str1 = "This is a;test";
//     char str[strlen(str1)+1];
//     strcpy(str, str1);
//     char* sep = "-;: ";
//     char** tokens;
//     int i = 0;
//     tokens[i] = strtok(str, sep);

//     while(tokens[i] != NULL){
//         printf("Token is: %s\n", tokens[i]);

//         // str1 += strlen(token);
        
//         // printf("Rest of the string is: %s\n", str1);
//         i++;
//         tokens[i] = strtok(NULL, sep);
//     }
//     for(int j = 0; j <= i; j++){
//         if(tokens[j]){
//             printf("String Token: %s\n", tokens[j]);
//         }
//     }
    enum {max=100};
    char buf[max+1];

    Data d = (Data)malloc(sizeof(*d));
    d->changeSeparators = 0;
    d->separators = ";: \t";
    char* strP = "abc;def;ghi;jklmn";
    char stringToToken[strlen(strP)+1];
    strcpy(stringToToken, strP);
    write(d, stringToToken, strlen(stringToToken));
    
    while(read(d, buf, max) >= 0){
        buf[strlen(buf)]= 0;
        printf("Token: %s\n", buf);
    }
    
    while(*(d->tokens) != NULL){
        Token* t = *(d->tokens);
        free(t);
        d->tokens++;
    }
    // free(d->tokens);
    free(d);
    return 0;
}