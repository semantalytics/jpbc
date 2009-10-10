package it.unisa.dia.gas.jpbc.plaf.crypto.rfid.utma.strong.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.plaf.crypto.rfid.utma.weak.params.UTMAWeakMasterSecretKeyParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongMasterSecretKeyParameters extends UTMAWeakMasterSecretKeyParameters {

    public UTMAStrongMasterSecretKeyParameters(UTMAStrongPublicParameters publicParameters, Element t1, Element t2, Element t3, Element w) {
        super(publicParameters, t1, t2, t3, w);
    }

}