package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.engines.HHVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators.HHVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators.HHVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators.HHVEIP08SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEBenchmark {


    public void benchmark() {
        AsymmetricCipherKeyPair keyPair = setup(genParam(30));

        int iter = 10;
        int sumEnc = 0, sumDec = 0;
        for (int i = 0; i < iter; i++) {
            int[] attributes = HVEAttributes.randomBinaryAttributes(new Random(), 30);

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
                (HHVEIP08PublicKeyParameters) publicKey,
                (HHVEIP08SearchKeyParameters) searchKey,
                attributesPattern)
        );

        return generator.generateKey();
    }


    protected HHVEIP08Parameters genParam(int... attributeLengths) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        HHVEIP08ParametersGenerator generator = new HHVEIP08ParametersGenerator();
        generator.init(curveParams, attributeLengths);

        return generator.generateParameters();
    }

    protected HHVEIP08Parameters genParam(int n) {
        HHVEIP08ParametersGenerator generator = new HHVEIP08ParametersGenerator();
        generator.init(
                new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")),
                n
        );

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HHVEIP08Parameters hveParameters) {
        HHVEIP08KeyPairGenerator generator = new HHVEIP08KeyPairGenerator();
        generator.init(new HHVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        byte[] attrs = HVEAttributes.attributesToByteArray(
                ((HHVEIP08PublicKeyParameters)publicKey).getParameters(),
                attributes
        );

        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(true, publicKey);

        return engine.processBlock(attrs, 0, attrs.length);
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HHVEIP08SearchKeyGenerator generator = new HHVEIP08SearchKeyGenerator();
        generator.init(new HHVEIP08SearchKeyGenerationParameters((HHVEIP08PrivateKeyParameters) privateKey, pattern));

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(false, searchKey);

        return engine.processBlock(ct, 0, ct.length)[0] == 0;
    }



    public static void main(String[] args) {
        HHVEBenchmark benchmark = new HHVEBenchmark();
        benchmark.benchmark();
    }

}
