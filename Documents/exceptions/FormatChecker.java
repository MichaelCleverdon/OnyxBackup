import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FormatChecker {

	public static void main(String...files) {
		
			if(files.length == 0) {
				System.out.println("Usage: $ java FormatChecker file1 [file2 ... fileN]");
				return;
			}
			for(int i = 0; i < files.length; i++) {
				try {
					checkFormat(files[i]);
				}
				catch(FileNotFoundException ex) {
					System.out.println(ex.toString());
					System.out.println("INVALID");
				}
				catch(DimensionMismatchException ex) {
					System.out.println(ex.toString());
					System.out.println("INVALID");
				}
				catch(InputMismatchException ex) {
					System.out.println(ex.toString());
					System.out.println("INVALID");
				}
				catch(NumberFormatException ex) {
					System.out.println(ex.toString());
					System.out.println("INVALID");
				}
				System.out.println();
		}
		
	}
	
	public static void checkFormat(String file) throws FileNotFoundException {
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(new File(file));
			int rows = scan.nextInt();
			int columns = scan.nextInt();
			String ex = scan.nextLine();
			if(!(ex.equals(""))) {
				throw new DimensionMismatchException("There were more than two dimensions in your file.");
			}
			double[][] doubleArray = new double[rows][columns];
			int r = 0;
			int c = 0;
			for(r = 0; r < rows; r++) {
				for(c = 0; c < columns; c++) {
					String nextValue = scan.next();
					double nextDouble;
					//Check if the next value is actually a double
					nextDouble = Double.parseDouble(nextValue);
						//throw new InputMismatchException("The value: " + nextValue + " at column " + (c+1) + ", row " + (r+1) + "is not a double");
					doubleArray[r][c] = nextDouble;	
				}
				if(scan.hasNext() && scan.hasNextLine()) {
					if(!(scan.nextLine().equals(""))) {
						throw new DimensionMismatchException("Your file was expected to have " + c + " columns but found a different number of columns on row " + (r+1));
					}
				}
			}
				
			if(scan.hasNextLine()) {
				throw new DimensionMismatchException("Your file expected " + r +" rows but got " + (r+1));
			}
			
			scan.close();	
			System.out.println(file);
			System.out.println("VALID");
	}

}
