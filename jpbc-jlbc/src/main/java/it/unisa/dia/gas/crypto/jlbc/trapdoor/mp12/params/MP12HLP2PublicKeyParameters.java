package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2PublicKeyParameters extends MP12PLP2PublicKeyParameters {

    protected Element A;
    protected int m, mInBytes;


    public MP12HLP2PublicKeyParameters(MP12Parameters parameters, int k, int m,
                                       Sampler<BigInteger> sampler,
                                       Matrix G,
                                       Field syndromeField, Field Zq,
                                       VectorField<Field> preimageField,
                                       Element A) {
        super(parameters, k, sampler, G, syndromeField, Zq, preimageField);

        this.A = A;
        this.m = m;
        this.mInBytes = (m + 7) / 8;
    }

    public Element getA() {
        return A;
    }

    public int getM() {
        return m;
    }

    public int getmInBytes() {
        return mInBytes;
    }
}
