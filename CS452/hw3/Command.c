#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <readline/history.h>
#include <sys/wait.h>

#include "Command.h"
#include "Pipeline.h"
#include "Redir.h"
#include "error.h"
#include "deq.h"

typedef struct {
//Command stuff
  char *file;
  char **argv;
  int pid;
//Redir stuff
  int isInput; //Describes the first input direction. 1 is an input '<' 0 is an output '>'
  char* file1;
  char* file2;
} *CommandRep;

typedef struct {
  Deq processes;
  int fg;			// not "&"
} *PipelineRep;

#define BIARGS CommandRep r, int *eof, Jobs jobs
#define BINAME(name) bi_##name
#define BIDEFN(name) static void BINAME(name) (BIARGS)
#define BIENTRY(name) {#name,BINAME(name)}

static char *owd=0;
static char *cwd=0;

static void builtin_args(CommandRep r, int n) {
  char **argv=r->argv;
  for (n++; *argv++; n--);
  if (n)
    ERROR("wrong number of arguments to builtin command"); // warn
}

BIDEFN(exit) {
  builtin_args(r,0);
  int status;
  while(sizeJobs(jobs)){
    PipelineRep r = (PipelineRep)getJob(jobs);
    for(int i = 0; i < deq_len(r->processes); i++){
      CommandRep c = (CommandRep)deq_head_ith(r->processes, i);
      if(c->pid != 0 && waitpid(c->pid, &status, 0) < 0){
        // char *string = "Process %s didn't wait before exiting";
        // sprintf(string, c->file);
        // ERROR(string);
        ERROR("Processes didn't wait before exiting");
      }
    }
    freePipeline(r);
  }
  // free(cwd);
  // free(owd);
  freeJobs(jobs);
  *eof=1;
}

BIDEFN(pwd) {
  builtin_args(r,0);
  if (!cwd)
    cwd=getcwd(0,0);
  printf("%s\n",cwd);
}

BIDEFN(cd) {
  builtin_args(r,1);
  if (strcmp(r->argv[1],"-")==0) {
    char *twd=cwd;
    cwd=owd;
    owd=twd;
  } else {
    if (owd) free(owd);
    if(!cwd)
      cwd = getcwd(0,0);
    owd=cwd;
    cwd = r->argv[1];
  }
  if (cwd && chdir(cwd))
    ERROR("chdir() failed"); // warn
  else 
    cwd = getcwd(0,0);
}

BIDEFN(history){
  builtin_args(r, 0);
  HIST_ENTRY** history = (HIST_ENTRY**)history_list();
  // HISTORY_STATE* historyState = (HISTORY_STATE*)history_get_history_state();
  if(!history)
    ERROR("There is no history for this shell instance");
  else{
    int i = 0;
    while(history[i]){
      printf("%s\n", (char*)history[i]->line);
      i++;
    }
  }
}

static int builtin(BIARGS) {
  typedef struct {
    char *s;
    void (*f)(BIARGS);
  } Builtin;
  static const Builtin builtins[]={
    BIENTRY(exit),
    BIENTRY(pwd),
    BIENTRY(cd),
    BIENTRY(history),
    {0,0}
  };
  int i;
  for (i=0; builtins[i].s; i++)
    if (!strcmp(r->file,builtins[i].s)) {
      int stdinFD = dup(STDIN_FILENO);
      int stdoutFD = dup(STDOUT_FILENO);
      if(r->file1){//Only redirect if there's a file to redirect to
        execRedir(r->isInput, r->file1, r->file2);
      }
      builtins[i].f(r,eof,jobs);
      dup2(stdinFD, STDIN_FILENO);
      dup2(stdoutFD, STDOUT_FILENO);
      close(stdinFD);
      close(stdoutFD);
      return 1;
    }
  return 0;
}

static char **getargs(T_words words) {
  int n=0;
  T_words p=words;
  //Loop through the linked list that is t_words
  while (p) {
    p=p->words;
    n++;
  }
  //Create an argv array of n+1 because null terminated array
  char **argv=(char **)malloc(sizeof(char *)*(n+1));
  if (!argv)
    ERROR("malloc() failed");
  p=words;
  int i=0;
  while (p) {
    //Start at 1 because argv[0] is the program name
    argv[i++]=strdup(p->word->s);
    p=p->words;
  }
  //Null terminated
  argv[i]=0;
  return argv;
}

extern Command newCommand(T_words words, T_redir redir) {
  CommandRep r=(CommandRep)malloc(sizeof(*r));
  if (!r)
    ERROR("malloc() failed");
  r->argv=getargs(words);
  r->file=r->argv[0];
  r->pid = 0;
  if(redir->direction1){
    if(strcmp(redir->direction1, "<") == 0)
      r->isInput = 1;
    else
      r->isInput = 0;
    r->file1 = redir->word1->s;
    if(redir->word2)
      r->file2 = redir->word2->s;
    else
      r->file2 = 0;
  }
  else{
    r->file1 = 0;
    r->file2 = 0;
  }
  return r;
}

//This is the method that executes the child processes
static void child(CommandRep r, int fg) {
  int eof=0;
  Jobs jobs=newJobs();
  //only exec the redirect if there's a file to redirect to
  if(r->file1){
    execRedir(r->isInput, r->file1, r->file2);
  }
  if (builtin(r,&eof,jobs))
    exit(0);
  execvp(r->argv[0],r->argv);
  ERROR("execvp() failed");
  exit(0);
}

extern void execCommand(Command command, Pipeline pipeline, Jobs jobs,
			int *jobbed, int *eof, int fg) {
  CommandRep r=command;
  if (fg && builtin(r,eof,jobs))
    return;
  if (!*jobbed) {
    *jobbed=1;
    addJobs(jobs, pipeline);
  }
  int pid=fork();
  if (pid==-1)
    ERROR("fork() failed");
  //Child
  if (pid==0){
    //Add the running command to the existing jobs list of things running in the background
    child(r,fg);
  }
  //Parent
  if(fg)
    while(wait(&pid)>0); //Had a bug where sleep wouldn't wait after a bg process, this fixed it
  else
    r->pid = pid;
}

extern void freeCommand(Command command) {
  CommandRep r=command;
  char **argv=r->argv;
  while (*argv)
    free(*argv++);
  free(r->argv);
  free(r);
}

extern void freestateCommand() {
  if (cwd) free(cwd);
  if (owd) free(owd);
}
