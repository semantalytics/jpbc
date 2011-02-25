package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HHVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HHVEIP08SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEBenchmark {
    private boolean usePBC;
    private int numAttributes;


    public HHVEBenchmark(boolean usePBC, int numAttributes) {
        this.usePBC = usePBC;
        this.numAttributes = numAttributes;
    }

    public void benchmark() {
        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);
        AsymmetricCipherKeyPair keyPair = setup(genParam(numAttributes));

        int iter = 10;
        int sumEnc = 0, sumDec = 0;
        for (int i = 0; i < iter; i++) {
            int[] attributes = HVEAttributes.randomBinaryAttributes(new Random(), numAttributes);

            long start = System.currentTimeMillis();
            byte[] ciphertext = enc(keyPair.getPublic(), attributes);
            long end = System.currentTimeMillis();

            sumEnc += (end-start);
            System.out.println("sumEnc = " + sumEnc);

            CipherParameters searchKey = keyGen(keyPair.getPrivate(), attributes);

            start = System.currentTimeMillis();
            test(searchKey, ciphertext);
            end = System.currentTimeMillis();

            sumDec += (end-start);
            System.out.println("sumDec = " + sumDec);
        }

        System.out.println("enc.elapsed = " + (sumEnc /iter));
        System.out.println("dec.elapsed = " + (sumDec /iter));
    }

    private CipherParameters delegate(CipherParameters publicKey, CipherParameters searchKey, int... attributesPattern) {
        HHVEIP08SearchKeyGenerator generator = new HHVEIP08SearchKeyGenerator();
        generator.init(new HHVEIP08DelegateSecretKeyGenerationParameters(
                (HVEIP08PublicKeyParameters) publicKey,
                (HHVEIP08SearchKeyParameters) searchKey,
                attributesPattern)
        );

        return generator.generateKey();
    }


    protected HVEIP08Parameters genParam(int... attributeLengths) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(curveParams, attributeLengths);

        return generator.generateParameters();
    }

    protected HVEIP08Parameters genParam(int n) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(
                new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")),
                n
        );

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        byte[] attrs = HVEAttributes.attributesToByteArray(
                ((HVEIP08PublicKeyParameters) publicKey).getParameters(),
                attributes
        );

        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(true, publicKey);

        return engine.processBlock(attrs, 0, attrs.length);
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HHVEIP08SearchKeyGenerator generator = new HHVEIP08SearchKeyGenerator();
        generator.init(new HVEIP08SearchKeyGenerationParameters((HVEIP08PrivateKeyParameters) privateKey, pattern));

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(false, searchKey);

        return engine.processBlock(ct, 0, ct.length)[0] == 0;
    }



    public static void main(String[] args) {
        HHVEBenchmark benchmark = new HHVEBenchmark(Boolean.parseBoolean(args[0]), Integer.parseInt(args[1]));
        benchmark.benchmark();
    }

}
