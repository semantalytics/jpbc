package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08Parameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class HVEIP08AbstractTest extends AbstractJPBCCryptoTest {

    public HVEIP08AbstractTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    protected HVEIP08Parameters genBinaryParam(int n) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(n, parameters);

        return generator.generateParameters();
    }

    protected HVEIP08Parameters genParam(int... attributeLengths) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(parameters, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }


    protected int[][] createMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            if (i != 0  && i != 1 && random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = result[0][i];
            }
        }
        return result;
    }

    protected int[][] createAllStarMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            result[0][i] = -1;
            result[1][i] = random.nextInt(2);
        }
        return result;
    }

    protected int[][] createNonMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (i != 0  && i != 1 && random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = 1 - result[0][i];
            }
        }
        return result;
    }

}
