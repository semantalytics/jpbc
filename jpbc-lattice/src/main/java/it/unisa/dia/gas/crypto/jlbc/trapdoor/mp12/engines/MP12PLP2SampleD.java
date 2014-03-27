package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;
import org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2SampleD extends AbstractElementCipher {

    protected MP12PLP2PublicKeyParameters parameters;
    protected int n, k;
    protected Queue<BigInteger> zero, one;


    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12PLP2PublicKeyParameters) param;

        this.n = parameters.getParameters().getN();
        this.k = parameters.getK();
        this.zero = new ArrayDeque<BigInteger>();
        this.one = new ArrayDeque<BigInteger>();

        return this;
    }

    public Element processElements(Element... input) {
        Vector syndrome = (Vector) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        Vector r = parameters.getPreimageField().newElement();

        for (int i = 0, base = 0; i < n; i++) {

            BigInteger u = syndrome.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = sampleZ(u);
                r.getAt(base + j).set(xj);

                u  = u.subtract(xj).shiftRight(1);
            }

            base += k;
        }

        return r;
    }

    private BigInteger sampleZ(BigInteger u) {
        boolean uLSB = u.testBit(0);

        if (uLSB && one.size() > 0)
            return one.poll();
        else  if (!uLSB && zero.size() > 0)
            return zero.poll();
        else {
            while (true) {
                BigInteger x = parameters.getZSampler().sample();
                boolean xLSB = x.testBit(0);
                if (xLSB == uLSB)
                    return x;
                else {
                    if (xLSB)
                        one.add(x);
                    else
                        zero.add(x);
                }
            }
        }
    }


}
