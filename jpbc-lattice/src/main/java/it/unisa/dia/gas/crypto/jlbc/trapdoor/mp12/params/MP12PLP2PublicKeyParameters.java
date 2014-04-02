package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2PublicKeyParameters extends MP12KeyParameters {

    protected int k;
    protected Sampler<BigInteger> discreteGaussianSampler;

    protected Vector g; // primitive vector
    protected Matrix G; // parity-check matrix

    protected Field syndromeField;

    protected Field Zq;
    protected VectorField<Field> preimageField;


    public MP12PLP2PublicKeyParameters(MP12Parameters parameters,
                                       int k,
                                       Sampler<BigInteger> discreteGaussianSampler,
                                       Vector g, Matrix G,
                                       Field syndromeField,
                                       Field Zq,
                                       VectorField<Field> preimageField) {
        super(false, parameters);

        this.k = k;
        this.discreteGaussianSampler = discreteGaussianSampler;
        this.g = g;
        this.G = G;
        this.syndromeField = syndromeField;
        this.Zq = Zq;
        this.preimageField = preimageField;
    }

    public int getK() {
        return k;
    }

    public VectorField<Field> getPreimageField() {
        return preimageField;
    }

    public Sampler<BigInteger> getDiscreteGaussianSampler() {
        return discreteGaussianSampler;
    }

    public Field getSyndromeField() {
        return syndromeField;
    }

    public Matrix getG() {
        return G;
    }

    public Field getZq() {
        return Zq;
    }
}
