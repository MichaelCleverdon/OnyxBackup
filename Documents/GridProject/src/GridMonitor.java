//import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GridMonitor implements GridMonitorInterface {
	private double[][] baseGrid;
	private double[][] surroundingSumGrid;
	private double[][] surroundingAvgGrid;
	private double[][] deltaGrid;
	private boolean[][] dangerGrid;
	private Scanner targetFile;
	private int rows;
	private int columns;

	public GridMonitor(String fileName) {
		try {
			Scanner reader = new Scanner(new File(fileName));
			setTargetFile(reader);
			setBaseGrid(getTargetFile());
			setSurroundingSumGrid(getBaseGrid());
			setSurroundingAvgGrid(getSurroundingSumGrid());
			setDeltaGrid(getSurroundingAvgGrid());
			setDangerGrid(getBaseGrid(), getSurroundingAvgGrid(), getDeltaGrid());
		}
		catch(FileNotFoundException e) {
		}
	}
	@Override
	/**
	 * Gets the base grid. If it's not set, set it then get it again.
	 * @return Base Grid
	 */
	public double[][] getBaseGrid() {
		if(this.baseGrid != null) {
			return this.baseGrid.clone();
		}
		else {
			setBaseGrid(this.getTargetFile());
			return getBaseGrid();
		}
	}
	
	/**
	 * Sets the base grid by passing in the required materials to create it, in this case: the scanner with the file
	 * @param targetFileForReading
	 */
	public void setBaseGrid(Scanner targetFileForReading) {
		//Creates a 2d array with targetFileForReading
		this.rows = targetFileForReading.nextInt();
		this.columns = targetFileForReading.nextInt();
		double[][] baseGrid = new double[this.rows][this.columns];
		
		//Now we set all the data for the grid
		for(int i = 0; i < this.rows; i++) {
			for(int k = 0; k < this.columns; k++) {
				baseGrid[i][k] = targetFileForReading.nextDouble();
			}
		}
		this.baseGrid = baseGrid.clone();
	}

	@Override
	/**
	 *Gets the surrounding sum grid, if it's not set, set it then get it
	 * @return surroundingSumGrid
	 */
	public double[][] getSurroundingSumGrid() {
		if(this.baseGrid != null && this.baseGrid.length != 0) {
			return this.surroundingSumGrid.clone();
		}
		else {
			this.getBaseGrid();
			return getSurroundingSumGrid();
		}
	}
	
	/**
	 * Sets the surrounding sum grid with the required information (base grid) being passed in
	 * @param baseGrid
	 */
	public void setSurroundingSumGrid(double[][] baseGrid){
		double[][] baseGridCopy = baseGrid.clone();
		double[][] surroundingSumGrid = new double[this.rows][this.columns];
		for(int i = 0; i < this.rows; i++) {
			for(int k = 0; k < this.columns; k++) {
				//If the position is on the top row
				if(i == 0) {
					//If it's in the top left corner
					if(k == 0) {
						//1xAnything
						if(this.rows == 1) {
							//1x1
							if(this.columns == 1) {
								surroundingSumGrid[i][k] = 4*baseGridCopy[i][k];								
							}
						}
						//Anythingx1
						else if(this.columns == 1) {
							//Top row
							if(i == 0) {
								surroundingSumGrid[i][k] = 3*baseGridCopy[i][k] + baseGridCopy[i+1][k];
							}
							//Bottom row
							else if(i == this.rows - 1) {
								surroundingSumGrid[i][k] = 3*baseGridCopy[i][k] + baseGridCopy[i-1][k];
							}
							//In the middle
							else {
								surroundingSumGrid[i][k] = 2*baseGridCopy[i][k] + baseGridCopy[i-1][k] + baseGridCopy[i+1][k];
							}
						}
						
						else {
							surroundingSumGrid[i][k] = 2*baseGridCopy[i][k] + baseGridCopy[i+1][k] + baseGridCopy[i][k+1];
						}
					}
					//If it's in the top right corner
					else if (k == this.columns-1) {
						surroundingSumGrid[i][k] = 2*baseGridCopy[i][k] + baseGridCopy[i+1][k] + baseGridCopy[i][k-1];
					} 
					//If it's just on the top row
					else {
						surroundingSumGrid[i][k] = baseGridCopy[i][k] + baseGridCopy[i+1][k] + baseGridCopy[i][k-1] + baseGridCopy[i][k+1];
					}
				}
				//If the position is on the bottom row
				else if(i == this.rows-1) {
					//Bottom left corner
					if(k == 0) {
						surroundingSumGrid[i][k] = 2*baseGridCopy[i][k] + baseGridCopy[i-1][k] + baseGridCopy[i][k+1];
					}
					//Bottom right corner
					else if(k == this.columns-1) {
						surroundingSumGrid[i][k] = 2*baseGridCopy[i][k] + baseGridCopy[i-1][k] + baseGridCopy[i][k-1];
					}
					//Anywhere else on bottom row
					else {
						surroundingSumGrid[i][k] = baseGridCopy[i][k] + baseGridCopy[i-1][k] + baseGridCopy[i][k+1] + baseGridCopy[i][k-1];
					}
				}
				//If the position is in the middle of the rows
				else {
					//Left side of array
					if(k == 0) {
						surroundingSumGrid[i][k] = baseGridCopy[i][k] + baseGridCopy[i-1][k] + baseGridCopy[i+1][k] + baseGridCopy[i][k+1];
					}
					//Right side of array
					else if(k == this.columns-1) {
						surroundingSumGrid[i][k] = baseGridCopy[i][k] + baseGridCopy[i-1][k] + baseGridCopy[i+1][k] + baseGridCopy[i][k-1];
					}
					//In the middle of the array
					else {
						surroundingSumGrid[i][k] = baseGridCopy[i-1][k] + baseGridCopy[i+1][k] + baseGridCopy[i][k-1] + baseGridCopy[i][k+1];
					}
				}
			}
		}
		this.surroundingSumGrid = surroundingSumGrid.clone();
	}

	@Override
	/**
	 * Gets surroundingAvgGrid. If it's not set, set it then get it
	 * @return surroundingAvgGrid
	 */
	public double[][] getSurroundingAvgGrid(){
		if(this.surroundingAvgGrid != null && this.surroundingAvgGrid.length != 0) {
			return this.surroundingAvgGrid.clone();
		}
		else {
			setSurroundingAvgGrid(getSurroundingSumGrid());
			return getSurroundingAvgGrid();
		}
	}
	
	/**
	 * sets the surrounding Average grid using the surrounding sum grid as a parameter
	 * @param surroundingSumGrid
	 */
	public void setSurroundingAvgGrid(double[][] surroundingSumGrid) {		
		double[][] surroundingAvgGrid = new double[this.rows][this.columns];
		for(int i = 0; i < this.rows; i++) {
			for(int k = 0; k < this.columns; k++) {
				surroundingAvgGrid[i][k] = (surroundingSumGrid[i][k] / 4);
			}
		}
		this.surroundingAvgGrid = surroundingAvgGrid.clone();
	}

	@Override
	/**
	 * gets delta grid. If it's not set, set it then get it
	 * @return deltaGrid
	 */
	public double[][] getDeltaGrid() {
		if(this.deltaGrid != null && this.deltaGrid.length != 0) {
			return this.deltaGrid.clone();
		}
		else {
			setDeltaGrid(getSurroundingAvgGrid());
			return getDeltaGrid();
		}
	}
	
	/**
	 * Sets the delta grid based on values from surroundingAvgGrid
	 * @param surroundingAvgGrid
	 */
	public void setDeltaGrid(double[][] surroundingAvgGrid) {
		double[][] deltaGrid = new double[this.rows][this.columns];
		for(int i = 0; i < this.rows; i++) {
			for(int k = 0; k < this.columns; k++) {
				deltaGrid[i][k] = surroundingAvgGrid[i][k] / 2;
			}
		}
		this.deltaGrid = deltaGrid;
	}

	@Override
	/**
	 * Gets the danger grid. If it's not set, set it then get it
	 * @return dangerGrid
	 */
	public boolean[][] getDangerGrid() {
		if(this.dangerGrid != null && this.dangerGrid.length != 0) {
			return this.dangerGrid.clone();
		}
		else {
			setDangerGrid(getBaseGrid(), getSurroundingAvgGrid(), getDeltaGrid());
			return getDangerGrid();
		}
	}
	
	/**
	 * Sets the danger grid. This grid requires 3 different grids as parameters in order to create it.
	 * @param baseGrid used to see if the grid is in danger
	 * @param surroundingAvgGrid used to have a starting point to add and subtract from
	 * @param deltaGrid used to add to or subtract from the surroundingAvgGrid
	 */
	public void setDangerGrid(double[][] baseGrid, double[][] surroundingAvgGrid, double[][] deltaGrid) {
		boolean[][] dangerGrid = new boolean[this.rows][this.columns];
		for(int i = 0; i < this.rows; i++) {
			for(int k = 0; k < this.columns; k++) {
				//If the baseGrid[i][k] is not within the outer bounds of explosions, set that cell to true
				if(surroundingAvgGrid[i][k] + Math.abs(deltaGrid[i][k]) >= baseGrid[i][k] && surroundingAvgGrid[i][k] - Math.abs(deltaGrid[i][k]) <= baseGrid[i][k]) {
					dangerGrid[i][k] = false;
				}
				else {
					dangerGrid[i][k] = true;
				}
			}
		}
		this.dangerGrid = dangerGrid.clone();
	}
	
	/**
	 * Gets the targetFile Scanner
	 * @return targetFile
	 */
	public Scanner getTargetFile() {
		return this.targetFile;
	}
	
	/**
	 * Pass in a Scanner in order to set the targetFile class variable to it
	 * @param reader Scanner to set the targetFile to
	 */
	public void setTargetFile(Scanner reader) {
		this.targetFile = reader;
	}

}
