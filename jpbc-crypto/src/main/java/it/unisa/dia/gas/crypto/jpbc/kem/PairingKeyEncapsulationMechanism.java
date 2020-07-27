package it.unisa.dia.gas.crypto.jpbc.kem;

import it.unisa.dia.gas.crypto.jpbc.cipher.PairingAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class PairingKeyEncapsulationMechanism extends PairingAsymmetricBlockCipher implements KeyEncapsulationMechanism {

    private final static byte[] EMPTY_BYTE_ARRAY = new byte[0];
    protected int keyBytes = 0;

    @Override
    public int getKeyBlockSize() {
        return keyBytes;
    }

    @Override
    public int getInputBlockSize() {
        if (forEncryption) {
            return 0;
        } else {
            return outBytes - keyBytes;
        }
    }

    @Override
    public int getOutputBlockSize() {
        if (forEncryption) {
            return outBytes;
        } else {
            return keyBytes;
        }
    }

    @Override
    public byte[] processBlock(byte[] in) throws InvalidCipherTextException {
        return processBlock(in, 0, in.length);
    }

    @Override
    public byte[] process() throws InvalidCipherTextException {
        return processBlock(EMPTY_BYTE_ARRAY, 0, 0);
    }
}