import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUArrayList<T> implements IndexedUnsortedList<T>{
	private int rear;
	private int size;
	private T[] genericArray;
	private T[] copyArray;
	
	IUArrayList(){
		size = 100;
		genericArray = (T[]) new Object[size];
		rear = 0;
	}
	@Override
	public void addToFront(T element) {
		// TODO Auto-generated method stub
		T[] copyArray = copyArray(genericArray);
		genericArray[0] = element;
		for(int i = 0; i < rear; i++) {
			genericArray[i+1] = copyArray[i];
		}
		rear++;
	}

	@Override
	public void addToRear(T element) {
		// TODO Auto-generated method stub
		genericArray[rear] = element;
		rear++;
	}

	@Override
	public void add(T element) {
		// TODO Auto-generated method stub
		genericArray[rear] = element;
		rear++;
	}

	@Override
	public void addAfter(T element, T target) {
		// TODO Auto-generated method stub
		for(int i = 0; i < rear; i++) {
			if(genericArray[i] == target) {
				T[] copyArray = copyArray(genericArray);
				genericArray[i+1] = element;
				rear++;
				for(int k = i+2; k < rear; k++) {
					genericArray[k] = copyArray[k-1];
				}	
				return;
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public void add(int index, T element) {
		if(index > rear || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		T[] copyArray = copyArray(genericArray);
		genericArray[index] = element;
		rear++;
		for(int i = index; i < rear; i++) {
			genericArray[i+1] = copyArray[i];
		}
	}

	@Override
	public T removeFirst() {
		if(rear == 0) {
			throw new NoSuchElementException();
		}
		T[] copyArray = copyArray(genericArray);
		T removedElement = genericArray[0];
		for(int i = 0; i < rear; i++) {
			genericArray[i] = copyArray[i+1];
		}
		rear--;
		return removedElement;
	}

	@Override
	public T removeLast() {
		if(rear == 0) {
			throw new NoSuchElementException();
		}
		T removedElement = genericArray[rear-1];
		genericArray[rear-1] = null;
		rear--;
		return removedElement;
	}

	@Override
	public T remove(T element) {
		T[] copyArray = copyArray(genericArray);
		for(int i = 0; i < rear; i++) {
			if(genericArray[i] == element) {
				T removedElement = genericArray[i];
				for(int k = i; k < rear; k++) {
					genericArray[k] = copyArray[k+1];
				}
				rear--;
				return removedElement;
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public T remove(int index) {
		if(index >= rear || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		T[] copyArray = copyArray(genericArray);
		T removedElement = genericArray[index];
		for(int i = index; i < rear; i++) {
			genericArray[i] = copyArray[i+1];
			genericArray[i+1] = null;
		}
		
		rear--;
		return removedElement;
	}

	@Override
	public void set(int index, T element) {
		if(index >= rear || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		genericArray[index] = element;
	}

	@Override
	public T get(int index) {
		if(index >= rear || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return genericArray[index];
	}

	@Override
	public int indexOf(T element) {
		for(int i = 0; i < rear; i++) {
			if(genericArray[i] == element) {
				return i;	
			}
		}
		return -1;
	}

	@Override
	public T first() {
		if(rear == 0) {
			throw new NoSuchElementException();
		}
		return genericArray[0];
	}

	@Override
	public T last() {
		if(rear == 0) {
			throw new NoSuchElementException();
		}
		return genericArray[rear-1];
	}

	@Override
	public boolean contains(T target) {
		for(int i = 0; i < rear; i++) {
			if(genericArray[i] == target) {
				return true;	
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (rear==0);
	}

	@Override
	public int size() {
		return rear;
	}

	@Override
	public Iterator<T> iterator() {
		return new CustomIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
	private T[] copyArray(T[] arrayToBeCopied) {
		copyArray = (T[]) new Object[size];
		copyArray = arrayToBeCopied.clone();
		return copyArray;
	}
	
	public T[] getCopyArray() {
		return copyArray.clone();
	}

	class CustomIterator<T> implements Iterator<T>{
		
		protected int size;
		protected int index;
		CustomIterator(){
			size = rear;
			index = -1;
		}
		CustomIterator(int pIndex){
			if(index >= size || index < 0) {
				throw new IndexOutOfBoundsException();
			}
			size = rear;
			index = pIndex;
		}
		@Override
		public boolean hasNext() {
			if(size > 0) {
			return(index < rear-1);
			}
			else {
				return false;
			}
		}

		@Override
		public T next() {
			if(hasNext()) {
				index++;
				@SuppressWarnings("unchecked")
				T ret = (T) genericArray[index];
				return ret;				
			}
			else {
				throw new NoSuchElementException();
			}
		}
		
		@Override
		public void remove() {
			copyArray(genericArray);
			if(size == 0 || (index >= size && index > rear) || index == -1) {
				throw new IllegalStateException();
			}
			if(genericArray[index] == null) {
				throw new IllegalStateException();
			}
			genericArray[index] = null;
			
			size--;
		}
	}
}
