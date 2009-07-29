package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {

    private static Map<CurveParams, Pairing> pairings = new WeakHashMap<CurveParams, Pairing>();

    public static Pairing getPairing(CurveParams curveParams) {
        Pairing pairing = pairings.get(curveParams);
        if (pairing == null) {
            if (PBCLibraryProvider.isAvailable())
                pairing = new PBCPairing(curveParams);
            else
                pairing = it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory.getPairing(curveParams);

            pairings.put(curveParams, pairing);
        }

        return pairing;
    }

}
