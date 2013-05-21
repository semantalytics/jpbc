package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GGHSW13SecretKeyParameters extends GGHSW13KeyParameters {

    private Circuit circuit;


    public GGHSW13SecretKeyParameters(GGHSW13Parameters parameters, Circuit circuit) {
        super(true, parameters);
        this.circuit = circuit;
    }

    public Circuit getCircuit() {
        return circuit;
    }
}