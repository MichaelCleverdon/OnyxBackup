public class AtomicClock extends Clock{
    /**
     * Creates an instance of a clock with a quantum clockType and 0.00 drift, which are properties of an atomic clock
     */
    public AtomicClock() {
        //Always going to be constant parameters for the clock constructor, so no parameters on this class's constructor
        super(ClockType.quantum, 0.00);
    }

    /**
     * Printing all the useful information to the console
     */
    public void display(){
        System.out.println(super.getClockType() + ", atomic clock, time [" + time.formattedTime() + "], total drift = " +  Math.round(time.getTotalDrift()*Math.pow(10,2))/Math.pow(10,2)  + " seconds");
    }
}
