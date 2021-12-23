public class Process<T> implements Comparable<Process>{
    int arrivalTime, currentPriorityLevel, timeNotProcessed, timeRemaining;

    Process(int currentTime, int processTime, int priorityLevel){
        arrivalTime = currentTime;
        timeRemaining = processTime;
        currentPriorityLevel = priorityLevel;
        timeNotProcessed = 0;
    }

    private Process(int currentTime, int processTime, int processLevel, int timeNotProcessed){
        arrivalTime = currentTime;
        timeRemaining = processTime;
        currentPriorityLevel = processLevel;
        this.timeNotProcessed = timeNotProcessed;
    }

    public int getTimeRemaining(){
        return timeRemaining;
    }
    public boolean finish(){
        return timeRemaining == 0;
    }
    public void reduceTimeRemaining(){
        timeRemaining--;
    }
    public void resetTimeNotProcessed(){
        timeNotProcessed = 0;
    }
    public int getPriority(){
        return currentPriorityLevel;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }

    public int compareTo(Process p) {
        if(currentPriorityLevel < p.currentPriorityLevel){
            return -1;
        }
        else if(currentPriorityLevel > p.currentPriorityLevel){
            return 1;
        }
        //They're equal
        else{
            if(arrivalTime > p.arrivalTime){
                return -1;
            }
            else{
                return 1;
            }
        }
    }

    @Override
    protected Process clone(){
        return new Process(this.arrivalTime, this.timeRemaining, this.currentPriorityLevel, this.timeNotProcessed);
    }
}

