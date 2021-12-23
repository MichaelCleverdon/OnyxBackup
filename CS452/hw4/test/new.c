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
    int numOfToken;
    int pos;
} *Data;


void freeTokens(Data d){
    for(int i = 0; i < d->numOfToken; i++){
        Token* t = d->tokens[i];
        if(t != NULL){ //Already freed
            free(t->s);
            free(t);
            t=NULL;
        }
    }
    free(d->tokens);
}

int read(Data d, char* buf, int max){
    if(d->tokens == NULL){
        printf("ERROR: no more tokens");
        return -1;
    }
    Token* token = d->tokens[d->pos];
    if(token != NULL){
        strncpy(buf, token->s + token->pos, max);
        if(max + token->pos >= token->length){
            //a b c d e
            //Max = 2
            //
            int length = token->length - token->pos;
            free(token->s);
            free(token);
            d->tokens[d->pos] = NULL;
            d->pos++;
            if(d->pos == d->numOfToken){
                free(d->tokens);
                d->tokens = NULL;
            }
            return length;
        }
        else{
            token->pos = max;
            return max;
        }
    }
    return -1;
}
int ioctl(Data d, int cmd, int command){
    if(!cmd && !command){
        d->changeSeparators = 1;
        return 0;
    }
    return -1;
}

int write(Data d, const char* constStr, int len){
    // char strCopy[len+1];
    // strncpy(strCopy, str, len+1);
    // char* strP = &strCopy[0]; //This is to prevent messing with the original string that's passed in
    // char* str = (char*)malloc(len);
    // strncpy(str, constStr, len);
    // str[len] = '\0';
    if(d->changeSeparators == 1){
        free(d->separators);
        d->separators = strdup(constStr);
        d->changeSeparators = 0;
        return 0;
    }
    int tokenArrayLength = 0;
    int stringLength = 0;
    //str[strlen(str)] = '\0'; //Ensure null termination for strtok
    // Token** tempTokenArray = (Token**)malloc(sizeof(Token**));
    char** stringTokens = malloc(sizeof(char*)*len);
    stringTokens[tokenArrayLength] = NULL;
    while(stringLength < len){
        // stringTokens[tokenArrayLength] = token;
        // tokenArrayLength++;
        // stringLength += strlen(token) + 1; //+1 for the separator
        // token = strtok(NULL, d->separators);
        int i = strcspn(constStr+stringLength, d->separators);
        char* token;
        
        token = (char*)malloc(sizeof(i+1));
        if (!token) {
          printf("%s: kmalloc() failed when setting separators in write\n", "hi");
          return -1;
        } 
        strncpy(token, constStr+stringLength, i);
        token[i] = '\0';
        stringTokens[tokenArrayLength] = token;
        stringLength += i+1;
        tokenArrayLength++;
    }
    stringTokens[tokenArrayLength] = NULL;

    if(d->tokens != NULL){ //Wipe out the existing tokens array before mallocing new stuff
        freeTokens(d);
    }
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
    free(stringTokens);
    tokenArray[i] = NULL;
    d->tokens = tokenArray;
    d->numOfToken = tokenArrayLength;
    d->pos = 0;
    return stringLength-1;
}



int main(){
    enum {max=100};
    char buf[max+1];

    Data d = (Data)malloc(sizeof(*d));
    d->changeSeparators = 0;
    d->separators = strdup(";: \t\0");
    d->tokens = NULL;
    char* strP = "abc\0def\0ghi\0jklmn";
    char stringToToken[strlen(strP)+1];
    strncpy(stringToToken, strP, 17);
    // ioctl(d, 0, 0);
    // write(d, ":", 1);
    // char test[strlen(stringToToken)];
    // for(int j = 0; j < 4; j++){
    //     int i = strcspn(strP, d->separators);
    //     printf("%d", i);
    //     strncpy(test, strP, i);
    //     test[i] = '\0';
    //     strP += strcspn(strP, d->separators) +1;
    //     printf("Test: %s\n", test);
    // }
    int len;
    char* line;
    scanf("%m[^\n]",&line);
    if(*line!=EOF) {
        len=strlen(line);
        if (len!=write(d,line,len))
            printf("write() of data failed");
        free(line);
        while ((len=read(d,buf,max))>=0) {
        buf[len]=0;
        printf("%s%s",buf,(len ? " " : "\n"));
        }
    }
    
    freeTokens(d);
    free(d->separators);
    free(d);
    return 0;
}