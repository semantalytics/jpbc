package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.newApfloat;
import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.newApint;
import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.square;
import static org.apfloat.ApfloatMath.exp;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianRSSampler implements Sampler<BigInteger> {

    protected SecureRandom random;
    protected Apfloat sigma;
    protected Apfloat center;
    protected int precision;

    protected Apfloat h, sigmaTau;
    protected int left, interval;

    protected Apint tau = ApfloatUtils.ITWELVE;

    public ZGaussianRSSampler(SecureRandom random, Apfloat sigma, Apfloat center, int precision) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.sigma = sigma;
        this.precision = precision;
        this.center = center;

        this.h = ApfloatMath.pi(precision, 2).divide(square(sigma)).negate();
        this.sigmaTau = sigma.multiply(tau);

        setCenter(center);
    }

    public ZGaussianRSSampler(SecureRandom random, Apfloat sigma, int precision) {
        this(random, sigma, ApfloatUtils.IZERO, precision);
    }

    public ZGaussianRSSampler(SecureRandom random, Apfloat sigma) {
        this(random, sigma, ApfloatUtils.IZERO, ApfloatUtils.precision);
    }

    public ZGaussianRSSampler setCenter(Apfloat center) {
        this.center = center;
        this.interval = center.add(sigmaTau).ceil().subtract(center.subtract(sigmaTau).floor()).intValue() + 1;
        this.left = center.subtract(sigmaTau).floor().intValue();

//        System.out.println("left = " + left);
//        System.out.println("interval = " + interval);

        return this;
    }

    public BigInteger sample() {
        while (true) {
            int x = left + random.nextInt(interval);
//            System.out.println("x = " + x);

            Apfloat rhos = exp(h.multiply(square(newApint(x).subtract(center))));
            // TODO: sample with given precision
            double sample = random.nextDouble();
//            System.out.println("sample = " + sample);
//            System.out.println("rhos.doubleValue() = " + rhos.doubleValue());

            if (sample <= rhos.doubleValue())
                return BigInteger.valueOf(x);
        }
    }

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        int n = 4;
        int k = 16;


        int nn = 10;
        int mm = 10;
        BigInteger q = BigInteger.ONE.shiftLeft(k);

        Field Zq = new SymmetricZrField(q);
        ZGaussianRSSampler sampler = new ZGaussianRSSampler(
                new SecureRandom(), newApfloat(9)
        );

        MatrixField<Field> RField = new MatrixField<Field>(random, Zq, nn, mm);
        Matrix R = RField.newElement();
        for (int i = 0; i < nn; i++) {
            for (int j = 0; j < mm; j++) {
                R.getAt(i, j).set(sampler.sample());
            }
        }

        System.out.println("R = " + R);
    }

}
