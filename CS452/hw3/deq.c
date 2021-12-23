#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "deq.h"
#include "error.h"

// indices and size of array of node pointers
typedef enum {Head,Tail,Ends} End;

//Node: Describes each node of the deq object
//Has np[] which has pointers to the next and previous nodes in the structure
//Data is a pointer to the data stored in the structure. DATA IS NOT AUTOMATICALLY MALLOC'D
typedef struct Node {
  struct Node *np[Ends];		// next/prev neighbors
  Data data;
} *Node;

//Rep: representation of the double ended queue structure
//Has ht[] for head and tail nodes plus a length property
typedef struct {
  Node ht[Ends];			// head/tail nodes
  int len;
} *Rep;

static Rep rep(Deq q) {
  if (!q) ERROR("zero pointer");
  return (Rep)q;
}

static void put(Rep r, End e, Data d) {
  Node newNode = (Node) malloc(sizeof(struct Node)); //If you do just Node here instead of struct Node, you will get memory errors in Valgrind
  if(!newNode) ERROR("malloc() failed");
  newNode->data = d; //Assign the data
  //Add node to the end of the list
  if(r->len == 0){ //If the node is the first thing to add to the list
    r->ht[Head] = newNode;
    r->ht[Tail] = newNode;
  }
  else{
    //Reassign some pointers in the list
    r->ht[e]->np[e] = newNode;
    if(e == Head){
      newNode->np[Tail] = r->ht[e];
    }
    else{
      newNode->np[Head] = r->ht[e];
    }

    r->ht[e] = newNode; //Reassign the end value to be the new node 
  }
  r->len++; //Increment the length
}

//Get's the i'th element from the r list starting at e end
static Data ith(Rep r, End e, int i) { 
  if(r->len < i){
    ERROR("Index out of range");
    return 0; //This is the default error call if the list is in a bad state. ERROR("message") and return 0;
  }
  int index = 0;
  Node indexNode = r->ht[e]; //Don't need to malloc this because it only exists in the Stack for this one function call
  //Traverse Head to Tail
  if(e == Head){
    while(index < i){
      indexNode = indexNode->np[Tail];
      index++;
    }
  }
  //Traverse Tail to Head
  else{
    while(index < i){
      indexNode = indexNode->np[Head];
      index++;
    }
  }
  //Data doesn't need to be malloc'd here either, because it will already have been done by the caller if the data needs it
  //Or it will just be a primitive type, which wouldn't need to be malloc'd anyways.
  //Pretty much just leave the implementation of the Data to the caller and just have it be a pointer here
  Data data = indexNode->data; 
  return data;
}

//Pops the end off the list and returns it.
static Data get(Rep r, End e) {
  if(r->len == 0){
    ERROR("Cannot remove from an empty list");
    return 0;
  }
  //Have to specify the return data here because the node must be freed and the head/tail pointer will be overwritten in the removal process
  Data ret = r->ht[e]->data;

  if(r->len == 1){
    free(r->ht[e]);
    r->ht[Head] = NULL;
    r->ht[Tail] = NULL;
  }
  else{
    //Reassign pointers for neighbors
    Node nodeToRemove = r->ht[e];
    if(e == Head){
      r->ht[e] = r->ht[e]->np[Tail];
      r->ht[e]->np[e] = NULL;
    }
    else{
      r->ht[e] = r->ht[e]->np[Head];
      r->ht[e]->np[e] = NULL;
    }
    //Delete the node from memory
    free(nodeToRemove);
  }
  r->len--;

  return(ret);
}

//Removes the first instance of Data d starting the search from End e
//Then returns that Data value
static Data rem(Rep r, End e, Data d) { 
  
  //Traverse Head to Tail
  Node nodeIndex = r->ht[e];
  int index = 0;
  if(r->len == 0){
    ERROR("Cannot remove from an empty list");
    return 0;
  }

  if(r->len == 1){
    //Only element to remove
    if(r->ht[e]->data == d){
      free(nodeIndex);
      r->ht[Head] = NULL;
      r->ht[Tail] = NULL;
      r->len--;
      return d;
    }
    else{
      ERROR("Could not find object");
      return 0;
    }
  }

//Search Head to Tail
  if(e == Head){
    while(nodeIndex->data != d && index < r->len){
      nodeIndex = nodeIndex->np[Tail];
      index++;
    }
  }
  //Search Tail to Head
  else{
    while(nodeIndex->data != d && index < r->len){
      nodeIndex = nodeIndex->np[Head];
      index++;
    }
  }
  if(nodeIndex->data != d){
    ERROR("Could not find element to remove");
    return 0;
  }
  else{
    //Remove the item from the list
    if(e == Head){
      if(index == 0){
          //Reset the pointers
          r->ht[Head] = r->ht[Head]->np[Tail];
          r->ht[Head]->np[Head] = NULL;
      }
      else if(index != r->len-1){
        //Check Head neighbor
        if(nodeIndex->np[Head]){
          nodeIndex->np[Head]->np[Tail] = nodeIndex->np[Tail];
        }
        //Check Tail Neighbor
        if(nodeIndex->np[Tail]){
          nodeIndex->np[Tail]->np[Head] = nodeIndex->np[Head];
        }
      }
      //Index is at the end of the list
      else{
        r->ht[Tail] = nodeIndex->np[Head];
        r->ht[Tail]->np[Tail] = NULL;
      }
    }
    //Tail Search
    else{
      if(index == 0){
          //Reset the pointers
          r->ht[Tail] = r->ht[Tail]->np[Head];
          r->ht[Tail]->np[Tail] = NULL;
      }
      //If the node is in the middle of the list
      else if(index != r->len-1){
        //Check Head neighbor
        if(nodeIndex->np[Head] != NULL){
          nodeIndex->np[Head]->np[Tail] = nodeIndex->np[Tail];
        }
        //Check Tail Neighbor
        if(nodeIndex->np[Tail] != NULL){
          nodeIndex->np[Tail]->np[Head] = nodeIndex->np[Head];
        }
      }
      //Index is at the end of the list
      else{
        r->ht[Head] = nodeIndex->np[Tail];
        r->ht[Head]->np[Head] = NULL;
      }
    }
    Data ret = nodeIndex->data; // Look at the get function for the reason we're doing this
    free(nodeIndex);
    
    r->len--;
    return(ret);
  }
 }

//Creates a new Double Ended Queue Data Structure
extern Deq deq_new() {
  Rep r=(Rep)malloc(sizeof(*r));
  if (!r) ERROR("malloc() failed");
  r->ht[Head]=0;
  r->ht[Tail]=0;
  r->len=0;
  return r;
}

extern int deq_len(Deq q) { return rep(q)->len; }

extern void deq_head_put(Deq q, Data d) {        put(rep(q),Head,d); }
extern Data deq_head_get(Deq q)         { return get(rep(q),Head); }
extern Data deq_head_ith(Deq q, int i)  { return ith(rep(q),Head,i); }
extern Data deq_head_rem(Deq q, Data d) { return rem(rep(q),Head,d); }

extern void deq_tail_put(Deq q, Data d) {        put(rep(q),Tail,d); }
extern Data deq_tail_get(Deq q)         { return get(rep(q),Tail); }
extern Data deq_tail_ith(Deq q, int i)  { return ith(rep(q),Tail,i); }
extern Data deq_tail_rem(Deq q, Data d) { return rem(rep(q),Tail,d); }

extern void deq_map(Deq q, DeqMapF f) {
  for (Node n=rep(q)->ht[Head]; n; n=n->np[Tail])
    f(n->data);
}

extern void deq_del(Deq q, DeqMapF f) {
  if (f) deq_map(q,f);
  Node curr=rep(q)->ht[Head];
  while (curr) {
    Node next=curr->np[Tail];
    free(curr);
    curr=next;
  }
  free(q);
}

extern Str deq_str(Deq q, DeqStrF f) {
  char *s=strdup("");
  for (Node n=rep(q)->ht[Head]; n; n=n->np[Tail]) {
    char *d = f ? f(n->data) : n->data;
    char *t; asprintf(&t,"%s%s%s",s,(*s ? " " : ""),d);
    free(s); s=t;
    if (f) free(d);
  }
  return s;
}
