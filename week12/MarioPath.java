import java.util.*;
public class MarioPath{
	
	static class State{
		final private int stateId;
		private int vx;
		private int vy;
		private int id;
		private HashMap<Integer, Boolean> visited;
		private List<State> nextStates = new ArrayList<State>();
		public State(int stateId, int id, int vx, int vy, HashMap<Integer, Boolean> visited){
			this.id = id;
			this.vx = vx;
			this.vy = vy;
			this.visited = visited;
			this.stateId = stateId;
			//addToVisited(id, true);
		}

		public int getVX()			{	return vx;		}
		public int getVY()			{	return vy;		}
		public int getId()			{	return id;		}
		public int getStateId()		{	return stateId;	}

		public HashMap<Integer, Boolean> getVisited()		{	return visited;				}
		public void setVisited()							{	this.visited = visited;		}
		
		//public List<State> getNextStates()					{	return nextStates;				}
	//	public void setNextStates(List<State> nextStates)	{	this.nextStates = nextStates;	}
		
	//	public void addNextState(State n) 			{	nextStates.add(n);	}
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

	static class StateEdge{
		private int v;
		private int w;
		public StateEdge( int v, int w){
			this.v = v;
			this.w = w;
		}
		public int getV()	{	return v;	}
		public int getW()	{	return w;	}
	}
	private final static int[] dx = {-1,0,1};
	private final static int[] dy = {-1,0,1};
	private static int[][] grid;
	//public static HashMap<Node, Integer> nMap = new HashMap<Node, Integer>();
	private static Queue<StateEdge> qu = new Queue<StateEdge>();
	private static Node[] keys;
	private static Queue<State> stateQueue = new Queue<State>();
	private static int start;
	private static int id = 0;
	private static int finish;
	private static Graph G;
	private static int startStateId = 0;
	private static int finishStateId = 0;
	private static int N;
	public static void main(String[] args){
		int c = StdIn.readInt();
		int r = StdIn.readInt();
		N = c*r;
		grid = new int[c][r];
		keys = new Node[N];
		StdOut.println("Grid size: "+N+ ", cloumn: "+c+", row: "+r);
		//StdOut.println("Graph size: "+(int)Math.round(Math.pow(c*r*8,N)));
		//G = new Graph((int)Math.round(Math.pow(100, 8)));
		String[] s = StdIn.readAllLines();		//read all lines in String array
		int count = 0;							//the id/num of the node.
		for (int i = 0; i<s.length-1; i++ ){
			char[] chars = s[i+1].toCharArray();	//split string into char array.
			for (int j = 0;j<chars.length; j++) { 	//updated
				keys[count] = new Node(i, j, chars[j], count);
				//nMap.put(keys[count], count);
				grid[i][j] = count; 
				if(chars[j] =='S'){
					HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
					State startState = new State(id, count, 0, 0, new HashMap<Integer, Boolean>());
					startState.addToVisited(count, true);
					stateQueue.enqueue(startState);
					startStateId = id;
					StdOut.println("S state: "+id);
					id++;
					start = count;
				}
				if (chars[j] == 'F'){
					StdOut.println("F state: "+id);
					finishStateId = id;
					id++;
				}
				count++;
			}
		}
		//print(grid);
		idNextStates();
		G = addEdgesToGraph();
		searchPath(G, startStateId, finishStateId);
	}

	public static Graph addEdgesToGraph(){
		Graph G = new Graph(id);
		while(!qu.isEmpty()){
			StateEdge e = qu.dequeue();
			G.addEdge(e.getV(), e.getW());
		}
		return G;
	}

	public static void searchPath(Graph g, int s, int f){
		StdOut.println("Edges in graph: "+g.E());
		BreadthFirstPaths bfs = new BreadthFirstPaths(g, s);
		if(bfs.hasPathTo(f)){
			StdOut.println("Path from  "+s+" ==> "+f+": "+ (bfs.distTo(f)+1));
		}else{
			StdOut.println("No path to node: "+f);
		}
	}

	public static void idNextStates(){
		while(!stateQueue.isEmpty()){
			State s = stateQueue.dequeue();
			idStates(s);
			
		}
		StdOut.println("Total state count: "+id);
		StdOut.println("All states have been identified");
		//StdOut.println("Graph size: "+(int)Math.round(Math.pow((8),N)));
	}
	

	public static void idStates(State s){
		
		//List<State> list = new ArrayList<State>();
		int stateId = s.getStateId();
		Node n = keys[s.getId()];			//get the node
		int x = n.getX();				//get node's x in the grid
		int y = n.getY(); 				//get node's y in the grid 
		int stateVX = s.getVX();				//get state's velocity
		int stateVY = s.getVY();				//get state's velocity
		State newState = null;				
		
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
							
							if(keys[newNode].getValue()=='F'){
								newState = new State(finishStateId, newNode, vx, vy, visited);
								//G.addEdge(stateId, finishStateId);
								qu.enqueue(new StateEdge(stateId, finishStateId));
							}else{
								newState = new State(id, newNode, vx, vy, visited);
								qu.enqueue(new StateEdge(stateId, id));
								//G.addEdge(stateId, id);
								id++;
							}
							stateQueue.enqueue(newState);
							
							//StdOut.println("Edge:"+ start+" -> "+newNode +" | Velocity:"+vx+" - "+ vy);
							//StdOut.println("Edge:"+ stateId+" -> "+id +" | Velocity:"+vx+" - "+ vy);
						}
						newState = null;
						visited = null;
					}
				}catch(ArrayIndexOutOfBoundsException e){
					//Node does not exist on the grid.
				}
			}
		}
		
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