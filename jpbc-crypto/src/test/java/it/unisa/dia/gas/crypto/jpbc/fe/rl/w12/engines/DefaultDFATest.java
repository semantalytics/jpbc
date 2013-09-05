package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.crypto.dfa.DefaultDFA;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
public class DefaultDFATest {

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
