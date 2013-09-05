package it.unisa.dia.gas.crypto.kem;

import org.bouncycastle.crypto.AsymmetricBlockCipher;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface KeyEncapsulationMechanism extends AsymmetricBlockCipher{

    int getKeyBlockSize();

}
