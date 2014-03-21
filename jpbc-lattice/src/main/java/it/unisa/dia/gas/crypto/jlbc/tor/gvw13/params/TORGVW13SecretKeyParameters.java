package it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13SecretKeyParameters extends TORGVW13KeyParameters {

    private CipherParameters latticePrivateKey;
    private Field owfInputField;

    public TORGVW13SecretKeyParameters(TORGVW13Parameters parameters, CipherParameters latticePrivateKey, Field owfInputField) {
        super(true, parameters);

        this.latticePrivateKey = latticePrivateKey;
        this.owfInputField = owfInputField;
    }

    public CipherParameters getLatticePrivateKey() {
        return latticePrivateKey;
    }

    public Field getOwfInputField() {
        return owfInputField;
    }
}
