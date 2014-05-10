package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params;

import it.unisa.dia.gas.crypto.circuit.ArithmeticCircuit;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14SecretKeyParameters extends BNS14KeyParameters {

    private BNS14PublicKeyParameters publicKey;
    private ArithmeticCircuit circuit;
    private Element skC;

    public BNS14SecretKeyParameters(BNS14PublicKeyParameters publicKey,
                                    ArithmeticCircuit circuit,
                                    Element skC) {
        super(true, publicKey.getParameters());

        this.publicKey = publicKey;
        this.circuit = circuit;
        this.skC = skC;
    }

    public BNS14PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public ArithmeticCircuit getCircuit() {
        return circuit;
    }

    public Element getSkC() {
        return skC;
    }

    public Element getRAt(int index, int i) {
        return null;
    }
}