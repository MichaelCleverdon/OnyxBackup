import java.util.Comparator;
import java.util.ListIterator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Mergesort algorithm.
 *
 * @author CS221
 */
public class Sort
{	
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. 
	 * As configured, uses WrappedDLL. Must be changed if using 
	 * your own IUDoubleLinkedList class. 
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() 
	{
		return new IUDoubleLinkedList<T>(); //TODO: replace with your IUDoubleLinkedList for extra-credit
	}
	
	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @see IndexedUnsortedList 
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) 
	{
		mergesort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList 
	 */
	public static <T> void sort(IndexedUnsortedList <T> list, Comparator<T> c) 
	{
		mergesort(list, c);
	}
	
	/**
	 * Mergesort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface, 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 */
	private static <T extends Comparable<T>> void mergesort(IndexedUnsortedList<T> list)
	{
		IndexedUnsortedList<T> tempList = mergeSort(list);
		while(list.size() != 0) {
			list.removeFirst();
		}
		while(tempList.size() != 0) {
			list.add(tempList.first());
			tempList.removeFirst();
		}
	}
		
	/**
	 * Mergesort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 */
	private static <T> void mergesort(IndexedUnsortedList<T> list, Comparator<T> c)
	{
		IndexedUnsortedList<T> tempList = mergeSort(list, c);
		while(list.size() != 0) {
			list.removeFirst();
		}
		while(tempList.size() != 0) {
			list.add(tempList.first());
			tempList.removeFirst();
		}
	}
	
	
	
	private static <T> T updateRemovedElements(IndexedUnsortedList<T> list, boolean isFirstHalf) {
		if(list.size() == 0) {
			return null;
		}
		return (isFirstHalf ? list.first() : list.last());
	}
	
	private static <T extends Comparable<T>> IndexedUnsortedList<T> mergeSort(IndexedUnsortedList<T> list){
		//Sorted list of 0 or 1 element
		if(list.size() <= 1) {
			return list;
		}
		int size = list.size();
		int center = size/2;
		IndexedUnsortedList<T> firstHalf = newList();
		IndexedUnsortedList<T> secondHalf = newList();
		T firstElement = list.first();
		T lastElement = list.last();
		
		for(int i = 0; i < center; i++) {
			firstHalf.add(firstElement);
			list.removeFirst();
			firstElement = updateRemovedElements(list, true);
		}
		for(int i = center; i < size; i++) {
			secondHalf.add(lastElement);
			list.removeLast();
			lastElement = updateRemovedElements(list, false);
		}
		if(size != 2) {
			firstHalf = mergeSort(firstHalf);
			secondHalf = mergeSort(secondHalf);
		}
		
		return merge(firstHalf, secondHalf);
	}
	
	private static <T> IndexedUnsortedList<T> mergeSort(IndexedUnsortedList<T> list, Comparator<T> c){
		//Sorted list of 0 or 1 element
		if(list.size() <= 1) {
			
			return list;
		}
		int size = list.size();
		int center = size/2;
		IndexedUnsortedList<T> firstHalf = newList();
		IndexedUnsortedList<T> secondHalf = newList();
		T firstElement = list.first();
		T lastElement = list.last();
		
		for(int i = 0; i < center; i++) {
			firstHalf.add(firstElement);
			list.removeFirst();
			firstElement = updateRemovedElements(list, true);
		}
		for(int i = center; i < size; i++) {
			secondHalf.add(lastElement);
			list.removeLast();
			lastElement = updateRemovedElements(list, false);
		}
		firstHalf = mergeSort(firstHalf, c);
		secondHalf = mergeSort(secondHalf, c);
		
		return merge(firstHalf, secondHalf, c);
	}
	
	/**
	 * Merge method to merge the lists as they come back
	 */
	private static <T extends Comparable<T>> IndexedUnsortedList<T> merge(IndexedUnsortedList<T> list1, IndexedUnsortedList<T> list2) {
		IndexedUnsortedList<T> newList = newList();
		if(list1.size() == 0 || list2.size() == 0) {
			IndexedUnsortedList<T> tempList = newList();
			if(list1.size() == 0) {
				while(list2.size() != 0) {
					tempList.add(list2.first());
					list2.removeFirst();
				}
			}
			else {
				while(list1.size() != 0) {
					tempList.add(list1.first());
					list1.removeFirst();
				}
			}
			return tempList;
		}
		
		int comparison = list1.first().compareTo(list2.first());
		if(comparison > 0) {
			newList.add(list2.first());
			list2.removeFirst();
		}
		else if(comparison <= 0) {
			newList.add(list1.first());
			list1.removeFirst();
		}
		
		IndexedUnsortedList<T> tempList = merge(list1, list2);
		while(tempList.size() != 0) {
			newList.add(tempList.first());
			tempList.removeFirst();
		}
		return newList;
		
	}
	
	/**
	 * Merge method to merge the lists as they come back with comparator usage
	 */
	private static <T> IndexedUnsortedList<T> merge(IndexedUnsortedList<T> list1, IndexedUnsortedList<T> list2, Comparator<T> comparator) {
		IndexedUnsortedList<T> newList = newList();
		if(list1.size() == 0 || list2.size() == 0) {
			/*for(int i = 0; i < (list1 == null ? list2.size() : list1.size()); i++) {
				newList.
			}*/
			IndexedUnsortedList<T> tempList = newList();
			if(list1.size() == 0) {
				while(list2.size() != 0) {
					tempList.add(list2.first());
					list2.removeFirst();
				}
			}
			else {
				while(list1.size() != 0) {
					tempList.add(list1.first());
					list1.removeFirst();
				}
			}
			return tempList;
		}
		else {
			int comparison = comparator.compare(list1.first(), list2.first());
			if(comparison > 0) {
				newList.add(list2.first());
				list2.removeFirst();
			}
			else if(comparison <= 0) {
				newList.add(list1.first());
				list1.removeFirst();
			}
		}
		IndexedUnsortedList<T> tempList = merge(list1, list2, comparator);
		while(tempList.size() != 0) {
			newList.add(tempList.first());
			tempList.removeFirst();
		}
		return newList;
	}
}
