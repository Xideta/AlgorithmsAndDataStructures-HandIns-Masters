import java.util.*;
public class MarioDirectedPath{
	static class State{
		private int vx;
		private int vy;
		private int id;
		private HashMap<Integer, Boolean> visited;
		private List<State> nextStates = new ArrayList<State>();
		public State(int id, int vx, int vy, HashMap<Integer, Boolean> visited){
			this.id = id;
			this.vx = vx;
			this.vy = vy;
			this.visited = visited;
			//addToVisited(id, true);
		}

		public int getVX()			{	return vx;		}
		public int getVY()			{	return vy;		}
		public int getId()			{	return id;		}
		
		public HashMap<Integer, Boolean> getVisited()		{	return visited;				}
		public void setVisited()							{	this.visited = visited;		}
		
		public List<State> getNextStates()					{	return nextStates;				}
		public void setNextStates(List<State> nextStates)	{	this.nextStates = nextStates;	}
		
		public void addNextState(State n) 			{	nextStates.add(n);	}
		public void addToVisited(int i, boolean b)	{	visited.put(i, b);	}

	}

	static class Node{
		final private int x; 		// possition x.
		final private int y;		// possition y.
		final private char value;	//value of the node
		final private int num;		//number/id of the node.
		public Node(int x, int y, char value, int num){
			this.x=x;
			this.y=y;
			this.value = value;
			this.num = num;

		}
		public int getX()			{	return x;		}
		public int getY()			{	return y;		}
		public char getValue()		{	return value;	}
		public int getNum()			{	return num;		}
	}

	
	private final static int[] dx = {-1,0,1};
	private final static int[] dy = {-1,0,1};
	private static int[][] grid;
	public static HashMap<Node, Integer> nMap = new HashMap<Node, Integer>();

	private static Queue<Edge> qu = new Queue<Edge>();
	private static Node[] keys;
	private static Queue<State> stateQueue = new Queue<State>();
	private static int start;
	private static int finish;
	private static EdgeWeightedDigraph G;
	public static void main(String[] args){
		int c = StdIn.readInt();
		int r= StdIn.readInt();
		grid = new int[c][r];
		keys = new Node[c*r];
		G = new EdgeWeightedDigraph (c*r);
		String[] s = StdIn.readAllLines();		//read all lines in String array
		int count = 0;							//the id/num of the node.
		for (int i = 0; i<s.length-1; i++ ){
			char[] chars = s[i+1].toCharArray();	//split string into char array.
			for (int j = 0;j<chars.length; j++) { 	//updated
				keys[count] = new Node(i, j, chars[j], count);
				nMap.put(keys[count], count);
				grid[i][j] = count; 
				if(chars[j] =='S'){
					HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
					State startState = new State(count, 0, 0, new HashMap<Integer, Boolean>());
					startState.addToVisited(count, true);
					stateQueue.enqueue(startState);
					start = count;
				}
				if (chars[j] == 'F')
					finish = count;

				count++;
			}
		}
		//print(grid);
		idNextStates();

		searchPath(G, start, finish);
	}

	public static void searchPath(EdgeWeightedDigraph  g, int s, int f){
		StdOut.println("Edges in graph: "+g.E());
		DijkstraSP sp = new DijkstraSP(g, s);
		if(sp.hasPathTo(f)){
			int c = 0;		//count how many nodes visited. 
			Iterator<DirectedEdge> iter = sp.pathTo(endPoint.getNum()).iterator(); 
			while(iter.hasNext()){
				DirectedEdge e =iter.next(); 
				//StdOut.println("Jumped nodes " + keys[e.from()].getX()+ ","+keys[e.from()].getY());
				c++;
			}
		StdOut.println(c++);
			StdOut.println("Path from  "+s+" ==> "+f+": "+ c++);
		}else{
			StdOut.println("No path to node: "+f);
		}
	}

	public static void idNextStates(){
		while(!stateQueue.isEmpty()){
			State s = stateQueue.dequeue();
			idStates(s);
		}

		StdOut.println("All states have been identified");
	}

	public static void idStates(State s){
		
		//List<State> list = new ArrayList<State>();
		int start = s.getId();			//get id of a node
		Node n = keys[start];			//get the node
		int x = n.getX();				//get node's x in the grid
		int y = n.getY(); 				//get node's y in the grid 
		int stateVX = s.getVX();				//get state's velocity
		int stateVY = s.getVY();				//get state's velocity
		
		for (int i=0; i<dx.length; i++) {			//iterate through all posible velocities
			for (int j=0; j<dy.length; j++) {
				try{								//try to get next node, catch exception if out of bounds.
					int vx = stateVX+dx[i];
					int vy = stateVY+dy[j];
					int newNode = grid[x+vx][y+vy];
									
					 if( keys[newNode].getValue()!= 'O'){
					 	HashMap<Integer,Boolean> visited = new HashMap<Integer, Boolean>();
						visited.putAll(s.getVisited());
					 	if (visited.get(newNode)==null) {
							visited.put(newNode, true);
							State newState = new State(newNode, vx, vy, visited);
							s.addNextState(newState);
							stateQueue.enqueue(newState);
							//StdOut.println("Edge:"+ start+" -> "+newNode +" | Velocity:"+vx+" - "+ vy);
							G.addEdge(new DirectedEdge(start, newNode, weight(vx, vy)));
						}
					}
				}catch(ArrayIndexOutOfBoundsException e){
					//Node does not exist on the grid.
				}
			}
		}
		
	}

	private static double weight(double i, double j){
		return Math.abs(Math.sqrt((Math.pow(i,2))+(Math.pow(j,2))));
	}

	//for printing the grid.
	private static void print(int[][] nodes){
		for (int i = 0; i < nodes.length; i++) {
			String str = "";
			for (int j = 0; j < nodes[0].length; j++) {
				str += "" + keys[nodes[i][j]].getValue()+ "("+nodes[i][j]+")";
			}
			StdOut.println(str);
		}
	}
	



	
}