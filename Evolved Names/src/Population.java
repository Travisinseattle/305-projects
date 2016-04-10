import java.util.PriorityQueue;
import java.util.Random;

/**
 * TCSS 342 - Winter 2016
 */

/**
 * The Population Class.
 * 
 * @author Travis Holloway
 * @version 01/29/2016
 */
public class Population {
	/*************************************** Constants ***************************************/
	
	/**
	 * The target string that the Genome's are attempting to evolve to. (Use upper-case only)  
	 */
	private static final String TARGET_STRING = "STEVEN TRAVIS HOLLOWAY";
	
	/**
	 * Constant that represents the maximum size of the population.  Set to 100 by default.
	 */
	private static final int MAX_POP = 100;
	
	/**
	 * Constant that represents the maximum size of the population.  Set to 100 by default.
	 */
	private static final int MIN_POP = 50;
	
	/***************************************** Fields ****************************************/
	
	/**
	 * Field that represents the population of the Genome's as priority queue (min heap) based
	 * on their fitness levels.
	 */
	private PriorityQueue<Genome> myPop;
	
	/************************************** Constructors *************************************/
	
	/**
	 * The constructor for the Population class.  Initializes the Priority Queue used to
	 * contain the Genome's.
	 */
	public Population(final Integer numGenomes, final Double mutationRate, final Random rand) {
		myPop = new PriorityQueue<Genome>();
		for (int i = 0; i < numGenomes; i++) {
			final Genome genome = new Genome(mutationRate, rand);
			myPop.add(genome);
		}
	}
	/************************************* Public Methods ************************************/
	
	/**
	 * Method to complete a breeding cycle.  Update's mostFit to the Genome with lowest fitness 
	 * score.  Re-populates myPop with the 50 most fit Genome's of the previous day's cycle.
	 * Creates new Genome's to bring the total of myPop back to 100 using 2 options: clone a 
	 * random Genome and mutate it, clone a random genome and crossover with another random
	 * Genome and mutate the result.
	 * 
	 * Run time for method: line 67-77: O(1), 78-80: O(n),  88-121: O(nm) 123-135 O(n).
	 * Final math: C(all the constant time operations) + 2O(n) (2 loops) + O(mn) = O(n + nm). 
	 */
	public void day() {
		//Declare a random to use in the method.
		Random rand = new Random();
		
		//Declare an array of MAX_POP size to hold the top Genome's and newly created Genome's.
		final Genome[] temp = new Genome[MAX_POP]; 
		
		/*
		 * Create a temporary PriorityQueue to hold the top 50 best Genome's.  Transfer the
		 * top 50 back to myPop.
		 * 
		 * Run time is the size of the target minimum population: O(n).
		 */
		for (int i = 0; i < MIN_POP; i++) {
			temp[i] = myPop.remove();
		}
		
		/*
		 * Re-populate the queue.
		 * 
		 * Run time for the for loop is: 87-100: O(1), 101 O(m), 102-114: O(1), 115: O(m), 116: O(m),
		 * 117-121: O(1).  Final math: C + (O(n) x 3O(m)) = O(nm).
		 */		
		for (int i = MIN_POP; i < MAX_POP; i++) {
			
			//Make a 'coin' to flip and randomly choose one of the 2 options.
			final int coin = rand.nextInt(2);
			
			//Flip the coin.
			if (coin == 0) {
				/*
				 * Pick random index within the bounds of 0 to i - 1.  Create a new Genome by cloning
				 * the Genome at the random index.  Mutate the new Genome. Add it to temp at index i.
				 */
				final int index = rand.nextInt(i - 1);
				final Genome target = new Genome(temp[index], rand);
				target.mutate(); //O(m).
				temp[i] = target;
				//Enable S.O.P for test cases.
//				System.out.println("We are Mutating!");
			} else {
				/*
				 * Pick 2 random index's and clone the Genome's at those indexes.  Crossover the first
				 * Genome with the second and then mutate the result.  Add the result to temp at 
				 * index i. 
				 */
				final int index1 = rand.nextInt(i - 1);
				final int index2 = rand.nextInt(i - 1);
				final Genome parentMom = new Genome(temp[index1], rand);
				final Genome parentDad = new Genome(temp[index2], rand);
				parentMom.crossover(parentDad); //O(m)
				parentMom.mutate(); //O(m)
				temp[i] = parentMom;
				//Enable S.O.P for test cases.
//				System.out.println("We are Crossing Over!");
			}
		}
		
		/*
		 * First run fitness on each Genome in temp to update the fitness score for that Genome,
		 * then transfer the Genome into myPop.
		 * 
		 * Run time for a for loop is O(n).
		 */
		myPop = new PriorityQueue<Genome>();
		
		for (int i = 0; i < MAX_POP; i++) {
			temp[i].fitness();
			myPop.add(temp[i]);
		}		
	}
	
	/**
	 * Getter for myMostFit, will return whatever Genome currently has the lowest fitness score.
	 * 
	 * @return Returns whatever Genome has lowest fitness score (top of the heap).
	 */
	public Genome getMostFit() {
		return myPop.peek();
	}
	
	/**
	 * Getter for myPop.  Only used to access the heap for testing purposes, no use in actual 
	 * program.
	 * 
	 * @return myPop.
	 */
	public PriorityQueue<Genome> getMyPop() {
		//Deep copy myPop and return the copy.
		final PriorityQueue<Genome> returnPop = new PriorityQueue<Genome>(myPop);
		return returnPop;
	}
	
	/**
	 * Method to pass the TARGET_STRING to the Genome's for fitness testing.
	 * 
	 * @return Returns the TARGET_STRING.
	 */
	public static String getTarget() {
		return TARGET_STRING;
	}
	/************************************* Private Methods ***********************************/
}
