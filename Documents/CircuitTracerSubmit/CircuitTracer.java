import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {
	private CircuitBoard board;
	private Storage<TraceState> stateStore;
	private ArrayList<TraceState> bestPaths;
	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			new CircuitTracer(args); //create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		System.out.println("java CircuitTracer [-q | -s] -c [filename]");
		System.out.println("-q -- Uses a queue for storage");
		System.out.println("-s -- Uses a stack for storage");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	private CircuitTracer(String[] args) {
		char storageType = args[0].charAt(1);
		if(storageType == 'q') {
			this.stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
		}
		else if(storageType == 's') {
			this.stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		}
		else {
			System.out.println("Invalid storage type");
			printUsage();
			System.exit(1);
		}
		
		//GUI not implemented, throw error if they use it
		if(args[1].charAt(1) == 'g') {
			System.out.println("Unsupported display type");
			printUsage();
			System.exit(1);
		}
		
		bestPaths = new ArrayList<TraceState>();
		
		try {
			setBoard(new CircuitBoard(args[2]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		searchForBestPaths();
		for(TraceState ts : bestPaths) {
			System.out.println(ts);
		}
		//TODO: parse command line args
		//TODO: initialize the Storage to use either a stack or queue
		//TODO: read in the CircuitBoard from the given file
		//TODO: run the search for best paths
		//TODO: output results to console or GUI, according to specified choice

	}

	/**
	 * Searches for the best paths in the circuit board
	 */
	private void searchForBestPaths() {
		
		ArrayList<Point> adjacentPoints = board.getAllAdjacentPoints(getBoard().getStartingPoint().x, getBoard().getStartingPoint().y);
		for(Point point : adjacentPoints) {
			stateStore.store(new TraceState(board, point.x, point.y));
		}
		
		while(!stateStore.isEmpty()) {
			TraceState state = stateStore.retrieve();
			if(state.isComplete()) {
				if(state.pathLength() == getShortestPath()) {
					bestPaths.add(state);
				}
				else if (state.pathLength() < getShortestPath()){
					bestPaths.clear();
					bestPaths.add(state);
				}
			}
			else {
				ArrayList<Point> adjacent = state.getBoard().getAllAdjacentPoints(state.getRow(), state.getCol());
				for(Point point : adjacent) {
					stateStore.store(new TraceState(state, point.x, point.y));
				}
			}
		}
	}
	
	/**
	 * Gets the board
	 * @return board
	 */
	public CircuitBoard getBoard() {
		return board;
	}

	/**
	 * Sets the board
	 * @param board
	 */
	public void setBoard(CircuitBoard board) {
		this.board = board;
	}
	
	/**
	 * Gets the shortest path from bestPaths storage container
	 * @return
	 */
	private int getShortestPath() {
		int shortestPath = Integer.MAX_VALUE;
		for(TraceState ts : bestPaths) {
			if(ts.pathLength() < shortestPath) {
				shortestPath = ts.pathLength();
			}
		}
		return shortestPath;
	}
} // class CircuitTracer
