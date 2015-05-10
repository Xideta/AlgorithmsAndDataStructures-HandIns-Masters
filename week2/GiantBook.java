/**
 *@author Mindaugas Kniazevas
 *@author Gleb Cymliakov
 */

public class GiantBook{

	private static int N; // Number of randomly generated nodes
	private static int T; // Number of repeasti
	private static double time;

	private static double[] giantSizes;
	private static double[] noMoreIsolated;
	private static double[] connected;
	private static double[] timesElapsed;

	private static boolean isGiantFound;
	private static boolean isNoMoreIsolatedLeft;
	private static boolean isAllConnected;

	private static void createArrays(int size) {
		giantSizes = new double[size];
		timesElapsed = new double[size];
		noMoreIsolated = new double[size];
		connected = new double[size];
    }

	/**
	 * Calculates statistics, mean and standard deviation.
	 */
	private static void calculateStatitstics() {
		StdOut.println("Time elapsed : " + StdStats.mean(timesElapsed) + " +/- " + StdStats.stddev(timesElapsed));
		StdOut.println("Giant emerged at : " + StdStats.mean(giantSizes) + " +/- " + StdStats.stddev(giantSizes));
		StdOut.println("Last non isolated : " + StdStats.mean(noMoreIsolated) + " +/- " + StdStats.stddev(noMoreIsolated));
		StdOut.println("Last connected : " + StdStats.mean(connected) + " +/- " + StdStats.stddev(connected));
	}

	public static void main(String[] args){
		StdOut.println("insert N");
		int N = StdIn.readInt();
		StdOut.println("insert T");
		int T = StdIn.readInt();
		Stopwatch sw = null;
		MyUnionFind uf = null;

		
		createArrays(T);

		for (int j=0; j<T; j++) {
			int counter = 0;
			
			sw = new Stopwatch();
			uf = new MyUnionFind(N);

			isGiantFound = false;
			isNoMoreIsolatedLeft = false;
			isAllConnected = false;

			while (!uf.allConnected()) {
				int p = StdRandom.uniform(0, N);
				int q = StdRandom.uniform(0, N);
				if (!uf.connected(p, q)) {
					uf.union(p, q);
				}
				if (!isGiantFound && uf.giantDetected()) {
					giantSizes[j]=counter;
					timesElapsed[j] = sw.elapsedTime();
					isGiantFound = true;
				}
				if (!isNoMoreIsolatedLeft && uf.noIsolated()) {
					noMoreIsolated[j]=counter;
					isNoMoreIsolatedLeft = true;
				}
				if (!isAllConnected && uf.allConnected()) {
					connected[j]=counter;
					isAllConnected = true;
				}
				counter++;
			}
			
			
		}
		calculateStatitstics();
	}

}
