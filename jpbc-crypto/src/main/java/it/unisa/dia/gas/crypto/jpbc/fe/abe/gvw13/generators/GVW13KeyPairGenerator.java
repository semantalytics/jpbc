package it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13PublicKeyParameters;
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

        AsymmetricCipherKeyPairGenerator tor = null;
        int n = parameters.getN();

        CipherParameters[] publicKeys =  new CipherParameters[n*2+1];
        CipherParameters[] secretKeys =  new CipherParameters[n*2+1];

        for (int i = 0, size = 2 * n; i < size; i++) {
            AsymmetricCipherKeyPair keyPair = tor.generateKeyPair();
            publicKeys[i] = keyPair.getPublic();
            secretKeys[i] = keyPair.getPrivate();
        }

        AsymmetricCipherKeyPair keyPair = tor.generateKeyPair();
        publicKeys[2*n] = keyPair.getPublic();
        secretKeys[2*n] = keyPair.getPrivate();

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new GVW13PublicKeyParameters(parameters, publicKeys),
                new GVW13MasterSecretKeyParameters(parameters, secretKeys)
        );
    }


}
