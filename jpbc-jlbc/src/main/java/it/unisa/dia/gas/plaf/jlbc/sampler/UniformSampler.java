package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class UniformSampler implements Sampler<Apfloat> {

    protected SecureRandom random;
    protected Apfloat[] powers;

    public UniformSampler(SecureRandom random) {
        this.random = random;

        powers = new Apfloat[ApfloatUtils.precision];
        Apfloat power = ApfloatUtils.TWO;
        for (int i = 0; i < ApfloatUtils.precision; i++) {
            powers[i] = ApfloatUtils.ONE.divide(power);
            power = power.multiply(ApfloatUtils.TWO);
        }
    }

    public Apfloat sample() {
        Apfloat value = ApfloatUtils.ZERO;
        for (int i = 0; i < ApfloatUtils.precision; i++) {
            if (random.nextBoolean())
                value = value.add(powers[i]);
        }
        return value;
    }

}
