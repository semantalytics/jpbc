package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface DFA {

    int getInitialState();

    DFATransition getTransition(int from, Character reading);

    boolean isFinalState(int state);

    int getNumTransitions();

    DFATransition getTransitionAt(int index);

    int getNumStates();

    int getNumFinalStates();

    int getFinalStateAt(int index);
}
