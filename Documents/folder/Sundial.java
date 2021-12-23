public class Sundial extends Clock{
    /**
     * Creates an instance of a clock with a natural clockType and 0.00 drift, which are properties of a sundial
     */
    public Sundial() {
        //Always going to be constant parameters for the clock constructor, so no parameters on this class's constructor
        super(ClockType.natural, 0.00);
    }

    /**
     * Printing all the useful information to the console
     */
    public void display(){
        //The Math.round is used to get 2 decimals out of the total drift, this is because the total drift is a long number that needs to be rounded
        System.out.println(super.getClockType() + ", sun dial, time[" + time.formattedTime() + "], total drift = " + Math.round(time.getTotalDrift()*Math.pow(10,2))/Math.pow(10,2) + " seconds");
    }
}
