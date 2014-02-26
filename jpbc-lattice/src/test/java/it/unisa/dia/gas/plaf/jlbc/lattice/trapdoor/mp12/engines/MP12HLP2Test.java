package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.*;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianSampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2Test {

    private AsymmetricCipherKeyPair keyPair, keyPair1, keyPair2;
    private SecureRandom random = new SecureRandom();

    @Before
    public void setUp() throws Exception {
        MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                random, new MP12Parameters(random, 4), 12, new ZGaussianSampler(100, random, 4)
        ));
        keyPair = gen.generateKeyPair();
        keyPair1 = gen.generateKeyPair();
        keyPair2 = gen.generateKeyPair();
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
    public void testSampleDMatrix() throws Exception {
        MP12HLP2PublicKeyParameters latticePk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();

        MP12HLP2SampleD sampler = new MP12HLP2SampleD();
        sampler.init(new MP12HLP2SampleParameters(keyPair));

        MP12HLP2Decoder decoder = new MP12HLP2Decoder();
        decoder.init(keyPair.getPublic());

        // Sample R1 from D_Z,s
        MatrixField<Field> RField = new MatrixField<Field>(
                latticePk.getParameters().getRandom(),
                latticePk.getZq(),
                latticePk.getM(),
                latticePk.getM()
        );
        MatrixElement R1 = RField.newElement();
        for (int i = 0; i < latticePk.getM(); i++) {
            for (int j = 0; j < latticePk.getM(); j++) {
                R1.getAt(i, j).set(latticePk.getSampler().sample());
            }
        }

        System.out.println("R1 = " + R1);

        // Compute U
        MatrixElement U = (MatrixElement) ((MP12HLP2PublicKeyParameters) keyPair2.getPublic()).getA().duplicate().sub(
                ((MP12HLP2PublicKeyParameters) keyPair1.getPublic()).getA().mul(R1)
        );

        System.out.println("U = " + U);

        // Sample R0

        MP12HLP2SampleD sampleD = new MP12HLP2SampleD();
        sampleD.init(new MP12HLP2SampleParameters(keyPair.getPublic(), keyPair.getPrivate()));
        MatrixElement R0 = RField.newElement();

        for (int i = 0; i < latticePk.getM(); i++) {
            R0.setColAt(i, sampleD.processElements(U.columnAt(i)));
        }

        System.out.println("R0 = " + R0);


        // Decode
        MatrixElement U1 = U.getField().newElement();

        for (int i= 0; i < latticePk.getM(); i++) {
            Element u = decoder.processElements(R0.columnAt(i));
            System.out.println("u = " + u);
            U1.setColAt(i, u);
        }

        System.out.println("U1 = " + U1);

        Element U2 = ((MP12HLP2PublicKeyParameters) keyPair.getPublic()).getA().mul(R0);
        System.out.println("U2 = " + U2);

        assertEquals(U, U1);
        assertEquals(U, U2);
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
