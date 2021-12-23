public class CuckooClock extends Clock{
    /**
     * Creates an instance of a clock with a mechanical clockType and 0.000694444 drift, which all line up with a cuckoo clock
     */
    public CuckooClock() {
        //Always going to be constant parameters for the clock constructor, so no parameters on this class's constructor
        super(ClockType.mechanical, 0.000694444);
    }

    /**
     * Printing all the useful information to the console
     */
    public void display(){
        System.out.println(super.getClockType() + ", cuckoo clock, time [" + time.formattedTime() + "], total drift = " +  Math.round(time.getTotalDrift()*Math.pow(10,2))/Math.pow(10,2)  + " seconds");
    }
}
