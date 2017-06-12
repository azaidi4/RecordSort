///////////////////////////////////////////////////////////////////////////////
//	Semester:		CS367 Spring 2017
//  PROJECT:		P3
//  FILE:			Reducer.java
//
//  TEAM:    16;
//
// Authors:
// Author1: Ahmad Zaidi, azaidi4@wisc.edu, azaidi4, LEC001
// Author2: Devin Samaranayake, dsamaranayak@wisc.edu>, dsamaranayak, LEC001
//
///////////////////////////////////////////////////////////////////////////////

import java.util.Comparator;

/**
 * This class describes a data structure that maintains a minimum priority minList,
 *  supporting isEmpty(), removeMin(), and insert().
 * 
 * An implementation of the MinPriorityQueueADT interface. This implementation 
 * stores FileLine objects.
 *
 */
public class FileLinePriorityQueue implements MinPriorityQueueADT<FileLine> {
	private FileLine [] minList;   		// array to store FileLine objects
	private Comparator<FileLine> cmp;   //a Comparator which can compare two 
										// FileLine objects
	private int maxSize;				// maximum size of the FileLine array
	private int numItems;				// number of FileLine objects

	/**
	 * Construct new FileLinePriorityQueue instance by intializing the FileLine
	 * array, fileLine comparator, number of FileLine object, and array size
	 * 
	 */
	public FileLinePriorityQueue(int initialSize, Comparator<FileLine> cmp) {
		this.cmp = cmp;
		numItems = 0;
		maxSize = initialSize;
		this.minList = new FileLine[maxSize+1];

	}
	/**
	 * Removes the minimum element from the Priority Queue, and returns it.
	 *
	 * @return the minimum element in the minList, according to the compareTo()
	 * method of FileLine.
	 * @throws PriorityQueueEmptyException if the priority minList has no elements
	 * in it
	 */
	public FileLine removeMin() throws PriorityQueueEmptyException {
		if (isEmpty()) throw new PriorityQueueEmptyException();
		FileLine min = minList[1];     // fileLine to to removed
		minList[1] = minList[numItems];  // replace root with last fileLine
		minList[numItems] = null; 	 // empty last item
		numItems--;					 // decrement number of fileLine

		int pos = 1;
		// checks the order (minimum priority) at each level and swaps if out order
		while (pos <= numItems / 2){
			int minPos = pos;
			// if value in parent node is greater than left child, assign the 
			// child's index to parent index
			if(2*pos <= numItems && cmp.compare(minList[2*pos] , minList[minPos]) < 0)
				minPos = 2 * pos;
			// if value in parent node is greater than right child, assign the 
			// child's index to parent index
			if(2*pos+1 <= numItems && cmp.compare(minList[2*pos+1] , minList[minPos]) < 0)
				minPos = 2*pos+1;
			
			if(minPos != pos){
				swapQueue(pos,minPos); // call method to swap 
				pos = minPos;
			}else break;
		}

		return min;
	}

	/**
	 * Inserts a FileLine into the minList, making sure to keep the shape and
	 * order properties intact.
	 *
	 * @param fl the FileLine to insert
	 * @throws PriorityQueueFullException if the priority minList is full.
	 */
	public void insert(FileLine fl) throws PriorityQueueFullException {
		
		if (fl == null) throw new IllegalArgumentException();
	
		if (numItems == maxSize) throw new PriorityQueueFullException();
		
		boolean done = false;
		int pos = numItems + 1;
		minList[pos] = fl;  // adds Fileline fl at next elements
		 
		// check order at each level and restore the order
		while (!done){
			int parentIdx = pos / 2;
			if (parentIdx == 0) done = true;
			// if parent is smaller than child set done to true
			else if (cmp.compare(minList[parentIdx], minList[pos]) <= 0) done = true;
			else {
				swapQueue(parentIdx, pos); 

				pos = parentIdx; // set child to parent
			}
		}

		numItems++;
	}

	/**
	 * Checks if the minList is empty.
	 *  @return true, if it is empty; false otherwise
	 */
	public boolean isEmpty() {
		return numItems == 0;
	}

	/**
	 * This is a help method which swaps parent's value  and child's value
	 * @param parent - index of the parent 
	 * @param child - index of child
	 */
	private void swapQueue(int parent, int child){
		FileLine temp;
		temp = minList[child];
		minList[child] = minList[parent];
		minList[parent] = temp;
	}
}
