package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.AbstractCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.DefaultCTL13MMInstance;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMInstanceGenerator {

    private SecureRandom random;

    private CTL13InstanceParameters instanceParameters;


    public CTL13MMInstanceGenerator(SecureRandom random, CTL13InstanceParameters instanceParameters) {
        this.random = random;
        this.instanceParameters = instanceParameters;
    }


    public AbstractCTL13MMInstance generateInstance() {
        return new DefaultCTL13MMInstance(random, instanceParameters);
    }

}
