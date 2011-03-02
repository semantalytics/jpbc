package it.unisa.dia.gas.crypto.jpbc.fe.ip.ksw08.hve.params;

import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEKSW08IPEncryptionParameters implements CipherParameters {
    private CipherParameters publicKey;
    private Field Zr;
    private int n;

    private byte[] pattern;


    public HVEKSW08IPEncryptionParameters(CipherParameters publicKey, int n, Field Zr, byte[] pattern) {
        this.publicKey = publicKey;
        this.n = n;
        this.Zr = Zr;
        this.pattern = Arrays.copyOf(pattern, pattern.length);
    }


    public CipherParameters getPublicKey() {
        return publicKey;
    }

    public int getN() {
        return n;
    }

    public Field getZr() {
        return Zr;
    }

    public byte getPatternAt(int index) {
        return pattern[index];
    }
}
