import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TCSS 342 - Winter 2016
 */

/**
 * The 
 * 
 * @author Travis Holloway
 * @version 03/09/16
 */
public class Maze {

	/*************************************** Constants ***************************************/

	/***************************************** Fields ****************************************/
	
	/**
	 * Field to represent the boolean for debug.
	 */
	private final boolean myDebug;
	
	/**
	 * Field to represent the height of the maze.
	 */
	private final int myDepth;
	
	/**
	 * Field to represent the 2D grid of cells that constitutes a maze.
	 */
	private final Cell[][] myGrid;
	
	/**
	 * Field to represent the random generator for the chooseNextCell() method.
	 */
	private final Random myRand;
	
	/**
	 * Field to represent the buffered reader used to write to output
	 */
	private BufferedWriter myWriter;
	
	/**
	 * Field to represent the width of the maze.
	 */
	private final int myWidth;

	/************************************** Constructors *************************************/
	
	/**
	 * The constructor for a Maze. Creates a 2D maze of NxM size where N is theDepth and M is 
	 * theWidth.  If the boolean is true, the steps of how the maze is made will be shown.
	 *
	 * @param theWidth the width of the maze.
	 * @param theDepth the height of the maze.
	 * @param debug If true, the steps of the maze construction will be shown.
	 * @throws IOException 
	 */
	public Maze(final int theDepth, final int theWidth, final boolean debug) throws IOException {
		myDebug = debug;
		myDepth = theDepth;
		myWidth = theWidth;
		myGrid = new Cell[theDepth][theWidth];
		populateCells();
		myRand = new Random();
		myWriter = new BufferedWriter(new FileWriter(new File("trace.txt")));
	}

	/************************************* Public Methods ************************************/
	
	/**
	 * Method to procedurally generate a random maze.  Takes a grid of cells and generates the 
	 * maze. 
	 * @throws IOException 
	 */
	public void createMaze() throws IOException {
		Cell start = myGrid[0][0];
		Cell current = myGrid[0][0];
		Cell target = chooseNextCell(current);
		current.setVisited();
		
		while(!target.equals(start)) {
			if (!target.equals(current.getLast())) {
				if (target.equals(current.myNorth)){
					current.setNorthFlag();
					target.setSouthFlag();
					target.setLast(current);
					current = target;
					current.setVisited();
					target = chooseNextCell(current);
				} else if (target.equals(current.mySouth)) {
					current.setSouthFlag();
					target.setNorthFlag();
					target.setLast(current);
					current = target;
					current.setVisited();
					target = chooseNextCell(current);
				} else if (target.equals(current.myEast)) {
					current.setEastFlag();
					target.setWestFlag();
					target.setLast(current);
					current = target;
					current.setVisited();
					target = chooseNextCell(current);
				} else if (target.equals(current.myWest)) {
					current.setWestFlag();
					target.setEastFlag();
					target.setLast(current);
					current = target;
					current.setVisited();
					target = chooseNextCell(current);
				}
			} else {
				current = target;
				target = chooseNextCell(current);
			}
			
			if(myDebug) {
				System.out.println("\n\n" + this.toString());
				myWriter.write("\n\n" + this.toString());				
			}			
		}
		myWriter.write("********* Finished! **********\r\n\r\n");
		myWriter.close();
	}
	
	/**
	 * Method to populate a grid with cells.
	 * 
	 * @param theGrid is the grid to populate with cells.
	 * @return returns the populated grid of cells.
	 */
	public Cell[][] populateCells() {
		
		for(int i = 0; i < myDepth; i++) {
			for(int j = 0; j < myWidth; j++) {
				myGrid[i][j] = new Cell();
			}
		}
		
		for(int i = 0; i < myDepth; i++) {
			for(int j = 0; j < myWidth; j++) {
				if (i == 0) {
					if (j == 0){
						myGrid[i][j].setCells(null, myGrid[i + 1][j], myGrid[i][j + 1], null);
					} else if (j == myWidth - 1) {
						myGrid[i][j].setCells(null, myGrid[i + 1][j], null, myGrid[i][j - 1]);
					} else {
						myGrid[i][j].setCells(null, myGrid[i + 1][j], myGrid[i][j + 1], myGrid[i][j - 1]);
					}
				} else if (i == myDepth - 1) {
					if (j == 0){
						myGrid[i][j].setCells(myGrid[i - 1][j], null, myGrid[i][j + 1], null);
					} else if (j == myWidth - 1) {
						myGrid[i][j].setCells(myGrid[i - 1][j], null, null, myGrid[i][j - 1]);
					} else {
						myGrid[i][j].setCells(myGrid[i - 1][j], null, myGrid[i][j + 1], myGrid[i][j - 1]);
					}
				} else {
					if (j == 0) {
						myGrid[i][j].setCells(myGrid[i - 1][j], myGrid[i + 1][j], myGrid[i][j + 1], null);
					} else if (j == myWidth - 1) {
						myGrid[i][j].setCells(myGrid[i - 1][j], myGrid[i + 1][j], null, myGrid[i][j - 1]);
					} else {
						myGrid[i][j].setCells(myGrid[i - 1][j], myGrid[i + 1][j], myGrid[i][j + 1], myGrid[i][j - 1]);
					}					
				}
			}
		}
		
		myGrid[0][0].setNorthFlag();
		myGrid[myDepth - 1][myWidth - 1].setSouthFlag();
		
		return myGrid;
	}
	
	/**
	 * Method to show the path that solves the maze.
	 * @throws IOException 
	 */
	public void showPath() throws IOException {
		Cell current = myGrid[myDepth - 1][myWidth - 1];
		
		while (!current.equals(myGrid[0][0])) {
			current.setPath();
			current = current.getLast();
		}
		myGrid[0][0].setPath();
		myWriter = new BufferedWriter(new FileWriter(new File("trace.txt"), true));
		myWriter.write("********* Solution! **********\r\n");
		myWriter.write("\r\n\r\n" + this.toString());		
		myWriter.close();
		
	}
	
	/**
	 * ToString method for the maze.  Returns a string that when printed show's the maze's
	 * state as an ascii printout.
	 * 
	 * @return a String that represents the maze in ascii.
	 */
	public String toString() {
		final String[] grid = new String[myDepth * 3];
		int count = 0;
		StringBuilder north = new StringBuilder();
		StringBuilder middle = new StringBuilder();
		StringBuilder south = new StringBuilder();
		StringBuilder last = new StringBuilder();		
		
		for (final Cell[] cellArr: myGrid) {
			north.append("-");
			middle.append("|");
			south.append("|");
			for (final Cell cell: cellArr ) {
				if (cell.getNorthFlag()) {
					north.append("---");
				} else {
					north.append("- -");
				}
				if (cell.getSouthFlag()) {
					south.append("---");
				} else {
					south.append("- -");
				}
				if (cell.getWestFlag()) {
					middle.append("|");
				} else {
					middle.append(" ");
				}				
				if (cell.getPath()) {
					middle.append("x");
				} else if (cell.getVisited()) {
					middle.append(" ");
				} else {
					middle.append("*");
				}
				if (cell.getEastFlag()) {
					middle.append("|");
				} else {
					middle.append(" ");
				}
			}
			north.append("-");
			middle.append("|");
			south.append("-");
			grid[count] = north.toString();
			grid[count + 1] = middle.toString();
			grid[count + 2] = south.toString();
			north = new StringBuilder();
			middle = new StringBuilder();
			south = new StringBuilder();
			count = count + 3;
		}
		
		for (final String temp: grid) {
			last.append(temp);
			last.append("\r\n");			
		}
		last.append("\r\n\r\n");
		return last.toString();		
	}

	/************************************ Private  Methods ***********************************/
	
	/**
	 * A private method to choose the direction to go when generating the Maze.
	 * 
	 * @param theCell the current cell.
	 * @return the cell that will be the next current cell. Returns the previous cell if no 
	 * other valid options are present.
	 */
	private Cell chooseNextCell(final Cell theCurrent) {
		//set a counter to capture the amount of available options.
		int count = 3;
		int index = myRand.nextInt(count + 1);
		Cell returnCell = null;
		List<Cell> cells = new ArrayList<Cell>();
		//always add all 4 cells to the list..
		cells.add(theCurrent.myNorth);
		cells.add(theCurrent.mySouth);
		cells.add(theCurrent.myEast);
		cells.add(theCurrent.myWest);
		
		//set returnCell to a random cell by removing it from the list.
		returnCell = cells.remove(index);
		
		/*
		 * if that cell is null or visited, and the count is more than 0, decrement count and
		 * do it again.
		 */
		while((returnCell == null  || returnCell.getVisited()) && count > 0) {
			count--;
			index = myRand.nextInt(count + 1);
			returnCell = cells.remove(index);
		}
		
		//if count is 0, then no directions are valid and so return the last cell.
		if (count <= 0) {
			returnCell = theCurrent.getLast();
		}
		
		return returnCell;		
	}
	
	/**
	 * A private class to represent a location in the maze.  Each Cell has 4 connections to 
	 * neighboring Cells: North, South, East, and West.  Each Cell has a boolean to indicate
	 * if it has been visited.  Each cell has a pointer that points to the cell that was visited
	 * previous to itself.  
	 * 
	 * @author Travis Holloway
	 * @version 03/09/16 
	 */
	private class Cell {
		
		/*************************************** Constants ***************************************/

		/***************************************** Fields ****************************************/
		
		/**
		 * Field for the direction east.
		 */
		private Cell myEast;
		
		/**
		 * Field for a east edge in the cell.
		 */
		private boolean myEastEdge;
		
		/**
		 * Field to indicate the cell that was visited previously.
		 */
		private Cell myLast;
		
		/**
		 * Field for the direction north.
		 */
		private Cell myNorth;
		
		/**
		 * Field for a north edge in the cell.
		 */
		private boolean myNorthEdge;
		
		/**
		 * Field to indicate that cell is part of the path that solves the maze.
		 */
		private boolean myPath;
		
		/**
		 * Field for the direction south.
		 */
		private Cell mySouth;
		
		/**
		 * Field for a south edge in the cell.
		 */
		private boolean mySouthEdge;
		
		/**
		 * Field for the boolean that indicates if the Cell has been visited.
		 */
		private boolean myVisited;
				
		/**
		 * Field for the direction west.
		 */
		private Cell myWest;
		
		/**
		 * Field for a west edge in the cell.
		 */
		private boolean myWestEdge;		

		/************************************** Constructors *************************************/		
		
		/**
		 * Default constructor for a cell. All directions are set to null and visited is false.
		 */
		private Cell() {
			myNorth = null;
			mySouth = null;
			myEast = null;
			myWest = null;
			myNorthEdge = true;
			mySouthEdge = true;
			myEastEdge = true;
			myWestEdge = true;			
			myVisited = false;
			myLast = null;
		}
		
		/**
		 * The constructor for a Cell. Provides links to it's neighboring cells and sets myVisited
		 * as false and myLast as null. 
		 *
		 * @param theNorth the Cell to the north.
		 * @param theSouth the Cell to the south.
		 * @param theEast the Cell to the east.
		 * @param theWest the Cell to the west.
		 */
		private Cell(final Cell theNorth,
				final Cell theSouth,
				final Cell theEast,
				final Cell theWest) {
			myNorth = theNorth;
			mySouth = theSouth;
			myEast = theEast;
			myWest = theWest;
			myNorthEdge = true;
			mySouthEdge = true;
			myEastEdge = true;
			myWestEdge = true;
			myVisited = false;
			myLast = null;
			myPath = false;
		}

		/************************************ Private  Methods ***********************************/
		
		/**
		 * Getter for the east flag.
		 */
		private boolean getEastFlag() {
			return this.myEastEdge;
		}
		
		/**
		 * Getter for last cell visited (myLast).
		 */
		private Cell getLast() {
			return this.myLast;
		}
		
		/**
		 * Getter for the north flag.
		 */
		private boolean getNorthFlag() {
			return this.myNorthEdge;
		}
		
		/**
		 * Getter for myPath.
		 */
		private boolean getPath() {
			return this.myPath;
		}
		
		/**
		 * Getter for the north flag.
		 */
		private boolean getSouthFlag() {
			return this.mySouthEdge;
		}
		
		/**
		 * Getter for the visited flag.
		 */
		private boolean getVisited() {
			return this.myVisited;
		}
		
		/**
		 * Getter for the north flag.
		 */
		private boolean getWestFlag() {
			return this.myWestEdge;
		}
		
		/**
		 * Setter for the north edge flag of a Cell.
		 */
		private void setEastFlag() {
			myEastEdge = !myEastEdge;
		}
		
		/**
		 * Method to set the field for myLast.
		 */
		private void setLast(final Cell theCell) {
			this.myLast = theCell;
		}
		
		/**
		 * Setter for the north edge flag of a Cell.
		 */
		private void setNorthFlag() {
			myNorthEdge = !myNorthEdge;
		}
		
		/**
		 * Setter for myPath boolean.
		 */
		private void setPath() {
			myPath = !myPath;
		}
		
		/**
		 * Setter for the north edge flag of a Cell.
		 */
		private void setSouthFlag() {
			mySouthEdge = !mySouthEdge;
		}
		
		/**
		 * setter for the visited flag.
		 */
		private void setVisited() {
			myVisited = !myVisited;
		}
		
		/**
		 * Setter for the north edge flag of a Cell.
		 */
		private void setWestFlag() {
			myWestEdge = !myWestEdge;
		}
		
		/**
		 * Setter for linking Cells.  Sets all neighboring Cells at the same time.
		 * 
		 * @param theNorth the north cell to be set.
		 * @param theSouth the south cell to be set.
		 * @param theEast the east cell to be set.
		 * @param theWest the west cell to be set.
		 */
		private void setCells(final Cell theNorth,
				final Cell theSouth,
				final Cell theEast,
				final Cell theWest) {
			this.myNorth = theNorth;
			this.mySouth = theSouth;
			this.myEast = theEast;
			this.myWest = theWest;
		}
	}		
}
	
		
