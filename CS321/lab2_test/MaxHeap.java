public class MaxHeap <T> {
    protected Process[] Heap;
    //Also index of last item in the list
    protected int size;
    protected int maxsize = 300;

    public MaxHeap() {
        this.size = 0;
        Heap = new Process[this.maxsize++];
        //Dummy element to make the calculations easier on me
        Heap[0] = new Process(0,0,0);
    }

    private int parent(int position){
        return position/2;
    }

    //Store the equations from lecture for children in here
    private int leftChild(int position){
        return (2*position);
    }
    private int rightChild(int position){
        return (2*position + 1);
    }

    private boolean isLeaf(int position){
        //If the current position is on the last layer of the tree
        if(position >= (size/2) && position <= size){
            return true;
        }
        return false;
    }

    //Swaps values of 2 nodes
    private void swap(int firstPosition, int secondPosition){
        //Technically need 3 nodes to swap values of 2 nodes
        Process thirdNode;
        thirdNode = Heap[firstPosition];
        Heap[firstPosition] = Heap[secondPosition];
        Heap[secondPosition] = thirdNode;
    }

    protected boolean isEmpty(){
        return size == 0;
    }

    protected void maxHeapifyUp(int position){
        //Ran all the way up the tree
        if(position == 1){
            return;
        }

        //If the current parent is less important than the current node, lift it up
        if(Heap[parent(position)].compareTo(Heap[position]) < 0){
            swap(parent(position), position);
            maxHeapifyUp(parent(position));
        }
    }
}
