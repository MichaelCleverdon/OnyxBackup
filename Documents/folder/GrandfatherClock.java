public class GrandfatherClock extends Clock{
    /**
     * Creates an instance of a clock with a mechanical clockType and 0.000347222 drift, which are all properties of a grandfather clock
     */
    public GrandfatherClock() {
        //Always going to be constant parameters for the clock constructor, so no parameters on this class's constructor
        super(ClockType.mechanical, 0.000347222);
    }

    /**
     * Printing all the useful information to the console
     */
    public void display(){
        System.out.println(super.getClockType() + ", grandfather clock, time [" + time.formattedTime() + "], total drift = " +  Math.round(time.getTotalDrift()*Math.pow(10,2))/Math.pow(10,2)  + " seconds");
    }
}
