package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2Decoder implements ElementCipher {

    protected MP12HLP2PublicKeyParameters parameters;

    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12HLP2PublicKeyParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        return parameters.getA().mul(input[0]);
    }

}
