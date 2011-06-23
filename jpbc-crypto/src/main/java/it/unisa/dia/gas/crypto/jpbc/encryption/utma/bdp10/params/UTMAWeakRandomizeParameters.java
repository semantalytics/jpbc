package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakRandomizeParameters extends UTMAWeakKeyParameters {

    public UTMAWeakRandomizeParameters(UTMAWeakPublicParameters publicParameters) {
        super(false, publicParameters);
    }

}
