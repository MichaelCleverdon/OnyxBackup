import static java.lang.System.exit;

public class ProcessGenerator {
    private double probability;
    ProcessGenerator(double probability){
        this.probability = probability;
    }
    public boolean query(){
        int comparison = Double.compare(Math.random(), this.probability);
        //If the random number is below the probability
        if(comparison < 0){
            return true;
        }
        return false;
    }
    public Process getNewProcess(int currentTime, int maxProcessTime, int maxLevel){
        long tempProcessTime = Math.round(Math.random()*maxProcessTime);
        int processTime = tempProcessTime > 0 ? (int)tempProcessTime : 1;

        long tempPriorityLevel = Math.round(Math.random()*maxLevel);
        int priorityLevel = tempPriorityLevel > 0 ? (int)tempPriorityLevel : 1;

        return new Process(currentTime, processTime, priorityLevel);
    }
}
