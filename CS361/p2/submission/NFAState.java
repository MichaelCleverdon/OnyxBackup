package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.State;

public class NFAState extends State{
    private HashMap<Character, Set<NFAState>> delta;
    private boolean isFinal;

    /**
     * Constructor 1: Default isFinal = false
     * @param name Name of the state
     */
    public NFAState(String name){
        this.name = name;
        delta = new LinkedHashMap<Character, Set<NFAState>>();
        isFinal = false;
    }

    /**
     * Constructor 2: isFinal able to be set
     * @param name Name of state
     * @param isFinal If it is final
     */
    public NFAState(String name, boolean isFinal){
        this.name = name;
        delta = new LinkedHashMap<Character, Set<NFAState>>();
        this.isFinal = isFinal;
    }

    /**
     * encapsulation for isFinal
     * @return isFinal == true
     */
    public boolean isFinal(){
        return isFinal;
    }

    /**
     * Returns the name of the state
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Adds a transition to the current state
     * @param symb symbol to transition on
     * @param toState state the machine goes to after the transition
     */
    public void addTransition(char symb, NFAState toStates){
        Set<NFAState> states = delta.get(symb); //Get the states
        if(states != null){
            states.add(toStates); //Add the toState to the set
        }
        else{
            Set<NFAState> s = new LinkedHashSet<>();
            s.add(toStates);
            delta.put(symb, s);
        }
    }
    
    /**
     * Transition from a -> b
     * @param symb symbol to transition on
     * @return the set of states the machine can be in now
     */
    public Set<NFAState> getTo(char symb){
        Set<NFAState> ret = delta.get(symb);
        if(ret == null){
            return new LinkedHashSet<>();
        }
        return ret;
    }
}
