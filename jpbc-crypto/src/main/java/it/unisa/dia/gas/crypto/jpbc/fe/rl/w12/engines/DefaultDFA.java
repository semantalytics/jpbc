package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.DFA;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.DFAAlphabet;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.DFATransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DefaultDFA implements DFA {

    private int numStates;
    private DFAAlphabet alphabet;

    private Map<String, DFATransition> index;
    private List<DFATransition> DFATransitions;
    private List<Integer> finalStates;


    public DefaultDFA(int numStates) {
        this(numStates, null);
    }

    public DefaultDFA(int numStates, DFAAlphabet alphabet) {
        this.numStates = numStates;
        this.alphabet = alphabet;

        this.DFATransitions = new ArrayList<DFATransition>();
        this.index = new HashMap<String, DFATransition>();
        this.finalStates = new ArrayList<Integer>();
    }



    public void addFinalState(int state) {
        finalStates.add(state);
    }

    public void addTransition(int from, Character reading, int to) {
        if (alphabet != null && alphabet.getIndex(reading) == -1)
            return;

        DFATransition DFATransition = new DefaultDFATransition(from, reading, to);

        this.DFATransitions.add(DFATransition);
        this.index.put(String.format("%d_%s", from, reading), DFATransition);
    }

    public void addTransition(int from, Character start, Character end, int to) {
        for (Character c = start; c <= end; c++)
            addTransition(from, c, to);
    }


    public DFATransition getTransitionAt(int index) {
        return DFATransitions.get(index);
    }

    public int getNumTransitions() {
        return DFATransitions.size();
    }

    public boolean accept(String w) {
        int currentState = 0; // Initial state

        for (int i = 0; i < w.length(); i++) {
            currentState = getTransition(currentState, w.charAt(i)).getTo();
        }

        return finalStates.contains(currentState);
    }

    public int getNumStates() {
        return numStates;
    }

    public int getNumFinalStates() {
        return finalStates.size();
    }

    public int getFinalStateAt(int index) {
        return finalStates.get(index);
    }

    public int getInitialState() {
        return 0;
    }

    public DFATransition getTransition(int from, Character reading) {
        return index.get(String.format("%d_%s", from, reading));
    }

    public boolean isFinalState(int state) {
        for (Integer finalState : finalStates) {
            if (finalState == state)
                return true;
        }
        return false;
    }


    public static class DefaultDFATransition implements DFATransition {
        private int from;
        private Character reading;
        private int to;

        public DefaultDFATransition(int from, Character reading, int to) {
            this.from = from;
            this.reading = reading;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public Character getReading() {
            return reading;
        }

        public int getTo() {
            return to;
        }

    }


    public static void main(String[] args) {
        DefaultDFA dfa = new DefaultDFA(2);

        dfa.addFinalState(0);

        dfa.addTransition(0, '0', 1);
        dfa.addTransition(0, '1', 0);
        dfa.addTransition(1, '0', 0);
        dfa.addTransition(1, '1', 1);

        boolean accepted = dfa.accept("01111000");

        System.out.println("accepted = " + accepted);
    }

}
