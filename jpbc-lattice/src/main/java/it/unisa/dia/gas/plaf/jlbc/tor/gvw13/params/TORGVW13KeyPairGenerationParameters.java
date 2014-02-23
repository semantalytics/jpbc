package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines.MP12HLP2OneWayFunction;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13KeyPairGenerationParameters extends KeyGenerationParameters {

    private TORGVW13Parameters parameters;
    private AsymmetricCipherKeyPairGenerator latticeKeyPairGenerator;
    private ElementCipher encoder;


    public TORGVW13KeyPairGenerationParameters(SecureRandom random, int strength, TORGVW13Parameters parameters) {
        super(random, strength);

        this.parameters = parameters;
        this.latticeKeyPairGenerator = new MP12HLP2KeyPairGenerator();
        this.encoder = new MP12HLP2OneWayFunction();
    }

    public TORGVW13Parameters getParameters() {
        return parameters;
    }

    public AsymmetricCipherKeyPairGenerator getLatticeKeyPairGenerator() {
        return latticeKeyPairGenerator;
    }

    public ElementCipher getEncoder() {
        return encoder;
    }
}
