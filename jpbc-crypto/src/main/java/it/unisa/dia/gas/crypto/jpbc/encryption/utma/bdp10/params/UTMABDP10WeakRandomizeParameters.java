package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10WeakRandomizeParameters extends UTMABDP10WeakKeyParameters {

    public UTMABDP10WeakRandomizeParameters(UTMABDP10WeakPublicParameters publicParameters) {
        super(false, publicParameters);
    }

}
