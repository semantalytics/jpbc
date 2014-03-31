package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianCDTSampler implements Sampler<BigInteger> {

    static final BigInteger long_255 = BigInteger.valueOf(255);
    static final BigInteger mask_init = long_255.shiftLeft(64 - 8);
    static final int tau = 13;
    static final double sigma_bin_inv_lowprec = 1.17741002251547469101156932645969963; // sqrt(2 ln 2)
    static final double sigma_bin_lowprec = 0.8493218002880190427215028341028896; //  sqrt(1/(2 ln 2))

    protected SecureRandom random;
    protected BigInteger[] CDT, CDT_inv_min, CDT_inv_max;
    protected int CDT_length;


    public ZGaussianCDTSampler(SecureRandom random, int gaussianParameter) {
        if (random == null)
            random = new SecureRandom();
        this.random = random;

        CDTDataBuilder.CDTData data = CDTDataBuilder.getInstance().compute(gaussianParameter);
        this.CDT = data.CDT;
        this.CDT_inv_min = data.CDT_inv_min;
        this.CDT_inv_max = data.CDT_inv_max;
        this.CDT_length = data.CDT_length;
    }

    public BigInteger sample() {
        int r0 = random.nextInt(256);

        BigInteger min = CDT_inv_min[r0];
        BigInteger max = CDT_inv_max[r0];
        if (max.subtract(min).compareTo(BigIntegerUtils.TWO) < 0) {
            return (random.nextBoolean()) ? min : min.negate();
        }

        int mask_index = 56;
        BigInteger r1 = BigInteger.valueOf(r0).shiftLeft(mask_index);
        BigInteger r2 = mask_init;
        int cur = min.add(max).divide(BigIntegerUtils.TWO).intValue();

        while (true) {
            if (r1.compareTo(CDT[cur]) > 0)
                min = BigInteger.valueOf(cur);
            else if (r1.compareTo(CDT[cur].and(r2)) < 0)
                max = BigInteger.valueOf(cur);
            else {
                if (mask_index <= 0)
                    break;
                mask_index -= 8;

                r2 = r2.or(long_255.shiftLeft(mask_index));
                r1 = r2.or(BigInteger.valueOf(random.nextInt(256)).shiftLeft(mask_index));
            }

            if (max.subtract(min).compareTo(BigIntegerUtils.TWO) < 0) {
                return (random.nextBoolean()) ? min : min.negate();
            }

            cur = min.add(max).divide(BigIntegerUtils.TWO).intValue();
        }

        r2 = new BigInteger(64, random);
        while (true) {
            if (r1.compareTo(CDT[cur]) < 0 || ((r1.compareTo(CDT[cur]) == 0) && (r2.compareTo(CDT[cur + CDT_length])) < 0))
                max = BigInteger.valueOf(cur);
            else
                max = BigInteger.valueOf(cur); // TODO: this is strange!!!

            cur = min.add(max).divide(BigIntegerUtils.TWO).intValue();

            if (max.subtract(min).compareTo(BigIntegerUtils.TWO) < 0)
                return (random.nextBoolean()) ? min : min.negate();
        }
    }


    public static class CDTDataBuilder {

        private static CDTDataBuilder INSTANCE = new CDTDataBuilder();

        public static CDTDataBuilder getInstance() {
            return INSTANCE;
        }

        protected Map<Integer, CDTData> dataMap;


        private CDTDataBuilder() {
            this.dataMap = new HashMap<Integer, CDTData>();
        }

        public CDTData compute(int gaussianParameter) {
            CDTData data = this.dataMap.get(gaussianParameter);
            if (data != null)
                return data;

            BigInteger[] CDT, CDT_inv_min, CDT_inv_max;
            int CDT_length;

            // f = 2 sigma^2 = 2 k^2 1/(2ln(2)) = k^2/ln(2)
            int k = (int) (sigma_bin_inv_lowprec * gaussianParameter) + 1;
            //sqrt(2 ln 2)
//            int kk = ApfloatMath.sqrt(
//                    ApfloatUtils.TWO.multiply(
//                            ApfloatMath.log(ApfloatUtils.TWO)
//                    )
//            ).multiply(ApfloatUtils.to_Apfloat(gaussianParameter)).add(ApfloatUtils.IONE).truncate().intValue();

            Apfloat f = ApfloatUtils.newApfloat(k * k).divide(ApfloatMath.log(ApfloatUtils.TWO));

            //sqrt(1/(2 ln 2))
//            int cdtl = ApfloatMath.sqrt(
//                    ApfloatUtils.ONE.divide(
//                        ApfloatUtils.TWO.multiply(
//                                ApfloatMath.log(ApfloatUtils.TWO)
//                        )
//                    )
//            ).multiply(ApfloatUtils.to_Apfloat(k))
//                    .multiply(ApfloatUtils.to_Apfloat(tau))
//                    .add(ApfloatUtils.IONE).truncate().intValue();

            // compute normalization constant
            Apfloat t = ApfloatUtils.ZERO;
            CDT_length = (int) (k * sigma_bin_lowprec * tau) + 1;

            for (int i = 1; i < CDT_length; i++) {
                Apfloat z = ApfloatUtils.newApfloat(i - 1);
                z = z.multiply(z);          // z =  (i-1)^2
                z = z.negate();             // z = -(i-1)^2
                z = z.divide(f);            // z = -(i-1)^2/f
                z = ApfloatMath.exp(z);     // z = exp(-(i-1)^2/f)
                if (i == 1)
                    z = z.divide(ApfloatUtils.TWO);

                t = t.add(z);
            }

            Apfloat ff = ApfloatMath.pow(ApfloatUtils.TWO, 64);
            Apfloat y = ApfloatUtils.ZERO;
            CDT = new BigInteger[CDT_length * 2];

            for (int i = 1; i < CDT_length; i++) {
                Apfloat z = ApfloatUtils.newApfloat(i - 1);
                z = z.multiply(z);          // z =  (i-1)^2
                z = z.negate();             // z = -(i-1)^2
                z = z.divide(f);            // z = -(i-1)^2/f
                z = ApfloatMath.exp(z);     // z = exp(-(i-1)^2/f)
                if (i == 1)
                    z = z.divide(ApfloatUtils.TWO);

                z = z.divide(t); // normalize
                y = y.add(z); // accumulate

                z = y;

                for (int j = 0; j < 2; j++) {
                    z = z.multiply(ff);

                    Apint zTruncate = z.truncate();
                    CDT[i + j * CDT_length] = zTruncate.toRadix(10).toBigInteger();

                    z = z.subtract(zTruncate);
                }
            }

            for (int j = 0; j < 2; j++)
                CDT[j * CDT_length] = BigInteger.ZERO;


            CDT_inv_min = new BigInteger[256];
            CDT_inv_max = new BigInteger[256];

            int min = 0, max = 0;
            BigInteger mask = BigInteger.valueOf(255).shiftLeft(56);
            for (int i = 0; i < 256; i++) {
                BigInteger val = BigInteger.valueOf(i).shiftLeft(56);

                while (CDT[min + 1].compareTo(val) < 0)
                    min++;

                while ((max + 1 < CDT_length) && (CDT[max].and(mask).compareTo(val) <= 0))
                    max++;

                CDT_inv_min[i] = BigInteger.valueOf(min);
                CDT_inv_max[i] = BigInteger.valueOf(max);
            }

            data = new CDTData(CDT, CDT_inv_min, CDT_inv_max, CDT_length);
            this.dataMap.put(gaussianParameter, data);

//            System.out.println("CDT");
//            for (int i = 0; i < CDT.length; i++) {
//                BigInteger bigInteger = CDT[i];
//                System.out.println(bigInteger);
//            }

            return data;
        }


        public class CDTData {
            BigInteger[] CDT, CDT_inv_min, CDT_inv_max;
            int CDT_length;

            public CDTData(BigInteger[] CDT, BigInteger[] CDT_inv_min, BigInteger[] CDT_inv_max, int CDT_length) {
                this.CDT = CDT;
                this.CDT_inv_min = CDT_inv_min;
                this.CDT_inv_max = CDT_inv_max;
                this.CDT_length = CDT_length;
            }
        }

    }


    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        int n = 4;
        int k = 16;


        int nn = 2 *n;
        int mm = n * k;
        BigInteger q = BigInteger.ONE.shiftLeft(k);

        Field Zq = new SymmetricZrField(q);
        ZGaussianCDTSampler sampler = new ZGaussianCDTSampler(random, 9);
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
