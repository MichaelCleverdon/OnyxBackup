package fa.dfa;

import java.util.HashSet;
import java.util.Set;
import java.util.*;

import fa.State;

public class DFA implements DFAInterface {
    Set<DFAState> states = new HashSet<DFAState>();
    Set<DFAState> finalStates = new HashSet<DFAState>();
    Set<Character> alphabet = new HashSet<Character>();

    Map<String, DFAState> transitions = new HashMap<String, DFAState>();
    DFAState startState;

    /**
    * @param name the name of the state
    * Sets the start state to a new DFA State with passed in name
     */
    @Override
    public void addStartState(String name) {

        startState = new DFAState(name);
        states.add(startState);
    }

    /**
    * @param name the name of the state
    * Adds a new DFA State with passed in name to the states Set
     */
    @Override
    public void addState(String name) {
        
        DFAState dfaState = new DFAState(name);
        states.add(dfaState);
    }

    /**
    * @param name the name of the state
    * Adds a new DFA State with passed in name to the finalStates Set
     */
    @Override
    public void addFinalState(String name) {
        
        DFAState dfaState = new DFAState(name);
        states.add(dfaState);
        finalStates.add(dfaState);
    }

    /**
     * Adds a transition to the transition map
     * @param fromState the state this is coming from
     * @param onSymb the symbol to transition on
     * @param toState the state the machine will end up being in
     */
    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        //This won't cause errors if the symbol already exists
        alphabet.add(onSymb);
        //Transitions will use a state+symbol pair to designate which state to end up in (column + row = cell on the table)
        transitions.put(fromState+onSymb, findState(toState));
    }

    private DFAState findState(String stateName){
        DFAState state;
        for(Iterator<DFAState> it = states.iterator(); it.hasNext();){
            state = it.next();
            if(state.toString().equals(stateName)){
                return state;
            }
        }
        return null;
    }
    /**
     * Returns the states
     */
    @Override
    public Set<? extends State> getStates() {
        return states;
    }

    /**
     * 
     * @param stringStates Which states to be stringified
     * @return The states in a space separated list (no trailing space either)
     */
    private String statesToString(){
        String ret = "";
        for(Iterator<DFAState> it = finalStates.iterator(); it.hasNext();){
            ret += it.next().toString() + " ";
        }
        ret += finalStates.contains(startState) ? "" : startState.toString() + " ";
        for(Iterator<DFAState> it = states.iterator(); it.hasNext();){
            DFAState state = it.next();
            if(finalStates.contains(state) || state == startState)
                continue;
            ret += state.toString() + " ";
        }
        return ret.trim();
    }

    /**
     * returns the final states
     */
    @Override
    public Set<? extends State> getFinalStates() {
        return finalStates;
    }

    /**
     * returns the start state
     */
    @Override
    public State getStartState() {
        return startState;
    }

    /**
     * returns the alphabet
     */
    @Override
    public Set<Character> getABC() {
        return alphabet;
    }

    /**
     * Meat and potatoes of the DFA, this is the engine that runs the machine
     * @param s the string to check against the DFA
     * @return True if the machine accepts the string, false if it doesn't
     */
    @Override
    public boolean accepts(String s) {
        char[] characters = s.toCharArray();
        DFAState currentState = startState;
        for(int i = 0; i < characters.length; i++){
            currentState = (DFAState)getToState(currentState, characters[i]);
        }
        return finalStates.contains(currentState);
    }

    /**
     * Gets the state on a specific transition
     * @param DFAState from the state we are transitioning from
     * @param onSymb the character to transition on
     * @return The new state
     */
    @Override
    public State getToState(DFAState from, char onSymb) {
        // Big line that just gets the value from the transition table for the state it will be going to
        // and returns a new DFA state with the name of the state it goes to. Making a new DFA state doesn't cause issues
        // so this is just the easiest way to do it
        // System.out.println("Input: "+ from.toString() + onSymb);
        // System.out.println("Result: " +transitions.get(from.toString() + onSymb));
        return transitions.get(from.toString()+onSymb);
    }

    /**
     * Returns the alphabet in a space separated string
     * @return the alphabet
     */
    private String alphabetToString(){
        String s = "";
        for(Iterator<Character> it = alphabet.iterator(); it.hasNext();){
            s += it.next() + " ";
        }
        return s.trim();
    }

    /**
     * Makes the transition table in string format
     * @return the transition table in string format
     */
    private String transitionToString(){
        DFAState[] statesArray = new DFAState[states.size()];
        char[] alphabetArray = new char[alphabet.size()];
        int i = 0;
        for(Iterator<DFAState> it = finalStates.iterator(); it.hasNext();){
            statesArray[i] = it.next();
            i++;
        }
        statesArray[i] = finalStates.contains(startState) ? null : startState;
        i++;

        for(Iterator<DFAState> it = states.iterator(); it.hasNext();){
            DFAState state = it.next();
            if(finalStates.contains(state) || startState == state){
                continue;
            }
            
            statesArray[i] = state;
            i++;
        }

        i = 0;
        for(Iterator<Character> it = alphabet.iterator(); it.hasNext();){
            alphabetArray[i] = it.next();
            i++;
        }

        String ret = "";
        for(int row = 0; row <= statesArray.length; row++){
            if(row == 0)
                ret += "\t\t";
            else
                ret += "\t" + statesArray[row-1] + '\t';
            
            for(int column = 1; column <= alphabetArray.length; column++){
                
                if(row == 0){
                    ret += alphabetArray[column-1] + "\t";
                }
                else{
                    ret += transitions.get(statesArray[row-1].toString()+alphabetArray[column-1]).toString() + '\t';
                }
            }
            ret += '\n';
        }
        return ret;
        
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Q = { " + statesToString() + " }\n");
        sb.append("Sigma = { " + alphabetToString() + " }\n");
        sb.append("delta = \n" + transitionToString() + "\n");
        sb.append("q0 = " + startState.toString() + "\n");
        sb.append("F = { ");
        for(Iterator<DFAState> it = finalStates.iterator(); it.hasNext();){
            
            sb.append(it.next().toString() + " ");
        } 
        sb.append("}\n ");
        sb.append("\n");
        return sb.toString();
    }
    
}
