public class PQueue extends MaxHeap {
    PQueue(){
        super();
    }
    protected void enPQueue(Process p){
        //Have to do ++ because of the dummy node in position 0
        Heap[super.size+1] = p;
        maxHeapifyUp(super.size+1);
        super.size++;
    }

    protected Process dePQueue(){
        Process temp = Heap[1].clone();
        //Only < instead of <= because of the i+1 access of Heap[]
        Heap[1] = null;
        for(int i = 1; i < super.size; i++){
            Heap[i] = Heap[i+1];
            Heap[i+1] = null;
            maxHeapifyUp(i);
        }
        super.size--;
        return temp;
    }
    protected void update(int timeToIncrementPriorityLevel, int maxPriorityLevel){
        //Start at 1 because the process that already ran is out the queue
        for(int i = 1; i <= super.size; i++){
            Process temp = Heap[i];
            temp.timeNotProcessed++;
            if(temp.timeNotProcessed >= timeToIncrementPriorityLevel){
                temp.timeNotProcessed = 0;
                if(temp.currentPriorityLevel < maxPriorityLevel){
                    temp.currentPriorityLevel++;
                    maxHeapifyUp(i);
                }
            }

        }
    }

}
