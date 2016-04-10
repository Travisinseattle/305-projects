import java.util.PriorityQueue;
import java.util.Random;

/**
 * TCSS 342 - Winter 2016
 */

/**
 * The Main Class.
 * 
 * @author Travis Holloway
 * @version 01/29/2016
 */
public class Main {

	/**
	 * The Main Method.
	 * 
	 * @param theArgs passed to the main method.
	 */
	public static void main(String[] theArgs) {
		
		/*
		 * Test methods, disabled by default  You will need to enable S.O.P in Genome and Population
		 * in order to see the test methods function correctly.  Look for '//' in left most margin 
		 * to identify what needs enabled.
		 */
//		testGenome();
//		testPopulation();
		
		//create a start time for use with capturing system runtime.
		final long startTime = System.currentTimeMillis();
		
		//Create a random to use in the program.
		Random rand = new Random();
		
		//Create a population.
		final Population population = new Population(100, 0.05, rand);
		
		//Create a counter for each call of day().
		int dayCount = 1;
		
		//Print out the most fit Genome after population is initialized.
		System.out.println("On day: 0, The most fit Genome is: " + population.getMostFit().toString());
		
		//While there is no perfect match, call the method day() on the population.
		while (population.getMostFit().getFitness() != 0) {
			population.day();
			System.out.println("On day: " + dayCount + ", The most fit Genome is: " + population.getMostFit().toString());
			dayCount++;
		}
		
		//Complete the capture of runtime and print results.
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("\nThe runtime of this program in milliseconds is: " + totalTime);
	}
	
	/**
	 * A method to test the genome class to see that it is working correctly.
	 */
	static void testGenome() {
		final Random rand = new Random();
		final Genome testGenome1 = new Genome(0.05, rand);
		final Genome testGenome2 = new Genome(0.05, rand);
		
		//Print out getters.
		System.out.println("Test of fitness is: " + testGenome1.getFitness() + ", Should return 43.");
		System.out.println("Test of rate is: " + testGenome1.getRate() + ", Should return 0.05.");
		System.out.println("Test of char's is: " + testGenome1.getChars().toString() + ", Should return [A]\n\n");
		
		//Run the fitness method on testGenome, should return 43.
		testGenome1.fitness();
		System.out.println(testGenome1.toString());
				
		
		/*
		 * Run the mutate method 100 times on the Genome and enable the S.O.P reporting in the mutate
		 * method in order to confirm it is functioning as per expectation.
		 */
		for (int i = 0; i < 100; i++) {
			testGenome1.mutate();
			testGenome1.fitness();
			System.out.println("After mutate call " + i + ", the test Genome looks like this: "
					+ testGenome1.toString());
		}
		
		System.out.println("\n\n");
		
		/*
		 * Run the crossover method 100 times on the Genome and enable the S.O.P reporting in the crossover
		 * method in order to confirm it is functioning as per expectation.
		 */
		for (int i = 0; i < 100; i++) {
			testGenome1.crossover(testGenome2);
			testGenome1.mutate();
			testGenome2.mutate();
			System.out.println("After crossover call " + i + ", the testGenome1 looks like this: "
					+ testGenome1.toString());
			System.out.println("After crossover call " + i + ", the testGenome2 looks like this: "
					+ testGenome2.toString());
		}
	}
	
	/**
	 * A method to test the Population class to see that it is working correctly.
	 */
	static void testPopulation() {
		Random rand = new Random();
		final Population testPop = new Population(100, 0.05, rand);
		
		//Test getters.  Go to population and enable getter for myPop, only used for testing.
		Genome best = testPop.getMostFit();
		
		System.out.println("The best Genome is: " + best.toString());
		
		PriorityQueue<Genome> thePop = testPop.getMyPop();
		
		int count = 1;
		
		for (final Genome test: thePop) {
			System.out.println("Genome " + count + " is: " + test + " and mutation rate is: "
		+ test.getRate());
			count++;
		}
		
		System.out.println("\n\n");
		
		/*
		 * Run the method day() once on the testPop and enable all reporting from mutate and crossover
		 * methods to ensure that methods run.
		 */		
		testPop.day();
		
		System.out.println("\n\n");
		
		/*
		 * Print out the pop after the method has completed.  Then print out the new most fit.
		 */
		count = 1;
		
		thePop = testPop.getMyPop();
		
		for (final Genome test: thePop) {
			System.out.println("Genome " + count + " is: " + test);
			count++;
		}
		
		//Check the pop for the most fit after one pass of day.
		best = testPop.getMostFit();
		System.out.println("\n\nAfter day(), The best Genome is: " + best.toString() + "\n");
	}
}
