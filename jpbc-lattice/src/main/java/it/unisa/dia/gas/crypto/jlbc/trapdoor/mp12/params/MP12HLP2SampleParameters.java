package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2SampleParameters extends MP12KeyParameters {

    protected AsymmetricCipherKeyPair keyPair;

    public MP12HLP2SampleParameters(AsymmetricCipherKeyPair keyPair) {
        super(true, null);

        this.keyPair = keyPair;
    }

    public MP12HLP2SampleParameters(CipherParameters pk, CipherParameters sk) {
        super(true, null);

        this.keyPair = new AsymmetricCipherKeyPair(pk, sk);
    }


    public AsymmetricCipherKeyPair getKeyPair() {
        return keyPair;
    }
}
