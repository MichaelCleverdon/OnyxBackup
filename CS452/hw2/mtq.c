#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#include "deq.h"
#include "mtq.h"
#include "error.h"

typedef struct {
    struct Deq *deq;
    pthread_cond_t isFilled;
    pthread_cond_t isEmptied;
    pthread_mutex_t lock;
    int maxCapacity;
} *MtqRep;

static MtqRep mtq_rep(Mtq mtq){
    if(!mtq) ERROR("Zero pointer");
    return (MtqRep)mtq;
}

extern void mtq_del(Mtq mtq, DeqMapF f){
    //delete deq and all the threading stuff
    MtqRep mtqR = mtq_rep(mtq);
    deq_del(mtq_rep(mtq)->deq,0);
    pthread_cond_destroy(&mtqR->isFilled);
    pthread_cond_destroy(&mtqR->isEmptied);
    pthread_mutex_destroy(&mtqR->lock);
    free(mtq); //Always remember to free the queue
}

extern Mtq mtq_new(int maxCapacity){
    //Pretty much copied from deq_new
    MtqRep r = (MtqRep)malloc(sizeof(*r));
    if(!r) ERROR ("Malloc() failed");
    r->deq = deq_new();
    r->maxCapacity = maxCapacity;
    pthread_mutex_init(&r->lock,0);
    pthread_cond_init(&r->isFilled,0);
    pthread_cond_init(&r->isEmptied,0);
    return r;
}

extern Data mtq_head_get(Mtq r){
    MtqRep mtqR = mtq_rep(r);
    pthread_mutex_lock(&mtqR->lock);
    //Check if the queue is empty
    while(deq_len(mtqR->deq) == 0)
        //If it is, wait for it to be filled
        pthread_cond_wait(&mtqR->isFilled, &mtqR->lock);
    Data d = deq_head_get(mtqR->deq);
    printf("lawnimp_mole retrieved from queue\n");
    //Signal that the queue has been emptied
    pthread_cond_signal(&mtqR->isEmptied);
    pthread_mutex_unlock(&mtqR->lock);
    return d;
}

extern void mtq_tail_put(Mtq r, Data d){
    MtqRep mtqR = mtq_rep(r);
    pthread_mutex_lock(&mtqR->lock);
    if(mtqR->maxCapacity != 0){ //Unbounded
        //Check if the queue is at capacity
        while(deq_len(mtqR->deq) >= mtqR->maxCapacity)
            //Wait for it to be emptied
            pthread_cond_wait(&mtqR->isEmptied, &mtqR->lock);
    }
    deq_tail_put(mtqR->deq, d);
    printf("lawnimp_mole inserted into queue\n");
    //Signal that the queue has been filled
    pthread_cond_signal(&mtqR->isFilled);
    pthread_mutex_unlock(&mtqR->lock);
}