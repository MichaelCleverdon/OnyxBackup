#ifndef MTQ_H
#define MTQ_H

typedef void* Mtq;
typedef void* Data;

extern Mtq mtq_new(int size);
extern Data mtq_head_get(Mtq mtq);
extern void mtq_tail_put(Mtq mtq, Data d);
extern void mtq_del(Mtq q, DeqMapF f);

#endif