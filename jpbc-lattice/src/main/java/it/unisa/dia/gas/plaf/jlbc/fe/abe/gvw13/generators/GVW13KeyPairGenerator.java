package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.generators;

import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13MasterSecretKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13Parameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private GVW13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (GVW13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        GVW13Parameters parameters = params.getParameters();

        AsymmetricCipherKeyPairGenerator tor = parameters.getTorKeyPairGenerater();
        int ell = parameters.getEll();

        CipherParameters[] publicKeys = new CipherParameters[ell * 2 + 1];
        CipherParameters[] secretKeys = new CipherParameters[ell * 2 + 1];

        for (int i = 0, size = 2 * ell; i < size; i++) {
            AsymmetricCipherKeyPair keyPair = tor.generateKeyPair();
            publicKeys[i] = keyPair.getPublic();
            secretKeys[i] = keyPair.getPrivate();
        }

        AsymmetricCipherKeyPair keyPair = tor.generateKeyPair();
        publicKeys[2 * ell] = keyPair.getPublic();
        secretKeys[2 * ell] = keyPair.getPrivate();

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new GVW13PublicKeyParameters(parameters, publicKeys),
                new GVW13MasterSecretKeyParameters(parameters, secretKeys)
        );
    }


}
