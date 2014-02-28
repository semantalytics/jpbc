package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorElement;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2SampleD extends AbstractElementCipher {

    protected MP12PLP2PublicKeyParameters parameters;
    protected int n, k;

    public ElementCipher init(CipherParameters param) {
        this.parameters = (MP12PLP2PublicKeyParameters) param;
        this.n = parameters.getParameters().getN();
        this.k = parameters.getK();

        return this;
    }

    public Element processElements(Element... input) {
        VectorElement syndrome = (VectorElement) input[0];
        if (syndrome.getSize() != n)
            throw new IllegalArgumentException("Invalid syndrome length.");

        VectorElement r = parameters.getPreimageField().newElement();

        for (int i = 0, base = 0; i < n; i++) {

            BigInteger u = syndrome.getAt(i).toBigInteger();

            for (int j = 0; j < k; j++) {
                BigInteger xj = sampleZ(u);
                r.getAt(base + j).set(xj);

                u = u.subtract(xj).divide(BigIntegerUtils.TWO);
            }

            base += k;
        }

        return r;
    }

    private BigInteger sampleZ(BigInteger u) {
        boolean uLSB = u.testBit(0);

        while (true) {
            BigInteger x = parameters.getSampler().sample();
            if (x.testBit(0) ==  uLSB)
                return x;
        }
    }


}
