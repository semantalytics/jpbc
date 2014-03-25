package it.unisa.dia.gas.plaf.jlbc.sampler;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianRejectionSampler implements Sampler<BigInteger> {

    protected SecureRandom random;
    protected Apfloat sigma;
    protected int precision;
    protected Apfloat center;

    protected Apfloat h, sigmaTau;
    protected int left, interval;

    protected Apint tau = new Apint(12, 2);

    public ZGaussianRejectionSampler(SecureRandom random, Apfloat sigma, Apfloat center, int precision) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.sigma = sigma;
        this.precision = precision;
        this.center = center;

        this.h = ApfloatMath.pi(precision, 2).divide(ApfloatMath.pow(sigma, 2)).negate();
        this.sigmaTau = sigma.multiply(tau);

        setCenter(center);
    }

    public ZGaussianRejectionSampler(SecureRandom random, Apfloat sigma, int precision) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.sigma = sigma;
        this.precision = precision;

        this.h = ApfloatMath.pi(precision, 2).divide(ApfloatMath.pow(sigma, 2)).negate();
        this.sigmaTau = sigma.multiply(tau);
    }


    public ZGaussianRejectionSampler setCenter(Apfloat center) {
        this.center = center;
        this.interval = center.add(sigmaTau).ceil().subtract(center.subtract(sigmaTau).floor()).intValue();
        this.left = center.subtract(sigmaTau).floor().intValue();

        System.out.println("left = " + left);
        System.out.println("interval = " + interval);

        return this;
    }

    public BigInteger sample() {
        while (true) {
            int x = left + random.nextInt(interval);
//            System.out.println("x = " + x);

            Apfloat rhos = ApfloatMath.exp(h.multiply(ApfloatMath.pow(new Apint(x, 2).subtract(center), 2)));
            double sample = random.nextDouble();

//            System.out.println("rhos = " + rhos.toRadix(10).toString(true));
//            System.out.println("sample = " + sample);

            if (sample <= rhos.doubleValue())
                return BigInteger.valueOf(x);
        }
    }

    public static void main(String[] args) {
        ZGaussianRejectionSampler sampler = new ZGaussianRejectionSampler(
                new SecureRandom(),
                new Apfloat("5", 128).toRadix(2),
                new Apfloat("-10.3", 128).toRadix(2),
                128
        );

        System.out.println("sampler.sample() = " + sampler.sample());
    }

}
