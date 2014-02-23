package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12Parameters;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianSampler;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2Test {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test1() throws Exception {
        SecureRandom random = new SecureRandom();

        MP12PLP2KeyPairGenerator gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLP2KeyPairGenerationParameters(
                random, new MP12Parameters(random, 10), 6, new ZGaussianSampler(100, random, 4)
        ));


        MP12PLP2PublicKeyParameters pk = (MP12PLP2PublicKeyParameters) gen.generateKeyPair().getPublic();

        MP12PLP2SampleD sampler = new MP12PLP2SampleD();
        sampler.init(pk);

        MP12PLP2Decoder decoder = new MP12PLP2Decoder();
        decoder.init(pk);

        Element syndrome = pk.getSyndromeField().newRandomElement();
        System.out.println("syndrome = " + syndrome);

        Element x = sampler.processElements(syndrome);
        System.out.println("x = " + x);

        Element syndromePrime = decoder.processElements(x);
        System.out.println("syndromePrime = " + syndromePrime);

        assertEquals(syndrome, syndromePrime);
    }
}
