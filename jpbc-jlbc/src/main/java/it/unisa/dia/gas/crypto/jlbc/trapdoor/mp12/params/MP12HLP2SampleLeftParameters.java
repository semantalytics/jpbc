package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2SampleLeftParameters extends MP12HLP2SampleParameters {

    public MP12HLP2SampleLeftParameters(AsymmetricCipherKeyPair keyPair) {
        super(keyPair);
    }

    public MP12HLP2SampleLeftParameters(CipherParameters pk, CipherParameters sk) {
        super(pk, sk);
    }
}
