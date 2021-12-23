import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double Linked List with full iterator implemented
 * 
 * @author michael c
 *
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	protected DLLNode<T> head;
	protected DLLNode<T> tail;
	
	IUDoubleLinkedList(){
	}
	
	/**
	 * CustomIterator that I created to loop through the lists
	 * 
	 * @author michael c
	 *
	 * @param <T> Generics 
	 */
	class CustomListIterator<T> implements ListIterator<T>{
		
		private int currentIndex;
		private DLLNode<T> head, tail, prevNode, nextNode, lastReturnedNode;
		private boolean canRemove, canSet;
		CustomListIterator(DLLNode<T> pHead, DLLNode<T> pTail){
			canRemove = false;
			canSet = false;
			prevNode = null;
			head = pHead;
			nextNode = pHead;
			tail = pTail;
			currentIndex = -1;
		}
		
		CustomListIterator(DLLNode<T> pHead, DLLNode<T> pTail, int pStartIndex, int pSize){
			if(pStartIndex > pSize || pStartIndex < 0) {
				throw new IndexOutOfBoundsException();
			}
			canRemove = false;
			canSet = false;
			prevNode = null;
			head = pHead;
			nextNode = pHead;
			tail = pTail;
			currentIndex = pStartIndex;
		}
		
		/**
		 * Iterator - Checks to see if the list has another element after the current one
		 */
		@Override
		public boolean hasNext() {
			return(nextNode != null);
		}

		/**
		 * Iterator - goes to the next element in the list
		 */
		@Override
		public T next() {
			if(hasNext()) {
				prevNode = nextNode;
				nextNode = nextNode.getNext();
				currentIndex++;
				canRemove = true;
				canSet = true;
				lastReturnedNode = prevNode;
				return prevNode.getElement();
			}
			else {
				throw new NoSuchElementException();
			}
		}
		
		/**
		 * Iterator - Removes the current element from the list
		 */
		@Override
		public void remove() {			
			//If the entry is already deleted
			if(canRemove == false)
			{
				throw new IllegalStateException();
			}
			
			if(lastReturnedNode == head) {
				//Removing final element in list
				if(lastReturnedNode == tail) {
					head = null;
					tail = null;
					prevNode = null;
					nextNode = null;
					
				}
				//Removing first element of list
				else {
					lastReturnedNode.getNext().setPrevious(null);
					nextNode = lastReturnedNode.getNext();
					head = nextNode;
				}
			}
			//Removing last element in a list
			else if(lastReturnedNode == tail) {
				lastReturnedNode.getPrevious().setNext(null);
				prevNode = lastReturnedNode.getPrevious();
				nextNode = null;
				tail = lastReturnedNode.getPrevious();
			}
			//Somewhere in the middle
			else {
				lastReturnedNode.getPrevious().setNext(lastReturnedNode.getNext());
				lastReturnedNode.getNext().setPrevious(lastReturnedNode.getPrevious());
				prevNode = lastReturnedNode.getPrevious();
				nextNode = lastReturnedNode.getNext();
			}
				lastReturnedNode.setElement(null);
				canRemove = false;
				canSet = false;
				return;	
			
		}

		@Override
		public void add(T element) {
			if(prevNode != null) {
				DLLNode<T> newNode = new DLLNode<T>(element);
				prevNode.setNext(newNode);
				
				if(nextNode != null) {
					nextNode.setPrevious(newNode);
				}
				
				newNode.setPrevious(prevNode);
				newNode.setNext(nextNode);
				prevNode = newNode;
			}
			else {
				DLLNode<T> newNode = new DLLNode<T>(element);
				newNode.setNext(nextNode);
				
				//Only set the previous of the nextNode if it's not an empty list
				if(nextNode != null) {
					nextNode.setPrevious(newNode);
				}
				
				if(nextNode == head) {
					head = newNode;
				}
				prevNode = newNode;
			}
			canRemove = false;
			canSet = false;
			currentIndex++;
		}

		@Override
		public boolean hasPrevious() {
			return(prevNode != null);
		}

		@Override
		public int nextIndex() {
			return currentIndex + 1;
		}

		@Override
		public T previous() {
			if(hasPrevious()) {
				nextNode = prevNode;
				prevNode = prevNode.getPrevious();
				currentIndex--;
				lastReturnedNode = nextNode;
				return nextNode.getElement();
			}
			else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public int previousIndex() {
			return currentIndex;
		}

		@Override
		public void set(T element) {
			if(canSet) {
				lastReturnedNode.setElement(element);
			}
			else {
				throw new IllegalStateException();
			}
			
		}
	}

	/**
	 * Adds an element to the front of the list
	 */
	@Override
	public void addToFront(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		//If the list is empty
		if(head == null) {
			head = newNode;
			tail = newNode;
		}
		//List not empty
		else {
			head.setPrevious(newNode);
			//Set the newNode to point to the current head
			newNode.setNext(head);
			//Update head to the new node
			head = newNode;
		}		
	}

	/**
	 * Adds an element to the end of the list
	 */
	@Override
	public void addToRear(T element) {
		DLLNode<T> index = head;
		DLLNode<T> newNode = new DLLNode<T>(element);
		//List is empty
		if(index == null) {
			head = newNode;
			tail = newNode;
			return;
		}
		newNode.setPrevious(tail);
		tail.setNext(newNode);
		tail = newNode;
	}

	/**
	 * Adds an element to the end of the list
	 */
	@Override
	public void add(T element) {
		addToRear(element);		
	}

	/**
	 * Adds an element after a target element
	 */
	@Override
	public void addAfter(T element, T target) {
		if(head == null || element == null) {
			throw new NoSuchElementException();
		}
		
		DLLNode<T> index = head;
		if(!(contains(target))) {
			throw new NoSuchElementException();
		}
		
		while(index.getElement() != target) {
			index = index.getNext();
		}
		DLLNode<T> newNode = new DLLNode<T>(element);
		newNode.setNext(index.getNext());
		newNode.setPrevious(index);
		if(index != tail) {
			index.getNext().setPrevious(newNode);
		}
		index.setNext(newNode);
		
		if(tail == index) {
			tail = newNode;
		}
	}

	/**
	 * Adds an element to a certain index in the list
	 */
	@Override
	public void add(int index, T element) {
		//Setting currentIndex to be 1 because we need to get to the node before the index to remove it and add our new one
		int currentIndex = 1;
		DLLNode<T> indexNode = head;
		//Empty list
		if(index == 0) {
			addToFront(element);
			return;
		}
		if(index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}

		//If you're adding to the end of the list
		else if(index == size()) {
			addToRear(element);
		}
		else {
			while (currentIndex < index) {
				currentIndex++;
				indexNode = indexNode.getNext();
			}
			//It hit the end
			if(indexNode == null) {
				throw new IndexOutOfBoundsException();
			}
			
			else {
				DLLNode<T> newNode = new DLLNode<T>(element);
				if(indexNode != tail) {
					indexNode.getNext().setPrevious(newNode);;
				}
				newNode.setPrevious(indexNode);
				newNode.setNext(indexNode.getNext());
				indexNode.setNext(newNode);
			}
		}
	}

	/**
	 * Removes the first element in the list
	 */
	@Override
	public T removeFirst() {
		if(head == null) {
			throw new NoSuchElementException();
		}
		DLLNode<T> removedNode = head;
		if(head == tail) {
			tail = removedNode.getNext();
			removedNode.setNext(null);
			removedNode.setPrevious(null);
		}
		else {
			head = removedNode.getNext();
			removedNode.getNext().setPrevious(null);
		}
		head = removedNode.getNext();
		return removedNode.getElement();
	}

	/**
	 * Removes the last element in the list
	 */
	@Override
	public T removeLast() {
		T removedElement;
		//Trying to remove stuff from empty list
		if(head == null) {
			throw new NoSuchElementException();
		}
		//Removing the only object in a list
		else if(head == tail) {
			removedElement = head.getElement();
			head = null;
			tail = null;
		}
		else {
		DLLNode<T> nodeBeforeRemoval = tail.getPrevious();
		removedElement = tail.getElement();
		tail = nodeBeforeRemoval;
		tail.setNext(null);
		}
		return removedElement;
	}

	/**
	 * Removes the first instance of an element in the list
	 */
	@Override
	public T remove(T element) {
		//Empty list or invalid element
		if(head == null || element == null) {
			throw new NoSuchElementException();
		}
		else {
			DLLNode<T> indexNode = head;
			if(!(contains(element))) {
				throw new NoSuchElementException();
			}
			if(head.getElement() == element) {
				return removeFirst();
			}
			if(tail.getElement() == element) {
				return removeLast();
			}
			else {
				while(indexNode.getNext().getElement() != element) {
					indexNode = indexNode.getNext();
				}	
			}
			
			DLLNode<T> removalNode =  indexNode.getNext();
			indexNode.setNext((removalNode == null ? null : removalNode.getNext()));
			
			if(removalNode.getNext() != null) {
				removalNode.getNext().setPrevious((removalNode == null ? null : removalNode.getPrevious()));
			}
			return (removalNode == null ? null : removalNode.getElement());
		}
	}

	/**
	 * 
	 */
	@Override
	public T remove(int index) {
		if(index < 0 || index > size()-1) {
			throw new IndexOutOfBoundsException();
		}
		if(head == null) {
			throw new NoSuchElementException();
		}
		if(index == 0) {
			return removeFirst();
		}
		else if(index == size()-1){
			return removeLast();
		}
		else {
			DLLNode<T> indexNode = head;
			//Setting the currentIndex 1 ahead of where it's supposed to be so the index is right behind the targetNode
			int currentIndex = 1;
			while(currentIndex < index) {
				currentIndex++;
				indexNode = indexNode.getNext();
			}
			DLLNode<T> removalNode = indexNode.getNext();
			//indexNode.setNext((removalNode == null ? null : removalNode.getNext()));
			indexNode.setNext(removalNode.getNext());
			removalNode.getNext().setPrevious(indexNode);
			return removalNode.getElement();
		}
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index > size()-1) {
			throw new IndexOutOfBoundsException();
		}
		//Setting currentIndex to be 1 because we need to get to the node before the index to remove it and add our new one
				int currentIndex = 1;
				DLLNode<T> indexNode = head;
				//Empty list
				if(head == null) {
					addToFront(element);
				}
				//If the it's the first element
				if(index == 0) {
					removeFirst();
					addToFront(element);
				}
				
				else {
					while (currentIndex < index) {
						currentIndex++;
						indexNode = indexNode.getNext();
					}
					if(indexNode == null) {
						throw new IndexOutOfBoundsException();
					}
					indexNode.getNext().setElement(element);
					/*DLLNode<T> newNode = new DLLNode<T>(element);
					//Skip past the next node on to the next's next node because we're replacing the next node
					newNode.setPrevious(indexNode);
					newNode.setNext(indexNode.getNext().getNext());
					DLLNode<T> removedNode = indexNode.getNext();
					indexNode.setNext(newNode);
					
					//If the node before the index is the tail, update the tail
					if(tail == removedNode) {
						tail = newNode;
					}*/
				}
}

	@Override
	public T get(int index) {
		if(index < 0 || index > size()-1) {
			throw new IndexOutOfBoundsException();
		}
		if(head == null) {
			throw new NoSuchElementException();
		}
		if(index == 0) {
			return first();
		}
		DLLNode<T> indexNode = head;
		int currentIndex = 0;
		while(currentIndex < index) {
			indexNode = indexNode.getNext();
			currentIndex++;
		}
		if(indexNode != null) {
			return indexNode.getElement();
		}
		else {
			throw new IndexOutOfBoundsException();
		}
		
		
	}

	@Override
	public int indexOf(T element) {
		if(head == null || !(contains(element))) {
			return -1;
		}
		DLLNode<T> indexNode = head;
		int index = 0;
		while(indexNode != tail) {
			if(indexNode.getElement() == element) {
				return index;
			}
			else {
				indexNode = indexNode.getNext();
				index++;
			}
		}
		if(tail.getElement() != element) {
			return -1;
		}
		else {
			return(index++);
		}
}
		

	@Override
	public T first() {
		if(head != null) {
			return head.getElement();
		}
		throw new NoSuchElementException();
	}

	@Override
	public T last() {
		if(tail != null) {
			return tail.getElement();
		}
		throw new NoSuchElementException();
	}

	@Override
	public boolean contains(T target) {
		if(head == null) {
			return false;
		}
		Iterator<T> itr = iterator();
		while(itr.hasNext()) {
			if(itr.next() == target) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public boolean isEmpty() {
		return (head == null);
	}

	@Override
	public int size() {
		if(head == null) {
			return 0;
		}
		int size = 0;
		Iterator<T> itr = iterator();
			while(itr.hasNext()) {
				itr.next();
				size++;
			}
		return size;		
	}
	
	/**
	 * Creates an iterator for the list
	 */
	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) new CustomListIterator<T>(head, tail);
	}

	/**
	 * Creates a listIterator for the list. This includes backwards and forwards directions for the list
	 */
	@Override
	public ListIterator<T> listIterator() {
		return new CustomListIterator<T>(head, tail);
	}

	/**
	 * 
	 */
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new CustomListIterator<T>(head, tail, startingIndex, size());
	}
}
