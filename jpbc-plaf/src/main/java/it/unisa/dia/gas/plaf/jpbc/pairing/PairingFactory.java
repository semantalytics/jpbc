package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeEPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.g.TypeGPairing;

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

    public static Pairing getPairing(CurveParameters curveParameters) {
        return getInstance().initPairing(curveParameters);
    }

    public static Pairing getPairing(String curveParametersPath) {
        return getInstance().initPairing(curveParametersPath);
    }

    private boolean usePBCWhenPossible = false;
    private boolean reuseIstance = true;

    private Class pbcPairingFactoryClass = null;
    private Method getPairingMethod;
    private Throwable pbcPairingFailure;

    private Map<CurveParameters, Pairing> instances;

    public PairingFactory() {
        // Try to load jpbc-pbc factory
        try {
            pbcPairingFactoryClass = Class.forName("it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory");
            getPairingMethod = pbcPairingFactoryClass.getMethod("getPairing", CurveParams.class);
        } catch (Exception e) {
            pbcPairingFailure = e;
        }
        this.instances = new HashMap<CurveParameters, Pairing>();
    }

    public Pairing initPairing(String curveParametersPath) {
        return initPairing(loadCurveParameters(curveParametersPath));
    }

    public Pairing initPairing(CurveParameters curveParameters) {
        if (curveParameters == null)
            throw new IllegalArgumentException("curveParameters cannot be null.");

        if (usePBCWhenPossible && pbcPairingFactoryClass != null) {
            try {
                return (Pairing) getPairingMethod.invoke(null, curveParameters);
            } catch (Exception e) {                
            }
        }

        String type = curveParameters.getString("type");

        Pairing pairing;
        if (reuseIstance) {
            pairing = instances.get(curveParameters);
            if (pairing != null)
                return pairing;
        }

        if ("a".equalsIgnoreCase(type))
            pairing = new TypeAPairing(curveParameters);
        else if ("a1".equalsIgnoreCase(type))
            pairing = new TypeA1Pairing(curveParameters);
        else if ("d".equalsIgnoreCase(type))
            pairing = new TypeDPairing(curveParameters);
        else if ("e".equalsIgnoreCase(type))
            pairing = new TypeEPairing(curveParameters);
        else if ("f".equalsIgnoreCase(type))
            return new TypeFPairing(curveParameters);
        else if ("g".equalsIgnoreCase(type))
            return new TypeGPairing(curveParameters);
        else
            throw new IllegalArgumentException("Type not supported. Type = " + type);

        if (reuseIstance)
            instances.put(curveParameters, pairing);
        
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

    public CurveParameters loadCurveParameters(String path) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(path);

        return curveParams;
    }
}
