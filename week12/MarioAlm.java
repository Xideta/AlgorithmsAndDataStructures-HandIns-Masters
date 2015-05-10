import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Mario{
	
	static class Node implements Serializable{
		private  int x;
		private int y;
		private int vx=0;
		private int vy=0;
		private char value;
		private int num;
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

		public Node deepClone() {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(this);

				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				return (Node) ois.readObject();
			} catch (IOException e) {
				return null;
			} catch (ClassNotFoundException e) {
				return null;
			}
		}

	}
	
	


	private static Queue<DirectedEdge> qe;
	private static Queue<Node> no;
	private static boolean[] visited;
	//private static HashMap<Node, Integer> nodeMap;
	private static Node startPoint;
	private static Node endPoint;
	private static Node[] keys;
	private static int vx;
	private static int vy;
	
	public static void main(String []args){
		
		//nodeMap = new HashMap();
 		qe = new Queue<DirectedEdge>();
 		no = new Queue<Node>();

		int c = StdIn.readInt();
		int r= StdIn.readInt();
		
		
		Node [][] nodes = new Node[c][r];
		keys= new Node[c*r];		
		visited = new boolean[c*r];
		String[] s = StdIn.readAllLines();
		int count = 0;
		for (int i = 0; i<s.length-1; i++ ){
			char[] chars = s[i+1].toCharArray();
			for (int j = 0;j<chars.length; j++) { //updated
				//StdOut.println("i: "+i+" j: "+j);
				nodes[i][j] = new Node(i, j, chars[j], count);
				visited[count] = false;
				keys[count] = nodes[i][j]; 
				if(chars[j]=='S')
					//StdOut.println("Start i: "+i+" j: "+j);
					startPoint = nodes[i][j];
				if(chars[j] == 'F')
					endPoint = nodes[i][j];
				count++;
			}
		}
		print(nodes);
		findPath(graph(nodes, startPoint), startPoint.getNum(), endPoint.getNum());
	}

	


	private static void findPath(EdgeWeightedDigraph G, int s, int n){
		DijkstraSP sp = new DijkstraSP(G, s);
		StdOut.println("Path: "+sp.distTo(endPoint.getNum()));
		int c = 1;
		Iterator<DirectedEdge> iter = sp.pathTo(endPoint.getNum()).iterator(); 
		while(iter.hasNext()){
			DirectedEdge e =iter.next(); 
			StdOut.println("Jumped nodes " + keys[e.from()].getX()+ ","+keys[e.from()].getY());
			c++;
		}
		StdOut.println(c++);
				
	} 
		
	private static EdgeWeightedDigraph graph(Node[][] nodes, Node s){
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(visited.length);
		
		int vx = s.getVX();
		int vy = s.getVY();
		//Inspired by breadth first search algorithm.
		//StdOut.println("Velocity before adding to the graph(Start) "+s.getVX()+" "+ s.getVY());
		visited[s.getNum()] =  true;
		addEdgesToQueue(nodes, s, vx, vy);
		while(!qe.isEmpty()){
			DirectedEdge e = qe.dequeue();
			//Node n = no.dequeue();
			if (!isVisited(e.to())) {
				//StdOut.println("Velocity before adding to the graph "+keys[e.to()].getVX()+" "+ keys[e.to()].getVY());
				G.addEdge(e);
				visited[e.to()] = true;
				vx = keys[e.to()].getVX();
				vy = keys[e.to()].getVY();
				//n.setVX(0);
				//n.setVY(0);
				addEdgesToQueue(nodes, keys[e.to()], vx, vy);
			}
		}
						
		return G;
	}

	private static void addEdgesToQueue(Node[][] nodes, Node s, int vx, int vy){
		//StdOut.println("Velocity before adding to the edge (Start)"+s.getVX()+" "+ s.getVY());
		Queue<Node> queue = getNodesQueue(nodes, s, vx, vy);
		
		while(!queue.isEmpty()){
			DirectedEdge edge = null;
			Node n = queue.dequeue();
			if(!isVisited(n.getNum())){
				StdOut.println("Node in queue "+n.getX()+" "+ n.getY());
				//Node m = n.deepClone();
				//no.enqueue(m);
				//n.setVX(0);
				//n.setVY(0);
				//StdOut.println("N pos: "+n.getX()+" "+ n.getY());
				qe.enqueue(new DirectedEdge(s.getNum(), n.getNum(), weight(s.getX()-n.getX(), s.getY()-n.getY())));
			}
		}
	}

	
	private static Queue<Node> getNodesQueue(Node[][] nodes, Node s, int vx, int vy){
		Queue<Node> queue = new Queue<Node>();
		//StdOut.println("Velocity before node jump(not 0): "+vx+" "+vy);
		
		int hvxr = vx+1;
		int hvxl = vx-1;
		int vvyu = vy+1;
		int vvyd = vy-1;
		
		
		//horizontally neighbouring nodes (1,0), (-1, 0)
		try{
			Node n = nodes[s.getX()+hvxr][s.getY()];
			if(n.getValue()!='O'){
				n.setVX(hvxr);
				queue.enqueue(n);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			//array out of bounds
			//do nothing
		} 
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
				
		//vertically neighbouring nodes (0,1), (0,-1)
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
				
		//diagonally neighbouring nodes (1,1), (1,-1), (-1,1), (-1,-1)
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

	private static boolean isVisited(int n){
		return visited[n];
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