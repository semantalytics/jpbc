package it.unisa.dia.gas.crypto.engines.kem;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipher {
    protected Cipher cipher;
    protected KeyEncapsulationMechanism kem;

    public KEMCipher(Cipher cipher, KeyEncapsulationMechanism kem) {
        this.cipher = cipher;
        this.kem = kem;
    }

    public byte[] init(boolean forEncryption, CipherParameters cipherParameters) throws GeneralSecurityException, CryptoException {
        int strength = (128 + 7) / 8;

        byte[] key, ct;
        if (forEncryption) {
            // encaps key
            kem.init(forEncryption, cipherParameters);
            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            key = Arrays.copyOfRange(ciphertext, 0, strength);
            ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);
        } else {
            // decaps key
            KEMCipherDecryptionParameters parameters = (KEMCipherDecryptionParameters) cipherParameters;
            byte[] encapsulation = parameters.getEncapsulation();

            kem.init(forEncryption, parameters.getCipherParameters());
            key = Arrays.copyOfRange(kem.processBlock(encapsulation, 0, encapsulation.length), 0, strength);
            ct = null;
        }

        cipher.init(
                forEncryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                new SecretKeySpec(key, "AES")
        );

        return ct;
    }


    public byte[] update(byte[] bytes) {
        return cipher.update(bytes);
    }


    public byte[] update(byte[] bytes, int i, int i1) {
        return cipher.update(bytes, i, i1);
    }


    public int update(byte[] bytes, int i, int i1, byte[] bytes1) throws ShortBufferException {
        return cipher.update(bytes, i, i1, bytes1);
    }


    public int update(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
        return cipher.update(bytes, i, i1, bytes1, i2);
    }


    public int update(ByteBuffer byteBuffer, ByteBuffer byteBuffer1) throws ShortBufferException {
        return cipher.update(byteBuffer, byteBuffer1);
    }


    public byte[] doFinal() throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal();
    }

    public int doFinal(byte[] bytes, int i) throws IllegalBlockSizeException, ShortBufferException, BadPaddingException {
        return cipher.doFinal(bytes, i);
    }


    public byte[] doFinal(byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes);
    }

    public byte[] doFinal(byte[] bytes, int i, int i1) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes, i, i1);
    }

    public int doFinal(byte[] bytes, int i, int i1, byte[] bytes1) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes, i, i1, bytes1);
    }

    public int doFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(bytes, i, i1, bytes1, i2);
    }

    public int doFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer1) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(byteBuffer, byteBuffer1);
    }

    public Provider getProvider() {
        return cipher.getProvider();
    }

    public String getAlgorithm() {
        return cipher.getAlgorithm();
    }

    public int getBlockSize() {
        return cipher.getBlockSize();
    }

    public int getOutputSize(int i) {
        return cipher.getOutputSize(i);
    }

    public byte[] getIV() {
        return cipher.getIV();
    }

    public AlgorithmParameters getParameters() {
        return cipher.getParameters();
    }

    public ExemptionMechanism getExemptionMechanism() {
        return cipher.getExemptionMechanism();
    }

    public void init(int i, Key key) throws InvalidKeyException {
        cipher.init(i, key);
    }

    public void init(int i, Key key, SecureRandom random) throws InvalidKeyException {
        cipher.init(i, key, random);
    }

    public void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(i, key, algorithmParameterSpec);
    }

    public void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(i, key, algorithmParameterSpec, random);
    }

    public void init(int i, Key key, AlgorithmParameters algorithmParameters) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(i, key, algorithmParameters);
    }

    public void init(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(i, key, algorithmParameters, random);
    }

    public void init(int i, Certificate certificate) throws InvalidKeyException {
        cipher.init(i, certificate);
    }

    public void init(int i, Certificate certificate, SecureRandom random) throws InvalidKeyException {
        cipher.init(i, certificate, random);
    }
}
