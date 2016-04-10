
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;


/**
 * TCSS 342 - Winter 2016
 */

/**
 * The Coding Tree class.  Contains constructor and methods to create a CodingTree object,
 * which uses Huffman Tree algorithms to compress a target text file.  Also contains a 
 * custom node class called 'Bud' that is used to create a binary Huffman tree.
 * 
 * @author Travis Holloway
 * @version 02/12/16
 * @param <K>
 */
public class CodingTree {
	/*************************************** Constants ***************************************/
	
	/**
	 * Constant to represent the upper bound of MyHashTable.  Use value 42500 to run optional
	 * file Ulysses.txt.
	 */
	private final static int LARGE_INT = 32768;
	
	/**
	 * Constant to represent the lower bound of MyHashTable.  Current value will accommodate the 
	 * unique chars in WarAndPeace.txt..
	 */
	private final static int SMALL_INT = 97;

	/***************************************** Fields ****************************************/
	
	/**
	 * Field that represents the map of unique characters found in the text file.
	 */
	private Map<String, Integer> myChars;
	
	/**
	 * Field that represents the map of unique characters found in the text file.
	 */
	private Map<String, String> myCodes;
	
	/**
	 * Field to control what type of Huffman tree is built.  Boolean set to false will make a 
	 * character based tree, and boolean set to true will make a word based tree.
	 */
	private boolean myFlag;
	
	/**
	 * Field that represents the root node of the Huffman Tree.
	 */
	private Bud myRoot;

	/************************************** Constructors *************************************/
	
	/**
	 * Constructor for CodingTree.  Takes the text of a file that's to be compressed.  Utilizes
	 * private methods to implement Huffman tree algorithms and creates a custom map of the 
	 * characters in the text file that is used to compress the file.
	 * 
	 * @param theMessage The string that represents the text of the file to be encoded.
	 * @param theType A boolean flag to determine what type of Huffman tree to create.  False is
	 * char based, and true is word based.
	 */
	public CodingTree(final String theMessage, final boolean theType) {
		myFlag = theType;
		myChars = stringToMap(theMessage);
		if(theType) {
			
			myCodes = new MyHashTable<String, String>(LARGE_INT);
		} else {
			myCodes = new MyHashTable<String, String>(SMALL_INT);
		}
		myRoot = createTree(myChars);		
		StringBuilder test = new StringBuilder();
		createCodes(myCodes, myRoot, test);
	}

	/************************************* Public Methods ************************************/
	
	/**
	 * Getter for myCodes.
	 * 
	 * @returns the map of Character/Strings that is myCodes.
	 */
	public Map<String, String> getCodes() {
		return this.myCodes;
	}

	/************************************ Private  Methods ***********************************/
	
	/**
	 * A method to create myCodes, a map of each character with a unique string that represents
	 * it's Huffman tree binary value.
	 * 
	 * @return Returns a map of char's as keys and Strings as values.
	 */
	private void createCodes(final Map<String, String> theMap,
			final Bud theBud, final StringBuilder theSB) {
		
		/*
		 * If the node has a character, we are in a leaf and we will add the char and the 
		 * stringbuilder.
		 */
		if (theBud.getString() != null) {
			theMap.put(theBud.getString(), theSB.toString());
		}
		
		//If I can go left, go left and add 0 to the stringbuilder.
		if (theBud.getLeft() != null) {
			theSB.append("0");
			createCodes(theMap, theBud.getLeft(), theSB);
		}
		
		//If I can go right, go right and add 1 to the stringbuilder.
		if (theBud.getRight() != null) {
			theSB.append("1");
			createCodes(theMap, theBud.getRight(), theSB);
		}
		
		//If I can't do anything else, remove a character from the stringbuilder.
		if (theSB.length() > 0) {
			theSB.setLength(theSB.length() - 1);
		}
	}
	
	/**
	 * Private method to generate a tree from the myChars hashMap.  Generates the tree
	 * using Huffman's algorithm and returns the root node.
	 * 
	 * @return Returns the root node of the tree created by the method.
	 */
	private Bud createTree(final Map<String, Integer> theKeys) {
		final PriorityQueue<Bud> queue = new PriorityQueue<Bud>();
		for (final Entry<String, Integer> temp : theKeys.entrySet()) {
			final Bud bud = new Bud(temp.getKey(), temp.getValue());
			queue.add(bud);
		}
		
		Bud left = new Bud();
		Bud right = new Bud();
		
		while (!queue.isEmpty()) {
			left = queue.remove();
			if (!queue.isEmpty()) {
				right = queue.remove();
			} else {
				break;
			}
			
			if (!right.equals(null)) {
				final Bud bud = new Bud(left, right);
				queue.add(bud);
			}
			right = new Bud();
		}
		return left;
	}
	
	/**
	 * Method to check if a char is alphanumeric or "'" or "-".  Found code here:
	 * http://stackoverflow.com/questions/11241690/regex-for-checking-if-a-string-is-strictly
	 * -alphanumeric
	 * 
	 * @param theChar the character to be checked.
	 * @return true if it is, false if it is not alphanumeric or "'" "-".
	 */
	private boolean isAlphaNumeric(String s){
	    String pattern= "^[\\pL\\pN\\'-]+$";
	        if(s.matches(pattern)){
	            return true;
	        }
        return false;   
	}
	
	/**
	 * A method to take all characters from a string and store them in a map of char's.  The
	 * value of each char will the number of times it was added to the map.
	 * 
	 * @param theString The string that will be loaded into the map.
	 * @return Returns the map with the char's as keys and integers as values where the integer
	 * represents the amount of times the char was added to the map.
	 */
	private Map<String, Integer> stringToMap(final String theString) {
		Map<String, Integer> tempMap = null;
		if (myFlag == true) {
			StringBuilder sb = new StringBuilder();
			tempMap = new MyHashTable<String, Integer>(LARGE_INT);
			char[] stringChars = theString.toCharArray();
			
			for (final char temp: stringChars) {
				String s = String.valueOf(temp);
				if (isAlphaNumeric(s)) {
					sb.append(s);					
				} else {
					/*
					 * If the key already exists, increment it's value by one and insert it back 
					 * into the map.  If not, add it to the map with a value of 1.
					 */
					if (!sb.toString().isEmpty()) {
						if (tempMap.containsKey(sb.toString())) {
							int count = tempMap.get(sb.toString());
							count++;
							//System.out.println(sb.toString() + ", " + count);
							tempMap.put(sb.toString(), count);					
						} else {
							tempMap.put(sb.toString(), 1);
						}
					}
					
					
					/*
					 * If s is not alphanumeric, add it to the map as a single char, following
					 * the same procedure as above.
					 */
					
					if (tempMap.containsKey(s)) {
						int count = (int) tempMap.get(s);
						count++;
						tempMap.put(s, count);
					} else {
						tempMap.put(s, 1);
					}
					
					sb = new StringBuilder();
				}
				
			}
		} else {
			tempMap = new MyHashTable<String, Integer>(SMALL_INT);
			char[] stringChars = theString.toCharArray();
			
			for (final char temp: stringChars) {
				/*
				 * If the key already exists, increment it's value by one and insert it back 
				 * into the map.  If not, add it to the map with a value of 1.
				 */
				if (tempMap.containsKey(String.valueOf(temp))) {
					int count = (int) tempMap.get(String.valueOf(temp));
					count++;
					tempMap.put(String.valueOf(temp), count);
				} else {
					tempMap.put(String.valueOf(temp), 1);
				}
			}
		}
		
		return tempMap;
	}
}

/************************************** Private  Classes *************************************/

/**
 * A private class to build custom node objects called buds.  
 * 
 * 
 */
class Bud implements Comparable<Bud> {
	/**
	 * A field that represents the character of the bud, will be null if the bud is not a leaf.
	 */
	final private String myChar;
	
	/**
	 * A field that represents the integer value of the bud.  The higher the value, the higher
	 * in the tree the bud exists.
	 */
	final private int myCount;
	
	/**
	 * Field that represents the left child of the bud.
	 */
	final private Bud myLeft;
	
	/**
	 * Field that represents the right child of the bud.
	 */
	final private Bud myRight;
	
	/**
	 * Default constructor.  Creates a bud with no children, 0 for the count, and
	 * null for char.  
	 */
	Bud() {
		myLeft = null;
		myRight = null;
		myCount = 0;
		myChar = null;
	}
	
	/**
	 * Constructor for non-leaf Buds.  Initializes a bud with a left and right child.
	 * The count of the new bud will be left + right.  Used for creating non leaf's,
	 * so does not include a char.
	 * 
	 * @param theLeft The left child of the bud, can be null.
	 * @param theRight The right child of the bud, can be null.
	 */
	Bud(final Bud left, final Bud right) {
		myLeft = left;
		myRight = right;
	    myCount = left.myCount + right.myCount;
		myChar = null;
	}
	
	/**
	 * Constructor for leaf Buds.  Initializes a bud with a char and count.  Used for creating
	 * leaf's, so the left and right are set to null.
	 * 
	 * @param theChar The char value of the leaf Bud.
	 * @param theCount the count of the leaf Bud.
	 */
	Bud(final String theChar, final int theCount) {
		myLeft = null;
		myRight = null;
		myCount = theCount;
		myChar = String.valueOf(theChar);
	}
	

	/**
	 * Getter for the char of a Bud.
	 * 
	 * @returns the char of the Bud.
	 */
	String getString() {
		return myChar;
	}
	
	/**
	 * Getter for the count of a Bud.
	 * 
	 * @returns the count of the Bud.
	 */
	int getCount() {
		return this.myCount;
	}
	
	/**
	 * Getter for the left child of a Bud.
	 * 
	 * @returns the left child of the Bud.
	 */
	Bud getLeft() {
		return this.myLeft;
	}
	
	/**
	 * Getter for the right child of a Bud.
	 * 
	 * @returns the right child of the Bud.
	 */
	Bud getRight() {
		return this.myRight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Bud theBud) {		
		return this.getCount() - theBud.getCount();
	}
}
