package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2InverterParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2Inverter extends MP12PLP2Inverter {

    protected MP12HLP2InverterParameters parameters;


    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12HLP2InverterParameters) param;
        super.init(parameters.getPk());

        return this;
    }

    public Element processElements(Element... input) {
        Vector b = (Vector) input[0];

        Vector hatB = (Vector) parameters.getSk().getR().mul(b.subVectorTo(n * 2));

        for (int i = 0; i < n * k; i++) {
            hatB.getAt(i).add(b.getAt(2 * n + i));
        }

        System.out.println("hatB = " + hatB);

        Element s = super.processElements(hatB);

        return s;
    }
}
