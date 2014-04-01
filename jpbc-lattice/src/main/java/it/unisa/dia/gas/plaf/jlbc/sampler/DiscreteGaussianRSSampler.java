package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.*;
import static org.apfloat.ApfloatMath.exp;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DiscreteGaussianRSSampler implements GaussianSampler<BigInteger> {

    protected SecureRandom random;
    protected Apfloat sigma;
    protected Apfloat center;
    protected int precision;

    protected Apfloat h, sigmaTau;
    protected int left, interval;

    protected Apint tau = ApfloatUtils.ITWELVE;
//    protected Apfloat normalization;

    protected Sampler<Apfloat> uniform;

    public DiscreteGaussianRSSampler(SecureRandom random, Apfloat gaussianParameter, Apfloat center, int precision) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.sigma = gaussianParameter.divide(SQRT2PI);
        this.precision = precision;
        this.center = center;

        this.h = ApfloatMath.pi(precision, 2).divide(square(gaussianParameter)).negate();
//        this.h = ONE.divide(TWO.multiply(square(sigma))).negate();
        this.sigmaTau = sigma.multiply(tau);
        this.uniform = new UniformSampler(random);

        setCenter(center);
    }

    public DiscreteGaussianRSSampler(SecureRandom random, Apfloat gaussianParameter, int precision) {
        this(random, gaussianParameter, IZERO, precision);
    }

    public DiscreteGaussianRSSampler(SecureRandom random, Apfloat gaussianParameter) {
        this(random, gaussianParameter, IZERO, ApfloatUtils.precision);
    }



    public DiscreteGaussianRSSampler setCenter(Apfloat center) {
        this.center = center;
        this.interval = center.add(sigmaTau).ceil().subtract(center.subtract(sigmaTau).floor()).intValue() + 1;
        this.left = center.subtract(sigmaTau).floor().intValue();
//        System.out.println("interval = " + interval);

//        normalization = ZERO;
//        for (int i = left; i < left + interval; i++) {
//            normalization = normalization.add(exp(h.multiply(square(newApint(i).subtract(center)))));
//        }
//        System.out.println("toString(normalization) = " + ApfloatUtils.toString(normalization));

        return this;
    }

    public BigInteger sample() {
        while (true) {
            int x = left + random.nextInt(interval);

            Apfloat rhos = exp(h.multiply(square(newApint(x).subtract(center))));
            Apfloat sample = uniform.sample();

            if (sample.compareTo(rhos) <= 0) {
                return BigInteger.valueOf(x);
            }
        }
    }

}
