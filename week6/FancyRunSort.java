/**
 * RunSort implementation
 *@author Gleg Cymliakov
 *@author Mindaugas Kniazevas
 */
public class FancyRunSort{
	
  	// Size of an array. If size of a given array gets smaller than stated here, insert sort will be performed
  	private static final int INSERT_SRT_LENGTH = 8;	// max length of subarray to run insert sort.
  
	/**
	 *Constructor
	 */
	public FancyRunSort(){
	}

	/**
	 * Sort method. Copies array, devides it into 
	 * subarrays and uses merge method to sort it.
	 * Inspiration from Sedgewick and Wayne, section 2.2
	 *@param arr. Comparable array.
	 */
	public static void sort(Comparable[] arr){
		int N = arr.length;						//get size of the array
		Comparable[] aux = new Comparable[N];		//create auxilary array
		for(int n=1; n<N; n=n+n){					//n:size of subarray. With every iteration, increase n by 2.
			for(int i=0; i<N-n; i+=n+n){			//i:subarray index. With every iteration, increase i with a 2*n 
				int lo = i;							//low index of subarray
				int mid = i+n-1;					//mid index. To low index add half of subarray size and deduct 1.
				int hi = Math.min((i+n+n-1), N-1);	//high index. Choose smallest from two; either low plus two subarray sizes, or array size(at some point this will be smaller).
                if(hi-lo <= INSERT_SRT_LENGTH){
                  insertSort(arr, lo, hi);			//run insertSort method if the length of sub-array is less than or equal to 8
                } else {
                  merge(arr, aux, lo, mid, hi);	
                }	
			}
		}
	}
  
  
 
	/**
	 *Merge method 
	 *Taken from Sedgewick and Wayne, section 2.2
	 *@param arr. original array.
	 *@param aux. auxilary array.
	 *@param lo. low index of subarray.
	 *@param mid. mid index of subarray.
	 *@param hi. high index of subarray.
	 */
	private static void merge(Comparable[] arr, Comparable[] aux, int lo, int mid, int hi){
		for(int k = lo; k<=hi;k++){								//copy elements from original to aux array. 
			aux[k] = arr[k];
		}
        int i = lo, j = mid+1;						
        for (int k = lo; k <= hi; k++) {							// merge back to a[]
            if      (i > mid)              arr[k] = aux[j++];		// left half exhausted (take from the right). This copying is unneccessary
            else if (j > hi)               arr[k] = aux[i++];		// right half exhausted (take from the left)
            else if (less(aux[j], aux[i])) arr[k] = aux[j++];		// current key on right less than current key on left (take from the right).
            else                           arr[k] = aux[i++];		// current key on right greater than or equal to current key on left (take from the left).
        }
	}
  
      /**
     * Perform insert sort on a given array of Comparable objects
     * @param pArr array of Comparable objects to be sorted
     */
    private static void insertSort(Comparable[] arr, int lo, int hi) {
		for (int i = lo; i <=hi; i++) {
            for (int j = i; j > 0 && less(arr[j], arr[j-1]); j--) { 
                exch(arr, j, j-1);								 
            }
        }
    }
  
  	/**
     *Swap positions of two objects
     *@param i, id of one object
     *@param j, id of another object
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
  
	/**
     * Compare two elements.
     * If specified object is less, returns -1, if equal - 0, and if greater - 1
     *@param a, comparable object
     *@param b, compare to object
     *@return boolean. True if specified item is smaller, otherwise return False
     */
	private static boolean less(Comparable a, Comparable b){
		return a.compareTo(b)<0;				//return true if a is smaller than b
	}

	// print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

	public static void main(String[] args){
		String[] arr = StdIn.readStrings();
		FancyRunSort.sort(arr);
		show(arr);
	}

}