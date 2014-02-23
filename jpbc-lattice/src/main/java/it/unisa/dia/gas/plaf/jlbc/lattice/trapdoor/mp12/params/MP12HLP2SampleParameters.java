package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2SampleParameters extends MP12KeyParameters {

    protected AsymmetricCipherKeyPair keyPair;

    public MP12HLP2SampleParameters(AsymmetricCipherKeyPair keyPair) {
        super(true, null);

        this.keyPair = keyPair;
    }

    public AsymmetricCipherKeyPair getKeyPair() {
        return keyPair;
    }
}
