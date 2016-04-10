/**
 * TCSS 342 - Winter 2016
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Main Class. Contains methods to compress and decompress a
 * target text file.  Creates 3 new files: codes.txt, compressed.txt, and decoded_text.txt.
 * These files can be found in the folder of the program: \.Compressed Literature\.*
 * 
 * @author Travis Holloway
 * @version 02/12/16
 */
public class Main {

	/**
	 * The main class of the program. 
	 *  
	 * @param theArgs arguments of the program.
	 * @throws IOException 
	 */
	public static void main(String[] theArgs) throws IOException {
		
		/*
		 * Create a string that is the target text for compression.  Text should be in the main 
		 * folder of the program.  This is the only place that needs to be changed if different 
		 * files are to be compressed.
		 */
		final String target = "WarAndPeace.txt";
		
		//create a start time for use with capturing system runtime.
		final long startTime = System.currentTimeMillis();
		
		/*
		 * Run the compression algorithm method on the target file. Flag false if you want to
		 * create a tree just using characters, or true if you want to used words.
		 */		
		compress(target, true);
		
		//Run the decompression algorithm method on the target file.
		decode("compressed.txt");
		
		//Create a double that represents the compression ratio.		
		double ratio = compressionRatio(target);		
		
		//perform a few alterations in order to print it out as a percent.
		ratio = ratio * 100;		
		DecimalFormat df = new DecimalFormat("######.##"); 
		ratio = Double.valueOf(df.format(ratio));
		
		System.out.printf("The compression ratio on " + target + " after this program runs"
				+ " is: " + ratio + " percent.");
		
		//Complete the capture of runtime and print results.
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("\nThe runtime of this program in milliseconds is: " + totalTime);
	}
	
	/**
	 * A method to create a compressed file using the CodingTree.
	 * 
	 * @param theText The string representation of the target text file.
	 * @param theBool flag for which type of tree to create: true is word based Huffman
	 * tree, and false is char based.
	 * @throws IOException 
	 */
	static void compress(final String theText, final boolean theBool) throws IOException {
		
		//create a string to hold the contents of the text file.
		String content = "";
		
		//read the contents of the text file to content.
		try {
			content = readFile(theText, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Create a CodingTree by passing it content.
		final CodingTree tree = new CodingTree(content, theBool);
		
		//Generate the code file and save it to codes.txt.
		generateCodeFile(tree);
		
		//convert content to a char array.
		char[] contentChars = content.toCharArray();
		
		/*
		 * Using a StringBuilder and the code map in CodingTree, convert the chars
		 * in contentChars to the value of the char in CodingTree.
		 */
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        if (theBool) {
        	for (final char temp: contentChars) {
    			String nonAlpha = String.valueOf(temp);
    			if (isAlphaNumeric(nonAlpha)) {
    				sb1.append(nonAlpha);
    			} else {
    				if (!sb1.toString().isEmpty()) {
    					sb2.append(tree.getCodes().get(sb1.toString()));
    				}
    				
    				sb2.append(tree.getCodes().get(nonAlpha));
    				sb1 = new StringBuilder();
    			}
    		}
        } else {
        	for (final char temp: contentChars) {
        		sb2.append(tree.getCodes().get(String.valueOf(temp)));
        		//System.out.println(temp + ": " + tree.getCodes().get(String.valueOf(temp)));
        	}
        }
		
		
		
		//convert sb to a char array of 1's and 0's.
		char[] binaries = sb2.toString().toCharArray();
		
		/*
		 * Convert the 1's and 0's to bits using BitSet.
		 */
		BitSet bits = new BitSet(binaries.length);
		for (int i = 0; i < binaries.length; i++) {
			if (binaries[i] == '1') {
				bits.set(i, true);
			} else {
				bits.set(i, false);
			}
		}
		
		/*
		 * finally convert the BitSet to a byte array and write the array to text
		 * file called compressed.text
		 */
		byte[] bytes = bits.toByteArray();
		FileOutputStream fos = new FileOutputStream("compressed.txt");
		fos.write(bytes);		
		fos.close();
		((MyHashTable<String,String>) tree.getCodes()).stats();
	}
	
	/**
	 * A method to find the compression ratio of the raw file and compressed file.
	 * Performs the math using the compressed file 'compressed.txt' and the string passed to
	 * the file, which should be the original file.
	 * 
	 * @param theFile The string representing the location of the original file to be
	 * compressed.
	 * @return The double that represents the compression ratio.
	 */
	static double compressionRatio(final String theFile) {
		
		final File raw = new File(theFile);
		final File compressed = new File("compressed.txt");
		final double rawSize = raw.length();
		final double compSize = compressed.length();
		System.out.println("The original size of " + theFile + " is: " + rawSize + ".\nThe"
				+ " compressed size of " + theFile + " is: " + compSize + ".");
		
		return  rawSize / compSize;
	}
	
	/**
	 * A method to create a new hashmap with strings as keys and chars as values using
	 * the codes.txt file created by generateCodeFile.
	 * 
	 * @return Return the hashmap of strings and characters.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	static Map<String, String> createDecodeMap() throws FileNotFoundException, IOException {
		final Map<String, String> map = new HashMap<String, String>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File("codes.txt")));
		
		/*
		 * Read in the codes.txt and make an array of Strings split on '='.  If 
		 * the first char in data[0] is a '~' then make sure data[0] is bigger
		 * than 1.  If it is then check the second char in data[0].  If it is
		 * a r, use '\r' as the value, if it is a n, use '\n' as the value, etc.
		 * Otherwise, fill of the map with the key/values found in the 
		 * text file.
		 */
		for (String line; (line = reader.readLine()) != null;) {
			if (line.charAt(0) != '=') {
				String[] data = line.split("=");
				if (data[0].charAt(0) == '~') {
					if (data[0].charAt(1) == 'r') {
						map.put(data[1], String.valueOf('\r'));
					} else if (data[0].charAt(1) == 'n') {
						map.put(data[1], String.valueOf('\n'));
					} else if (data[0].charAt(1) == 'x') {
						map.put(data[1], String.valueOf('='));
					} else if (data[0].charAt(1) == 'z') {
						map.put(data[1], String.valueOf('\ufeff'));
					}
				}  else {
					final String c = data[0];
					if (!c.equals(String.valueOf('~'))) {
						map.put(data[1], c);
					}				
				}
			}			
		}
		return map;
	}
	
	/**
	 * A method to decode an encoded text file.
	 * 
	 * @param rawFile The original raw text file, used for buffered writer.
	 * @param thePath The encoded text file.
	 * @throws IOException 
	 */
	static void decode(final String thePath) throws IOException {
		
		/*
		 * Read in the bytes of file passed to the method, which will be the compressed
		 * file.
		 */
		byte[] bytes = readBytes(thePath);
		
		//Create a BitSet from array of bytes.
		BitSet decode = BitSet.valueOf(bytes);
		
		//Create a StringBuilder to build strings of binary to check against the map.
		StringBuilder result = new StringBuilder();
		
		//Create a new map using the createDecodeMap method.
		Map<String, String> decodeMap = createDecodeMap();
		
		//Create a StringBuilder to hold the decoded text.
		StringBuilder finalResult = new StringBuilder();
		
		for (int i = 0; i < decode.length(); i++) {
			
			//Add each bit to result, one at a time.
			if (decode.get(i) == true) {
				result.append(1);
			} else {
				result.append(0);
			}
			
			/*
			 * After each bit is added, check to see if result exists in the decodeMap.
			 * If it does exist, add the associated char value to finalResult and 
			 * clear result.
			 */
			if (decodeMap.containsKey(result.toString())) {
				finalResult.append(decodeMap.get(result.toString()));
				result = new StringBuilder();
			}
		}
		
		//Write finalResult to a new file called 'decoded_text.txt".
		FileWriter fw = new FileWriter ("decoded_text.txt");		
		@SuppressWarnings("resource")
		BufferedWriter writer = new BufferedWriter(fw);
		writer.write(finalResult.toString());
		writer.flush();
	}
	
	/**
	 * A method to save the codes to a file called codes.txt.
	 * 
	 * @param theTree The CodingTree used to generate the code file.
	 * @throws IOException 
	 */
	static void generateCodeFile(final CodingTree theTree) throws IOException {
		
		FileWriter fw = new FileWriter ("codes.txt");		
		BufferedWriter writer = new BufferedWriter(fw);
		
		/*
		 * iterate through the hashmap of huffman codes in the CodingTree.  Create
		 * a StringBuilder that makes a string that has the char followed by '='
		 * followed by the binary value of that char and writes it to a line.
		 */
		for (Entry<String, String> entry : theTree.getCodes().entrySet()) {
			if (entry.getKey().equals(String.valueOf('\r'))) {
				writer.write("~r=" + entry.getValue());	
			} 
			else if (entry.getKey().equals(String.valueOf('\n'))) {
				writer.write("~n=" + entry.getValue());
			} 
			else if (entry.getKey().equals(String.valueOf('='))) {
				writer.write("~x=" + entry.getValue());
			} 
			else {
				writer.write(entry.toString());
			}
			writer.newLine();
		}		
	    writer.close();
	}
	
	
	/**
	 * Method to check if a char is alphanumeric or "'" or "-".  Found code here:
	 * http://stackoverflow.com/questions/11241690/regex-for-checking-if-a-string-is-strictly
	 * -alphanumeric
	 * 
	 * @param theChar the character to be checked.
	 * @return true if it is, false if it is not alphanumeric or "'" "-".
	 */
	private static boolean isAlphaNumeric(String s){
	    String pattern= "^[\\pL\\pN\\'-]+$";
	        if(s.matches(pattern)){
	            return true;
	        }
        return false;   
	}
	
	
	/**
	 * A method to create a byte array from the encoded text file.
	 *  
	 * @param thePath the text file to read from.
	 * @return A string that represents all text in a text file.
	 */
	static byte[] readBytes(final String thePath) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(thePath));
		return encoded;
	}
	
	
	/**
	 * A method used to create a string from the text in a text file. (code acquired from
	 * the following source: http://stackoverflow.com/questions/326390/how-to-create-a
	 * -java-string-from-the-contents-of-a-file.)
	 * 
	 * @param thePath The text document to read from.
	 * @param theEncoding The charset to use for the conversion to bytes.
	 * 
	 * @return A string that represents all text in a text file.
	 */
	static String readFile(final String thePath, final Charset theEncoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(thePath));
		return new String(encoded, theEncoding);
	}	
}


