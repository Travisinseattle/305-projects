import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * TCSS 342 - Winter 2016
 */

/**
 * The HashTable of the 
 * 
 * @author Travis Holloway
 * @version 03/04/16
 * @param <K> parameter for the keys used by the hash table.
 * @param <V> parameter for the values assigned to the keys.
 */
public class MyHashTable<K, V> extends HashMap<K, V> {

	/*************************************** Constants ***************************************/
	
	/**
	 * A constant representing an auto-generated serial ID.
	 */
	private static final long serialVersionUID = 1553268912064147558L;
	
	/**
	 * Constant to set the size of the histogram array called myProbes.  Use value 200 to run 
	 * optional file Ulysses.txt.
	 */
	private static final int SIZE = 500;
	

	/***************************************** Fields ****************************************/
	
	/**
	 * Field to represent the value at which to mod the table by.
	 */
	private static int mySize;
	
	/**
	 * Field that is a list of integers set 0.  Each time a probe occurs increment the index of
	 * the length of the probe by 1.
	 */
	private int[] myProbes;
	
	/**
	 * Field to represent the array that holds the key values.  
	 */
	private List<MyEntry<K,V>> myTable;

	/************************************** Constructors *************************************/
	
	/**
	 * Constructor for the MyHashTable. Takes an integer value as a parameter that represents
	 * how many 'buckets' will be created by the table.
	 * 
	 * @param theSize the integer value of buckets.
	 */
	public MyHashTable(final int theSize) {
		mySize = theSize;
		myProbes = new int[SIZE];
		Entry<K, V> entry = new MyEntry<K, V>(null, null);
		myTable = new ArrayList<MyEntry<K,V>>(theSize);
		for (int i = 0; i < theSize; i++) {
			myTable.add((MyHashTable<K, V>.MyEntry<K, V>) entry);
		}		
	}

	/************************************* Public Methods ************************************/
	
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * 
	 * @param theKey The key whose presence in this map is to be tested.
	 * @return true if this map contains a mapping for the specified key.
	 */
	@SuppressWarnings("unchecked")
	public boolean containsKey(Object theKey) {
		boolean returnBool = false;
		
		//protect against null keys.
		if (theKey.equals(null)) {
			//print the value so it can be checked why it's associated with a null key.
			System.out.println("****NULL KEY****");
			throw new NullPointerException();
		}
		
		//Declare a boolean to provide an escape for the method.
		boolean flag = false;
		
		//acquire the hashcode of the key.
		int index = hash((K) theKey);
		
		while (!flag) {
			if (myTable.get(index).getKey() == null) {
				return returnBool;
			} else if (myTable.get(index).getKey().equals(theKey)) {
				//System.out.println("contains");
				/*
				 * If the entry at the index has a matching key, return that key's value and set
				 * the flag to true to get out of the loop.
				 */
				returnBool = true;
				flag = true;
			} else {
				/*
				 * Increment index so the method will check the next index forward.  This will
				 * keep the method going until an empty spot is found or until the previous
				 * instance of the key is found.
				 */
				index++;
				/*
				 * If the index gets larger than the table capacity, reset it to 0 so it can
				 * start from the beginning.  This insures that the search wraps around the table. 
				 */				
				if (index > mySize) {
					index = 0;
				}
			}
		}		
		return returnBool;		
	}
	
	/**
	 * Method to return a set of entries.  Iterates through myTable and only adds entries that
	 * don't have null Key's. 
	 * 
	 * @return The set of entries in myTable.
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		final Set<Map.Entry<K, V>> set = new TreeSet<Map.Entry<K, V>>();
		
		for (final MyEntry<K, V> entry: myTable) {
			if(entry.getKey() != null) {
				set.add(entry);
			}
		}
		return set;		
	}
	
	
	/**
	 * 
	 * Method for getting values out of the map, based on the key passed to the method. Since
	 * Java used Object in their signature, I must too.  I have cast theKey back to K fr use
	 * in the method.
	 * 
	 * @param theKey The key whose associated value is to be returned.
	 * @return The value to which the specified key is mapped, or null if this map contains no
	 * mapping for the key.
	 */
	@SuppressWarnings("unchecked")
	public V get(Object theKey) {
		//Declare a V object and set it to null.
		V value = null;
		
		//protect against null keys.
		if (theKey.equals(null)) {
			//print the value so it can be checked why it's associated with a null key.
			System.out.println("****NULL KEY****");
			throw new NullPointerException();
		}
		
		//Declare a boolean to provide an escape for the method.
		boolean flag = false;
		
		//acquire the hashcode of the key.
		int index = hash((K) theKey);
		
		while (!flag) {
			if (myTable.get(index).getKey() == null) {
				return value;
			} else if (myTable.get(index).getKey().equals(theKey)) {
				/*
				 * If the entry at the index has a matching key, return that key's value and set
				 * the flag to true to get out of the loop.
				 */
				value = myTable.get(index).getValue();
				flag = true;
			} else {
				/*
				 * Increment index so the method will check the next index forward.  This will
				 * keep the method going until an empty spot is found or until the previous
				 * instance of the key is found.
				 */
				index++;
				/*
				 * If the index gets larger than the table capacity, reset it to 0 so it can
				 * start from the beginning.  This insures that the search wraps around the table. 
				 */				
				if (index > mySize) {
					index = 0;
				}
			}
		}
		//System.out.println(value);
		return value;		
	}
	
	
	/**
	 * The method to add key/value pairs to the map.  
	 * 
	 * @param theKey The key to place in the map.
	 * @param theValue The Value to associate to the key.
	 * 
	 * @return The old value if the value was replaced or null if none was replaced.
	 */
	@Override
	public V put(K theKey, V theValue) {
		//Int to count the amount of times a probe occurs.
		int probe = 0;
		//protect against null keys.
		if (theKey.equals(null)) {
			//print the value so it can be checked why it's associated with a null key.
			System.out.println("****NULL****" + theValue + "****NULL****");
			throw new NullPointerException();
		}
		
		//First, acquire the hashcode of the key.
		int index = hash(theKey);
		
		//Declare a boolean to provide an escape for the method.
		boolean flag = false;
		
		//Make an Entry for the key/value pair.
		final MyEntry<K, V> entry = new MyEntry<K, V>(theKey, theValue);
		
		/*
		 * Look for a place to put the entry.  If the table doesn't already have an entry 
		 * at the matching index, place the new Entry in that index. If there is an entry
		 * in that index, check it's key against the entry's key.  If they are equal, update
		 * the value of the entry at the index with the new entry's value.
		 */
		while (!flag) {
			if(myTable.get(index).getKey() == null) {
				myTable.remove(index);
				myTable.add(index, entry);
				theValue = null;
				flag = true;
				//increment myProbes at the index of probe.
				myProbes[probe]++;
				probe = 0;
			} else if(myTable.get(index).getKey().equals(theKey)) {
				//update value with the old value at this index for the return.
				theValue = myTable.get(index).getValue();
				myTable.remove(index);
				//add the new entry at the index to the old entry's value.
				myTable.add(index, entry);
				//System.out.println(entry.getKey() + ", " + entry.getValue());
				//System.out.println("put replace old: " + entry.getKey());
				flag = true;
				//increment myProbes a the index of probe.
				myProbes[probe]++;
				probe = 0;
			} else {
				/*
				 * increment index so the method will check the next index forward.  This will
				 * keep the method going until an empty spot is found or until the previous
				 * instance of the key is found.
				 */
				index++;
				probe++;
				/*
				 * if the index gets larger than the table capacity, reset it to 0 so it can
				 * start from the beginning.  This insures that the search wraps around the table. 
				 */				
				if (index > mySize) {
					index = 0;
				}
			}
		}		
		return theValue;		
	}
	
	public void stats(){
		final Set<java.util.Map.Entry<K, V>> set = entrySet();
		final int keys = set.size();
		int last = 0;
		float percent = (float) ((float)keys /(float) mySize);
		percent = percent * 100;
		float average = findAverage(myProbes);
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = 0; i < myProbes.length; i++) {
			sb.append(myProbes[i]);
			sb.append(", ");
			if(myProbes[i] != 0) {
				last = i;
			}
			if((i % 15) == 14) {
				sb.append('\n');
			}
		}
		sb.setLength(sb.length() - 2);
		sb.append(']');
		
		System.out.println("MyHashTable Stats:");
		System.out.println("******************");
		System.out.println("Number of Entries: " + keys);
		System.out.println("Number of Buckets: " + mySize);
		System.out.println("Histogram of Probes: " + sb.toString());
		System.out.println("Fill Percentage: %" + percent);
		System.out.println("Max Linear Probe: " + last);
		System.out.println("Average Linear Probe: " + average);
		System.out.println("***********************");
		
	}
	
	
	/**
	 * ToString method for the map.
	 * @return 
	 */
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (MyEntry<K, V> entry : myTable) {			
			sb.append(entry.toString());
			sb.append(", ");
		}
		sb.setLength(sb.length() - 1);
		sb.append('}');
		
		return sb.toString();		
	}	
	
	
	/**
	 * 
	 * Custom implementation of Entry to preserve the fuctionality of entrySet in 
	 * MyHashTable. 
	 * 
	 * @author Travis Holloway
	 * @version 03/04/16
	 */
	public class MyEntry<E, A> implements Entry<E, A>, Comparable<MyEntry<K,V>>{
	    private final E key;
	    private A value;	    
	    
	    /**
	     * 
	     * The constructor for MyEntry.  Creates an Entry consisting of generic Key and 
	     * generic Value.
	     *
	     * @param key The key for the entry.
	     * @param value The value for the entry.
	     */
	    public MyEntry(final E key, final A value) {
	        this.key = key;
	        this.value = value;
	    }
	    
	    /**
	     * Returns the key corresponding to this entry.
	     * 
	     * @return The key corresponding to this entry.
	     */
	    public E getKey() {
	        return key;
	    }
	    
	    /**
	     * Returns the value corresponding to this entry. If the mapping has been removed
	     * from the backing map (by the iterator's remove operation), the results of this
	     * call are undefined.
	     * 
	     * @return The value corresponding to this entry.
	     */
	    public A getValue() {
	        return value;
	    }
	    
	    /**
	     * Replaces the value corresponding to this entry with the specified value (optional
	     * operation). (Writes through to the map.) The behavior of this call is undefined if
	     * the mapping has already been removed from the map (by the iterator's remove
	     * operation).
	     * 
	     * @param value New value to be stored in this entry.
	     * 
	     * @return Old value corresponding to the entry.
	     */
	    public A setValue(final A value) {
	        final A oldValue = this.value;
	        this.value = value;
	        return oldValue;
	    }
	    
	    /**
	     * Method to return a string value of an entry.
	     * 
	     * @return The string representation of an entry.
	     */
	    public String toString() {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(this.getKey().toString());
	    	sb.append('=');
	    	sb.append(this.getValue().toString());
	    	return sb.toString();
	    }

		/**
	     * 
	     * CompareTo method required to implement Comparable.  Used by the TreeSet in entrySet.
	     * compare's the hashCode of the key of each entry. 
	     * 
	     * @param entry The entry to compare to.
	     * @return the value of the Integer.compare() method.
	     */
		@Override
		public int compareTo(MyHashTable<K, V>.MyEntry<K, V> o) {
			// TODO Auto-generated method stub
			return Integer.compare(this.getKey().hashCode(), ((MyHashTable<K, V>.MyEntry<K, V>)
					o).getKey().hashCode());
		}
	}

	/************************************ Private  Methods ***********************************/
	
	/**
	 * Method to create a key's hash value.  Modulates the raw hash value returned from the
	 * hashCode() method with a prime that is slightly smaller than the table size.  This 
	 * insures that all hashcodes will be between 0 and the capacity of the list(32768).
	 * 
	 * @return returns the modulated hashcode.
	 */
	private int hash(K key) {
		final int modulatedHash = Math.floorMod(key.hashCode(), mySize);
		return modulatedHash;
	}
	
	private float findAverage(int[] array) {
		
		float total = 0;
		
		for (int i = 0; i < array.length; i++) {
			total = total + ((i + 1) * array[i]);
		}
		
		total = total / mySize;
		
		return total;
	}
	
}


