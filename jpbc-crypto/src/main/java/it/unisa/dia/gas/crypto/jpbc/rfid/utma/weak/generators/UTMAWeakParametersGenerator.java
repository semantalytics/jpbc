package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params.UTMAWeakMasterSecretKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params.UTMAWeakParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params.UTMAWeakPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakParametersGenerator {

    private CurveParams curveParams;

    private Pairing pairing;


    public void init(CurveParams curveParams) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
    }

    public UTMAWeakParameters generateParameters() {
        Element g = pairing.getG1().newElement().setToRandom();
        Element g0 = pairing.getG1().newElement().setToRandom();
        Element g1 = pairing.getG1().newElement().setToRandom();

        Element t1 = pairing.getZr().newElement().setToRandom();
        Element t2 = pairing.getZr().newElement().setToRandom();
        Element t3 = pairing.getZr().newElement().setToRandom();
        Element w  = pairing.getZr().newElement().setToRandom();

        Element omega = pairing.pairing(g, g).powZn(w.duplicate().mul(t1).mul(t2).mul(t3));
        Element T1 = g.duplicate().powZn(t1);
        Element T2 = g.duplicate().powZn(t2);
        Element T3 = g.duplicate().powZn(t3);

        UTMAWeakPublicParameters utmaPublicParameters = new UTMAWeakPublicParameters(curveParams, g, g0, g1, omega, T1, T2, T3);
        return new UTMAWeakParameters(
                utmaPublicParameters,
                new UTMAWeakMasterSecretKeyParameters(utmaPublicParameters, t1, t2, t3, w)
        );
    }

}
