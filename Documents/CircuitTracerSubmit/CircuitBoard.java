import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));
		String tempRows = fileScan.next();
		String tempCols = fileScan.next();
		try{
		  ROWS = Integer.parseInt(tempRows);
		  COLS = Integer.parseInt(tempCols);
		}
		catch(NumberFormatException ex){
		    throw new InvalidFileFormatException(tempRows + " " + tempCols + " is not a valid component");
		}
		String tooManyDimensions = fileScan.nextLine();
		if(!tooManyDimensions.equals("")) {
			fileScan.close();
			throw new InvalidFileFormatException("Too many dimensions");
		}
		char[][] newBoard = new char[ROWS][COLS];
		for(int i = 0; i < ROWS; i++) {
			for(int k = 0; k < COLS; k++) {
				try{
				  newBoard[i][k] = fileScan.next().charAt(0);
				}
				catch(NoSuchElementException ex){
				  throw new InvalidFileFormatException("The number of columns was incorrect");
				}
				if(newBoard[i][k] == START) {
					if(startingPoint == null) {
						startingPoint = new Point(i, k);
					}
					else {
						fileScan.close();
						throw new InvalidFileFormatException("More than one starting point");
					}
				}
				else if(newBoard[i][k] == END) {
					if(endingPoint == null) {
						endingPoint = new Point(i, k);
					}
					else {
						fileScan.close();
						throw new InvalidFileFormatException("More than one ending point");
					}
				}
				else if(!(ALLOWED_CHARS.contains(Character.toString(newBoard[i][k])))){
				  fileScan.close();
				  throw new InvalidFileFormatException(Character.toString(newBoard[i][k]) + " is not a valid component.");
				}
			}
			if(fileScan.hasNext() && fileScan.hasNextLine()) {
				String wrongColumns = fileScan.nextLine().trim();
				if(wrongColumns.length() != 0){
					fileScan.close();
					throw new InvalidFileFormatException("The number of columns was incorrect");
				}
			}
		}
		if(startingPoint == null || endingPoint == null){
		  fileScan.close();
		  throw new InvalidFileFormatException("Missing starting or ending point");
		}
		if(fileScan.hasNextLine()) {
		  if(fileScan.nextLine().trim().length() != 0){
			fileScan.close();
			throw new InvalidFileFormatException("The number of rows was incorrect");
		  }
		}
		board = newBoard.clone();
		//TODO: parse the given file to populate the char[][]
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any formatting or parsing issues are encountered
		
		fileScan.close();
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/**
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return ArrayList full of all the open, adjacent points of the passed in x and y coordinates
	 */
	public ArrayList<Point> getAllAdjacentPoints(int x, int y){
		ArrayList<Point> pointsToReturn = new ArrayList<Point>();
		Point origin = new Point(x, y);
		for(int r = 0; r < board.length; r++) {
			for(int c = 0; c < board[r].length; c++) {
				Point pointToCheck = new Point(r, c);
				if(arePointsAdjacent(origin, pointToCheck) && isOpen(r, c)) {
					//Add it to the pointsToReturn because it passed the check
					pointsToReturn.add(pointToCheck);
				}
			}
		}
		return pointsToReturn;
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}
	
	/**
	 * @param p1 first Point
	 * @param p2 second Point
	 * @return true if p1 and p2 are adjacent, else false
	 */
	private boolean arePointsAdjacent(Point p1, Point p2) {
		if (p1.x-1 == p2.x && p1.y == p2.y) {
			return true;
		}
		if (p1.x+1 == p2.x && p1.y == p2.y) {
			return true;
		}
		if (p1.x == p2.x && p1.y-1 == p2.y) {
			return true;
		}
		if (p1.x == p2.x && p1.y+1 == p2.y) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
