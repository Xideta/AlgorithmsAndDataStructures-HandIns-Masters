/**
 * @author Mindaugas Kniazevas
 * @author Gleb Cymliakov
 */

import java.util.Iterator;

public class PriorityQueue<Key extends Comparable>{
    private Key[] itemArray; 	// Queue of Items
    private int N; 				// size

    /**
     * Create an empty random queue
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(int maxN){
        N = 0;											// N=0 not used.
        itemArray = (Key[]) new Comparable[maxN+1];	//heap, binary tree
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
     * Add an item
     * @param Item
     */
    public void enqueue(Key v){
        itemArray[++N] = v;		//increment N and add element at the end of the queue
        swim(N);
        						// use swim to restore heap order.
    }

    /**
     * Remove and return a random item
     * @return Item from the queue
     */
    public Key dequeue(){
        if(isEmpty())						//throw exception if queue is empty
            throw new RuntimeException();

        Key max = itemArray[1];				//Retrieve first(max) from the heap
        exch(1, N);							//exchange posstion with the last item.
        itemArray[N--] = null;				//Avoid loitering
        sink(1);							//Sink first element
        return max;
    }


    /**
     * Pushes element up the heap to restore binary heap order.
     *@param k, element  location in the heap
     */
    private void swim(int k) {
        while (k > 1 && less(k/2, k)) { //element location is not at the top and is smaller than a parent element
            exch(k, k/2); 				//swap child position with parnent position.
            k = k/2;					//assign new element position
        }
    }
    /**
     * Pushes element down the heap to restore binary heap order.
     *@param k, element location in the heap
     */
    private void sink(int k) {
        while (2*k <= N) {					//while element child is smaller or equal to heap size
            int j = 2*k;					//element shild loaction
            if (j < N && less(j, j+1)) j++; //if element child is not at the end of the heap and child right sibling is greader than it, increment child possiton
            if (!less(k, j)) break;			//if element at k position si
            exch(k, j);
            k = j;
        }
    }

    /**
     * Compare two element using Comparable.
     * If specified object is less, returns -1, if equal - 0, and if greater - 1
     *@param i, id of one element
     *@param j, id of another element
     *@return boolean. True if specified item is smaller, otherwise return False
     */
    private boolean less(int i, int j) {
        return itemArray[i].compareTo(itemArray[j]) < 0;
    }
    /**
     *Swap positions of two objects
     *@param i, id of one object
     *@param j, id of another object
     */
    private void exch(int i, int j) {
        Key swap = itemArray[i];
        itemArray[i] = itemArray[j];
        itemArray[j] = swap;
    }

    /**
     * Return an iterator over the items in random order
     * @return Item
     */
    public Iterator<Key> iterator(){
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Key>{
        private int i = 0;  										// size
        private Key[] newItemArray = (Key[]) itemArray.clone();  	//refers to a copy of a original item array


        /**
         * Check if queue has any elements
         * @return boolean
         */
        public boolean hasNext() {
            return i<N;
        }

        /**
         * Return items from the beginning to the end of the queue
         * @return Item
         */
        public Key next(){
            if(isEmpty())
                throw new RuntimeException();
            return newItemArray[++i];
        }

        /**
         * Remove is not implemented
         */
        public void remove(){
            // Do stuff
        }
    }
    /**
     * State object that has name, population, seats in congress and priority value
     * priority value is calculated by using Huntington-Hill's methos.
     */
	@SuppressWarnings("unchecked")
    public static class State implements Comparable{

        private String name;
        private int population;
        private int seats;
        private double priorityValue;

        @Override
        public int compareTo(Object object){
			State otherState = (PriorityQueue.State) object;
            if (this.priorityValue<otherState.priorityValue) {
                return -1; // Return -1 if this State has lower priority value than other
            } else if (this.priorityValue>otherState.priorityValue) {
                return 1; // Return 1 if this State has higher priority value than other
            } else { // Return 0 if this State has the same priority as other
                return 0;
            }
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public int getPopulation(){
            return population;
        }

        public void setPopulation(int population){
            this.population = population;
        }

        public int getSeats(){
            return seats;
        }

        public void setSeats(int seats){
            this.seats = seats;
        }

        public double getPriorityValue(){
            return priorityValue;
        }

        public void setPriorityValue(double priorityValue){
            this.priorityValue = priorityValue;
        }

    }


    public static void main(String[] args){

        int states = StdIn.readInt();
        int seats =  StdIn.readInt();

        PriorityQueue<State> queue = new PriorityQueue<State>(states);

        while(!StdIn.isEmpty()){
            State state = new State();
            state.setName(StdIn.readString());
            String nextValue = StdIn.readString();
            try {
            	int nextNumberValue = Integer.parseInt(nextValue);
                state.setPopulation(nextNumberValue);
            } catch (NumberFormatException nfe) {
                state.setName(state.getName()+" "+nextValue);
                nextValue = StdIn.readString();
                int nextNumberValue = Integer.parseInt(nextValue);
                state.setPopulation(nextNumberValue);
            }
            state.setSeats(1);
            seats--;
            state.setPriorityValue(state.getPopulation()/modifiedDivisor(1));
            queue.enqueue(state);
        }

       while(seats>0){
            State state = queue.dequeue();
            state.setSeats(state.getSeats()+1);
            state.setPriorityValue(state.getPopulation()/modifiedDivisor(state.getSeats()));
            seats--;
            queue.enqueue(state);
        }

        Iterator I = queue.iterator();

        while(I.hasNext()){
            State state = (State)I.next();
            StdOut.println(state.getName() + " " + state.getSeats());
        }

    }



    /**
     * Huntington-Hill's modified divisor.
     *@param n amount of current seats
     *@return d a modified divisor.
     */
    private static double modifiedDivisor(int i){
        return Math.sqrt((double)(i*(i+1)));
    }
}
