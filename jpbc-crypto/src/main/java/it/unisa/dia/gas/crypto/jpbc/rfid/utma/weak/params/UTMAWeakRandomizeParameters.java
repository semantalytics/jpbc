package it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakRandomizeParameters extends UTMAWeakKeyParameters {

    public UTMAWeakRandomizeParameters(UTMAWeakPublicParameters publicParameters) {
        super(false, publicParameters);
    }

}
