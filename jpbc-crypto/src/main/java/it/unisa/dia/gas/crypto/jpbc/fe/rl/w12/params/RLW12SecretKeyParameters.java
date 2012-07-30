package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12SecretKeyParameters extends RLW12KeyParameters {
    private DFA dfa;

    private Element[] kStarts;
    private Map<DFATransition, Element[]> kTransitions;
    private Map<Integer, Element[]> kEnds;

    public RLW12SecretKeyParameters(RLW12Parameters parameters,
                                    DFA dfa,
                                    Element[] kStarts,
                                    Map<DFATransition, Element[]> kTransitions,
                                    Map<Integer, Element[]> kEnds) {
        super(true, parameters);

        this.dfa = dfa;
        this.kStarts = kStarts;
        this.kTransitions = kTransitions;
        this.kEnds = kEnds;
    }

    public DFA getDfa() {
        return dfa;
    }

    public Element getkStart(int index) {
        return kStarts[index];
    }

    public Element getkTransition(DFATransition DFATransition, int index) {
        return kTransitions.get(DFATransition)[index];
    }

    public Element getkEnd(int state, int index) {
        return kEnds.get(state)[index];
    }
}