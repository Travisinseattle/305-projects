/**
 * TCSS 342 - Winter 2016
 */

/**
 * The MyStack Class.  Responsible for creating stacks that will represent burgers in the 
 * program.  
 * 
 * @author Travis Holloway
 * @version 01/15/2016
 * @param <T> The Type of Item stored by MyStack.
 */
public class MyStack<T> implements Cloneable {
	
	/**************************************** Constants **************************************/
	
	/***************************************** Fields ****************************************/
	
	/**
	 * A field to point at the item that is on the top of the stack.
	 */
	private Node myTop;
	
	/**
	 * A field to store the size of the stack.
	 */
	private int mySize;
	
	/************************************** Constructors *************************************/
	
	/**
	 * The constructor for a MyStack Object.  Initializes the mySize variable as 0.
	 */
	public MyStack() {
		mySize = 0;
	}	
	
	/************************************* Public Methods ************************************/

	/**
	 * A method to deep copy a MyStack array.
	 * 
	 * @return an identical copy of the Stack including all elements.
	 */
	public MyStack<T> copy() {
		MyStack<T> tempStack1 = new MyStack<T>();
		MyStack<T> tempStack2 = new MyStack<T>();
		while (!this.isEmpty()) {
			tempStack1.push(this.pop());
		}
		while (!tempStack1.isEmpty()) {
			this.push(tempStack1.peek());
			tempStack2.push(tempStack1.pop());
		}
		return tempStack2;
	}
	
	/**
	 * Boolean method to determine if the Stack is empty.
	 * 
	 * @return True if mySize is < 1, false otherwise.
	 */
	public boolean isEmpty() {
		return mySize < 1;
	}
	
	/**
	 * Method to view the item that is currently on top of the Stack.
	 * 
	 * @return The item that is currently on top of the Stack.
	 */
	public T peek() {
		if (this.isEmpty()) {
			System.err.println("Stack is empty, there are no elements to peek.");
			return null;
		}	
		return myTop.getItem();
	}
	
	/**
	 * Method to remove an item from the top of the Stack.
	 */
	public T pop() {
		T theT;
		if (this.isEmpty()) {
			System.err.println("Stack is empty, there are no elements to pop.");
			return null;
		}	
		if (myTop.getNode() == null) {
			theT = myTop.getItem();
			mySize--;
			myTop = null;
			} else {
				theT = myTop.getItem();
				mySize--;
				myTop = myTop.getNode();
			}
		//System.out.println("*** " + theT.toString());
		return theT;
	}
	
	/**
	 * Method to add a Item to the Stack, using the private node class to handle the storing
	 * of the Item.
	 * 
	 * @param t The Item to push to the Stack.
	 */
	public void push(T t) {
		/*
		 * If the size is 0, make a new node with no link, otherwise create a new Node that
		 * points at the old myTop.  Increment mySize in either case.
		 */
		if (mySize == 0) {
			myTop = new Node(t);
			mySize++;
		} else {
			myTop = new Node(t, myTop);
			mySize++;
		}
	}
	
	/**
	 * A method to get the current size of the Stack.
	 * 
	 * @return the integer representing the number of elements in the stack.
	 */
	public int size() {
		return mySize;
	}
	
	/**
	 * ToString method for the MyStack class.
	 * 
	 * @return A String representation of the items in the Stack.
	 */
	public String toString() {
		MyStack<T> tempStack = this.copy();
		StringBuilder sb = new StringBuilder();
		int count = tempStack.size();
		sb.append("[ ");
		for (int i = 0; i < count; i++) {			
			sb.append(tempStack.pop().toString());
			sb.append(" ");
		}
		sb.append("]");
		String returnString = sb.toString();
		return returnString;
	}
	
	/************************************* Private Methods ***********************************/
	
	/**
	 * A Private node class that will store the address of the next element in the stack.
	 * 
	 * @author Travis Holloway.
	 * @version 01/15/2016.
	 */
	private class Node {
		
		/*************************************** Fields **************************************/
		
		/**
		 * Field for the Item stored in the node.
		 */
		private T myItem;
		
		/**
		 * Field for the preceding node of the node.
		 */
		private Node myNode;
		
		/************************************ Constructors ************************************/
		
		/**
		 * Constructor for the Node, used only for the initial that will be on the bottom.
		 *
		 * @param T The Item that the Node will hold. 
		 */
		private Node(T t) {
			myItem = t;
			myNode  = null;
		}
		
		/**
		 * Constructor for the Node, used for all nodes except the bottom Node.
		 *
		 * @param T The Item that the Node will hold. 
		 * @param theNode The next Node, used when the next node is not null.
		 */
		private Node(T t, Node theNode) {
			myItem = t;
			myNode = theNode;			
		}
		
		/************************************ Private Methods **********************************/
		
		/**
		 * Getter for the Item stored by the Node.
		 * 
		 * @return the Item stored by the Node.
		 */
		private T getItem() {
			return myItem;
		}
		
		/**
		 * Getter for the preceding Node of the node.
		 * 
		 * @return the preceding node.
		 */
		private Node getNode() {
			return myNode;
		}
		
	}
}
