package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.d.TypeDPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeEPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.g.TypeGPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.DefaultPropertiesParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactory {
    private static final PairingFactory INSTANCE = new PairingFactory();


    public static PairingFactory getInstance() {
        return INSTANCE;
    }

    public static Pairing getPairing(PairingParameters parameters) {
        return getInstance().initPairing(parameters);
    }

    public static Pairing getPairing(String parametersPath) {
        return getInstance().initPairing(parametersPath);
    }

    public static Pairing getPairing(PairingParameters parameters, Random random) {
        return getInstance().initPairing(parameters, random);
    }

    public static Pairing getPairing(String parametersPath, Random random) {
        return getInstance().initPairing(parametersPath, random);
    }


    private boolean usePBCWhenPossible = false;
    private boolean reuseInstance = true;

    private boolean pbcAvailable = false;
    private Method getPairingMethod;
    private Throwable pbcPairingFailure;

    private Map<PairingParameters, Pairing> instances;

    public PairingFactory() {
        // Try to load jpbc-pbc factory
        try {
            Class pbcPairingFactoryClass = Class.forName("it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory");
            Method isPBCAvailable = pbcPairingFactoryClass.getMethod("isPBCAvailable", null);
            pbcAvailable = ((Boolean)isPBCAvailable.invoke(null));
            if (pbcAvailable)
                getPairingMethod = pbcPairingFactoryClass.getMethod("getPairing", PairingParameters.class);
        } catch (Exception e) {
            pbcAvailable = false;
            pbcPairingFailure = e;
        }

        this.instances = new HashMap<PairingParameters, Pairing>();
    }

    public Pairing initPairing(String parametersPath) {
        return initPairing(loadParameters(parametersPath), null);
    }

    public Pairing initPairing(PairingParameters parameters) {
        return initPairing(parameters, null);
    }

    public Pairing initPairing(String parametersPath, Random random) {
        return initPairing(loadParameters(parametersPath), random);
    }

    public Pairing initPairing(PairingParameters parameters, Random random) {
        if (parameters == null)
            throw new IllegalArgumentException("parameters cannot be null.");

        Pairing pairing = null;
        if (reuseInstance && random == null) {
            pairing = instances.get(parameters);
            if (pairing != null)
                return pairing;
        }

        if (usePBCWhenPossible && pbcAvailable)
            pairing = getPBCPairing(parameters);

        if (pairing == null) {
            String type = parameters.getString("type");
            if ("a".equalsIgnoreCase(type))
                pairing = new TypeAPairing(random, parameters);
            else if ("a1".equalsIgnoreCase(type))
                pairing = new TypeA1Pairing(random, parameters);
            else if ("d".equalsIgnoreCase(type))
                pairing = new TypeDPairing(random, parameters);
            else if ("e".equalsIgnoreCase(type))
                pairing = new TypeEPairing(random, parameters);
            else if ("f".equalsIgnoreCase(type))
                return new TypeFPairing(random, parameters);
            else if ("g".equalsIgnoreCase(type))
                return new TypeGPairing(random, parameters);
            else
                throw new IllegalArgumentException("Type not supported. Type = " + type);
        }

        if (reuseInstance)
            instances.put(parameters, pairing);

        return pairing;
    }

    public boolean isUsePBCWhenPossible() {
        return usePBCWhenPossible;
    }

    public void setUsePBCWhenPossible(boolean usePBCWhenPossible) {
        this.usePBCWhenPossible = usePBCWhenPossible;
    }

    public boolean isReuseInstance() {
        return reuseInstance;
    }

    public void setReuseInstance(boolean reuseInstance) {
        this.reuseInstance = reuseInstance;
    }

    public Throwable getPbcPairingFailure() {
        return pbcPairingFailure;
    }

    public CurveParameters loadCurveParameters(String path) {
        DefaultPropertiesParameters curveParams = new DefaultPropertiesParameters();
        curveParams.load(path);

        return curveParams;
    }

    public PairingParameters loadParameters(String path) {
        PropertiesParameters curveParams = new PropertiesParameters();
        curveParams.load(path);

        return curveParams;
    }


    public Pairing getPBCPairing(PairingParameters parameters) {
        try {
            return (Pairing) getPairingMethod.invoke(null, parameters);
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    public boolean isPBCAvailable() {
        return pbcAvailable;
    }

}
