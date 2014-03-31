package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianRSDoubleSampler implements Sampler<BigInteger> {

    protected SecureRandom random;
    protected double sigma;
    protected double center;

    protected double h, normalization, sigmaTau;
    protected int left, interval;

    protected int tau = 13;

    public ZGaussianRSDoubleSampler(SecureRandom random, double sigma, double center) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.sigma = sigma;
        this.center = center;

        this.h = - (Math.PI / (sigma * sigma));
//        this.h = - (1 / (2 * (sigma * sigma)));
//        this.normalization = Math.sqrt(2 * Math.PI) * sigma;
//        this.normalization = 1;
        this.normalization = sigma;

        System.out.println("normalization = " + normalization);
//        this.normalization = 1.0d;
        this.sigmaTau = sigma * tau;

        setCenter(center);
    }

    public ZGaussianRSDoubleSampler(SecureRandom random, double sigma) {
        this(random, sigma, 0.0d);
    }

    public ZGaussianRSDoubleSampler(SecureRandom random, Apfloat sigma) {
        this(random, sigma.doubleValue(), 0.0d);
    }


    public ZGaussianRSDoubleSampler setCenter(double center) {
        this.center = center;
        this.interval = (int) (Math.ceil(center + sigmaTau) -  Math.floor(center - sigmaTau)) + 1;
        this.left = (int) Math.floor(center - sigmaTau);

        double sum =0.0d;
        for (int i =left; i<  left + interval; i++ ){
            sum+=Math.exp(h * ((i * i) - center));
        }

        System.out.println("sum = " + sum);

//        System.out.println("left = " + left);
//        System.out.println("interval = " + interval);

        return this;
    }

    public BigInteger sample() {
        while (true) {
            int x = left + random.nextInt(interval);

            double rhos = Math.exp(h * ((x * x) - center)) / normalization;
            double sample = random.nextDouble();

            if (sample <= rhos)
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
        ZGaussianRSDoubleSampler sampler = new ZGaussianRSDoubleSampler(
                new SecureRandom(), 9.0d
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
