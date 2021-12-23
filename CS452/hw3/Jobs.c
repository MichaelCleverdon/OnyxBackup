#include "Jobs.h"
#include "deq.h"
#include "error.h"


extern Jobs newJobs() {
  return deq_new();
}

extern void addJobs(Jobs jobs, Pipeline p) {
  deq_tail_put(jobs, p);
}

extern int sizeJobs(Jobs jobs) {
  return deq_len(jobs);
}

extern Pipeline getJob(Jobs jobs){
  return deq_head_get(jobs);
}

extern void freeJobs(Jobs jobs) {
  deq_del(jobs,freePipeline);
}
