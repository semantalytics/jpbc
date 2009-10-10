package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeEPairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {

    public static Pairing getPairing(CurveParams curveParams) {
        if (curveParams == null)
            throw new IllegalArgumentException("curveParams cannot be null.");

        // Try to load the wrapper first...
        try {
            Class.forName("it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        String type = curveParams.getType();

        if ("a".equalsIgnoreCase(type))
            return new TypeAPairing(curveParams);
        else if ("a1".equalsIgnoreCase(type))
            return new TypeA1Pairing(curveParams);
        else if ("d".equalsIgnoreCase(type))
            return new TypeDPairing(curveParams);
        else if ("e".equalsIgnoreCase(type))
            return new TypeEPairing(curveParams);
        else
            throw new IllegalArgumentException("Type not supported. Type = " + type);
    }

}
