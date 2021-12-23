#include <stdio.h>
#include <stdlib.h>

#include <string.h>
#include "deq.h"

int main() {
  Deq q=deq_new();

  char hiStringArray[] = "Hi";
  char* hiString = hiStringArray;
  char* helloString = "Hello";
  char* howdyString = "Howdy";
  char* newString = "New";
  char* hi1String = "Hi1";
  char* hi2String = "Hi2";
//Create [Hello, Hi, New, Hi, Hi, Howdy]
  deq_tail_put(q, hiString);
  deq_tail_put(q, newString);
  deq_tail_put(q, hi1String);
  deq_tail_put(q, hi2String);
  deq_tail_put(q, howdyString);
  
  deq_head_put(q, helloString);

//Should return Hi1
printf("\ndeq_tail_ith 2 test\n");
  char* retString = deq_tail_ith(q, 2);
  printf("Expected: Hi1. Actual: %s\n",retString);
  //free(retString);

//Should return Howdy
  printf("\ndeq_tail_ith 0 test\n");
  retString = deq_tail_ith(q,0);
  printf("Expected: Howdy. Actual: %s\n", retString);
  //free(retString);

//Should return Hello
  printf("\ndeq_head_ith 0 test\n");
  retString = deq_head_ith(q,0);
  printf("Expected: Hello. Actual: %s\n", retString);

//Should remove Hello
printf("\ndeq_head_get test\n");
  retString = deq_head_get(q);
  printf("Expected: Hello. Actual: %s\n", retString);
//List is currently [Hi, New, Hi1, Hi2, Howdy]

printf("\ndeq_tail_rem test\n");
  retString = deq_tail_rem(q, hi2String);
//List should now be [Hi, New, Hi1, Howdy]
  //Should print Hi2
  printf("Expected: Hi2. Actual: %s\n",retString);
  //free(retString);

printf("\ndeq_head_rem test\n");
  retString = deq_head_rem(q, hiString);
//List should now be [New, Hi1, Howdy]
  //Should print Hi
  printf("Expected: Hi. Actual: %s\n",retString);

  printf("\ndeq_str test\n");
  char* toString = deq_str(q, 0);
  //Should print "New", "Hi1", "Howdy"
  printf("Expected: New Hi1 Howdy Actual: %s\n", toString);
  //The returned value of deq_str is a malloc'd string. Free is
  free(toString);

// free(hiString);
// free(helloString);
// free(newString);
// free(howdyString);
  deq_del(q,0);
  return 0;
}
