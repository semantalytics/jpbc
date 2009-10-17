package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params.UTMAWeakPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongPublicParameters extends UTMAWeakPublicParameters {
    private CipherParameters rPublicKey;


    public UTMAStrongPublicParameters(CurveParams curveParams, Element g, Element g0, Element g1, Element omega, Element T1, Element T2, Element T3, CipherParameters rPublicKey) {
        super(curveParams, g, g0, g1, omega, T1, T2, T3);

        this.rPublicKey = rPublicKey;
    }


    public CipherParameters getRPublicKey() {
        return rPublicKey;
    }
}