/**
 * @author Mindaugas Kniazevas
 * @author Gleb Cymliakov
 */

import java.util.Iterator;

public class RandomQueue<Item> implements Iterable<Item>{
	private Item[] itemArray; 	// Queue of Items
	private int N; 				// size

	/**
	 * Create an empty random queue
	 */
	@SuppressWarnings("unchecked")
	public RandomQueue(){
		N = 0;
		itemArray = (Item[]) new Object[1];	
	}

	/**
	 * Is it empty
	 * @return boolean is array empty
	 */
	public boolean isEmpty(){ 
		return N==0;	
	}

	/**
	 * Return the number of elements
	 * @return N
	 */
	public int size(){
		return N; 		//returns size
		
	}

	/**
	 * Resize the array 
	 * @param int newSize of the array
	 */
	private void resize(int newSize){
		@SuppressWarnings("unchecked")
		Item[] temp = (Item[]) new Object[newSize]; // create a temp ARRAY with new size
		for (int i=0; i<N; i++)						
			temp[i] = itemArray[i];					// copy elements from original array to temp
		itemArray = temp;							// rename temp array to original array
	}
	/** 
	 * Add an item
	 * @param Item 
	 */
	public void enqueue(Item item){
		if(N==itemArray.length)				//check if array if full of elemenets
			resize(2*itemArray.length);		//increase the size of the array twice
		itemArray[N++] = item;				//add element at the end of the queue
		}

	/** 
	 * Return (but do not remove) a random item
	 * @return Item
	 */
	public Item sample(){
		if(isEmpty())								//throw exception if queue is empty
			throw new RuntimeException();
		int randomElement = StdRandom.uniform(0, N);
		return itemArray[randomElement];
	}

	/**
	 * Remove and return a random item
	 * @return Item from the queue
	 */
	public Item dequeue(){
		if(isEmpty())										//throw exception if queue is empty
			throw new RuntimeException();				//requirements for the exercise.
			
		int randomElement = StdRandom.uniform(0,N); 	//generate random number.
		Item returnItem = itemArray[randomElement]; 	//retreive item from array
		itemArray[randomElement] = itemArray[--N];		//replace removed element with the last element. 
		itemArray[N] = null;							//avoiding loitering				
		if(N>0 && N==itemArray.length/4)				//check if elements occupy quarter of the array
			resize(itemArray.length/2);					//resize the array in half
		
		return returnItem; 			
	}

	/**
	 * Return an iterator over the items in random order
	 * @return Item
	 */
	public Iterator<Item> iterator(){
		return new ArrayIterator();
	}

	private class ArrayIterator implements Iterator<Item>{
		private int i = N;  										// size
		private Item[] newItemArray = (Item[]) itemArray.clone();  	//refers to a copy of a original item array
 
			
		/**
		 * Check if queue has any elements
		 * @return boolean
		 */
		public boolean hasNext() {
			return i>0;		
		}

		/**
		 * Return random item
		 * @return Item
		 */
		public Item next(){
			if(isEmpty())
				throw new RuntimeException();
			int randomElement = StdRandom.uniform(0, i);				//generate random number
			Item returnItem = newItemArray[randomElement];				//select a random element
			newItemArray[randomElement] = newItemArray[--i];			//replacing selected element with the last one
			newItemArray[i] = null;										//since it is a copy, we avoid loitering.
			return  returnItem;
		}

		/**
		 * Remove is not implemented
		 */
		public void remove(){
			// Do stuff
		}
	}

	// The main method below tests your implementation. Do not change it.
	public static void main(String args[]){
		
		// Build a queue containing the Integers 1,2,...,6:
		RandomQueue<Integer> Q= new RandomQueue<Integer>();
		for (int i = 1; i < 7; ++i) 
			Q.enqueue(i); // autoboxing! cool!
		
		// Print 30 die rolls to standard output
		StdOut.print("Some die rolls: ");
		for (int i = 1; i < 30; ++i) 
			StdOut.print(Q.sample() +" ");
		StdOut.println();
		
		// Let’s be more serious: do they really behave like die rolls?
		int[] rolls= new int [10000];
		for (int i = 0; i < 10000; ++i)
			rolls[i] = Q.sample(); // autounboxing! Also cool!
		StdOut.printf("Mean (should be around 3.5): %5.4f\n", StdStats.mean(rolls));
		StdOut.printf("Standard deviation (should be around 1.7): %5.4f\n",
			StdStats.stddev(rolls));
		
		// Now remove 3 random values
		StdOut.printf("Removing %d %d %d\n", Q.dequeue(), Q.dequeue(), Q.dequeue());
		
		// Add 7,8,9
		for (int i = 7; i < 10; ++i) 
			Q.enqueue(i);
		
		// Empty the queue in random order
		while (!Q.isEmpty()) 
			StdOut.print(Q.dequeue() +" ");
		StdOut.println();
		
		// Let’s look at the iterator. First, we make a queue of colours:
		RandomQueue<String> C= new RandomQueue<String>();
		C.enqueue("red"); C.enqueue("blue"); C.enqueue("green"); C.enqueue("yellow");
		Iterator I= C.iterator();
		Iterator J= C.iterator();
		StdOut.print("Two colours from first shuffle: "+I.next()+" "+I.next()+" ");
		StdOut.print("\nEntire second shuffle: ");
		while (J.hasNext()) 
			StdOut.print(J.next()+" ");
		StdOut.println("\nRemaining two colours from first shuffle: "+I.next()+" "+I.next());
	}
	
}

