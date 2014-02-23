package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2PublicKeyParameters extends MP12PLP2PublicKeyParameters {

    protected Element A;
    protected Element barA;

    public MP12HLP2PublicKeyParameters(MP12Parameters parameters, int k, Sampler<BigInteger> sampler, VectorElement g, MatrixElement G, Field syndromeField, ZrField Zq, VectorField<ZrField> preimageField, Element A, Element barA) {
        super(parameters, k, sampler, g, G, syndromeField, Zq, preimageField);

        this.A = A;
        this.barA = barA;
    }

    public Element getA() {
        return A;
    }
}
