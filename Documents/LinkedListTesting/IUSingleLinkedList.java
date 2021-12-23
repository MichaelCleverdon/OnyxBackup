import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	protected SLLNode<T> head;
	protected SLLNode<T> tail;
	
	IUSingleLinkedList(){
	}
	
	class CustomIterator<T> implements Iterator<T>{
		
		protected int size;
		protected SLLNode<T> head;
		protected SLLNode<T> tail;
		protected SLLNode<T> currentNode;
		CustomIterator(SLLNode<T> pHead, SLLNode<T> pTail){
			head = pHead;
			currentNode = null;
			tail = pTail;
		}
		
		@Override
		public boolean hasNext() {
			if(head == null) {
				return false;
			}
			return((currentNode == null ? head != null : currentNode.getNext() != null));
		}

		@Override
		public T next() {
			if(head == null) {
				throw new NoSuchElementException();
			}
			if(currentNode == null) {
				currentNode = head;
				return currentNode.getElement();
			}
			if(hasNext()) {
				currentNode = currentNode.getNext();
				return currentNode.getElement();
			}
			else {
				throw new NoSuchElementException();
			}
		}
		
		@Override
		public void remove() {
			//Empty list or already removed the element
			if(head == null || currentNode == null ) {
				throw new IllegalStateException();
			}
			
			//If the entry is already deleted
			if(currentNode.getElement() == null)
			{
				throw new IllegalStateException();
			}
			
			//If the current node is the first element
			SLLNode<T> nodeBeforeRemoval = head;
			if(currentNode == head) {
				nodeBeforeRemoval = currentNode.getNext();
				head = nodeBeforeRemoval;
				
				//If the list is 1 element long
				if(currentNode == tail) {
					tail = nodeBeforeRemoval;
				}
				currentNode.setElement(null);
				return;
			}
			
			//Loop through the entire list to get to the element before the current node 
			while(nodeBeforeRemoval.getNext() != currentNode) {
				nodeBeforeRemoval = nodeBeforeRemoval.getNext();
			}
			nodeBeforeRemoval.setNext(currentNode.getNext());
			if(tail == currentNode) {
				tail = nodeBeforeRemoval;
			}
			currentNode.setElement(null);
		}
	}

	@Override
	public void addToFront(T element) {
		SLLNode<T> newNode = new SLLNode<T>(element);
		//If the list is empty
		if(head == null) {
			head = newNode;
			tail = newNode;
		}
		//List not empty
		else {
			//Set the newNode to point to the current head
			newNode.setNext(head);
			//Update head to the new node
			head = newNode;
		}		
	}

	@Override
	public void addToRear(T element) {
		SLLNode<T> index = head;
		SLLNode<T> newNode = new SLLNode<T>(element);
		//List is empty
		if(index == null) {
			head = newNode;
			tail = newNode;
			return;
		}
		
		tail.setNext(newNode);
		tail = newNode;
	}

	@Override
	public void add(T element) {
		addToRear(element);		
	}

	@Override
	public void addAfter(T element, T target) {
		if(head == null || element == null) {
			throw new NoSuchElementException();
		}
		SLLNode<T> index = head;
		if(!(contains(target))) {
			throw new NoSuchElementException();
		}
		
		while(index.getElement() != target) {
			index = index.getNext();
		}
		SLLNode<T> newNode = new SLLNode<T>(element);
		newNode.setNext(index.getNext());
		index.setNext(newNode);
		if(tail == index) {
			tail = newNode;
		}
	}

	@Override
	public void add(int index, T element) {
		//Setting currentIndex to be 1 because we need to get to the node before the index to remove it and add our new one
		int currentIndex = 1;
		SLLNode<T> indexNode = head;
		//Empty list
		if(index == 0) {
			addToFront(element);
			return;
		}
		if(index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		else {
			
			while (currentIndex < index) {
				currentIndex++;
				indexNode = indexNode.getNext();
			}
			if(indexNode == null && index != 0) {
				throw new IndexOutOfBoundsException();
			}
			SLLNode<T> newNode = new SLLNode<T>(element);
			newNode.setNext(indexNode.getNext());
			indexNode.setNext(newNode);
			
			//If the node before the index is the tail, update the tail
			if(tail == indexNode) {
				tail = newNode;
			}
		}
	}

	@Override
	public T removeFirst() {
		if(head == null) {
			throw new NoSuchElementException();
		}

		SLLNode<T> removedNode = head;
		if(head == tail) {
			tail = removedNode.getNext();
		}
		head = removedNode.getNext();
		return removedNode.getElement();
	}

	@Override
	public T removeLast() {
		SLLNode<T> indexNode = head;
		//Empty list
		if(indexNode == null) {
			throw new NoSuchElementException();
		}
		if(indexNode != tail) {		
			while(indexNode.getNext() != tail) {
				indexNode = indexNode.getNext();
			}
		}
		if(head == tail) {
			indexNode = null;
			head = indexNode;
		}
		T removedElement = tail.getElement();
		tail = indexNode;
		if(tail != null) {
			tail.setNext(null);
		}
		
		return(removedElement);
		
	}

	@Override
	public T remove(T element) {
		if(head == null || element == null) {
			throw new NoSuchElementException();
		}
		else {
			SLLNode<T> indexNode = head;
			if(!(contains(element))) {
				throw new NoSuchElementException();
			}
			if(head.getElement() != element) {
				while(indexNode.getNext().getElement() != element) {
					indexNode = indexNode.getNext();
				}	
			}
			
			SLLNode<T> removalNode = (head.getElement() == element ? head : indexNode.getNext());
			if(removalNode == head){
				head = indexNode.getNext();
			}
			if(tail == removalNode) {
				tail = indexNode.getNext();
			}
			indexNode.setNext((removalNode == null ? null : removalNode.getNext()));
			
			return (removalNode == null ? null : removalNode.getElement());
		}
	}

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
		else {
			SLLNode<T> indexNode = head;
			//Setting the currentIndex 1 ahead of where it's supposed to be so the index is right behind the targetNode
			int currentIndex = 1;
			while(currentIndex < index) {
				currentIndex++;
				indexNode = indexNode.getNext();
			}
			SLLNode<T> removalNode = indexNode.getNext();
			indexNode.setNext((removalNode == null ? null : removalNode.getNext()));
			if(tail == removalNode) {
				tail = indexNode;
			}
			return (removalNode == null ? null : removalNode.getElement());
		}
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index > size()-1) {
			throw new IndexOutOfBoundsException();
		}
		//Setting currentIndex to be 1 because we need to get to the node before the index to remove it and add our new one
				int currentIndex = 1;
				SLLNode<T> indexNode = head;
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
					SLLNode<T> newNode = new SLLNode<T>(element);
					//Skip past the next node on to the next's next node because we're replacing the next node
					newNode.setNext(indexNode.getNext().getNext());
					SLLNode<T> removedNode = indexNode.getNext();
					indexNode.setNext(newNode);
					
					//If the node before the index is the tail, update the tail
					if(tail == removedNode) {
						tail = newNode;
					}
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
		SLLNode<T> indexNode = head;
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
		SLLNode<T> indexNode = head;
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
	@Override
	public Iterator<T> iterator() {
		return new CustomIterator(head, tail);
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}
