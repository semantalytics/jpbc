package it.unisa.dia.gas.plaf.sampler;

import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianRSDoubleSampler;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianProber {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        int iterations = 1000000;
//        int iterations = 10;

        Apfloat sigma = ApfloatUtils.newApfloat(10);

        Sampler<BigInteger> sampler = new ZGaussianRSDoubleSampler(
                random,
                10
        );


        Map<BigInteger, Integer> values = new HashMap<BigInteger, Integer>();
        for (int i = 0; i < iterations; i++) {

            if (i % 10000 == 0)
                System.out.print(".");
            if (i % 100000 == 0)
                System.out.println();

            BigInteger value = sampler.sample();

            if (!values.containsKey(value)) {
                values.put(value, 1);
            } else
                values.put(value, values.get(value) + 1);
        }
        System.out.println();

        BigInteger[] keys = values.keySet().toArray(new BigInteger[values.size()]);
        Arrays.sort(keys);

        for (BigInteger key : keys) {
            System.out.println(key + " " + (values.get(key)/(double)iterations));
        }
    }


}
