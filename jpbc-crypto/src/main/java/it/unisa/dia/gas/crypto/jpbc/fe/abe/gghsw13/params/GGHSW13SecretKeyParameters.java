package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import it.unisa.dia.gas.crypto.circuit.BooleanCircuit;
import it.unisa.dia.gas.jpbc.Element;

import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13SecretKeyParameters extends GGHSW13KeyParameters {

    private final BooleanCircuit circuit;
    private final Map<Integer, Element[]> keys;


    public GGHSW13SecretKeyParameters(final GGHSW13Parameters parameters,
                                      final BooleanCircuit circuit,
                                      final Map<Integer, Element[]> keys) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = keys;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }

    public Element[] getKeyElementsAt(final int index) {
        return keys.get(index);
    }
}