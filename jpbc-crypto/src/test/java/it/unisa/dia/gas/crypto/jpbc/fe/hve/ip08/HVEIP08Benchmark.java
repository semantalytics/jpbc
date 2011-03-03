package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08SearchKeyGenerator;
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
public class HVEIP08Benchmark {

    protected HVEIP08AttributesEngine searchEngine;
    protected HVEIP08AttributesEngine encEngine;
    protected Random random = new SecureRandom();

    public void benchmark(int maxNumAttributes, int numDifferentPattern, int numIterations,
                          int upperBoundSingleAttribute,
                          boolean usePBC, boolean preProcessPK, boolean preProcessSK) {
        System.out.printf(
                "Benchmark [upperBoundSingleAttribute = %d, usePBC = %s, preProcessPK = %s, preProcessSK = %s]\n",
                upperBoundSingleAttribute, usePBC, preProcessPK, preProcessSK
        );
        searchEngine = new HVEIP08AttributesEngine();
        encEngine = new HVEIP08AttributesEngine();

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);

        for (int n = 5; n <= maxNumAttributes; n += 5) {
            System.out.printf("\tn = %d\n", n);

            int elapsedSearch = 0;
            int elapsedEnc = 0;

            for (int j = 0; j < numDifferentPattern; j++) {
                int[] pattern = randomPattern(n, upperBoundSingleAttribute);

                // Print pattern
                System.out.printf("\t\tPattern = [");
                for (int i = 0; i < pattern.length; i++) {
                    System.out.printf("%d, ", pattern[i]);
                }
                System.out.printf("]\n");
                AsymmetricCipherKeyPair keyPair = setup(genParam(pattern));
                if (preProcessPK)
                    ((HVEIP08PublicKeyParameters) keyPair.getPublic()).preProcess();

                // Test matching key/ct
                int[] searchAttr = getSearchAttribute(pattern);
                // Print searchAttr
                System.out.printf("\t\tSearch  = [");
                for (int i = 0; i < searchAttr.length; i++) {
                    System.out.printf("%d, ", searchAttr[i]);
                }
                System.out.printf("]\n");

                CipherParameters searchKey = keyGen(keyPair.getPrivate(), searchAttr);
                if (preProcessSK)
                    ((HVEIP08SearchKeyParameters) searchKey).preProcess();
                searchEngine.init(false, searchKey);

                for (int i = 0; i < numIterations; i++) {

                    long start = System.currentTimeMillis();
                    byte[] ct = enc(keyPair.getPublic(), getAttributeVector(pattern, searchAttr));
                    long end = System.currentTimeMillis();
                    elapsedEnc += (end - start);

                    start = System.currentTimeMillis();
                    test(ct);
                    end = System.currentTimeMillis();
                    elapsedSearch += (end - start);
                }
            }

            System.out.printf(
                    "\t\t\t[Enc = %f, Test = %f]\n",
                    (((double) elapsedEnc) / (numDifferentPattern * numIterations)),
                    (((double) elapsedSearch) / (numDifferentPattern * numIterations))
            );

        }
    }


    protected int[] randomPattern(int n, int upperBound) {
        if (upperBound <= 0 || upperBound > 256)
            throw new IllegalArgumentException("Bound not valid!");

        int[] patter = new int[n];
        for (int i = 0; i < n; i++) {
            if (upperBound == 1)
                patter[i] = 1;
            else {
                patter[i] = random.nextInt(upperBound);
                if (patter[i] == 0)
                    patter[i] = 1;
            }
        }
        return patter;
    }

    protected int[] getSearchAttribute(int... pattern) {
        int[] attrs = new int[pattern.length];

        for (int i = 0; i < attrs.length; i++) {
            if (random.nextDouble() < 0.3)
                attrs[i] = -1;
            else {
                attrs[i] = random.nextInt((int) Math.pow(2, pattern[i]));
            }

        }

        return attrs;
    }

    protected int[] getAttributeVector(int[] pattern, int[] searchAttr) {
        if (random.nextDouble() < 0.5)
            return getMatchingVector(pattern, searchAttr);
        return getNonMatchingVector(pattern, searchAttr);
    }

    protected int[] getMatchingVector(int[] pattern, int[] searchAttr) {
        int[] attrs = new int[pattern.length];

        Random random = new SecureRandom();
        for (int i = 0; i < attrs.length; i++) {
            if (searchAttr[i] == -1)
                attrs[i] = random.nextInt((int) Math.pow(2, pattern[i]));
            else {
                attrs[i] = searchAttr[i];
            }
        }

        return attrs;
    }

    protected int[] getNonMatchingVector(int[] pattern, int[] searchAttr) {
        int[] attrs = new int[pattern.length];

        Random random = new SecureRandom();
        for (int i = 0; i < attrs.length; i++) {
            if (searchAttr[i] == -1)
                attrs[i] = random.nextInt((int) Math.pow(2, pattern[i]));
            else {
                do {
                    attrs[i] = random.nextInt((int) Math.pow(2, pattern[i]));
                } while (attrs[i] == searchAttr[i]);
            }
        }

        return attrs;
    }

    protected HVEIP08Parameters genParam(int... attributeLengths) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(curveParams, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08SearchKeyGenerator generator = new HVEIP08SearchKeyGenerator();
        generator.init(new HVEIP08SearchKeyGenerationParameters(
                (HVEIP08PrivateKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        byte[] attrs = HVEAttributes.attributesToByteArray(
                ((HVEIP08PublicKeyParameters) publicKey).getParameters(),
                attributes
        );

        encEngine.init(true, publicKey);

        return encEngine.processBlock(attrs, 0, attrs.length);
    }

    protected boolean test(byte[] ct) {
        return searchEngine.processBlock(ct, 0, ct.length)[0] == 0;
    }


    public static void main(String[] args) {
        HVEIP08Benchmark benchmark = new HVEIP08Benchmark();
        benchmark.benchmark(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                false, false, false);
        benchmark.benchmark(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                true, true, true);
    }

}
