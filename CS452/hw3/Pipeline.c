#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#include "Pipeline.h"
#include "deq.h"
#include "error.h"

typedef struct {
  Deq processes;
  int fg;			// not "&"
} *PipelineRep;

extern Pipeline newPipeline(int fg) {
  PipelineRep r=(PipelineRep)malloc(sizeof(*r));
  if (!r)
    ERROR("malloc() failed");
  r->processes=deq_new();
  r->fg=fg;
  return r;
}

extern void addPipeline(Pipeline pipeline, Command command) {
  PipelineRep r=(PipelineRep)pipeline;
  deq_tail_put(r->processes,command);
}

extern int sizePipeline(Pipeline pipeline) {
  PipelineRep r=(PipelineRep)pipeline;
  return deq_len(r->processes);
}

static void execute(Pipeline pipeline, Jobs jobs, int *jobbed, int *eof) {
  PipelineRep r=(PipelineRep)pipeline;
  int fd[2*(sizePipeline(r)-1)];
  int stdinFD = dup(STDIN_FILENO);
  int stdoutFD = dup(STDOUT_FILENO);
  //Create all of the pipes for the pipeline
  for(int i = 0; i < sizePipeline(r)-1; i++)
    if(pipe(fd + i*2) < 0)
      ERROR("Failed to create pipe");
  
  for (int i=0; i<sizePipeline(r) && !*eof; i++){
    //No pipes here
    if(sizePipeline(r) == 1){
      execCommand(deq_head_ith(r->processes,i),pipeline,jobs,jobbed,eof,r->fg);
    }
    //There be some pipes in the mix
    else{
      //If not first command, overwrite stdin with pipe
      if(i != 0)
        dup2(fd[2*(i-1)], STDIN_FILENO);
      //If not last command, overwrite stdout
      if(i != sizePipeline(r)-1)
        dup2(fd[(2*i)+1], STDOUT_FILENO);
      //If last command, re-write stdout
      else
        dup2(stdoutFD, STDOUT_FILENO);

      execCommand(deq_head_ith(r->processes,i),pipeline,jobs,jobbed,eof,r->fg);
      close(fd[2*(i-1)]); //No need to have the input or output stay open here
      if(i != sizePipeline(r)-1) //Don't try to access out of bounds
        close(fd[2*i+1]);
      
    }
  }
  //Cleanup
  dup2(stdinFD, STDIN_FILENO);
  dup2(stdoutFD, STDOUT_FILENO); //This isn't necessarily needed, but I feel better about keeping it in
  close(stdinFD);
  close(stdoutFD);
}

extern void execPipeline(Pipeline pipeline, Jobs jobs, int *eof) {
  int jobbed=0;
  execute(pipeline,jobs,&jobbed,eof);
  if (!jobbed)
    freePipeline(pipeline);	// for fg builtins, and such
}

extern void freePipeline(Pipeline pipeline) {
  PipelineRep r=(PipelineRep)pipeline;
  deq_del(r->processes,freeCommand);
  free(r);
}
