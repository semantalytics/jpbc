package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13EncryptParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13RecodeParameters;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class TORGVW13Engine extends AbstractElementCipher {

    private CipherParameters param;

    public ElementCipher init(CipherParameters param) {
        this.param = param;

        return this;
    }

    @Override
    public ElementCipher init(Element key) {
        return init(new TORGVW13EncryptParameters((TORGVW13PublicKeyParameters) param, key));
    }

    public Element processBytes(byte[] buffer) {
        if (param instanceof TORGVW13EncryptParameters) {
            TORGVW13EncryptParameters keyParameters = (TORGVW13EncryptParameters) param;

            return keyParameters.getPublicKeyParameters().getOtp().init(
                    keyParameters.getKey()
            ).processBytes(buffer);
        } else
            throw new IllegalArgumentException("Invalid params!!!");
    }

    @Override
    public byte[] processElementsToBytes(Element... input) {
        if (param instanceof TORGVW13EncryptParameters) {
            TORGVW13EncryptParameters keyParameters = (TORGVW13EncryptParameters) param;

            return keyParameters.getPublicKeyParameters().getOtp().init(
                    keyParameters.getKey()
            ).processElementsToBytes(input[0]);
        } else
            throw new IllegalArgumentException("Invalid params!!!");
    }

    public Element processElements(Element... input) {
        if (param instanceof TORGVW13PublicKeyParameters) {
            TORGVW13PublicKeyParameters keyParameters = (TORGVW13PublicKeyParameters) param;

            return keyParameters.getOwf().processElements(input[0]);
        } else if (param instanceof TORGVW13RecodeParameters) {
            TORGVW13RecodeParameters keyParameters = (TORGVW13RecodeParameters) param;

            return VectorField.union(input[0], input[1]).mul(keyParameters.getR());
        } else
            throw new IllegalArgumentException("Invalid params!!!");
    }

}
