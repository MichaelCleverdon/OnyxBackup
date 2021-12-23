public class WristWatch extends Clock{
    /**
     * Creates an instance of a clock with a digital clockType and 0.000034722 drift, which are properties of a wristwatch
     */
    public WristWatch() {
        //Always going to be constant parameters for the clock constructor, so no parameters on this class's constructor
        super(ClockType.digital, 0.000034722);
    }

    /**
     * Printing all the useful information to the console
     */
    public void display(){
        System.out.println(super.getClockType() + ", wrist watch, time [" + time.formattedTime() + "], total drift = " +  Math.round(time.getTotalDrift()*Math.pow(10,2))/Math.pow(10,2)  + " seconds");
    }
}
