package it.unisa.dia.gas.crypto.jpbc.kem;

import it.unisa.dia.gas.crypto.jpbc.cipher.AbstractAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractKeyEncapsulationMechanism extends AbstractAsymmetricBlockCipher implements KeyEncapsulationMechanism {

    private static byte[] EMPTY_BYTE_ARRAY = new byte[0];

    protected int keyBytes = 0;

    public int getKeyBlockSize() {
        return keyBytes;
    }

    public int getInputBlockSize() {
        if (forEncryption) {
            return 0;
        } else {
            return outBytes - keyBytes;
        }
    }

    public int getOutputBlockSize() {
        if (forEncryption) {
            return outBytes;
        } else {
            return keyBytes;
        }
    }

    public byte[] processBlock(byte[] in) throws InvalidCipherTextException {
        return processBlock(in, 0, in.length);
    }

    public byte[] process() throws InvalidCipherTextException {
        return processBlock(EMPTY_BYTE_ARRAY, 0, 0);
    }
}
