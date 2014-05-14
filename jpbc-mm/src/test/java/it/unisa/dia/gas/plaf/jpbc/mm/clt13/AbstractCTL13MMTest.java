package it.unisa.dia.gas.plaf.jpbc.mm.clt13;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.MultiThreadCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMPublicParameterGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMSystemParameters;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
@RunWith(value = Parameterized.class)
public abstract class AbstractCTL13MMTest {

    static protected SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {CTL13MMSystemParameters.TOY},
        };

        return Arrays.asList(data);
    }


    protected CTL13MMSystemParameters instanceParameters;
    protected int type;
    protected CTL13MMInstance instance;


    public AbstractCTL13MMTest(CTL13MMSystemParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    @Before
    public void before() {
        instance = new MultiThreadCTL13MMInstance(
                random,
                new CTL13MMPublicParameterGenerator(random, instanceParameters).generate()
        );
    }
}
