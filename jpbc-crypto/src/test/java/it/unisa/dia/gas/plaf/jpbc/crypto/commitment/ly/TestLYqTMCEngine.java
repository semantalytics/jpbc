package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly;

import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.generators.LYqTMCKeyPairGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.generators.LYqTMCParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TestLYqTMCEngine extends TestCase {


    public void test() {
        // Load curve parameters
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        // generate system parameters
        LYqTMCParametersGenerator parametersGenerator = new LYqTMCParametersGenerator();
        parametersGenerator.init(curveParams);
        LYqTMCParameters parameters = parametersGenerator.generateParameters();

        assertNotNull(parameters);

        // generate a key pair for 4 messages
        LYqTMCKeyPairGenerator keyPairGenerator = new LYqTMCKeyPairGenerator();
        keyPairGenerator.init(new LYqTMCKeyGenerationParameters(new SecureRandom(), parameters, 4));
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        assertNotNull(keyPair);
    }

}
