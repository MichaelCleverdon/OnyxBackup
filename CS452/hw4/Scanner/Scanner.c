#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/slab.h>
#include <linux/fs.h>
#include <linux/uaccess.h>
#include <linux/cdev.h>

MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("BSU CS 452 HW4");
MODULE_AUTHOR("<buff@cs.boisestate.edu>");

typedef struct {
  dev_t devno;
  struct cdev cdev;
  char *separators;
} Device;			/* per-init() data */

typedef struct {
    char* s;
    int length; 
    int pos;
} Token;

typedef struct {
  Token** tokens; //Pointer to a Token pointer; functions as an array of tokens.
  char* separators; //Stored separators, mallocd
  int changeSeparators; //Flag to signal that we need to change separators
  int numOfToken; //Number of tokens
  int pos; //Position in the tokens array
  
} File;				/* per-open() data */

static Device device;


void freeTokens(File* f){
    int i;
    if(f->tokens != NULL){
      for(i = 0; i < f->numOfToken; i++){
          Token* t = f->tokens[i];
          if(t != NULL){ //Already freed
            printk(KERN_ERR "DEBUG: Freeing string %s", t->s);
              kfree(t->s);
              kfree(t);
              t=NULL;
          }
      }
      printk(KERN_ERR "DEBUG: Freeing tokens array");
      kfree(f->tokens);
    }
}

static int open(struct inode *inode, struct file *filp) {
  File *file=(File *)kmalloc(sizeof(*file),GFP_KERNEL);
  if (!file) {
    printk(KERN_ERR "%s: kmalloc() failed on open->file\n",DEVNAME);
    return -ENOMEM;
  }
  file->separators=(char *)kmalloc(strlen(device.separators)+1,GFP_KERNEL);
  if (!file->separators) {
    printk(KERN_ERR "%s: kmalloc() failed on open->separators\n",DEVNAME);
    return -ENOMEM;
  }
  strcpy(file->separators,device.separators);
  file->numOfToken = 0;
  file->changeSeparators = 0;
  file->pos = 0;
  file->tokens = NULL;

  filp->private_data=file;
  return 0;
}

static int release(struct inode *inode, struct file *filp) {
  File *file=filp->private_data;
  freeTokens(file); //Frees the tokens and the array
  printk(KERN_ERR "DEBUG: Freeing separators: %s", file->separators);
  kfree(file->separators);
  printk(KERN_ERR "DEBUG: Freeing file");
  kfree(file);
  return 0;
}

static ssize_t read(struct file *filp,
		    char *buf,
		    size_t max,
		    loff_t *f_pos) { 
  File *file=filp->private_data;
    if(file->tokens == NULL){ //Data hasn't been written yet
        printk(KERN_ERR "%s: Data has not been written yet\n",DEVNAME);
        return -1;
    }
    Token* token = file->tokens[file->pos];
    if(token != NULL){
        int n=strlen(token->s);
        n=(n<max ? n : max);
        if (copy_to_user(buf,token->s+token->pos,n)) {
            printk(KERN_ERR "%s: copy_to_user() failed\n",DEVNAME);
            return 0;
        }
        if(max + token->pos >= token->length){ //If it read to the end of the token
            int length = token->length - token->pos;
            kfree(token->s);
            kfree(token);
            token = NULL;
            file->tokens[file->pos] = NULL;
            file->pos++;
            if(file->pos == file->numOfToken){
                kfree(file->tokens);
                file->tokens = NULL;
            }
            return length;
        }
        else{ //Still more data in the token
            token->pos = max;
            return max;
        }
    }
    //No more tokens in the array
    printk(KERN_ERR "%s: No more tokens in the array\n",DEVNAME);
    return -1;
}

static ssize_t write(struct file *filp,
		    const char *constStr,
		    size_t len,
		    loff_t *f_pos) { 
    File *file=filp->private_data;
    // char* str = (char*)kmalloc(len+1, GFP_KERNEL);
    // strncpy(str, constStr, len);
    // str[len] = '\0';
    printk(KERN_ERR "%s: String being written is: %s\n",DEVNAME, constStr);
    if(file->changeSeparators == 1){
          kfree(file->separators);
          file->separators = (char*)kmalloc(len+1, GFP_KERNEL);
          if (!file->separators) {
            printk(KERN_ERR "%s: kmalloc() failed when setting separators in write\n",DEVNAME);
            return -ENOMEM;
          }
          strncpy(file->separators,constStr, len);
          file->separators[len] = '\0';
          file->changeSeparators = 0;
          printk(KERN_ERR "%i: size of separators after write\n", strlen(file->separators));
          printk(KERN_ERR "%i: size of input after write\n", strlen(constStr));

          return len;
    }
    printk(KERN_ERR "Write() is not writing separators");
    int tokenArrayLength;
    tokenArrayLength = 0;
    int stringLength;
    stringLength = 0;
    char** stringTokens;
    stringTokens = (char**)kmalloc(sizeof(char*)*len, GFP_KERNEL);
    stringTokens[tokenArrayLength] = NULL;
    while(stringLength < len){
        int i = strcspn(constStr+stringLength, file->separators);
        char* token;
        
        token = (char*)kmalloc(sizeof(char) * i+1, GFP_KERNEL);
        if (!token) {
          printk(KERN_ERR "%s: kmalloc() failed when setting separators in write\n",DEVNAME);
          return -ENOMEM;
        } 
        strncpy(token, constStr+stringLength, i);
        token[i] = '\0'; //Terminate the string
        stringTokens[tokenArrayLength] = token;
        stringLength += i+1; //keep track of the total string length read so far. +1 for the separator we removed
        tokenArrayLength++;
    }
    stringTokens[tokenArrayLength] = NULL;

    if(file->tokens != NULL){ //Wipe out the existing tokens array before mallocing new stuff
        freeTokens(file);
    }
    Token** tokenArray;
    tokenArray = kmalloc(sizeof(*tokenArray) * (tokenArrayLength+1), GFP_KERNEL);//+1 for the null termination entry at the end
    int i = 0;
    while(i < tokenArrayLength){
        Token* t = (Token*)kmalloc(sizeof(*t), GFP_KERNEL);
        t->s = stringTokens[i];
        t->length = strlen(stringTokens[i]);
        t->pos = 0;
        tokenArray[i] = t;
        i++;
    }
    kfree(stringTokens);
    // kfree(str);
    tokenArray[i] = NULL;
    file->tokens = tokenArray;
    file->numOfToken = tokenArrayLength;
    file->pos = 0;
    return stringLength-1; //-1 because we added an extra at the end of the string
}

static long ioctl(struct file *filp,
                 unsigned int cmd,
		 unsigned long arg) {
  if(!cmd && !arg){ //The set separators command is 0
        File *file=filp->private_data;
        file->changeSeparators = 1;
        printk(KERN_ERR "Changed separators\n");
        return 0;
  }
  return -1;
}


static struct file_operations ops={
  .open=open,
  .release=release,
  .read=read,
  .write=write,
  .unlocked_ioctl=ioctl,
  .owner=THIS_MODULE
};

static int __init my_init(void) {
  const char *separators=";: \t"; //Default separators
  int err;
  device.separators=(char *)kmalloc(strlen(separators)+1,GFP_KERNEL);
  if (!device.separators) {
    printk(KERN_ERR "%s: kmalloc() failed\n",DEVNAME);
    return -ENOMEM;
  }
  strcpy(device.separators,separators);
  err=alloc_chrdev_region(&device.devno,0,1,DEVNAME);
  if (err<0) {
    printk(KERN_ERR "%s: alloc_chrdev_region() failed\n",DEVNAME);
    return err;
  }
  cdev_init(&device.cdev,&ops);
  device.cdev.owner=THIS_MODULE;
  err=cdev_add(&device.cdev,device.devno,1);
  if (err) {
    printk(KERN_ERR "%s: cdev_add() failed\n",DEVNAME);
    return err;
  }
  printk(KERN_INFO "%s: init\n",DEVNAME);
  return 0;
}

static void __exit my_exit(void) {
  cdev_del(&device.cdev);
  unregister_chrdev_region(device.devno,1);
  kfree(device.separators);
  printk(KERN_INFO "%s: exit\n",DEVNAME);
}

module_init(my_init);
module_exit(my_exit);
