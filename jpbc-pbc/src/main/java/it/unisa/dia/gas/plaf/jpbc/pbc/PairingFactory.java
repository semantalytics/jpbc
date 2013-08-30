package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {

    private static Map<PairingParameters, Pairing> pairings = new WeakHashMap<PairingParameters, Pairing>();

    public static boolean isPBCAvailable() {
        return WrapperLibraryProvider.isAvailable();
    }

    public static Pairing getPairing(PairingParameters parameters) {
        Pairing pairing = pairings.get(parameters);
        if (pairing == null) {
            if (WrapperLibraryProvider.isAvailable())
                pairing = new PBCPairing(parameters);
            else
                return null;

            pairings.put(parameters, pairing);
        }

        return pairing;
    }

    public static Pairing getPairing(String parametersPath) {
        return getPairing(it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory.getInstance().loadParameters(parametersPath));
    }

}
