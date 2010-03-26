package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeEPairing;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {
    private static final PairingFactory INSTANCE = new PairingFactory();


    public static PairingFactory getInstance() {
        return INSTANCE;
    }

    public static Pairing getPairing(CurveParams curveParams) {
        return getInstance().initPairing(curveParams);
    }


    private boolean usePBCWhenPossible = false;
    private boolean reuseIstance = true;

    private Class pbcPairingFactoryClass = null;
    private Method getPairingMethod;
    private Throwable pbcPairingFailure;

    private Map<String, Pairing> instances;

    public PairingFactory() {
        // Try to load jpbc-pbc factory
        try {
            pbcPairingFactoryClass = Class.forName("it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory");
            getPairingMethod = pbcPairingFactoryClass.getMethod("getPairing", CurveParams.class);
        } catch (Exception e) {
            pbcPairingFailure = e;
        }
        this.instances = new HashMap<String, Pairing>();
    }


    public Pairing initPairing(CurveParams curveParams) {
        if (curveParams == null)
            throw new IllegalArgumentException("curveParams cannot be null.");

        if (usePBCWhenPossible && pbcPairingFactoryClass != null) {
            try {
                return (Pairing) getPairingMethod.invoke(null, curveParams);
            } catch (Exception e) {                
            }
        }

        String type = curveParams.getType();

        Pairing pairing;
        if (reuseIstance) {
            pairing = instances.get(type);
            if (pairing != null)
                return pairing;
        }

        if ("a".equalsIgnoreCase(type))
            pairing = new TypeAPairing(curveParams);
        else if ("a1".equalsIgnoreCase(type))
            pairing = new TypeA1Pairing(curveParams);
        else if ("d".equalsIgnoreCase(type))
            pairing = new TypeDPairing(curveParams);
        else if ("e".equalsIgnoreCase(type))
            pairing = new TypeEPairing(curveParams);
        else
            throw new IllegalArgumentException("Type not supported. Type = " + type);

        if (reuseIstance)
            instances.put(type, pairing);
        
        return pairing;
    }

    public boolean isUsePBCWhenPossible() {
        return usePBCWhenPossible;
    }

    public void setUsePBCWhenPossible(boolean usePBCWhenPossible) {
        this.usePBCWhenPossible = usePBCWhenPossible;
    }

    public boolean isReuseIstance() {
        return reuseIstance;
    }

    public void setReuseIstance(boolean reuseIstance) {
        this.reuseIstance = reuseIstance;
    }

    public Throwable getPbcPairingFailure() {
        return pbcPairingFailure;
    }

}
