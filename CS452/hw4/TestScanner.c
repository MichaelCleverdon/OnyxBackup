#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>

#define ERR(s) err(s,__FILE__,__LINE__)

static void err(char *s, char *file, int line) {
  fprintf(stderr,"%s:%d: %s\n",file,line,s);
  exit(1);
}

int main() {
  enum {max=100, min=5};
  char buf[max+1];
  int len;

  int scanner=open("/dev/Scanner",O_RDWR);
  if (scanner<0)
    ERR("open() failed");

  if (ioctl(scanner,0,0))
    ERR("ioctl() for separators failed");
  if (write(scanner,":",1)!=1)
    ERR("write() of separators failed");
  char *line;
  line = strdup("\nThis is the first test for general:token:parsing\n");
  len=strlen(line);
  if (len!=write(scanner,line,len))
    ERR("write() of data failed");
  free(line);
  while ((len=read(scanner,buf,max))>=0) {
    buf[len]=0;
    printf("%s%s",buf,(len ? "" : "\n"));
  }
  printf("\nExpected result: This is the first test for generaltokenparsing\n");


  line = strdup("\nThis is second test for ioctl\n");

  if (ioctl(scanner,0,0))
    ERR("ioctl() for separators failed");
  if (write(scanner," ",1)!=1)
    ERR("write() of separators failed");

  len=strlen(line);
  if (len!=write(scanner,line,len))
    ERR("write() of data failed");
  free(line);
  while ((len=read(scanner,buf,max))>=0) {
    buf[len]=0;
    printf("%s%s",buf,(len ? "" : "\n"));
  }
  printf("\nExpected Result: Thisissecondtestforioctl\n");


  // line = malloc(56);
  char line2[56] = "\nThis is\0the\0last test for null byte parsing and ioctl\n\0";//Has to be a char array otherwise things get nasty with 0 bytes
  // strncpy(line,"\nThis is\0the\0last test for null byte parsing and ioctl\n", 56);
  line[56] = '\0';
  if (ioctl(scanner,0,0))
    ERR("ioctl() for separators failed");
  if (write(scanner,"\0",1)!=1)
    ERR("write() of separators failed");
  int x = write(scanner,&line2,len);
  printf("%d", x);
  if (len!=x)//Have to hard code the length here because strlen will stop at the 0 byte
    ERR("write() of data failed");
  // free(line);
  while ((len=read(scanner,buf,max))>=0) {
    buf[len]=0;
    printf("%s%s",buf,(len ? "" : "\n"));
  }
  printf("\nExpected Result: This isthelast test for null byte parsing and ioctl\n");


  close(scanner);
  return 0;
}
