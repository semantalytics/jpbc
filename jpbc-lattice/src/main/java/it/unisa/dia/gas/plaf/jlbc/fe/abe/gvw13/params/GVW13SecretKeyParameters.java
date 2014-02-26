package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.CipherParameters;

import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13SecretKeyParameters extends GVW13KeyParameters {

    private Circuit circuit;
    private Map<Integer, CipherParameters[]> keys;
    private Field outputField;

    public GVW13SecretKeyParameters(GVW13Parameters parameters,
                                    Circuit circuit,
                                    Map<Integer, CipherParameters[]> keys,
                                    Field outputField) {
        super(true, parameters);

        this.circuit = circuit;
        this.keys = keys;
        this.outputField = outputField;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public CipherParameters getCipherParametersAt(int index, int b0, int b1) {
        if (b0 == 0 && b1 == 0)
            return keys.get(index)[0];
        if (b0 == 0 && b1 == 1)
            return keys.get(index)[1];
        if (b0 == 1 && b1 == 0)
            return keys.get(index)[2];
        if (b0 == 1 && b1 == 1)
            return keys.get(index)[3];

        throw new IllegalStateException("Impossible!!!");
    }

    public Field getOutputField() {
        return outputField;
    }
}