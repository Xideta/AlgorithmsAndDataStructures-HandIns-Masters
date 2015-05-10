/**
 * Spanning USA assignment
 *@author Mindaugas Kniazevas
 *@author Gleb Cymliakov
 * Majority of the code was used from the book, Sedgewick and Wayne, section 4 and the book's website.
 */

import java.util.Iterator;

public class SpanningUSA{
	private static ST<String, Integer> st;		//symbol table for mapping string '(key)' to integer '(value)'
	private String[] keys;						//array for mapping integer 'value' to the string '(key)'.
	

	public static void main(String[] args){
		st = new ST<String, Integer>();
		
		String[] data = StdIn.readAllLines();				// read data 
		keys = new String[data.length];
		for (int i=0; i<data.length; i++) {
			if(!data[i].contains("--")||!data[i].contains("[")||!data[i].contains("]")){ // checks if string represents  a node.
				String newstr = data[i].substring(0, data[i].length() - 1); 	//USE WITH USA DATA remove whitespace from the end of the string
				//String newstr = data[i];										//USE WITH TINY DATA
				st.put(newstr, st.size());										//add string to the symbol table.	
				keys[st.size()] = newstr;			
			}
		}
		
		EdgeWeightedGraph G = new EdgeWeightedGraph(st.size());
		for (int i=0; i<data.length; i++) {
			if(data[i].contains("--")||data[i].contains("[")||data[i].contains("]")){ //check if string represents an edge.
				String[] conn = data[i].split("(--)|\\[|\\]");						// parse edge data by splitting the string.
 				int v = st.get(conn[0]);											//retreive integers representing the node
				int w = st.get(conn[1].substring(0, conn[1].length()-1)); 			//retreive integers representing the node USE WITH TINY DATA
				int weight = Integer.parseInt(conn[2]);	
				G.addEdge(new Edge(v, w, weight));				//add edge to the graph
			}
		}
		PrimMST mst = new PrimMST(G);			//Run PrimsMST to construct mst.

		edgeList(mst);							//Lists all edges.
		StdOut.println("MST: "+mst.weight());  	//Retreive the weight of MST.
	}
	/**
	 * Lists all names of the verteces that is a part of MST
	 *@param mst, Takes PrimMST data structure with existing mst
	 */
	private static void edgeList(PrimMST mst){
		Iterator<Edge> iter = mst.edges().iterator();
		while(iter.hasNext()){
			Edge e = iter.next();
			StdOut.println(keys[e.either()] +" => "+keys[e.other(e.either())]);
		}
	}
}