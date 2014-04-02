package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jlbc.field.floating.FloatingElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DiscreteGaussianCOVSampler implements Sampler<Vector> {

    protected SecureRandom random;
    protected Matrix cov;
    protected Sampler<Vector> sampler;
    protected Field target;
    protected GaussianSampler<BigInteger> roundingSampler;


    public DiscreteGaussianCOVSampler(SecureRandom random, Matrix cov, Field target, Apfloat roundingGaussianParameter) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.cov = cov;
        this.sampler = new ContinuousGaussianSamplerVectorSampler(random, 128, cov.getN());
        this.target = new VectorField<Field>(random, target, cov.getN());
        this.roundingSampler = new DiscreteGaussianRSDoubleSampler(random, roundingGaussianParameter);
    }


    public Vector sample() {
        Vector sample = (Vector) cov.mul(sampler.sample());

        Vector result = (Vector) target.newElement();
        for (int i = 0, n = result.getSize(); i < n; i++) {
            result.getAt(i).set(
                    roundingSampler.setCenter(
                            ((FloatingElement) sample.getAt(i)).getValue().negate()
                    ).sample()
            );
        }

        return result;
    }
}
