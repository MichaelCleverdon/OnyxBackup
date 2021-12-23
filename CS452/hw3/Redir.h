#ifndef REDIR_H
#define REDIR_H

typedef void* Redir;

#include "Tree.h"

extern int execRedir(int isInput, char* file1, char* file2);

#endif