/**
 * Word Ladder assignment
 * @author Mindaugas Kniazevas
  * Based on Sedgewick and Wayne, section 4.
 */

import java.io.*;
import java.util.*;
public class WordLadders {
	private static ST<String, Integer> si;	 		// maps String -> index
	private static ST<String, Bag<Integer>> sb;		//maps all possible 4letter combinations to bags of nodes
	private static Queue<String> combinations;		//queue of 4letter combinations

	//arg[0] should be a path to a world list file, 
	//arg[1] should a path to a file of starting and ending vertices on which a path should be found.
	public static void main(String[] args) {
		Digraph D;
		String inputWordsPath = args[0];		//read path as argument
		String inputVerticesPath = args[1];		//read path as argument
		
		si = new ST<String, Integer>();
		sb = new ST<String, Bag<Integer>>();
		combinations = new Queue<String>();

		In wordsStream = new In(new File(inputWordsPath));			//retreive input stream from file
		In verticesStream = new In(new File(inputVerticesPath));	//retreive input stream from file
		
		String[] words = wordsStream.readAllStrings();			//read strea to array
		String[] vertices = verticesStream.readAllStrings(); 	//read vertices to array 
		String[] sources = new String[vertices.length/2];		//split vertices array to sources
		String[] ends = new String[vertices.length/2];			//split vertices array to ends
		
		
		//preprocess word list 
		for (int i=0; i<words.length; i++){
			String word = words[i];
			if(!si.contains(word))
				si.put(word, i);				//put word in symbol table and map to an integer
			stringCombos(word);					//identify all possible 4 letter combos of a word
			while(!combinations.isEmpty()){
				String subWord = combinations.dequeue();	//retreive combo from a queue
				if(!sb.contains(subWord)){					//if a 4 letter string does not exist in a sybmob table
					Bag<Integer> b = new Bag<Integer>();	//create a Bag.
					b.add(i);								//add node to the bag
					sb.put(subWord, b);						//insert 4 letter combo into symbol table and map to the corresponding bag 
				}else{				
					Bag<Integer> b = sb.get(subWord);		//retreive existing bag from ST
					b.add(i);								//add node to the bag
					sb.put(subWord, b);						//but bag back into ST
				}
			}
		}

		
		//separate sources from ends.
		int index = 0;
		for (int i = 1; i<vertices.length; i+=2) {
			sources[index] = vertices[i-1];			
			ends[index] = vertices[i];
			index++;
		}

		D = createGraph(words);				//create directed graph
		findPaths(D, sources, ends);		//find path from source to end.
	}
	
	/**
	 * A method that creates a Directed graph
	 *@param words, List of words.
	 *@return Directed graph.
	 */
	private static Digraph createGraph(String[] words){
		Digraph D = new Digraph(words.length);
		for (int i = 0; i<words.length;	i++) {
			Bag<Integer> b = null;
			String word = words[i];
			String subWord = word.substring(1);	//retreive a 4 letter string from a word.
			int v = si.get(word);				//retreive an integer that represents the word.

			if(sb.contains(subWord)){			//if 4 letter combo exist in the ST.
				b = sb.get(subWord);			//retreive a bag from ST.
				Iterator iter = b.iterator();	//create iterator of a bag.
				while(iter.hasNext()){			//while bag has nodes
				int w = (int)iter.next();		//get node
					if(v!=w){					//if current node is not the same as retreived node
						D.addEdge(v, w);		//add edge to the graph.
					}
				}
			}
		}
		return D;
	}
	
	/**
	 * Finds path in a Directed path using Breadth First Search
	 *@param D, Directed graph
	 *@param sources, list of verteces where the path should start
	 *@param ends, list of vertices where the path should end
	 */
	private static void findPaths(Digraph D, String[] sources, String[] ends){
		//do breadth first search 
		for (int i=0; i<sources.length; i++) {	//for every source node
			int s = si.get(sources[i]);			//get source node id from ST
			int e = si.get(ends[i]);			//get end node id from ST
			BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(D, s); //do BFS search
			
			if(bfs.hasPathTo(e)){				//is path exists
				StdOut.println(bfs.distTo(e));	//print distance
			}else{
				StdOut.println(-1);				//print -1
			}
		}
	}
	

	/**
	 *Initializes the procedure of building all possible 4 letter word combos
	 *@param s, a word
	 */
	private static void stringCombos(String s){
		int l = s.length()-1;						//length of a substring, which is 4.
		boolean[] used = new boolean[l+1];			//list of used letters.
		StringBuffer combo = new StringBuffer();	//buffer for building strings.
		char[] chars = s.toCharArray();				//split given word in a list of characters
		combine(l, used, combo, chars, 0); 			//run combine method.
	} 

	/**
	 *Method that bulds all possible word combinations
	 *@param l, lenght of a building string
	 *@param used, list of used letters
	 *@param combo, object on which the string will be build.
	 *@param chars, list of available characters
	 *@param comboL, integer for keeping track of current combo length.
	 */
	private static void combine(int l, boolean[] used, StringBuffer combo, char[] chars, int comboL){
		if(comboL == l){								//if a lenght of a built string is equal to desired string length
			combinations.enqueue(combo.toString());		//put new string in a queue
			return;										
		}
		for(int i=0; i<l+1; i++){						//for each character in a list
			if(used[i])									//check if character have been used
				continue;

			combo.append(chars[i]);						//append character to the new string
			used[i] = true;								//mark character as used
			combine(l, used, combo, chars, comboL+1);	//run combine method for next letter
			used[i] = false;							//after called combine method is finished unmark used character
			combo.setLength(combo.length()-1);			//reduce the length of new string.
		}
	}
}
