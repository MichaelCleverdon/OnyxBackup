package re;
import fa.State;
import fa.dfa.DFA;
import fa.nfa.NFA;
import fa.nfa.NFAState;

public class RE implements REInterface {
    private String regex; //Input
    private int countOfStates = 0;

    /**
     * Constructor
     * @param regex regex Input
     */
    public RE(String regex){
        this.regex = regex;
    }

    @Override
    public NFA getNFA() {
        return regex();
    }

    // Recursive descent algorithm 

    /**
     * Parses the regex from the string passed into the constructor
     * @return The created NFA
     */
    private NFA regex(){
        NFA term = term(); //Parses a term
        if(hasMoreTerms() && top() == '|'){
            eat('|');
            return union(term, regex()); //Return the union of this term + any other ones that come after the next |
        }
        return term;
    }

    /**
     * Eats the next character in the sequence
     * @param c character to eat
     */
    private void eat(char c){
        if(top() == c){
            regex = regex.substring(1);
        }else{
            throw new RuntimeException("Expected: " + c + "; got: " + top());
        }
    }

    /**
     * Checks if the sequence has more terms
     * @return true if the sequence has more terms
     */
    private boolean hasMoreTerms(){
        return regex.length() > 0;
    }   

    /**
     * Returns the next character in the sequence
     * @return the next character
     */
    private char nextTerm(){
        char c = top();
        eat(c);
        return c;
    }

    /**
     * Peeks at the next element of the sequence, but doesn't consume it or move the cursor
     * @return A copy of the next element in the sequence
     */
    private char top(){
        return regex.charAt(0);
    }
    
    /**
     * 
     * @param nfa1
     * @param nfa2
     * @return
     */
    public NFA union(NFA nfa1, NFA nfa2){
        NFAState nfaState = (NFAState) nfa1.getStartState();
        NFAState nfa2State = (NFAState) nfa2.getStartState();

        //Append the nfa2 states and alphabets to the nfa1 info
        nfa1.addNFAStates(nfa2.getStates());
        nfa1.addAbc(nfa2.getABC());

        //Add start state and final state to the first nfa's corresponding sections
        String startState = String.valueOf(countOfStates++);
        nfa1.addStartState(startState);
        String finalState = String.valueOf(countOfStates++);
        nfa1.addFinalState(finalState);

        //Add epsilon transitions from the new start state to the old start states
        nfa1.addTransition(startState, 'e', nfaState.getName());
        nfa1.addTransition(startState, 'e', nfa2State.getName());
        for(State s:nfa2.getFinalStates()){
            NFAState state = (NFAState)s;
            state.setNonFinal();
            nfa1.addTransition(state.getName(), 'e', finalState);
        }
        return nfa1;
    }

    /**
     * Check that the sequence has not hit the edge of a term or the EOF
     * @return NFA of the term
     */
    private NFA term(){
        NFA factor = new NFA();

        factor.addStartState(String.valueOf(countOfStates++));
        String finalState = String.valueOf(countOfStates);
        factor.addFinalState(String.valueOf(countOfStates++));
        factor.addTransition(factor.getStartState().getName(), 'e', finalState);

        while(hasMoreTerms() && top() != ')' && top() != '|'){ //While not end of sections
            NFA nextFactor = factor();
            factor = concat(factor, nextFactor); //Concat the factors together
        }

        return factor;
    }

    /**
     * Returns the concatenation of 2 NFAs
     * @param nfa1
     * @param nfa2
     * @return concatenated NFA
     */
    private NFA concat(NFA nfa1, NFA nfa2){
        nfa1.addAbc(nfa2.getABC());
        for(State s : nfa1.getFinalStates()){
            NFAState state = (NFAState)s;
            state.setNonFinal();
            state.addTransition('e', (NFAState)nfa2.getStartState());
        }
        nfa1.addNFAStates(nfa2.getStates());
        return nfa1;
    }

    /**
     * Parse the base factor and then any number of stars in the string
     * @return the NFA
     */
    public NFA factor(){
        NFA base = base();
        while(hasMoreTerms() && top() == '*'){
            eat('*');
            base = repeat(base);
        }
        return base;
    }

    /**
     * Returns the NFA if a pipe '|' symbol is found
     * @param base
     * @return NFA
     */
    public NFA repeat(NFA base){
        NFAState nfaState = (NFAState) base.getStartState();
        for(State nfa : base.getFinalStates()){
            base.addTransition(nfa.getName(), 'e', nfaState.getName());
        }

        String state = String.valueOf(countOfStates++);
        base.addStartState(state);
        base.addFinalState(state);
        base.addTransition(state, 'e', nfaState.getName());
        return base;
    }

    /**
     * Creates a base NFA from either a group of par
     * @return
     */
    private NFA base(){
        if(top() == '('){
            eat('(');
            NFA nfa = regex();
            eat(')');
            return nfa;
        }
        else{
            return primitive(nextTerm());
        }
    }

    public NFA primitive(char c){
        NFA nfa = new NFA();

        String startState = String.valueOf(countOfStates++);
        nfa.addStartState(startState);

        String finalState = String.valueOf(countOfStates++);
        nfa.addFinalState(finalState);

        nfa.addTransition(startState, c, finalState);

        return nfa;
    }
}
