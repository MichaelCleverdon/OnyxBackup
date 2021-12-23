package fa.nfa;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;

/**
 * @author Michael Cleverdon
 */
public class NFA implements NFAInterface{
    private Set<NFAState> states;
    private NFAState startState;
    private Set<Character> alphabet;

    public NFA(){
        states = new LinkedHashSet<NFAState>(); //States
        alphabet = new LinkedHashSet<Character>(); //Alphabet
    }

    @Override
    public void addStartState(String name) {
        startState = new NFAState(name);
        states.add(startState);   
    }

    // private NFAState checkIfExists(String name){
    //     NFAState ret = null;
    //     for(NFAState s : states){
    //         if(s.getName().equals(name)){
    //             ret=s;
    //             break;
    //         }
    //     }
    //     return ret;
    // }

    @Override
    public void addState(String name) {
        states.add(new NFAState(name));
    }

    @Override
    public void addFinalState(String name) {
        NFAState s = new NFAState(name, true);
        states.add(s);
        
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
       alphabet.add(onSymb); //Doesn't matter if the onSymb already is in alphabet since it's a set
       findState(fromState).addTransition(onSymb, findState(toState)); //Add the transition to the state
        
    }

    private NFAState findState(String stateName){
        for(NFAState state : states){
            if(state.toString().equals(stateName)){
                return state;
            }
        }
        return null;
    }
    @Override
    public Set<? extends State> getStates() {
        return states;
    }

    @Override
    public Set<? extends State> getFinalStates() {
        LinkedHashSet<NFAState> finalStates = new LinkedHashSet<>();
        for(NFAState state : states) {
            if (state.isFinal()) {
                finalStates.add(state);
            }
        }
        return finalStates;
    }

    @Override
    public State getStartState() {
        return startState;
    }

    @Override
    public Set<Character> getABC() {
        return alphabet;
    }

    /**
     * Checks if the state has a final state in it
     * @param states Set of states to check
     * @return states.contains(finalState) == true
     */
    private boolean hasFinalState(Set<NFAState> states){
        for(NFAState state : states){
            if(state.isFinal()){
                return true;
            }
        }
        return false;
    }

    @Override
    public DFA getDFA() {
        DFA dfa = new DFA();
        Map<Set<NFAState>, String> visitedStates = new LinkedHashMap<Set<NFAState>, String>();
        //Eclose the start state
        Set<NFAState> startStates = eClosure(startState);
        visitedStates.put(startStates, startStates.toString());

        LinkedList<Set<NFAState>> queue = new LinkedList<>(); //Gotta use a queue for the BFS
        queue.add(startStates); //Start with the e closed start state
        dfa.addStartState(visitedStates.get(startStates));
        while(!queue.isEmpty()){ //Loop through the queue
            states = queue.poll(); //Poll makes the linked list work like a queue
            for(char c : alphabet){
                LinkedHashSet<NFAState> tempList = new LinkedHashSet<>();
                for(NFAState st : states){
                    tempList.addAll(st.getTo(c)); //Add all of the states on the one transition
                }
                LinkedHashSet<NFAState> eClosureList = new LinkedHashSet<>();
                for(NFAState st : tempList){
                    eClosureList.addAll(eClosure(st));
                }
                if(!visitedStates.containsKey(eClosureList)){ //Add the eClosure list to the visited states if it isn't already there
                    visitedStates.put(eClosureList, eClosureList.toString());
                    queue.add(eClosureList); //Gotta search the new blood

                    //Add the states from the eclosureList
                    if(hasFinalState(eClosureList)){
                        dfa.addFinalState(visitedStates.get(eClosureList));
                    }else{
                        dfa.addState(visitedStates.get(eClosureList));
                    }
                }
                //Don't add the e transition
                if(c == 'e')
                    continue;
                dfa.addTransition(visitedStates.get(states), c, visitedStates.get(eClosureList));
            }
        }
        return dfa;
    }

    /**
     * Transitions the machine through a character
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    /**
     * Creates an eClosure through recursion
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        LinkedHashSet<NFAState> visitedStates = new LinkedHashSet<>();
        return dfs(visitedStates, s);
    }

    /**
     * Does a depth first search of the epsilon transitions from the state provided and returns them
     * @param list used in the recursive call for states that have already been visited
     * @param state State to traverse
     * @return All of the states able to be reached by the state provided by epsilon transition
     */
    private Set<NFAState> dfs(LinkedHashSet<NFAState> list, NFAState state){
        LinkedHashSet<NFAState> alreadyVisitedStates = list;
        LinkedHashSet<NFAState> eClosureSet = new LinkedHashSet<>();

        eClosureSet.add(state);
        //Try to get to another state by empty transition
        if(!alreadyVisitedStates.contains(state) && !state.getTo('e').isEmpty()){
            alreadyVisitedStates.add(state);
            for(NFAState nfa : state.getTo('e'))
                //Add the states to the eclosure set
                eClosureSet.addAll(dfs(alreadyVisitedStates, nfa));
        }
        return eClosureSet;
    }
    
}
