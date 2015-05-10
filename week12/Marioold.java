import java.util.*;

public class Mario{
	static class Node{
		private int x;
		private int y;
		private char value;
		public Node(int x, int y, char value){
			this.x=x;
			this.y=y;
			this.value = value;
		}
		public int getNextX(int vx)	{	return x+vx;	}
		public int getNextY(int vy)	{	return y+vy;	}
		public int getX()			{	return x;		}
		public int getY()			{	return y;		}
		public char getValue()		{	return value;	}
	}
	
	private static Queue<DirectedEdge> qe;
	private static HashMap<Node, Boolean> visited;
	private static HashMap<Node, Integer> nodeMap;
	private static Node startPoint;
	private static Node endPoint;
	private static Node[] keys;
	//private static Node[][] nodes;
	
	public static void main(String []args){
		visited = new HashMap();
		nodeMap = new HashMap();
 		qe = new Queue<DirectedEdge>();

		int c = StdIn.readInt();
		int r= StdIn.readInt();
		
		
		Node [][] nodes = new Node[c][r];
		keys= new Node[c*r];		
		
		String[] s = StdIn.readAllLines();
		int count = 0;
		for (int i = 1; i<s.length-1;i++ ){
			if(s[i]==null)
				continue;
			char[] chars = s[i].toCharArray();
			for (int j = 0;j<chars.length; j++) { //updated
				nodes[i][j] = new Node(i, j, chars[j]);
				nodeMap.put(nodes[i][j], count);
				visited.put(nodes[i][j], false);
				if(chars[j]=='S')
					startPoint = nodes[i][j];
				if(chars[j] == 'F')
					endPoint = nodes[i][j];
				keys[count] = nodes[i][j]; 
				count++;
			}
		}
		
		findPath(graph(nodes, startPoint), nodeMap.get(startPoint));
	}

	private static void findPath(EdgeWeightedDigraph G, int s){
		DijkstraSP sp = new DijkstraSP(G, s);
		StdOut.println("Path: "+sp.distTo(nodeMap.get(endPoint)));
		int c = 1;
		Iterator<DirectedEdge> iter = sp.pathTo(nodeMap.get(endPoint)).iterator(); 
		while(iter.hasNext()){
			iter.next();
			c++;
		}
		StdOut.println(c++);
				
	} 
		
	private static EdgeWeightedDigraph graph(Node[][] nodes, Node s){
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(nodeMap.size());
		
		//Inspired by breadth first search algorithm.
		addEdgesToQueue(nodes, s);
		visited.put(s, true);
		while(!qe.isEmpty()){
			DirectedEdge e = qe.dequeue();
			if (!isVisited(keys[e.to()])) {
				G.addEdge(e);
				visited.put(keys[e.to()], true);
				addEdgesToQueue(nodes, keys[e.to()]);
			}
		}
						
		return G;
	}

	private static void addEdgesToQueue(Node[][] nodes, Node s){
		Queue<Node> queue = getNeighbourQueue(nodes, s);

		while(!queue.isEmpty()){
			DirectedEdge edge;
			Node n = queue.dequeue();
			if(!isVisited(n)){
				if(n.getValue()=='O'){
					int vx = s.getX()-n.getX();
					int vy = s.getY()-n.getY();
					n = jump(nodes, n, 3, vx, vy);
					if(n!=null){
						edge = new DirectedEdge(nodeMap(s), nodeMap(n), weight(s.getX()-n.getX(), s.getY()-n.getY()));
					}
				}else{
					edge = new DirectedEdge(nodeMap(s), nodeMap(n), weight(s.getX()-n.getX(), s.getY()-n.getY()));
				}
				if(edge!=null)
					qe = enqueue(edge);
			}
		}






		//All enighbouring nodes 
		//horizontal (1,0), (-1, 0) 
		if(s.getNextX(1)<nodes.length && 
			!isVisited(nodes[s.getNextX(1)][s.getNextY(0)]) && 
			nodes[s.getNextX(1)][s.getNextY(0)].getValue()!='O' )
				qe.enqueue(new DirectedEdge(nodeMap.get(s), nodeMap.get(nodes[s.getNextX(1)][s.getNextY(0)]), weight(1,0)));
		if(s.getNextX(-1)>0 && 
			!isVisited(nodes[s.getNextX(-1)][s.getNextY(0)]) && 
			nodes[s.getNextX(-1)][s.getNextY(0)].getValue()!='O')
				qe.enqueue(new DirectedEdge(nodeMap.get(s),nodeMap.get( nodes[s.getNextX(-1)][s.getNextY(0)]), weight(-1,0)));
		
		
		//vertical (0,1), (0,-1)
		if(s.getNextY(1)<nodes[0].length && 
			!isVisited(nodes[s.getNextX(0)][s.getNextY(1)]) && 
			nodes[s.getNextX(0)][s.getNextY(1)].getValue()!='O')
				qe.enqueue(new DirectedEdge(nodeMap.get(s),nodeMap.get(nodes[s.getNextX(0)][s.getNextY(1)]), weight(0,1)));
		if(s.getNextY(-1)>0 && 
			!isVisited(nodes[s.getNextX(0)][s.getNextY(-1)]) && 
			nodes[s.getNextX(0)][s.getNextY(-1)].getValue()!='O')
				qe.enqueue(new DirectedEdge(nodeMap.get(s),nodeMap.get(nodes[s.getNextX(0)][s.getNextY(-1)]),  weight(0,-1)));
		
		
		//diagonal (1,1), (1 -1), (-1,1), (-1,-1)
		if(s.getNextX(1)<nodes.length && s.getNextY(1)]<nodes[0].length && 
			!isVisited(nodes[s.getNextX(1)][s.getNextY(1)]) &&  
			nodes[s.getNextX(1)][s.getNextY(1)].getValue()!='O')
				qe.enqueue(new DirectedEdge(nodeMap.get(s), nodeMap.get(nodes[s.getNextX(1)][s.getNextY(1)]), weight(1,1)));
		
		if(s.getNextX(1)]<nodes.length && s.getNextY(-1)>0 && 
			!isVisited(nodes[s.getNextX(1)][s.getNextY(-1)])&& 
			nodes[s.getNextX(1)][s.getNextY(-1)].getValue()!='O')
				qe.enqueue(new DirectedEdge(nodeMap.get(s),nodeMap.get(nodes[s.getNextX(1)][s.getNextY(-1)]),  weight(1,-1)));
		
		if(s.getNextX(-1)>0 && s.getNextY(1)<nodes[0].length && 
			!isVisited(nodes[s.getNextX(-1)][s.getNextY(1)]) && 
			nodes[s.getNextX(-1)][s.getNextY(1)].getValue()!='O')
				qe.enqueue(new DirectedEdge(nodeMap.get(s),nodeMap.get(nodes[s.getNextX(-1)][s.getNextY(1)]), weight(-1, 1)));
		
		if(s.getNextX(-1)>0 && s.getNextY(-1)>0 && 
			!isVisited(nodes[s.getNextX(-1)][s.getNextY(-1)]) && 
			nodes[s.getNextX(-1)][s.getNextY(-1)].getValue()!='O') 
				qe.enqueue(new DirectedEdge(nodeMap.get(s),nodeMap.get(nodes[s.getNextX(-1)][s.getNextY(-1)]),  weight(-1,-1)));
	}

	
	private static Queue<Node> getNeighbourQueue(Node[][] nodes, Node s){
		Queue<Node> queue = new Queue<Node>();

		//horizontally neighbouring nodes (1,0), (-1, 0) 
		if(s.getNextX(1)<nodes.length)
			queue.enqueue(nodes[s.getNextX(1)][s.getNextY(0)]);
		if(s.getNextX(-1)>0 )
			queue.enqueue(nodes[s.getNextX(-1)][s.getNextY(0)]);
				
		//vertically neighbouring nodes (0,1), (0,-1)
		if(s.getNextY(1)<nodes[0].length)
			queue.enqueue(nodes[s.getNextX(0)][s.getNextY(1)]);
		if(s.getNextY(-1)>0)
			queue.enqueue(nodes[s.getNextX(0)][s.getNextY(-1)]);
				
		//diagonally neighbouring nodes (1,1), (1,-1), (-1,1), (-1,-1)
		if(s.getNextX(1)<nodes.length && s.getNextY(1)]<nodes[0].length)
			queue.enqueue(nodes[s.getNextX(1)][s.getNextY(1)]);
		
		if(s.getNextX(1)]<nodes.length && s.getNextY(-1)>0)
			queue.enqueue(nodes[s.getNextX(1)][s.getNextY(-1)]);
		
		if(s.getNextX(-1)>0 && s.getNextY(1)<nodes[0].length)
			queue.enqueue(nodes[s.getNextX(-1)][s.getNextY(1)]);
		
		if(s.getNextX(-1)>0 && s.getNextY(-1)>0) 
			queue.enqueue(nodes[s.getNextX(-1)][s.getNextY(-1)]);
		
		return queue;
	}

	private static Node jump(Node[][] nodes, Node n, int l, int vx, int vy){
		if(l==0){
			return;
		}
		l--;
		Node m = nodes[n.getNextX(vx)][n.getNextY(vy)];
		if(n.getValue()== 'O'){
			m = jump(nodes, m, l, vx, vy);
		}

		return m;
	}

	
	private static boolean isVisited(Node n){
		return visited.get(n);
	}

	private static double weight(double i, double j){
		return Math.abs(Math.sqrt((Math.pow(i,2))+(Math.pow(j,2))));
	}

	private static void print(Node[][] nodes){
		for (int i = 0; i < nodes.length; i++) {
			String str = "";
			for (int j = 0; j < nodes[0].length; j++) {
				str += "" + nodes[i][j].getValue();
			}
			StdOut.println(str);
		}
	}
	
}