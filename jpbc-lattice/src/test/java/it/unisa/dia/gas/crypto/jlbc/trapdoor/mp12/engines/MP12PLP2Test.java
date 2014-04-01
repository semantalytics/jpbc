package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2Test {

    protected MP12PLP2KeyPairGenerator gen;
    protected SecureRandom random;

    @Before
    public void setUp() throws Exception {
        random = new SecureRandom();

        gen = new MP12PLP2KeyPairGenerator();
        gen.init(new MP12PLP2KeyPairGenerationParameters(
                random,
                4,  // n
                32  // k
                // s
        ));
    }

    @Test
    public void test1() throws Exception {
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
