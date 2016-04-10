import java.io.IOException;

/**
 * TCSS 342 - Winter 2016
 */

/**
 * The 
 * 
 * @author Travis Holloway
 * @version 
 */
public class Main {

	/**
	 * The constructor for 
	 *
	 * @param 
	 */
	public Main() {
		// TODO Auto-generated constructor stub
	}
	/*************************************** Constants ***************************************/

	/**
	 * Method for 
	 * 
	 * @param 
	 * @return 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final Maze maze = new Maze(8, 8, true);
		
		maze.createMaze();
		
		maze.showPath();
	}
}
