package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08AttributesOnlySearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
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

    public void benchmark(String curve,
                          int maxNumAttributes, int numDifferentPattern, int numIterations,
                          int upperBoundSingleAttribute,
                          boolean usePBC,
                          boolean preProcessPK,
                          boolean preProcessMSK,
                          boolean preProcessSearchKey) {
        System.out.printf(
                "Benchmark [\n\t\tcurve = %s, \n\t\tupperBoundSingleAttribute = %d, \n\t\tusePBC = %s, \n\t\tpreProcessPK = %s, \n\t\tpreProcessMSK = %s, \n\t\tpreProcessSearchKey = %s\n]\n",
                curve, upperBoundSingleAttribute, usePBC, preProcessPK, preProcessMSK, preProcessSearchKey
        );
        searchEngine = new HVEIP08AttributesEngine();
        encEngine = new HVEIP08AttributesEngine();

        PairingFactory.getInstance().setUsePBCWhenPossible(usePBC);

        for (int n = 5; n <= maxNumAttributes; n += 5) {
            int elapsedSearch = 0;
            int elapsedEnc = 0;
            int elapsedKeyGen = 0;
            int elapsedPreProcessSearchKey = 0;

            for (int j = 0; j < numDifferentPattern; j++) {
                int[] pattern = randomPattern(n, upperBoundSingleAttribute);

                // Print pattern
                AsymmetricCipherKeyPair keyPair = setup(genParam(curve, pattern));
                if (preProcessPK)
                    ((HVEIP08PublicKeyParameters) keyPair.getPublic()).preProcess();
                if (preProcessMSK)
                    ((HVEIP08PrivateKeyParameters) keyPair.getPrivate()).preProcess();

                // Test matching key/ct
                int[] searchAttr = getSearchAttribute(pattern);

                long start = System.currentTimeMillis();
                CipherParameters searchKey = keyGen(keyPair.getPrivate(), searchAttr);
                long end = System.currentTimeMillis();
                elapsedKeyGen += (end - start);

                if (preProcessSearchKey) {
                    start = System.currentTimeMillis();
                    ((HVEIP08SearchKeyParameters) searchKey).preProcess();
                    end = System.currentTimeMillis();
                    elapsedPreProcessSearchKey += (end - start);
                }
                searchEngine.init(false, searchKey);

                for (int i = 0; i < numIterations; i++) {
                    start = System.currentTimeMillis();
                    byte[] ct = enc(keyPair.getPublic(), getAttributeVector(pattern, searchAttr));
                    end = System.currentTimeMillis();
                    elapsedEnc += (end - start);

                    start = System.currentTimeMillis();
                    test(ct);
                    end = System.currentTimeMillis();
                    elapsedSearch += (end - start);
                }
            }

            System.out.printf(
                    "%d %f %f %f %f\n",
                    n,
                    (((double) elapsedEnc) / (numDifferentPattern * numIterations)),
                    (((double) elapsedKeyGen) / (numDifferentPattern * numIterations)),
                    (((double) elapsedPreProcessSearchKey) / (numDifferentPattern * numIterations)),
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
                patter[i] = random.nextInt(upperBound) + 1;
            }
        }
        return patter;
    }

    protected int[] getSearchAttribute(int... pattern) {
        int[] attrs = new int[pattern.length];

        for (int i = 0; i < attrs.length; i++) {
            attrs[i] = random.nextInt((int) Math.pow(2, pattern[i]));
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

    protected HVEIP08Parameters genParam(String curve, int... attributeLengths) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(
                PairingFactory.getInstance().loadCurveParameters(curve),
                attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08AttributesOnlySearchKeyGenerator generator = new HVEIP08AttributesOnlySearchKeyGenerator();
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
                args[0],
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Boolean.parseBoolean(args[5]),
                Boolean.parseBoolean(args[6]),
                Boolean.parseBoolean(args[7]),
                Boolean.parseBoolean(args[8]));
    }


}
