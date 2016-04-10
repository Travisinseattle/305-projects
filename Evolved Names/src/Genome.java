import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TCSS 342 - Winter 2016
 */

/**
 * The Genome Class. Creates Genome objects and assigns them an array of characters representing
 * their string value, assigns an integer that represents their fitness level (similarity to the
 * target string) and assigns them a mutation rate.
 * 
 * @author Travis Holloway
 * @version 01/29/2016
 */
public class Genome implements Comparable<Genome> {
	/*************************************** Constants ***************************************/
	
	/**
	 * The set of all possible char's that a Genome may mutate, used for determining what value 
	 * a Genome will add, delete, or change during mutation.
	 */
	private static final char[] CHAR_VALUES = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ',
			'-'};
	
	/**
	 * Field that stores the current String Target for the fitness test.
	 */
	private static final char[] TARGET = setTarget();
	
	/**
	 * Constant for the maximum value of myRate.
	 */
	private static final double MAX_RATE = 1.0;
	
	/**
	 * Constant for the minimum value of myRate.
	 */
	private static final double MIN_RATE = 0.0;
	
	
	/***************************************** Fields ****************************************/
	
	/**
	 * Field that represents the ArrayList of char's that represent the string of the genome.
	 */
	private List<Character> myChars;
	
	/**
	 * Field that represents the randomizer used for various functions in the program.
	 */
	private Random myRandom;
	
	/**
	 * Field that represents the mutation rate of the Genome.
	 */
	private final double myRate;
	
	/**
	 * Field that represents the fitness score of the Genome.
	 */
	private int myScore;	
	
	/************************************** Constructors *************************************/
	
	/**
	 * The constructor for a Genome.  Creates a Genome object with an initial value of 'A' and 
	 * randomly assigns a mutation rate between 0 and 1.
	 * 
	 * @param mutationRate The rate that the Genome will mutate at, represented by a double
	 * between the range of 0 and 1.
	 */
	public Genome(final double mutationRate, final Random rand) {
		myRate = mutationRate;
		myChars = new ArrayList<Character>();
		myChars.add('A');
		myScore = fitness();
		myRandom = rand;
	}
	
	/**
	 * Copy constructor for Genome that creates a Genome with the same values as the input gene.
	 * 
	 * @param gene The Genome that will be copied.
	 */
	public Genome(final Genome gene, final Random rand) {
		myRate = gene.getRate();
		myChars = gene.getChars();
		myRandom = rand;
	}
	
	/************************************* Public Methods ************************************/
	
	/**
	 * The compareTo method that is used to sort Genome's by their fitness level. 
	 * 
	 * @param theGenome The genome to compare to.
	 * @return Returns -1, 0, or 1 based on whether the target has a less, equal, or greater
	 * than fitness level.
	 */
	@Override
	public int compareTo(final Genome theGenome) {
		/*
		 * Subtract the value of the target Genome's fitness score from this Genome's fitness
		 * score and return the result.
		 */
		return (this.getFitness() - theGenome.getFitness());
	}
	
	/**
	 * Method to mix elements of myChars with another Genome.  Iterates through each element in
	 * the two Genome's arrays and randomly picks one.  Replaces the char at that index with 
	 * the random element.
	 * 
	 * @param other The Genome that will be used for comparison and will provide chars.
	 * 
	 * The runtime of this program is: 2xO(m) = O(m).
	 */
	public void crossover(final Genome other) {
		//create using the other's array of chars..
		final List<Character> targetArray = other.getChars();
		
		/*
		 * For each index, randomly choose between 0 and 1.  If the result is 1, replace 
		 * the char at the current index with the char in the targetArray at same index.
		 */
		for (int i = 0; i < myChars.size(); i++) {
			/*
			 * create a 'coin' to randomly choose between 0 and 1. This allows for a random
			 * choice between each parents char array at each index.
			 */
			final int coin = myRandom.nextInt(2);
			
			/*
			 * Flip the coin and on 1, remove the char from current index and insert char
			 * from the same index in the other Genome's array.
			 */
			if (coin == 1 && i < targetArray.size()) {
				final char removedChar = myChars.remove(i);
				final char addedChar = targetArray.get(i);
				myChars.add(i, addedChar);
				
				//Enable S.O.P for test case.
//				System.out.println("**** Removed char: " + removedChar + ", and replaced it "
//						+ "with this char: " + addedChar + " ****");
			}
		}		
	}

	/**
	 * Method to test the fitness of a Genome. Checks the Genome's array of char's against
	 * the TARGET_STRING.  
	 * 
	 *  @return Returns an integer that is the sum of the fitness algorithm. 
	 */
	public int fitness() {
		int fitnessSum = Math.abs(TARGET.length - myChars.size());
		int l = java.lang.Math.max(myChars.size(), TARGET.length);
		//Only used for reporting.
//		int m = java.lang.Math.min(myChars.size(), TARGET.length);
		
		//Enable S.O.P for test cases.
//		System.out.println("Initial sum should be the absolute value of the largest array:"
//				+ " " + l + " minus shortest: " + m + ",  which equals = " + fitnessSum);
		
		for (int i = 0; i < l; i++) {
			//if i is greater than either char array, add 1.
			if (i >= myChars.size() || i >= TARGET.length) {
				fitnessSum++;
				//Enable S.O.P for test cases.
//				System.out.println("Add 1 to fitness as one of the strings is shorter: "
//						+ fitnessSum);
			} else if (!myChars.get(i).equals(TARGET[i])) {
				fitnessSum++;
				//Enable S.O.P for test cases.
//				System.out.println("Add 1 to fitness as the two arrays don't match at"
//						+ " current index: " + fitnessSum);
			}
		}
		
		//Each time fitness is called on a Genome, update myScore to keep it accurate.
		myScore = fitnessSum;
		
		return fitnessSum;
	}
	
	/**
	 * Getter for the fitness score of the Genome.
	 * 
	 * @return Returns myScore which is the Genome's current fitness score.
	 */
	public int getFitness() {
		return this.myScore;
	}
	
	
	/**
	 * Getter for mutation rate of the Genome.
	 * 
	 * @return Return's the Genome's mutation rate.
	 */
	public double getRate() {
		return this.myRate;
	}
	
	/**
	 * Getter for the ArrayList of char's that represent the Genome's String.
	 * 
	 * @return Returns a deep copy of the Genome's array.
	 */
	public List<Character> getChars() {
		final List<Character> returnArray = new ArrayList<Character>();
		for (final char temp: myChars) {
			returnArray.add(temp);
		}
		return returnArray;
	}
	
	/**
	 * A method which will randomly perform one of 3 possible actions: Add, Delete, or change
	 * a single character in myChars randomly based on the mutation rate of myRate.
	 * 
	 * The run time of this method is: O(1) +O(1) + O(m) = O(m).
	 */
	public void mutate() {
		//double mutate = MIN_RATE + (myRandom.nextDouble() * MAX_RATE);
		double mutate = myRandom.nextDouble();
		
		//Enable for testing, shows mutate rate in console.
//		System.out.println("Mutate for add is: " + String.format( "%.2f", mutate));
		
		//Add chance:  Will add a char to myChars if it is less than the mutationRate.
		if (mutate <= myRate) {
			//randomly choose a Char from the list of CHAR_VALUES.
			final char randChar = CHAR_VALUES[myRandom.nextInt(CHAR_VALUES.length)];
			//randomly choose an index in the Genome's array of char's and add the new char.
			myChars.add(myRandom.nextInt(myChars.size()), randChar);
			//Enable S.O.P for test cases.
//			System.out.println("**** This character has been added: " + randChar
//					+ ". ****");
		}
		
		//refresh mutate with a new value.
		mutate = MIN_RATE + (myRandom.nextDouble() * MAX_RATE);
		
		//Enable for testing, shows mutate rate in console.
//		System.out.println("Mutate for remove is: " + String.format( "%.2f", mutate));
		
		/*
		 * Remove chance:  Will remove a random char from myChars if mutate is less than
		 * mutationRate.
		 */
		if (mutate <= myRate) {
			if (myChars.size() > 1){
				//Slightly more complicated than ideal, but used for testing.
				final int charToRemove = myRandom.nextInt(myChars.size());
				final char charToPrint = myChars.remove(charToRemove);
				//Enable S.O.P for test cases.
//				System.out.println("**** This character has been removed: "
//				+ charToPrint + ". ****");
			}
		}
		
		/*
		 * Iterate through each char in the array and if mutate is less than or equal to
		 * mutationRate, change the char.
		 */
		for (int i = 0; i < myChars.size(); i++) {
			mutate = MIN_RATE + myRandom.nextDouble() * MAX_RATE;
			
			//Enable for testing, shows mutate rate in console.
//			System.out.println("Mutate for change is: " + String.format( "%.2f", mutate));
			
			if (mutate <= myRate) {
				final char charChanged = myChars.remove(i);
				final char randChar = CHAR_VALUES[myRandom.nextInt(CHAR_VALUES.length)];
				myChars.add(i, randChar);
				
				//Enable for S.O.P test cases.				
//				System.out.println("**** This char: " + charChanged + " has changed to this: "
//				+ randChar + ". ****");
			}
		}
	}
	
	/**
	 * ToString() method for the Genome class.
	 * 
	 * @return Returns a String representing the characters in the Genome's myChars.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (final char temp: myChars) {
			sb.append(temp);
		}
		
		myScore = this.fitness();
		
		sb.append(" : Fitness Score: " + myScore);
		
		return sb.toString();
	}
	
	/************************************* Private Methods ***********************************/
	
	/**
	 * Private method to convert the target string to char array for use in fitness testing.
	 * 
	 * @return Returns a char array of the char's in the Target String.
	 */
	private static char[] setTarget() {
		char[] targetArray = Population.getTarget().toCharArray();
		return targetArray;
	}

	
}
