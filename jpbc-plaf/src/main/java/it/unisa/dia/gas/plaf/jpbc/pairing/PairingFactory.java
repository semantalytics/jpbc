package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;

import java.util.Properties;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {

    public static Pairing getPairing(Properties curveParams) {
        if (curveParams == null)
            throw new IllegalArgumentException("curveParams cannot be null.");

        String type = curveParams.getProperty("type");
        if ("a".equalsIgnoreCase(type))
            return new TypeAPairing(curveParams);
        else if ("d".equalsIgnoreCase(type))
            return new TypeDPairing(curveParams);
        else
            throw new IllegalArgumentException("Type not supported. Type = " + type);
    }

}
