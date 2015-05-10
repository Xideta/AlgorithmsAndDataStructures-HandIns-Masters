/**
 *Mario assignment.
 *@author Mindaugas Kniazevas 
 *Known problems: 
 *	-Is unable to find a path in large grids, with 
 *	 long straight paths due to too high velocity.
 *	 Unable to make turns. 
 */
import java.util.*;
public class Mario{
	/**
	 *An instance that is being used to represend a position in a grid.	 
	 */
	static class Node implements Serializable{
		private  int x; // possition x.
		private int y;	// possition y.
		private int vx=0;	//velocity x.
		private int vy=0;	//velocity y.
		private char value;	//value of the node
		private int num;	//number/id of the node.
		
		public Node(int x, int y, char value, int num){
			this.x=x;
			this.y=y;
			this.value = value;
			this.num = num;

		}
		
		public void setVX(int vx)	{	this.vx=vx;		}
		public void setVY(int vy)	{	this.vy=vy;		}
		public int getVX()			{	return vx;		}
		public int getVY()			{	return vy;		}
		public int getX()			{	return x;		}
		public int getY()			{	return y;		}
		public char getValue()		{	return value;	}
		public int getNum()			{	return num;		}
	}

	private static Queue<DirectedEdge> qe; // a queue of all edges
	private static boolean[] visited;		//list of visited nodes
	private static Node startPoint;			//starting node containing value 'S'.
	private static Node endPoint;			//ending node containing value 'F'.
	private static Node[] keys;				//map ids to the node.
	private static int vx;					
	private static int vy;	
	
	public static void main(String []args){
 		qe = new Queue<DirectedEdge>();
		int c = StdIn.readInt();
		int r= StdIn.readInt();
		Node [][] nodes = new Node[c][r]; //represent input grid in a 2d array.
		keys= new Node[c*r];			
		visited = new boolean[c*r];

		String[] s = StdIn.readAllLines();		//read all lines in String array
		int count = 0;
		for (int i = 0; i<s.length-1; i++ ){
			char[] chars = s[i+1].toCharArray();	//split string into char array.
			for (int j = 0;j<chars.length; j++) { 	//updated
				nodes[i][j] = new Node(i, j, chars[j], count); 
				visited[count] = false;
				keys[count] = nodes[i][j]; 
				if(chars[j]=='S')
					startPoint = nodes[i][j];
				if(chars[j]=='F')
					endPoint = nodes[i][j];
				count++;
			}
		}
		//print(nodes);
		findPath(graph(nodes, startPoint), startPoint.getNum(), endPoint.getNum()); 
	}

	/**
	 * Finds path from given graph, node s to node n.
	 */
	private static void findPath(EdgeWeightedDigraph G, int s, int n){
		DijkstraSP sp = new DijkstraSP(G, s);
		StdOut.println("Path: "+sp.distTo(endPoint.getNum()));
		int c = 0;		//count how many nodes visited. 
		Iterator<DirectedEdge> iter = sp.pathTo(endPoint.getNum()).iterator(); 
		while(iter.hasNext()){
			DirectedEdge e =iter.next(); 
			//StdOut.println("Jumped nodes " + keys[e.from()].getX()+ ","+keys[e.from()].getY());
			c++;
		}
		StdOut.println(c++);
	} 
	
	/**
	 * Constructs a graph from computed edges. 
	 * Initial input is the grid  and the starting node.
	 */
	private static EdgeWeightedDigraph graph(Node[][] nodes, Node s){
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(visited.length);
		
		visited[s.getNum()] =  true;  //assign the first node as visited 
		addEdgesToQueue(nodes, s, s.getVX(), s.getVY());	//compute all edges
		while(!qe.isEmpty()){
			DirectedEdge e = qe.dequeue();	//get edge from the queue.
			G.addEdge(e);		//add edge to the graph.
			visited[e.to()] = true;	//mark node as visited using node id.
			vx = keys[e.to()].getVX();	//retreive velocity x from node
			vy = keys[e.to()].getVY();	//retreive velocity y from node
			addEdgesToQueue(nodes, keys[e.to()], vx, vy);
		}
		return G;
	}

	/**
	 * Computes edges of a given node in a grid with specified velocity.
	 * Store edges in the queue.
	 */
	private static void addEdgesToQueue(Node[][] nodes, Node s, int vx, int vy){
		Queue<Node> queue = getNodesQueue(nodes, s, vx, vy);	//rereive a queue of next nodes
		
		while(!queue.isEmpty()){
			DirectedEdge edge = null;
			Node n = queue.dequeue();	//retreive node from the queue.
			if(!isVisited(n.getNum())){
				qe.enqueue(new DirectedEdge(s.getNum(), n.getNum(), weight(Math.abs(n.getVX()), Math.abs(n.getVY()))));	//add edge from a source node to the neighbouring node to the queue.
			}
		}
	}

	/**
	 *	Get queue of neighbouring nodes based on the the velocity of the source.
	 */
	private static Queue<Node> getNodesQueue(Node[][] nodes, Node s, int vx, int vy){
		Queue<Node> queue = new Queue<Node>();
		int hvxr = vx+1;	// increase x velocity
		int hvxl = vx-1;	// decrease x velocity
		int vvyu = vy+1;	// increase y velocity
		int vvyd = vy-1;	// decrease y velocity

		//horizontally neighbouring node (1,0)
		try{
			Node n = nodes[s.getX()+hvxr][s.getY()];
			if(n.getValue()!='O'){	//check if node value is 'O'
				n.setVX(hvxr); 		//set new velocity
				queue.enqueue(n);	//add node to the queue
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 
		//horizontally neighbouring node (-1, 0)
		try{
			Node n = nodes[s.getX()+hvxl][s.getY()];
			if(n.getValue()!='O'){
				n.setVX(hvxl);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 
				
		//vertically neighbouring node (0,1)
		try{
			Node n = nodes[s.getX()][s.getY()+vvyu];
			if(n.getValue()!='O'){
				n.setVY(vvyu);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 

		//vertically neighbouring node (0,-1)
		try{
			Node n = nodes[s.getX()][s.getY()+vvyd];
			if(n.getValue()!='O'){
				n.setVY(vvyd);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 
				
		//diagonally neighbouring node (1,1)
		try{
			Node n = nodes[s.getX()+hvxr][s.getY()+vvyu];
			if(n.getValue()!='O'){
				n.setVY(vvyu);
				n.setVX(hvxr);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		}

		//diagonally neighbouring node (1,-1)
		try{
			Node n = nodes[s.getX()+hvxr][s.getY()+vvyd];
			if(n.getValue()!='O'){
				n.setVY(vvyd);
				n.setVX(hvxr);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 
		
		//diagonally neighbouring node(-1,1)
		try{
			Node n = nodes[s.getX()+hvxl][s.getY()+vvyu];
			if(n.getValue()!='O'){
				n.setVY(vvyu);
				n.setVX(hvxl);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		}
		//diagonally neighbouring node(-1,-1) 
		try{
			Node n = nodes[s.getX()+hvxl][s.getY()+vvyd];
			if(n.getValue()!='O'){
				n.setVY(vvyd);
				n.setVX(hvxl);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 
		return queue;
	}

	/**
	 * check if node have been visited based on its id.
	 */
	private static boolean isVisited(int n){
		return visited[n];
	}

	/**
	 * Get the weight of the edge by using Pythagoras theorem
	 */
	private static double weight(double i, double j){
		return Math.abs(Math.sqrt((Math.pow(i,2))+(Math.pow(j,2))));
	}

	//for printing the grid.
	private static void print(Node[][] nodes){
		for (int i = 0; i < nodes.length; i++) {
			String str = "";
			for (int j = 0; j < nodes[0].length; j++) {
				str += "" + nodes[i][j].getValue();
			}
			StdOut.println(str);
		}
	}
	
//Build state(not NODE) tree with velocities vx(0, 1, -1) and vy(0, 1, -1).
//Use breadthe first search to build the tree.
//every starting state has an edge(not DIRECTED EDGE) to the new state. 
//When the graph is build use BFS to find the path.
