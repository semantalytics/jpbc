package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {

    private static Map<CurveParameters, Pairing> pairings = new WeakHashMap<CurveParameters, Pairing>();

    public static Pairing getPairing(CurveParameters curveParameters) {
            // TODO: reusing doesn't work!
        Pairing pairing = pairings.get(curveParameters);
        if (pairing == null) {
            if (WrapperLibraryProvider.isAvailable())
                pairing = new PBCPairing(curveParameters);
            else
                pairing = it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory.getPairing(curveParameters);

            pairings.put(curveParameters, pairing);
        }

        return pairing;
    }

    public static Pairing getPairing(String curveParametersPath) {
        return getPairing(it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory.getInstance().loadCurveParameters(curveParametersPath));
    }
}
