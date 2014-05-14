package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMMapParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public abstract class CTL13MMAbstractPublicParameterGenerator implements PairingParametersGenerator {

    protected SecureRandom random;
    protected CTL13MMSystemParameters parameters;
    protected boolean storeGeneratedInstance;


    public CTL13MMAbstractPublicParameterGenerator(SecureRandom random, CTL13MMSystemParameters parameters) {
        this(random, parameters, true);
    }

    public CTL13MMAbstractPublicParameterGenerator(SecureRandom random, PairingParameters parameters) {
        this(random, new CTL13MMSystemParameters(parameters), true);
    }

    public CTL13MMAbstractPublicParameterGenerator(SecureRandom random, CTL13MMSystemParameters parameters, boolean storeGeneratedInstance) {
        this.random = random;
        this.parameters = parameters;
        this.storeGeneratedInstance = storeGeneratedInstance;
    }

    public CTL13MMAbstractPublicParameterGenerator(SecureRandom random, PairingParameters parameters, boolean storeGeneratedInstance) {
        this(random, new CTL13MMSystemParameters(parameters), storeGeneratedInstance);
    }


    public PairingParameters generate() {
        CTL13MMMapParameters mapParameters = newCTL13MMMapParameters();
        if (storeGeneratedInstance) {
            if (mapParameters.load())
                return mapParameters;
        }

        mapParameters.init();
        generateInternal(mapParameters);

        if (storeGeneratedInstance)
            mapParameters.store();

        return mapParameters;
    }


    protected CTL13MMMapParameters newCTL13MMMapParameters() {
        return new CTL13MMMapParameters(parameters);
    }

    protected abstract void generateInternal(MutablePairingParameters mapParameters);

}