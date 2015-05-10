/****************************************************************************
 *  Compilation:  javac WeightedQuickUnionUF.java
 *  Execution:  java WeightedQuickUnionUF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Weighted quick-union (without path compression).
 *
 ****************************************************************************/

/**
 *  The <tt>WeightedQuickUnionUF</tt> class represents a union-find data structure.
 *  It supports the <em>union</em> and <em>find</em> operations, along with
 *  methods for determinig whether two objects are in the same component
 *  and the total number of components.
 *  <p>
 *  This implementation uses weighted quick union by size (without path compression).
 *  Initializing a data structure with <em>N</em> objects takes linear time.
 *  Afterwards, <em>union</em>, <em>find</em>, and <em>connected</em> take
 *  logarithmic time (in the worst case) and <em>count</em> takes constant
 *  time.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *     
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class MyUnionFind {
    private int[] id;    // id[i] = parent of i
    private int[] sz;    // sz[i] = number of objects in subtree rooted at i
    private int[] ie;
    private int count;   // number of components
    private int connections; // amaount of connections
    private int N;
    private boolean giantDetected;
    private boolean allConnected;
    private boolean noIsolated;
    private int numberIsolatedLeft;

    /**
     * Initializes an empty union-find data structure with N isolated components 0 through N-1.
     * @throws java.lang.IllegalArgumentException if N < 0
     * @param N the number of objects
     */
    public MyUnionFind(int N) {
        count = N;
        this.N = N;
        numberIsolatedLeft = N;
        connections =0;
        giantDetected = false;
        allConnected = false;
        noIsolated = false;
        id = new int[N];
        sz = new int[N]; //measuring size of of the network
        ie = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
            sz[i] = 1;
            ie[i] = 1;
        }
    }

    /**
     * Returns the number of components.
     * @return the number of components (between 1 and N)
     */
    public int count() {
        return count;
    }

    /**
     * Returns number of connections
     * @return  the number of nonections (between 0 and N-1)
     */
    public int connections(){
        return connections;
    }

    /**
     * @return true when giant is detected
     */
    public boolean giantDetected(){
        return giantDetected;
    }

    /**
     * @return true when no isolated nodes left
     */
    public boolean noIsolated(){
        return noIsolated;
    }

    /**
     * @return true when all nodes conneted
     */
    public boolean allConnected(){
        return allConnected;
    }

    /**
     * Returns the component identifier for the component containing site <tt>p</tt>.
     * @param p the integer representing one site
     * @return the component identifier for the component containing site <tt>p</tt>
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= p < N
     */
    public int find(int p) {
        while (p != id[p]){
            p = id[p];
        }
        return p;
    }

    /**
     * Are the two sites <tt>p</tt> and <tt>q</tt> in the same component?
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return <tt>true</tt> if the two sites <tt>p</tt> and <tt>q</tt>
     *    are in the same component, and <tt>false</tt> otherwise
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site<tt>p</tt> with the component
     * containing site <tt>q</tt>.
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
         // make smaller root point to larger one
        if   (sz[rootP] < sz[rootQ]) { id[rootP] = rootQ; sz[rootQ] += sz[rootP];}
        else                         { id[rootQ] = rootP; sz[rootP] += sz[rootQ];}
       
        connections++; // count connections
        count--;

        isNoIsolated(p,q);
        isGiant(sz[rootP], sz[rootQ]);
        isConnected(sz[rootP], sz[rootQ]);        
    }
    /**
     * Detects when giant(when N/2 nodes become connected) emerges.
     * @param pTree the amount of connected nodes in a p tree.
     * @param qTree the amount of connected nodes in a q tree. 
     */
    public void isGiant(int pTree, int qTree){
        if(!giantDetected){
            if(pTree>=N/2||qTree>=N/2){
                giantDetected = true;
            }
        }
    } 

    /**
     * Detects when all nodes (N) become connected.
     * @param pTree the amount of connected nodes in a p tree.
     * @param qTree the amount of connected nodes in a q tree. 
     */
    public void isConnected(int pTree, int qTree){
        if(!allConnected){
            if(pTree==N||qTree==N){
                allConnected = true;
            }
        }
    } 

    /**
     * Detects when there are no isolated nodes left.
     * @param pTree the amount of connected nodes in a p tree.
     * @param qTree the amount of connected nodes in a q tree. 
     */
    public void isNoIsolated(int p, int q){
        if(!noIsolated){
        		numberIsolatedLeft -= ie[p] + ie[q];
        		ie[p]=0;
        		ie[q]=0;
        		if(numberIsolatedLeft==0){
        			noIsolated=true;
        		}
        }
    } 
}
    /**
     * Reads in a sequence of pairs of integers (between 0 and N-1) from standard input, 
     * where each integer represents some object;
     * if the objects are in different components, merge the two components
     * and print the pair to standard output.
     */
   /* public static void main(String[] args) {
        int N = StdIn.readInt();
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(uf.count() + " components");

        }
        */