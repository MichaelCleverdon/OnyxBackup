#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>

#include "lawn.h"
#include "mole.h"
#include "deq.h"
#include "mtq.h"

static Mtq mtq;

typedef struct {
  Mtq q;
  Lawn l;
} *PassData;
// static Mole produce(Lawn l) { 
//   Mole m = mole_new(l,0,0); 
//   deq_tail_put(q, m);
//   return m;
// }

static void *produce(void *a){
  void **arg = a;
  Deq mtq = (Deq)arg[0];
  Lawn l = (Lawn)arg[1];
  mtq_tail_put(mtq, mole_new(l,0,0));
  return 0;
}

static void *consume(void *a) { 
  void **arg = a;
  Mtq mtq = (Mtq)arg[0];
  //This is put into here to make sure we see some actual limitation of the queue because otherwise consume will
  //almost always run faster than produce due to produce needing to create a mole before adding it to the queue
  sleep(5);
  Mole mole = mtq_head_get(mtq); 
  mole_whack(mole);
  return 0;  
}

int main() {
  mtq = mtq_new(2);
  srandom(time(0));
  const int n=10;
  pthread_t tids[2*n];

  Lawn lawn=lawn_new(0,0);
  PassData pd = (PassData)malloc(sizeof(*pd));
  //Set the data of the PassData
  pd->q = mtq;
  pd->l = lawn;
  for (int i=0; i<2*n; i+=2){ //N must ALWAYS be even. This will cause Index Out of Bounds errors if it is odd
    pthread_create(&tids[i],0,produce,(void*)pd);
    pthread_create(&tids[i+1],0,consume,(void*)pd);
  }
  for (int i=0; i<2*n; i++)
    pthread_join(tids[i],0);
  
  //Free the stuff
  free(pd);
  mtq_del(mtq,0);
  lawn_free(lawn);
}
