package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13RecodeParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class TORGVW13Engine implements ElementCipher {

    private CipherParameters param;

    public ElementCipher init(CipherParameters param) {
        this.param = param;

        return this;
    }


    public Element processElements(Element... input) throws InvalidCipherTextException {
        if (param instanceof TORGVW13PublicKeyParameters) {
            TORGVW13PublicKeyParameters keyParameters = (TORGVW13PublicKeyParameters) param;

            return keyParameters.getEncoder().processElements(input[0]);
        } else {
            TORGVW13RecodeParameters keyParameters = (TORGVW13RecodeParameters) param;

            // Read Input
            Element c0 = input[0];
            Element c1 = input[1];

            // Recode
            return null;
       }

    }
}
