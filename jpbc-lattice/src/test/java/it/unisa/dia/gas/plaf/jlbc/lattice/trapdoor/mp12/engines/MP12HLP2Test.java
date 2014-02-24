package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.*;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianSampler;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2Test {

    private AsymmetricCipherKeyPair keyPair;
    private SecureRandom random = new SecureRandom();

    @Before
    public void setUp() throws Exception {
        MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                random, new MP12Parameters(random, 10), 6, new ZGaussianSampler(100, random, 4)
        ));
        keyPair = gen.generateKeyPair();
    }

    @Test
    public void testSampleD() throws Exception {
        MP12HLP2SampleD sampler = new MP12HLP2SampleD();
        sampler.init(new MP12HLP2SampleParameters(keyPair));

        MP12HLP2Decoder decoder = new MP12HLP2Decoder();
        decoder.init(keyPair.getPublic());

        Element syndrome = ((MP12HLP2PublicKeyParameters)keyPair.getPublic()).getSyndromeField().newRandomElement();
        System.out.println("syndrome = " + syndrome);

        Element x = sampler.processElements(syndrome);
        System.out.println("x = " + x);

        Element syndromePrime = decoder.processElements(x);
        System.out.println("syndromePrime = " + syndromePrime);

        assertEquals(syndrome, syndromePrime);
    }

    @Test
    public void testOWF() throws Exception {
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic(),
                new ZGaussianSampler(100, random, 4)
        );
        owf.init(owfParams);

        Element x = owf.processElements(owfParams.getInputField().newRandomElement());
        System.out.println("x = " + x);

    }

}
