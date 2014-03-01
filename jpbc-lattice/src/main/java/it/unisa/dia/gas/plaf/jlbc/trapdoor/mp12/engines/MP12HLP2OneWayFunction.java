package it.unisa.dia.gas.plaf.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jlbc.trapdoor.mp12.params.MP12HLP2OneWayFunctionParameters;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2OneWayFunction extends AbstractElementCipher {

    protected MP12HLP2OneWayFunctionParameters parameters;

    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12HLP2OneWayFunctionParameters) param;

        return this;
    }

    public Element processElements(Element... input) {
        Vector vector = (Vector) parameters.getPk().getA().mul(input[0]);

        Vector error = (Vector) vector.getField().newElement();
        for (int i = 0; i < vector.getSize(); i++)
            error.getAt(i).set(parameters.getSampler().sample());

        return vector.add(error);
    }

}
