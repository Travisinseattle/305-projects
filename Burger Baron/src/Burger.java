/**
 * TCSS 342 - Winter 2016
 */


/**
 * The Burger Class.  This class creates burger objects and determines what items the burger 
 * will consist of using MyStack Objects.  
 * 
 * @author Travis Holloway
 * @version 01/15/2016
 */
public class Burger {
	/*************************************** Constants ***************************************/
	
	/***************************************** Fields ****************************************/
	
	/**
	 * A field to represent the MyStack object that contains the burger.
	 */
	private MyStack<String> myBurgerStack;
	
	/**
	 * A field to represent the burger's patty type.
	 */
	private String myPatty;
	
	/**
	 * A constant to represent the Recipe Stack which has all the items in a Baron Burger.
	 */
	private MyStack<String> myRecipeBurger;
	
	/**
	 * A field to represent the Recipe Stack which has all the items in a Baron Burger.
	 */

	/************************************** Constructors *************************************/
	
	/**
	 * The constructor for the Burger class.  Takes a boolean as a parameter that determines 
	 * if the burger will be constructed as a Baron Burger, or a custom burger.
	 * 
	 * @param theWorks Boolean that indicates a Baron Burger if true, custom burger if not.
	 */
	public Burger(boolean theWorks) {
		//Create a new Stack for the burger object.
		myBurgerStack = new MyStack<String>();
		myPatty = "Beef";
		myRecipeBurger = createRecipeStack();
		
		/*
		 * If the boolean is true, then make myBurgerStack using the createRecipeStack() method
		 * if it is not, then make a default burger stack using a bun and patty.
		 */
		if (theWorks) {
			myBurgerStack = createRecipeStack();
		} else {
			myBurgerStack.push("Bottom Bun");
			myBurgerStack.push(myPatty + " Patty");
			myBurgerStack.push("Top Bun");
		}
	}
	
	/************************************* Public Methods ************************************/
	
	/**
	 * A method to add an ingredient category to a Burger.  Categories consist of Cheese,
	 *  Veggies, and Sauces.  This method will add all items in a category.
	 * 
	 * @param theCategory The category that needs to be removed.
	 */
	public void addCategory(String theCategory) {
		switch (theCategory) {
		case "Cheese":
			this.addIngredient("Pepperjack");
			this.addIngredient("Mozzarella");
			this.addIngredient("Cheddar");
			break;
		case "Veggies":
			this.addIngredient("Pickle");
			this.addIngredient("Lettuce");
			this.addIngredient("Tomato");
			this.addIngredient("Onions");
			this.addIngredient("Mushrooms");
			break;
		case "Sauce":
			this.addIngredient("Mayonnaise");
			this.addIngredient("Baron-Sauce");
			this.addIngredient("Mustard");
			this.addIngredient("Ketchup");
			break;
		default:
			break;
		}
	}
	
	/**
	 * A method to add an ingredient to a Burger.
	 * 
	 * @param theItem the ingredient to be added to the burger.
	 */
	public void addIngredient(String theItem) {
		/*
		 * Create a temp stack to hold the original items in myBurgerStack as well as the
		 * additional item.  Once all items have been checked and stored, push all items 
		 * from the temp stack back to myBurgerStack so that they are in the correct order.
		 */
		MyStack<String> temp = new MyStack<String>();
		MyStack<String> tempRecipe = ((MyStack<String>) myRecipeBurger).copy();
		while (!myBurgerStack.isEmpty() && !tempRecipe.isEmpty()) {
			String recipeItem = tempRecipe.pop();
			//System.out.println("The Recipe Item is: " + recipeItem);
			String burgerItem = myBurgerStack.peek();
			//System.out.println("The Burger Item is: " + burgerItem);
			if (recipeItem.equals(burgerItem)) {
				if (isPatty(recipeItem) && isPatty(theItem)) {
					//System.out.println("I pushed this Patty: " + theItem);
					temp.push(theItem);
				}
				temp.push(myBurgerStack.pop());
			} else if (recipeItem.equals(theItem)) {
				temp.push(theItem);
				//System.out.println("I pushed this Item: " + theItem);
			}
		}
		//Use reverseStack to transfer the contents of temp to myBurgerStack.
		reverseStack(temp);
	}
	
	/**
	 * Method to add patties of the current type to the burger, up to a limit of
	 * three.
	 */
	public void addPatty() {
		MyStack<String> temp = new MyStack<String>();
		/*
		 *   
		 */
		if (this.pattyCount() < 2) {
			while (!isCheese(myBurgerStack.peek()) && !isPatty(myBurgerStack.peek())) {
				temp.push(myBurgerStack.pop());
			}
			temp.push(myPatty + " Patty"); 
			while (!temp.isEmpty()) {
				myBurgerStack.push(temp.pop());
			}
		} else if (this.pattyCount() == 2) {
			while (!isPatty(myBurgerStack.peek())) {
				temp.push(myBurgerStack.pop());
			}
			temp.push(myPatty + " Patty"); 
			while (!temp.isEmpty()) {
				myBurgerStack.push(temp.pop());
			}
		}		
	}
	
	/**
	 * Method to change whatever patties are on the burger to the patty type passed
	 * to the method.
	 * 
	 * @param pattyType The new patty type.
	 */
	public void changePatties(String pattyType) {
		/*
		 * First confirm that pattyType is a valid option.  If it is, dig down myBurgerStack
		 * till you reach the patties.  For each patty, push a new patty of the new type to
		 * the temp stack.  Rebuild myBurgerStack from the temp stack.
		 */
		if ((pattyType.equals("Beef")) || (pattyType.equals("Chicken")) ||
				(pattyType.equals("Veggie"))) {
			MyStack<String> temp = new MyStack<String>();
			while (!myBurgerStack.isEmpty()) {
				if (!isPatty(myBurgerStack.peek())) {
					temp.push(myBurgerStack.pop());
				} else {
					temp.push(pattyType + " Patty");
					myBurgerStack.pop();
				}
			}
			
			while (!temp.isEmpty()) {
				myBurgerStack.push(temp.pop());
			}
		}
		
	}
	
	/**
	 * A method to remove an ingredient category.  Categories consist of Cheese,
	 *  Veggies, and Sauces.  This method will remove all items in a category.
	 *   
	 * @param theCategory The category that needs to be removed.
	 */
	public void removeCategory(String theCategory) {
		switch (theCategory) {
		case "Cheese":
			this.removeIngredient("Pepperjack");
			this.removeIngredient("Mozzarella");
			this.removeIngredient("Cheddar");
			break;
		case "Veggies":
			this.removeIngredient("Pickle");
			this.removeIngredient("Lettuce");
			this.removeIngredient("Tomato");
			this.removeIngredient("Onions");
			this.removeIngredient("Mushrooms");
			break;
		case "Sauce":
			this.removeIngredient("Mayonnaise");
			this.removeIngredient("Baron-Sauce");
			this.removeIngredient("Mustard");
			this.removeIngredient("Ketchup");
			break;
		default:
			break;
		}
	}
	
	/**
	 * A method to remove an item from a burger.
	 * 
	 *@param theItem the Item to be removed.
	 */
	public void removeIngredient(String theItem) {
		/*
		 * Make a new Stack called temp.  While myBurgerStack is not empty, peek at top item and
		 * see if it is equal to theItem.  If it is, pop it and check the next item.  If not, 
		 * push the item from myBurgerStack to temp.  Finally, run the reverse method to 
		 * re-populate myBurgerStack with temp.
		 */
		MyStack<String> temp = new MyStack<String>();
		while (!myBurgerStack.isEmpty()) {
			if (myBurgerStack.peek().equals(theItem)) {
				myBurgerStack.pop();				
			} else {
				String temp1 = myBurgerStack.pop();
				temp.push(temp1);
			}
		}
		//Use reverseStack to transfer the contents of temp to myBurgerStack.
		reverseStack(temp);
	}
	
	/**
	 * Method to remove patties of the current type from the burger, while maintaining a
	 * minimum of one patty on the burger.
	 */
	public void removePatty() {
		MyStack<String> temp = new MyStack<String>();
		/*
		 * Make an empty temp array.  Use it to hold each layer of myBurgerStack as you 
		 * peel them off till you get to the patties.  If the patty count is higher than
		 * one, pop myBurgerStack once to get rid of a patty.  Transfer the contents of
		 * temp back to myBurgerStack to rebuild the burger.  
		 */
		while (!isPatty(myBurgerStack.peek())) {
			temp.push(myBurgerStack.pop());
		}
		if (pattyCount() > 1) {
			myBurgerStack.pop();
		}
		while (!temp.isEmpty()) {
			myBurgerStack.push(temp.pop());
		}
	}
	
	/**
	 * The ToString method for the Burger class.  Prints the myBurgerStack toString method.
	 */
	public String toString() {
		MyStack<String> tempStack = myBurgerStack.copy();
		StringBuilder sb = new StringBuilder();
		int count = tempStack.size();
		sb.append("[");
		for (int i = 0; i < count; i++) {			
			sb.append(tempStack.pop().toString());
			sb.append(", ");
		}
		sb.append("Yum!]");
		String returnString = sb.toString();
		return returnString;
	}
	
	/************************************* Private Methods ***********************************/
	
	/**
	 * A method to create the recipe stack.
	 */
	private MyStack<String> createRecipeStack() {
		MyStack<String> returnStack = new MyStack<String>();
		
		//Create the strings for the stack.
		String pickle = "Pickle";
		String topBun = "Top Bun";
		String mayo = "Mayonnaise";
		String sauce = "Baron-Sauce";
		String lettuce = "Lettuce";
		String tomato = "Tomato";
		String onion = "Onions";
		String pepper = "Pepperjack";
		String mozza = "Mozzarella";
		String cheddar = "Cheddar";
		String patty = myPatty + " Patty";
		String mush = "Mushrooms";
		String mustard = "Mustard";
		String catsup = "Ketchup";
		String botbun = "Bottom Bun";
		
		//add them to the Stack.
		returnStack.push(botbun);
		returnStack.push(catsup);
		returnStack.push(mustard);
		returnStack.push(mush);
		returnStack.push(patty);
		returnStack.push(cheddar);
		returnStack.push(mozza);
		returnStack.push(pepper);
		returnStack.push(onion);
		returnStack.push(tomato);
		returnStack.push(lettuce);
		returnStack.push(sauce);
		returnStack.push(mayo);
		returnStack.push(topBun);
		returnStack.push(pickle);
		
		return returnStack;
	}
	
	/**
	 * A boolean to track if an item is cheese. Used to place extra patties on top of
	 * the cheese layer.
	 * 
	 * @param theItem The item that the method is checking.
	 * @return true if it is a patty, false if it isn't.
	 */
	private boolean isCheese(String theItem) {
		boolean returnValue = false;
		if ((theItem.equals("Pepperjack")) || (theItem.equals("Mozzarella")) ||
				(theItem.equals("Cheddar"))) {
			returnValue = true;
		}
		return returnValue;
	}
	
	/**
	 * A boolean to track if an item is a patty.
	 * 
	 * @param theItem The item that the method is checking.
	 * @return true if it is a patty, false if it isn't.
	 */
	private boolean isPatty(String theItem) {
		boolean returnValue = false;
		if ((theItem.equals("Beef Patty")) || (theItem.equals("Chicken Patty")) ||
				(theItem.equals("Veggie Patty"))) {
			returnValue = true;
		}
		return returnValue;
	}
	
	/**
	 * A method to discover and return the amount of patties on the burger.
	 * 
	 * @return A integer count of the patties.
	 */
	private int pattyCount() {
		MyStack<String> temp = ((MyStack<String>) myBurgerStack).copy();
		int count = 0;
		while (!temp.isEmpty()) {
			if (isPatty(temp.peek())) {
				count++;
				temp.pop();
			} else {
				temp.pop();
			}
		}
		return count;
	}
	
	/**
	 * A method to reverse the temp stack and re-populate myBurgerStack in the add and remove
	 * methods.
	 * 
	 * @param myStack the temp stack to reverse.
	 */
	private void reverseStack(MyStack<String> theStack) {
		while (!theStack.isEmpty()) {
			myBurgerStack.push(theStack.pop());
		}
	}
}
