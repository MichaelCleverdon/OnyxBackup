public abstract class Clock extends Time implements TimePiece {
    public enum ClockType{
	natural, mechanical, digital, quantum
    }
    private ClockType clockType;
    protected Time time;
    /**
     * Constructor for the Clock class
     * @param pClockType a clockType that the clock is going to be set as
     * @param driftAmount (double) the drift amount the Time constructor needs to be passed in
     */
    public Clock(ClockType pClockType, double driftAmount){
        setClockType(pClockType);
        time = new Time(driftAmount);
    }

    public abstract void display();

    /**
     * getsClockType
     * @return returns the current ClockType
     */
    public ClockType getClockType(){
        ClockType copy = clockType;
        return copy;
    }

    /**
     * setsClockType
     * @param pClockType passed in clock type to set the current clockType to
     */
    public void setClockType(ClockType pClockType){
        clockType = pClockType;
    }

    /**
     * Resets current time back to starting point of midnight
     */
    public void reset(){
        time.resetToStartTime();
    }

    /**
     * Advances time one "tick"
     */
    public void tick(){
        time.incrementTime();
    }


}
