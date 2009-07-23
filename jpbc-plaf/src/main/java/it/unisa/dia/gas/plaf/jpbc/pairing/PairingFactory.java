package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {

    public static Pairing getPairing(CurveParams curveParams) {
        if (curveParams == null)
            throw new IllegalArgumentException("curveParams cannot be null.");

        String type = curveParams.getType();

        if ("a".equalsIgnoreCase(type))
            return new TypeAPairing(curveParams);
        else if ("a1".equalsIgnoreCase(type))
            return new TypeA1Pairing(curveParams);
        else if ("d".equalsIgnoreCase(type))
            return new TypeDPairing(curveParams);
        else
            throw new IllegalArgumentException("Type not supported. Type = " + type);
    }

}
