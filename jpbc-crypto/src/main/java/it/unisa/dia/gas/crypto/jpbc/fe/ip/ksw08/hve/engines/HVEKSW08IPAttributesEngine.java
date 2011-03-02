package it.unisa.dia.gas.crypto.jpbc.fe.ip.ksw08.hve.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.ksw08.hve.params.HVEKSW08IPEncryptionParameters;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEKSW08IPAttributesEngine implements AsymmetricBlockCipher {

    protected AsymmetricBlockCipher ip;

    protected CipherParameters cipherParameters;

    protected boolean forEncryption;
    protected int n;
    protected Field Zr;
    protected byte[] zero, one;
    protected int lenInBytes;

    protected int inBytes;

    public HVEKSW08IPAttributesEngine(AsymmetricBlockCipher ip) {
        this.ip = ip;
    }


    public void init(boolean forEncryption, CipherParameters parameters) {
        this.forEncryption = forEncryption;

        if (forEncryption) {
            if (!(parameters instanceof HVEKSW08IPEncryptionParameters)) {
                throw new IllegalArgumentException("HVEKSW08IPEncryptionParameters are required for encryption.");
            }

            HVEKSW08IPEncryptionParameters encParam = (HVEKSW08IPEncryptionParameters) parameters;
            this.n = encParam.getN();
            this.Zr = encParam.getZr();
            this.lenInBytes = Zr.getLengthInBytes();
            this.inBytes = n * lenInBytes;

            this.zero = Zr.newZeroElement().toBytes();
            this.one = Zr.newOneElement().toBytes();

            ip.init(forEncryption, encParam.getPublicKey());
        } else {
            ip.init(forEncryption, parameters);
        }
    }

    public int getInputBlockSize() {
        return ip.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return ip.getOutputBlockSize();
    }

    public byte[] processBlock(byte[] bytes, int inOff, int inLen) throws InvalidCipherTextException {
        if (forEncryption) {
            HVEKSW08IPEncryptionParameters encParam = (HVEKSW08IPEncryptionParameters) cipherParameters;

            // Move from hve to ip
            byte[] input = new byte[inBytes * 2];
            int offset = 0;

            for (int i = 0; i < n; i++) {
                if (encParam.getPatternAt(i) != -1) {
                    // This is not a star
                    System.arraycopy(one, 0, input, offset, one.length);
                    offset += one.length;

                    System.arraycopy(bytes, inOff, input, offset, lenInBytes);
                    inOff += lenInBytes;
                } else {
                    // This is a star
                    System.arraycopy(zero, 0, input, offset, zero.length);
                    offset += zero.length;

                    System.arraycopy(zero, 0, input, offset, zero.length);
                    offset += zero.length;
                }
            }

            return ip.processBlock(input, 0, input.length);
        }

        return new byte[0];
    }
}
