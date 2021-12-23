#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/slab.h>
#include <linux/fs.h>
#include <linux/uaccess.h>
#include <linux/cdev.h>

MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("BSU CS 452 HW4");
MODULE_AUTHOR("<michaelcleverdon@u.boisestate.edu>");

typedef struct {
  dev_t devno;
  struct cdev cdev;
  char *separators;
} Device;			/* per-init() data */

typedef struct {
  char *s;
  char *separators;
  int changeSeparators;
  Token** tokens;
  int tokenLength;
} File;				/* per-open() data */

static Device device;

static int open(struct inode *inode, struct file *filp) {
  File *file=(File *)malloc(sizeof(*file));
  if (!file) {
    printf("%s: malloc() failed\n",DEVNAME);
    return -ENOMEM;
  }
  file->s=(char *)malloc(strlen(device.s)+1);
  if (!file->s) {
    printf("%s: malloc() failed\n",DEVNAME);
    return -ENOMEM;
  }
  strcpy(file->s,device.s);
  strcpy(file->separators, device.separators);
  file->changeSeparators = device.changeSeparators;
  filp->private_data=file;
  return 0;
}

static int release(struct inode *inode, struct file *filp) {
  File *file=filp->private_data;
  free(file->s);
  free(file->separators);
  free(file);
  return 0;
}

static ssize_t read(struct file *filp,
		    char *buf,
		    size_t count,
		    loff_t *f_pos) { 
  File *file=filp->private_data;
  int n=strlen(file->s);
  n=(n<count ? n : count);
  if (copy_to_user(buf,file->s,n)) {
    printf("%s: copy_to_user() failed\n",DEVNAME);
    return 0;
  }
  return n;
}

static ssize_t write(struct file *filep,
    char* buf,
    size_t count,
    loff_t *f_pos) {
  File *f= filep->private_data;
  f->tokens = (Tokens**)malloc(sizeof(Token*));
}
/*
init() driver init with the cdev shenanigans
exit() frees the above shenanigans
open() mallocs a new scanner. Sets all the defaults like separators and ioctl flags
ioctl() sets a flag in the scanner
  Will also free, then malloc separators if the changeSeparators flag is set
  Parses the string down to tokens on write, so read is super fast
  Need to save a struct of 
    token
    current position in the token (for when the max read is smaller than the token itself)
  in the private_data of the filp


read() get's the next token by using strtok() on each of the separators until something gets returned
  strtok takes a string of separators and uses any that match to generate tokens
  if(currentPosition != strlen(token))
    return -1
  else
    return 0

*/

static long ioctl(struct file *filp,
                 unsigned int cmd,
		 unsigned long arg) {
       if(cmd == 0){
         File *file = filep->private_data;
         file->changeSeparators = 1;
         filep->private_data = file;
       }
}

static struct file_operations ops={
  .open=open,
  .release=release,
  .read=read,
  .unlocked_ioctl=ioctl,
  .write=write,
  .owner=THIS_MODULE
};

static int init(void) {
  const char* separators = ";:\t\s"; //Init definition of separator characters
  device.separators = (char*)malloc(strlen(s)+1);
  device.changeSeparators = 0;
  strcpy(device.s, "");
  strcpy(device.separators, separators);
  // const char *s="Hello world!\n";
  // int err;
  // device.s=(char *)malloc(strlen(s)+1);
  // if (!device.s) {
  //   printf("%s: malloc() failed\n",DEVNAME);
  //   return -ENOMEM;
  // }
  // strcpy(device.s,s);
  // err=alloc_chrdev_region(&device.devno,0,1,DEVNAME);
  // if (err<0) {
  //   printf("%s: alloc_chrdev_region() failed\n",DEVNAME);
  //   return err;
  // }
  // cdev_init(&device.cdev,&ops);
  // device.cdev.owner=THIS_MODULE;
  // err=cdev_add(&device.cdev,device.devno,1);
  // if (err) {
  //   printf("%s: cdev_add() failed\n",DEVNAME);
  //   return err;
  // }
  // printf("%s: init\n",DEVNAME);
  // my_exit();
  // return 0;
}

static void my_exit(void) {
  // cdev_del(&device.cdev);
  // unregister_chrdev_region(device.devno,1);
  free(device.s);
  free(device.separators);
  free(device);
  printf("%s: exit\n",DEVNAME);
}

// module_init(my_init);
// module_exit(my_exit);
