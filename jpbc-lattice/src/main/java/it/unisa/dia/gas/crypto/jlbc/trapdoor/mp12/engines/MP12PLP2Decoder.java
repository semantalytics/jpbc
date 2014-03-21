package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2Decoder extends AbstractElementCipher {

    protected MP12PLP2PublicKeyParameters parameters;

    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12PLP2PublicKeyParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        return parameters.getG().mul(input[0]);
    }

}
