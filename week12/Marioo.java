public class Marioo{
	static class Node{
		private int x;
		private int y;
		private char value;
		public Node(int x, int y, char value){
			this.x=x;
			this.y=y;
			this.value = value;
		}
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
		public int getValue(){
			return value;
		}
	}	
	
	public static void main(String []args){
		ST<Character, Integer> st = new ST<Character, Integer>();
		DijkstraSP  sp;
		int c = StdIn.readInt();
		int r= StdIn.readInt();
		
		Node[][] nodes = new Node[c][r];

		Character [][] characters= new Character[c][r];
		char[] keys = new char[c*r];		
		String[] s = StdIn.readAllLines();
		int count = 0;
		for (int i = 1; i<s.length;i++ ){
			char[] chars = s[i].toCharArray();
			for (int j = 0;j<chars.length; j++) {
				characters[i-1][j] = chars[j];
				st.put(characters[i-1][j], count);
				keys[count] = chars[j]; 
				count++;

			}
		}
		print(characters);

		for (int i = 0;i<keys.length ;i++ ) {
			StdOut.println(i+": "+ keys[i]);
		}
		StdOut.println("Character F: "+(int)st.get('F'));
		





		EdgeWeightedDigraph G = constructGraph(st, characters);
		sp = new DijkstraSP(G, (int)st.get('S'));
		//not workong properly
		if(sp.hasPathTo((int)st.get('F'))){
			StdOut.println("Path length: "+ sp.distTo((int)st.get('F')));
		}else{
			StdOut.println("no path");		
		}

	}

	
	private static EdgeWeightedGraph constructGraph(ST st, Character[][] characters){
		EdgeWeightedGraph G = new EdgeWeightedGraph((characters.length)*(characters[0].length)*8);
		for (int i=0; i<characters.length; i++) {
			for (int j=0;j<characters[i].length; j++) {
				
				//horizontal (i+1 & j+0), (i-1 & j-0), 
				if(i+1<characters.length)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i+1][j]), weight(1,0)));
				if(i-1>0)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i-1][j]), weight(1,0)));
			
				//vertical (i+0 & j+1), (i-0 & j-1), 
				if(j+1<characters[i].length)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i][j+1]), weight(0,1)));
				if(j-1>0)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i][j-1]), weight(0,1)));
				
				//diagonal (i+1 & j+1), (i+1 & j-1), (i-1 & j+1), (i-1 & j-1)
				if(i+1<characters.length && j+1<characters[i].length)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i+1][j+1]), weight(1,1)));
				if(i+1<characters.length && j-1>0)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i+1][j-1]), weight(1,1)));
				if(i-1>0 && j+1<characters[i].length)
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i-1][j+1]), weight(1,1)));
				if(i-1>0 && j-1>0) 
					G.addEdge(new Edge((int)st.get(characters[i][j]), (int)st.get(characters[i-1][j-1]), weight(1,1)));
			}	
			
		}
		return G;

	}

	private static double weight(double i, double j){
		return Math.abs(Math.sqrt((Math.pow(i,2))+(Math.pow(j,2))));
	}

	private static void print(Character[][] characters){
		for (int i = 0; i < characters.length; i++) {
			String str = "";
			for (int j = 0; j < characters[0].length; j++) {
				str += "" + characters[i][j];
			}
			System.out.println(str);
		}
	}

	
}