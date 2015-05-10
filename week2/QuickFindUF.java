/****************************************************************************
 *  Compilation:  javac QuickFindUF.java
 *  Execution:  java QuickFindUF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Quick-find algorithm.
 *
 ****************************************************************************/

/**
 *  The <tt>QuickFindUF</tt> class represents a union-find data structure.
 *  It supports the <em>union</em> and <em>find</em> operations, along with
 *  methods for determinig whether two objects are in the same component
 *  and the total number of components.
 *  <p>
 *  This implementation uses quick find.
 *  Initializing a data structure with <em>N</em> objects takes linear time.
 *  Afterwards, <em>find</em>, <em>connected</em>, and <em>count</em>
 *  takes constant time but <em>union</em> takes linear time.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *     
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class QuickFindUF {
    private int[] id;    // id[i] = component identifier of i
    private int count;   // number of components
    private int elementCount;
    private int N;
    private boolean giantDetected;

    /**
     * Initializes an empty union-find data structure with N isolated components 0 through N-1.
     * @throws java.lang.IllegalArgumentException if N < 0
     * @param N the number of objects
     */
    public QuickFindUF(int N) {
        this.N = N;
        count = N;
        giantDetected = false;
       
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }

    /**
     * Returns the number of components.
     * @return the number of components (between 1 and N)
     */
    public int count() {
        return count;
    }
    /**
     * @return true when giant is detected
     */
    public boolean giantDetected(){
        return giantDetected;
    }

    /**
     * Returns the component identifier for the component containing site <tt>p</tt>.
     * @param p the integer representing one site
     * @return the component identifier for the component containing site <tt>p</tt>
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= p < N
     */
    public int find(int p) {
        return id[p];
    }

    /**
     * Are the two sites <tt>p</tt> and <tt>q/tt> in the same component?
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return <tt>true</tt> if the two sites <tt>p</tt> and <tt>q</tt> are in
     *    the same component, and <tt>false</tt> otherwise
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }
  
    /**
     * Merges the component containing site<tt>p</tt> with the component
     * containing site <tt>q</tt>.
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public void union(int p, int q) {
        if (connected(p, q)) return;
        int pid = id[p];
        for (int i = 0; i < id.length; i++)
            if (id[i] == pid) id[i] = id[q];
        count--;
        isGiant(id[q]);
    }

     /**
     * Detects when giant(when N/2 nodes become connected) emerges.
     * @param pTree the amount of connected nodes in a p tree.
     * @param qTree the amount of connected nodes in a q tree. 
     */
    public void isGiant(int qid){
        elementCount=0;
        if(!giantDetected){
            for(int i=0; i<id.length;i++){
                if(id[i] == qid){
                elementCount++;
                    if(elementCount>N/2){
                        giantDetected=true;
                        StdOut.println("giantDetected:"+elementCount);
                    }
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
    
    public static void main(String[] args) {
        int N = StdIn.readInt();
        int T = StdIn.readInt();
        boolean isGiantFound = false;
        QuickFindUF uf = null;
        Stopwatch sw = null;
        double[] timesElapsed;
        timesElapsed = new double[T];

        for (int j=0; j<T; j++) {
            uf = new QuickFindUF(N);
            sw = new Stopwatch();
            isGiantFound = false;
           
            while (!isGiantFound) {
                int p = StdRandom.uniform(0,N);
                int q = StdRandom.uniform(0,N);
                if (uf.connected(p, q)) continue;
                uf.union(p, q);
                
                if(!isGiantFound && uf.giantDetected()){
                    timesElapsed[j] = sw.elapsedTime();
                    isGiantFound = true;
                }
            }
        }
        StdOut.println("Time elapsed : " + StdStats.mean(timesElapsed) + " +/- " + StdStats.stddev(timesElapsed));
    }

    
    
}
