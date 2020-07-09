package it.unisa.dia.gas.crypto.cipher;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MultiBlockAsymmetricBlockCipher implements AsymmetricBlockCipher {

    protected final AsymmetricBlockCipher cipher;
    protected final BlockCipherPadding padding;
    protected boolean forEncryption;

    public MultiBlockAsymmetricBlockCipher(final AsymmetricBlockCipher cipher) {
        this(cipher, null);
    }

    public MultiBlockAsymmetricBlockCipher(final AsymmetricBlockCipher cipher, final BlockCipherPadding padding) {
        this.cipher = cipher;
        this.padding = padding;
    }


    public void init(final boolean forEncryption, final CipherParameters parameters) {
        this.forEncryption = forEncryption;
        cipher.init(forEncryption, parameters);
        if (padding != null) {
            if (parameters instanceof ParametersWithRandom) {
                ParametersWithRandom p = (ParametersWithRandom) parameters;
                padding.init(p.getRandom());
            } else
                padding.init(new SecureRandom());
        }
    }

    public int getInputBlockSize() {
        return cipher.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        return cipher.getOutputBlockSize();
    }

    public byte[] processBlock(final byte[] in, final int inOff, final int len) throws InvalidCipherTextException {

        final int inputBlockSize = getInputBlockSize();
        int inputOffset = inOff;
        final byte[] buffer = new byte[inputBlockSize];
        final int outputBlockSize = getOutputBlockSize();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream((len / inputBlockSize + (len % inputBlockSize == 0 ? 0 : 1)) * outputBlockSize);

        while (inputOffset < len) {

            // Copy next piece of input...
            int bufLength = inputBlockSize;
            if (inputOffset + inputBlockSize > len) {
                bufLength = len - inputOffset;
            }
            System.arraycopy(in, inputOffset, buffer, 0, bufLength);
            inputOffset += bufLength;

            // Apply padding if necessary
            if (padding != null && forEncryption && bufLength < inputBlockSize) {
                padding.addPadding(buffer, bufLength);
                bufLength = inputBlockSize;
            }

            // Produce output for the current piece...
            final byte[] outputBlock = cipher.processBlock(buffer, 0, bufLength);
            try {
                outputStream.write(outputBlock);
            } catch (IOException e) {
                e.printStackTrace();
                throw new InvalidCipherTextException(e.getMessage());
            }
        }

        final byte[] output = outputStream.toByteArray();

        // Apply padding if necessary
        if (padding != null && !forEncryption) {
            final int padCount;
            try {
                padCount = padding.padCount(output);
                if (padCount > 0)
                    return Arrays.copyOf(output, output.length - padCount);
            } catch (InvalidCipherTextException e) {
                if (inputBlockSize % outputBlockSize != 0)
                    throw e;
            }
        }

        return output;
    }

}
