//Include definitions for C Runtime Library functions used in this program
#include <stdio.h>				//The standard I/O functions

//-------------------------------------------------------------------------------
//This is the main function, invoked by the C Runtime (CRT) to start the program
//-------------------------------------------------------------------------------
int main(void) {
  int vowels = 0;
  int consonants = 0;
  int numbers = 0;
  int upperCase = 0;
  int lowerCase = 0;
  int total = 0;
    //Insert your source statements here
  int c;
//If the read character isn't the end of the file
  while((c = getchar()) != EOF){
	switch(c){
	//Stacking these "case" statements will run the first code block found after the case
	case 'A':
        case 'E':
        case 'I':
        case 'O':
        case 'U':
        case 'Y':
            upperCase++;
            vowels++;
            break;
	case 'a':
        case 'e':
        case 'i':
        case 'o':
        case 'u':
        case 'y':
		lowerCase++;
		vowels++;
		break;
        case 'B':
        case 'C':
        case 'D':
        case 'F':
        case 'G':
        case 'H':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'V':
        case 'W':
        case 'X':
        case 'Z':
		upperCase++;
		consonants++;
		break;
	case 'b':
        case 'c':
        case 'd':
        case 'f':
        case 'g':
        case 'h':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'v':
        case 'w':
        case 'x':
        case 'z':
		lowerCase++;
		consonants++;
		break;
	case '1':
	case '2':
	case '3':
	case '4':
	case '5':
	case '6':
	case '7':
	case '8':
	case '9':
	case '0':
		numbers++;
		break;
	default:
		break;
	}
	//At the end just add to the total as well
	total++;	
  } 
 printf("upper-case:  %d\n",upperCase);
 printf("lower-case: %d\n", lowerCase);
 printf("vowels: %d\n", vowels);
 printf("consonants: %d\n", consonants);
 printf("digits: %d\n", numbers);
 printf("total: %d\n", total);
 return 0;
}

