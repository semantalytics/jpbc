package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.generators;


import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private TORGVW13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        AsymmetricCipherKeyPair latticeKeyPair  = params.getLatticeKeyPairGenerator().generateKeyPair();


        return new AsymmetricCipherKeyPair(
                new TORGVW13PublicKeyParameters(
                        params.getParameters(),
                        latticeKeyPair.getPublic()),
                null
        );
    }


}
