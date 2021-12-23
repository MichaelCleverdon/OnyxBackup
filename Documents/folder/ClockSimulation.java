import java.util.Iterator;

public class ClockSimulation {
    //Set the values for all the seconds we will be advancing by
    private static long daySeconds = 86400;
    private static long weekSeconds = 604800;
    private static long monthSeconds = 2592000;
    private static long yearSeconds = 31536000;
    private static Bag<Clock> bag;

    public static void main(String args[]){
	//Create a new bag
        bag = new Bag<>();

        //Add all clocks to the bag
        bag.add(new Sundial());
        bag.add(new CuckooClock());
        bag.add(new GrandfatherClock());
        bag.add(new WristWatch());
        bag.add(new AtomicClock());

        //Start of the display
        System.out.println("Times before start: ");
        tickAllClocks(0);

        System.out.println("Times after one day: ");
        tickAllClocks(daySeconds);

        System.out.println("Times after one week: ");
        tickAllClocks(weekSeconds);

        System.out.println("Times after one month: ");
        tickAllClocks(monthSeconds);

        System.out.println("Times after one year: ");
        tickAllClocks(yearSeconds);

    }

    /**
     * Ticks all clocks by the amount passed in
     * @param amountToTick amount of seconds to tick all the clocks
     */
    private static void tickAllClocks(long amountToTick){
	
	//Ticks all clocks in the bag
        for(Iterator<Clock> i = bag.iterator(); i.hasNext(); ){
	    //Each bag instance will be a clock, need to store that value
            Clock c = i.next();
            for(int j = 0; j < amountToTick; j++){
                //Tick each clock for the amount passed in
                c.tick();
            }
            //After ticking each clock, display the final results
            c.display();
        }
        //Reset all clocks after they've been looped through and displayed. After that add one space at the end of each block
        resetAllClocks();
        System.out.println();
    }

    /**
     * Resets all Clocks in bag
     */
    private static void resetAllClocks(){
	//Loop through all of the clocks in the bag
        for(Iterator<Clock> i = bag.iterator(); i.hasNext(); ){
            Clock c = i.next();
            //Reset each clock
            c.reset();
        }
    }
}


