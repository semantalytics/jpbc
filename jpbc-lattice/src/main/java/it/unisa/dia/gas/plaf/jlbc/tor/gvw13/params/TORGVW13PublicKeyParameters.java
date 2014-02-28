package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13PublicKeyParameters extends TORGVW13KeyParameters {

    private CipherParameters latticePublicKey;
    private ElementCipher owf;
    private Field owfInputField, owfOutputField;
    private ElementCipher otp;

    public TORGVW13PublicKeyParameters(TORGVW13Parameters parameters,
                                       CipherParameters latticePublicKey,
                                       ElementCipher owf,
                                       Field owfInputField,
                                       Field owfOutputField,
                                       ElementCipher otp) {
        super(false, parameters);

        this.latticePublicKey = latticePublicKey;
        this.owf = owf;
        this.owfInputField = owfInputField;
        this.owfOutputField = owfOutputField;
        this.otp = otp;
    }

    public CipherParameters getLatticePublicKey() {
        return latticePublicKey;
    }

    public ElementCipher getOwf() {
        return owf;
    }

    public Field getOwfInputField() {
        return owfInputField;
    }

    public Field getOwfOutputField() {
        return owfOutputField;
    }

    public ElementCipher getOtp() {
        return otp;
    }
}
