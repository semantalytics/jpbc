package it.unisa.dia.gas.crypto.dfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DefaultDFA implements DFA {

    private final int numStates;
    private final Alphabet alphabet;

    private final Map<String, Transition> index;
    private final List<Transition> transitions;
    private final List<Integer> finalStates;


    public DefaultDFA(final int numStates) {
        this(numStates, null);
    }

    public DefaultDFA(final int numStates, final Alphabet alphabet) {

        this.numStates = numStates;
        this.alphabet = alphabet;
        this.transitions = new ArrayList<Transition>();
        this.index = new HashMap<String, Transition>();
        this.finalStates = new ArrayList<Integer>();
    }

    public void addFinalState(final int state) {
        finalStates.add(state);
    }

    public void addTransition(final int from, final Character reading, final int to) {
        if (alphabet != null && alphabet.getIndex(reading) == -1) {
            return;
        }

        final Transition transition = new DefaultTransition(from, reading, to);

        this.transitions.add(transition);
        this.index.put(String.format("%d_%s", from, reading), transition);
    }

    public void addTransition(final int from, final Character start, final Character end, final int to) {
        for (Character c = start; c <= end; c++) {
            addTransition(from, c, to);
        }
    }

    public Transition getTransitionAt(final int index) {
        return transitions.get(index);
    }

    public int getNumTransitions() {
        return transitions.size();
    }

    public boolean accept(final String w) {
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

    public Transition getTransition(final int from, final Character reading) {
        return index.get(String.format("%d_%s", from, reading));
    }

    public boolean isFinalState(final int state) {
        for (final Integer finalState : finalStates) {
            if (finalState == state) {
                return true;
            }
        }
        return false;
    }

    public static class DefaultTransition implements Transition {

        private final int from;
        private final Character reading;
        private final int to;

        public DefaultTransition(final int from, final Character reading, final int to) {

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

    public static class DefaultAlphabet implements Alphabet {

        private int size;
        private final Map<Character, Integer> map;

        public DefaultAlphabet() {
            this.size = 0;
            this.map = new HashMap<Character, Integer>();
        }

        public void addLetter(final Character... characters) {
            for (final Character character : characters) {
                map.put(character, size++);
            }
        }

        public int getSize() {
            return size;
        }

        public int getIndex(final Character character) {
            if (map.containsKey(character)) {
                return map.get(character);
            } else {
                return -1;
            }
        }
    }
}
