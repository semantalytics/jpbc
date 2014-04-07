package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2Test {

    private MP12HLP2KeyPairGenerator gen;
    private AsymmetricCipherKeyPair keyPair;
    private SecureRandom random = new SecureRandom();

    @Before
    public void setUp() throws Exception {
        long start = System.currentTimeMillis();

        gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                random,
                16, // n
                20 // k
        ));
        keyPair = gen.generateKeyPair();
        gen.store(keyPair);

        long end = System.currentTimeMillis();

        System.out.println("+ (end-start) = " + (end - start));
    }

    @Test
    public void testSampleD() throws Exception {
        // Sample
        Element syndrome = ((MP12HLP2PublicKeyParameters) keyPair.getPublic()).getSyndromeField().newRandomElement();
        System.out.println("syndrome = " + syndrome);

        MP12HLP2Sampler sampler = new MP12HLP2Sampler();
        sampler.init(new MP12HLP2SampleParameters(keyPair));

        long start = System.currentTimeMillis();
        Element x = sampler.processElements(syndrome);
        long end = System.currentTimeMillis();
        System.out.println("processElements (end-start) = " + (end - start));
        System.out.println("x = " + x);

        // Decode
        MP12HLP2Decoder decoder = new MP12HLP2Decoder();
        decoder.init(keyPair.getPublic());
        Element syndromePrime = decoder.processElements(x);
        System.out.println("syndromePrime = " + syndromePrime);

        assertEquals(syndrome, syndromePrime);
    }

    @Test
    public void testSampleDMatrix() throws Exception {
        MP12HLP2PublicKeyParameters latticePk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();

        MatrixField<Field> RField = new MatrixField<Field>(latticePk.getParameters().getRandom(), latticePk.getZq(), latticePk.getM());
        // Compute U
        MatrixElement U = (MatrixElement) latticePk.getA().getField().newRandomElement();

//        System.out.println("U = " + U);

        // Sample R0
        MP12HLP2Sampler sampleD = new MP12HLP2MatrixSampler(RField);
        sampleD.init(new MP12HLP2SampleParameters(keyPair.getPublic(), keyPair.getPrivate()));

        Matrix R0 = (Matrix) sampleD.processElements(U);

//        System.out.println("R0 = " + R0);

        // Decode
        MP12HLP2Decoder decoder = new MP12HLP2Decoder();
        decoder.init(keyPair.getPublic());

        MatrixElement U1 = U.getField().newElement();
        for (int i = 0; i < latticePk.getM(); i++) {
            Element sample = R0.columnAt(i);
//            System.out.println("sample = " + sample);
            Element u = decoder.processElements(sample);
//            System.out.println("u = " + u);
            U1.setColAt(i, u);
        }
//        System.out.println("U1 = " + U1);

        Element U2 = ((MP12HLP2PublicKeyParameters) keyPair.getPublic()).getA().mul(R0);
//        System.out.println("U2 = " + U2);

        assertEquals(U, U1);
        assertEquals(U, U2);
    }


    @Test
    public void testInverter() throws Exception {
        // b=OWF(s)
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic()
        );
        owf.init(owfParams);

        Element s = owfParams.getInputField().newRandomElement();
        Element b = owf.processElements(s);

        System.out.println("s = " + s);
        System.out.println("b = " + b);

        // s'=Invert(b)
        MP12HLP2Inverter inverter = new MP12HLP2Inverter();
        inverter.init(new MP12HLP2InverterParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic(),
                (MP12HLP2PrivateKeyParameters) keyPair.getPrivate()
        ));
        Element sPrime = inverter.processElements(b);
        System.out.println("sPrime = " + sPrime);

        // Check for equality
        Assert.assertEquals(s, sPrime);
    }


    @Test
    public void testErrorTolerantOneTimePad() throws Exception {
        // Init OWF
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic()
        );
        owf.init(owfParams);
        Element key = owf.processElements(owfParams.getInputField().newRandomElement());

        // Init OTP
        ElementCipher otp = new MP12HLP2ErrorTolerantOneTimePad();
        otp.init(key);

        byte[] bytes = new byte[gen.getMInBytes()];
        random.nextBytes(bytes);
        System.out.println("bytes = " + Arrays.toString(bytes));

        Element c = otp.processBytes(bytes);

        byte[] bytesPrime = otp.processElementsToBytes(c);
        System.out.println("bytesPrime = " + Arrays.toString(bytesPrime));

        Assert.assertArrayEquals(bytes, bytesPrime);
    }

}
