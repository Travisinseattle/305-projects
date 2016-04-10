/**
 * TCSS 342 - Winter 2016
 */

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Main Class.  This is the class that initializes and executes the program.  
 * 
 * @author Travis Holloway
 * @version 01/15/2016
 */
public class Main {

	/**
	 * The main method that starts the program. In order to run the program, Place a 
	 * text file of burger orders called 'customer.txt' in the Burger Baron main folder.
	 * 
	 * @param theArgs Any arguments to be passed to the compiler.
	 */
	public static void main(String[] theArgs) {
		
		/*
		 * Test methods follow, comment them back in to see output.
		 */		
		//myStackTest();
		//burgerTest();
		
		/*
		 * Create a string for the text file and another to hold each line of the text
		 * file as its read.
		 */
		String file = ".\\customer.txt";
		String line = null;
		
		/*
		 * Create a 2D ArrayList to hold the lists of orders and create a List to
		 * hold the created burgers.
		 */
		List<String[]> orders = new ArrayList<String[]>();
		List<Burger> burgers = new ArrayList<Burger>();
		
		/*
		 * Use try to read the text file and populate the 2D ArrayList with String[]
		 * arrays that hold the Strings found on each line of the text file.  
		 */
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
	                orders.add(line.split(" "));
	            }
	        bufferedReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + file + "'");  
		} catch(IOException ex) {
            System.out.println("Error reading file '" + file + "'"); 
		}
		
		burgers = parseLine(orders, burgers);
		
		/*
		 * Print the orders followed by the completed burger so the results can be
		 * confirmed that they are correct.
		 */
		for (int i = 0; i < burgers.size(); i++) {
			StringBuilder sb = new StringBuilder();
			String[] array = orders.get(i);
			for (String temp: array) {
				sb.append(temp);
				sb.append(" ");
			}
			System.out.println("Order #" + (i + 1) + " is: " + sb.toString());
			System.out.println("Burger #" + (i + 1) +
					" looks like this: \n	    " + burgers.get(i).toString());
		}
	}
	
	/*****************************  Static Helper Methods  ***********************************/
	
	/** 
	 * This method checks my String[] arrays for keywords and returns true or false.
	 * 
	 * Acquired code from  
	 * http://www.programcreek.com/2014/04/check-if-array-contains-a-value-java/
	 * 
	 * @param theArray The String[] that needs to be checked.
	 * @param targetValue The String we are looking for.
	 * @return true if the string is in the array, false if not.
	 */
	static boolean checkArray(String[] theArray, String targetValue) {
		return Arrays.asList(theArray).contains(targetValue);
	}
	
	/**
	 * Method to check for the addition of any categories or individual items.
	 * 
	 * @param theArray The array of strings representing the burger order.
	 * @param theBurger The burger that needs to be altered.
	 * @return The burger, altered or not.
	 */
	static Burger parseAdditions(String[] theArray, Burger theBurger) {
		String modifier = "";
		String item = "";
		/*
		 * Check the order for the keyword 'with'. If it is not preceded by the keyword 'no', 
		 * this will indicate that an item needs added.  The method will call addCategory if the
		 * item is a category, and addIngredient otherwise.
		 */
		for (int i = 0; i < theArray.length - 1; i++) {
			if (theArray[i].equals("with")) {
				for (int j = (i + 1); j < theArray.length; j++) {
					modifier = theArray[j];
					if (!modifier.equals("no") || !modifier.equals("but") || !modifier.equals("with")) {
						item = modifier;
						if (item.equals("Cheese") || item.equals("Sauce") || item.equals("Veggies")) {
							theBurger.addCategory(item);
						} else {
							theBurger.addIngredient(item);
						}
					}
				}
			}
		}
		return theBurger;
	}
	
	/**
	 * Method to check for any exceptions to parseAdditions or parseOmissions.
	 * 
	 * @param theArray The array of strings representing the burger order.
	 * @param theBurger The burger that needs to be altered.
	 * @return The burger, altered or not.
	 */
	static Burger parseExceptions(String[] theArray, Burger theBurger) {
		String modifier = "";
		String item = "";
		/*
		 * Check the order for the keyword 'with'. If it is not preceded by the keyword 'no', 
		 * this will indicate that an item needs added.  The method will call addCategory if the
		 * item is a category, and addIngredient otherwise.
		 */
		for (int i = 0; i < theArray.length - 1; i++) {
			if (theArray[i].equals("but")) {
				modifier = theArray[i + 1];
				if (modifier.equals("no")) {
					item = theArray[i + 2];
					theBurger.removeIngredient(item);
				} else if (!modifier.equals("no")) {
					item = modifier;
					theBurger.addIngredient(item);
				}
			}
		}
		return theBurger;
	}
	
	/**
	 * Method to read a line of input from the customer text file and create a burger
	 * from that line.
	 */
	static ArrayList<Burger> parseLine(List<String[]> orders, List<Burger> burgers) {
		/*
		 * Check each String[] in the orders ArrayList and build an appropriate burger
		 * from the array.  There are 6 methods that will each call an appropriate 
		 * method from the Burger class based on keywords found in the order.  Run all
		 * 6 methods on each burger and then add the burger to the burgers ArrayList.  
		 */
		for (String[] tempArray: orders) {
			Burger theBurger = new Burger(checkArray(tempArray, "Baron"));
			theBurger = parseAdditions(tempArray, theBurger);
			theBurger = parseOmissions(tempArray, theBurger);
			theBurger = parseExceptions(tempArray, theBurger);			
			theBurger = pattyOrder(tempArray, theBurger);
			theBurger = pattyType(tempArray, theBurger);
			burgers.add(theBurger);
		}
		return (ArrayList<Burger>) burgers;
	}
	
	/**
	 * Method to check for the removal of any categories or individual items.
	 * 
	 * @param theArray The array of strings representing the burger order.
	 * @param theBurger The burger that needs to be altered.
	 * @return The burger, altered or not.
	 */
	static Burger parseOmissions(String[] theArray, Burger theBurger) {
		String modifier = "";
		String item = "";
		/*
		 * Check the order for the keyword 'with'. If it is preceded by the keyword 'no', 
		 * this will indicate that an item needs removed.  The method will call removeCategory if the
		 * item is a category, and removeIngredient otherwise.
		 */
		for (int i = 0; i < theArray.length - 1; i++) {
			if (theArray[i].equals("with")) {
				modifier = theArray[i + 1];
				if (modifier.equals("no")) {
					item = theArray[i + 2];
					if (item.equals("Cheese") || item.equals("Sauce") || item.equals("Veggies")) {
						theBurger.removeCategory(item);
					} else {
						theBurger.removeIngredient(item);
					}
				}
			}
		}
		return theBurger;
	}
	
	/**
	 * Method to check the order to see if it has any keywords 'Double' or 'Triple'
	 * and if so to add the appropriate amount of patties to the burger, ON TOP of
	 * of the cheese layer, if it exists.
	 * 
	 * @param theArray The array of strings representing the burger order.
	 * @param theBurger The burger that needs to be altered.
	 * @return The burger, altered or not.
	 */
	static Burger pattyOrder(String[] theArray, Burger theBurger) {
		if (checkArray(theArray, "Double")) {
			theBurger.addPatty();
		} else if (checkArray(theArray, "Triple")) {
			theBurger.addPatty();
			theBurger.addPatty();
		}
		return theBurger;
	}
	
	/**
	 * Method to check the order to see if it has any keywords 'Chicken' or 'Veggie'
	 * and if so, change all patties on the burger to the keyword.
	 * 
	 * @param theArray The array of strings representing the burger order.
	 * @param theBurger The burger that needs to be altered.
	 * @return The burger, altered or not.
	 */
	static Burger pattyType(String[] theArray, Burger theBurger) {
		if (checkArray(theArray, "Chicken")) {
			theBurger.changePatties("Chicken");
		} else if (checkArray(theArray, "Veggie")) {
			theBurger.changePatties("Veggie");
		}
		return theBurger;
	}
	
	/*********************************  Test Methods  ***************************************/
	
	/**
	 * A test method to confirm that the Burger Class works.
	 */
	static void burgerTest() {
		//Create 2 burgers to test boolean value of constructor.
		Burger baronBurger = new Burger(true);
		Burger normalBurger = new Burger(false);
		
		int count = 1;
		
		//print out the 2 burgers to see if they were created correctly and test toString();
		System.out.println("Baron Burger looks like this:\n		" + baronBurger.toString());
		System.out.println("Normal Burger looks like this:\n		" + normalBurger.toString());
		
		//CHANGE 1: test the ability to add categories to the burger and print the result.
		System.out.println("\n\nNormal Burger before change " + count + " looks like this:\n		" +
		normalBurger.toString());
		
		normalBurger.addCategory("Sauce");
		normalBurger.addCategory("Cheese");
		normalBurger.addCategory("Veggies");
		
		System.out.println("Normal Burger after change " + count + " looks like this:\n		" +
		normalBurger.toString());
		count++;
		
		//Change 2: test the ability to remove categories to the burger and print the result.
		System.out.println("\n\nBaron Burger before change " + count + " looks like this:\n		" +
		baronBurger.toString());
		
		baronBurger.removeCategory("Sauce");
		baronBurger.removeCategory("Cheese");
		baronBurger.removeCategory("Veggies");
		
		System.out.println("Baron Burger after change " + count + " looks like this:\n		" + baronBurger.toString());
		count++;
		
		/*
		 * Change 3: Test add Patty by adding one patty at a time till we reach 3 on the normal burger.
		 * additional patties should be placed on top of cheese.  Despite calling the method 
		 * more than 3 times, we should never exceed 3 patties.
		 */
		System.out.println("\n\nNormal Burger before change " + count + " looks like this:\n		" +
		normalBurger.toString());
		
		normalBurger.addPatty();
		
		System.out.println("Normal Burger after change " + count + " looks like this:\n		" +
		normalBurger.toString());
		count++;
		
		//Change 4:
		normalBurger.addPatty();
		
		System.out.println("\n\nNormal Burger after change " + count + " looks like this:\n		" +
		normalBurger.toString());
		count++;
		
		//Change 5:
		normalBurger.addPatty();
		normalBurger.addPatty();
		normalBurger.addPatty();
		normalBurger.addPatty();
		
		System.out.println("\n\nNormal Burger after change " + count + " looks like this:\n		" +
		normalBurger.toString());
		count++;
		
		/*
		 * Change 6: Perform same test, this time removing patties.  Should never be less than 1.
		 */
		System.out.println("\n\nNormal Burger before change " + count + " looks like this:\n		" +
		normalBurger.toString());
		
		normalBurger.removePatty();
		
		System.out.println("Normal Burger after change " + count + " looks like this:\n		" +
				normalBurger.toString());
		count++;
		
		//Change 7:
		normalBurger.removePatty();
		normalBurger.removePatty();
		normalBurger.removePatty();
		normalBurger.removePatty();
		normalBurger.removePatty();
		
		System.out.println("\n\nNormal Burger after change " + count + " looks like this:\n		" +
		normalBurger.toString());
		count++;
		
		//Change 8: Test changePatty() method by adding 3 patties to Baron Burger and then changing to Chicken.
		System.out.println("\n\nBaron Burger before change " + count + " looks like this:\n		" +
		baronBurger.toString());
		
		baronBurger.addPatty();
		baronBurger.addPatty();
		baronBurger.changePatties("Chicken");
		
		System.out.println("Baron Burger after change " + count + " looks like this:\n		" +
		baronBurger.toString());
		count++;
		
		//Change 9: Change them to Veggie now.
		System.out.println("\n\nBaron Burger before change " + count + " looks like this:\n		" +
		baronBurger.toString());
		
		baronBurger.changePatties("Veggie");
		
		System.out.println("Baron Burger after change " + count + " looks like this:\n		" +
		baronBurger.toString());
		count++;
		
		//Change 10: finally back to Beef.
		baronBurger.changePatties("Beef");
		
		System.out.println("\n\nBaron Burger after change " + count + " looks like this:\n		" +
		baronBurger.toString());
		count++;
		
		
		
	}
	
	/**
	 * A test method to confirm that MyStack works properly.
	 */
	static void myStackTest() {
		//First try Integers by pushing, peeking, and popping a MyStack containing them.
		MyStack<Integer> testStack = new MyStack<Integer>();
		
		for (int i = 0; i < 11; i++) {
			testStack.push(i);
		}
		
		//Test copy() method.
		MyStack<Integer> copyStack = testStack.copy();
		
		//Test equals method to make sure copy() worked.
		System.out.println("Equals method on testStack to copyStack: " + copyStack.equals(testStack));		
		
		//Test size() method.
		System.out.println("The Size of testStack is: " + testStack.size());
		
		//Test isEmpty() on a populated stack.
		System.out.println("Is the testStack empty? " + testStack.isEmpty());
		
		//test .toString() method.
		System.out.println("testStack toString() as follows: " + testStack.toString());
		
		//test the peek() and pop() methods by printing the peek() and pop()ing the stack.
		for (int j = 0; j < 11; j++) {
			System.out.println(testStack.peek().toString());
			testStack.pop();
		}
		
		//Test size() method on empty stack.
		System.out.println("The Size of testStack is: " + testStack.size());
		
		//Test isEmpty() on an empty stack.
		System.out.println("Is the testStack empty? " + testStack.isEmpty());
		
		//Try to pop() an empty Stack.
		System.out.println("Pop the empty testStack: " + testStack.pop());
		
		//Try to peek() an empty Stack.
		System.out.println("Peek the empty testStack: " + testStack.peek());
		
		//repeat test with Strings.
		MyStack<String> testStack1 = new MyStack<String>();
		
		testStack1.push("INPUT");
		testStack1.push("STRING");
		testStack1.push("SEE");
		testStack1.push("TO");
		testStack1.push("TEST");
		testStack1.push("A");
		testStack1.push("IS");
		testStack1.push("THIS");
		
		//test .toString() method on strings.
		System.out.println("testStack toString() as follows: " + testStack1.toString());
		
		//Test one more data type, color.
		MyStack<Color> testStack2 = new MyStack<Color>();
		
		testStack2.push(Color.RED);
		testStack2.push(Color.ORANGE);
		testStack2.push(Color.YELLOW);
		testStack2.push(Color.GREEN);
		testStack2.push(Color.BLUE);
		testStack2.push(Color.MAGENTA);
		
		//test .toString() method on Colors.
		System.out.println("testStack toString() as follows: " + testStack2.toString());
	}
}
