package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils;

import it.unisa.dia.gas.plaf.jlbc.sampler.SamplerFactory;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.*;
import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.sqrt;
import static org.apfloat.ApfloatMath.sqrt;

/**
 * Trapdoor for Lattices: Simpler, Tighter, Faster, Smaller.
 * Micciancio and Peikert
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class LatticeUtils {

    /**
     * Randomized-rounding parameter
     * r \approx sqrt(ln(2/epsilon)/pi)
     * for epsilon \approx 2^-90.
     */
    public static Apfloat RRP = convert(new Apfloat("4.5"));
    public static Apfloat RRP_SQUARE = square(RRP);

    public static Apfloat SQRT_TWO = sqrt(TWO);
    public static Apfloat SQRT_TWO_PI = sqrt(pi().multiply(ITWO));


    public static Apfloat getLWENoiseParameter(int n) {
        return SQRT_TWO.multiply(
                ITWO.multiply(sqrt(newApfloat(n)))
        ).multiply(RRP_SQUARE);
    }

    public static Sampler<BigInteger> getLWENoiseSampler(SecureRandom random, int n) {
        return SamplerFactory.getInstance().getDiscreteGaussianSampler(random, getLWENoiseParameter(n));
    }

    /**
     *
     * @param n
     * @param m
     * @return
     */
    public static Apfloat getS1R(int n, int m) {
        return getS1R(getLWENoiseParameter(n),n ,m);
    }

    public static Apfloat getS1R(Apfloat gaussianParamenter, int n, int m) {
        return gaussianParamenter.multiply(
                sqrt(n).add(sqrt(m)).add(ApfloatUtils.IONE)
        ).divide(SQRT_TWO_PI);
    }

}
