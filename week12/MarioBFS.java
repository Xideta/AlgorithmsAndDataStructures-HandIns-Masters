import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MarioBFS{
	
	static class Node{
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
	}

	private static Queue<DirectedEdge> qe;
	private static boolean[] visited;
	private static Node startPoint;
	private static Node endPoint;
	private static Node[] keys;
	private static Digraph G;

	public static void main(String []args){
 		qe = new Queue<DirectedEdge>();
		int c = StdIn.readInt();
		int r= StdIn.readInt();
		Node [][] nodes = new Node[c][r];
		keys= new Node[c*r];		
		visited = new boolean[c*r];
		G = new Digraph(r*c);

		String[] s = StdIn.readAllLines();
		int count = 0;
		for (int i = 0; i<s.length-1; i++ ){
			char[] chars = s[i+1].toCharArray();
			for (int j = 0;j<chars.length; j++) { //updated
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
		graph(nodes, startPoint);
		findPath(G, startPoint.getNum(), endPoint.getNum());
	}

	private static void findPath(Digraph G, int s, int n){
		BreadthFirstDirectedPaths sp = new BreadthFirstDirectedPaths(G, s);
		StdOut.println("Path: "+sp.distTo(endPoint.getNum()));
		int c = 0;
		Iterator<Integer> iter = sp.pathTo(endPoint.getNum()).iterator(); 
		while(iter.hasNext()){
			int e =iter.next(); 
			StdOut.println("Jumped nodes " + keys[e].getX()+ ","+keys[e].getY());
			c++;
		}
		StdOut.println(c++);
	} 
		
	private static void graph(Node[][] nodes, Node s){
		int vx;
		int vy;		
		visited[s.getNum()] =  true;
		addEdgesToQueue(nodes, s, s.getVX(), s.getVY());
		while(!qe.isEmpty()){
			DirectedEdge e = qe.dequeue();
			//G.addEdge(e);
			visited[e.to()] = true;
			vx = keys[e.to()].getVX();
			vy = keys[e.to()].getVY();
			addEdgesToQueue(nodes, keys[e.to()], vx, vy);
		}
	}

	private static void addEdgesToQueue(Node[][] nodes, Node s, int vx, int vy){
		Queue<Node> queue = getNodesQueue(nodes, s, vx, vy);
		while(!queue.isEmpty()){
			DirectedEdge edge = null;
			Node n = queue.dequeue();
			if(!isVisited(n.getNum())){
				G.addEdge(s.getNum(), n.getNum());
				
				qe.enqueue(new DirectedEdge(s.getNum(), n.getNum(), weight(Math.abs(n.getVX()), Math.abs(n.getVY()))));
			}
		}
	}

	
	private static Queue<Node> getNodesQueue(Node[][] nodes, Node s, int vx, int vy){
		Queue<Node> queue = new Queue<Node>();
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