package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2Inverter extends AbstractElementCipher {

    protected MP12PLP2PublicKeyParameters parameters;
    protected BigInteger oneFourthOrder;
    protected int n, k;


    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12PLP2PublicKeyParameters) param;

        this.oneFourthOrder = parameters.getZq().getOrder().divide(BigIntegerUtils.FOUR);
        this.n = parameters.getParameters().getN();
        this.k =  parameters.getK();

        return this;
    }

    public Element processElements(Element... input) {
        Vector v = (Vector) input[0];

        Vector result = (Vector) parameters.getSyndromeField().newZeroElement();

        for (int i = 0, cursor = 0; i < n; i++) {
            Element s = result.getAt(i);

            for (int j = k - 1; j >= 0; j--) {
                Element temp = parameters.getG().getAt(0, j).duplicate().mul(s).negate().add(v.getAt(cursor + j));

                BigInteger value = temp.toBigInteger().add(temp.getField().getOrder());
                boolean b1 = value.testBit(k - 1);
                boolean b2 = value.testBit(k - 2);
                if ((b1 || b2 ) && (!(b1 && b2)))
//                if (temp.toBigInteger().abs().compareTo(oneFourthOrder) >= 0)
                    s.add(parameters.getG().getAt(0, k - 1 - j));

//                e.getAt(j).set(v.getAt(j)).sub(parameters.getG().getAt(0, j));
            }

            cursor += k;
        }


        return result;
    }

}
