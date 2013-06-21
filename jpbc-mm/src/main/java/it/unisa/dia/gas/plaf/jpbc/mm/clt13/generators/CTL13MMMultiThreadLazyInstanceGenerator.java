package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMLazyMapParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerAddAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerMulAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.context.ContextExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.context.ContextRunnable;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils.IndexCallable;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMMultiThreadLazyInstanceGenerator extends CTL13MMInstanceGenerator {


    public CTL13MMMultiThreadLazyInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters) {
        super(random, parameters);
    }

    public CTL13MMMultiThreadLazyInstanceGenerator(SecureRandom random, PairingParameters parameters) {
        super(random, parameters);
    }

    public CTL13MMMultiThreadLazyInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters, boolean storeGeneratedInstance) {
        super(random, parameters, storeGeneratedInstance);
    }

    public CTL13MMMultiThreadLazyInstanceGenerator(SecureRandom random, PairingParameters parameters, boolean storeGeneratedInstance) {
        super(random, parameters, storeGeneratedInstance);
    }


    protected CTL13MMLazyMapParameters newCTL13MMMapParameters() {
        return new CTL13MMLazyMapParameters(parameters);
    }

    protected void generateInternal(MutablePairingParameters mapParameters) {
        ContextExecutor executor = new ContextExecutor(mapParameters);
        executor.submit(new ContextRunnable("x0+ps") {
            public void run() {
                // Generate CRT modulo x0
                Accumulator<BigInteger> x0 = new BigIntegerMulAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    x0.submit(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            BigInteger value = BigInteger.probablePrime(parameters.getEta(), random);
                            putBigIntegerAt("ps", i, value);

                            return value;
                        }

                    });
                }
                x0.awaitTermination();

                putBoolean("ps", true);
                putBigInteger("x0", x0.getResult());
            }

        }).submit(new ContextRunnable("gs") {
            public void run() {
                // Generate g_i's
                for (int i = 0; i < parameters.getN(); i++) {
                    putBigIntegerAt("gs", i, BigInteger.probablePrime(parameters.getAlpha(), random));
                }

                putBoolean("gs", true);
            }
        }).submit(new ContextRunnable("crtCoefficients") {
            public void run() {
                // Generate CRT Coefficients
                final BigInteger x0 = getBigInteger("x0");
                getObject("ps");

                System.out.println("GEN CRTCOEFF");
                Pool executor = new PoolExecutor();
                for (int i = 0; i < parameters.getN(); i++) {
                    executor.submit(new ExecutorServiceUtils.IndexRunnable(i) {
                        public void run() {
                            BigInteger temp = x0.divide(getBigIntegerAt("ps", i));
                            temp = temp.modInverse(getBigIntegerAt("ps", i)).multiply(temp);

                            putBigIntegerAt("crtCoefficients", i, temp);
                        }
                    });
                }
                executor.awaitTermination();

                putBoolean("crtCoefficients", true);
            }
        }).submit(new ContextRunnable("z+zInv") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");

                // Generate z
                BigInteger z, zInv;
                do {
                    z = getRandom(x0, random);
                    zInv = z.modInverse(x0);
                } while (zInv.equals(BigInteger.ZERO));

                putBigInteger("z", z);
                putBigInteger("zInv", zInv);
            }
        }).submit(new ContextRunnable("xsp") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                getObject("crtCoefficients");
                getObject("gs");

                // Generate xp_i's
                for (int i = 0; i < parameters.getEll(); i++) {

                    // xsp = encodeAt(0);
                    BigInteger xsp = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++) {
                        xsp = xsp.add(
                                getBigIntegerAt("gs", j).multiply(getRandom(parameters.getRho(), random))
                                        .add(getRandom(parameters.getAlpha(), random))
                                        .multiply(getBigIntegerAt("crtCoefficients", j))
                        );
                    }
                    xsp = xsp.mod(x0);

                    putBigIntegerAt("xsp", i, xsp);

                }

                putBoolean("xsp", true);
            }
        }).submit(new ContextRunnable("y") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                BigInteger zInv = getBigInteger("zInv");
                getObject("crtCoefficients");
                getObject("gs");

                // Generate y = encodeOneAt(1)
                Accumulator<BigInteger> y = new BigIntegerAddAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    y.submit(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return getBigIntegerAt("gs", i).multiply(getRandom(parameters.getRho(), random))
                                    .add(BigInteger.ONE)
                                    .multiply(getBigIntegerAt("crtCoefficients", i));
                        }
                    });
                }

                putBigInteger("y", y.awaitResult().multiply(zInv).mod(x0));
            }
        }).submit(new ContextRunnable("pzt") {
            public void run() {
                final BigInteger x0 = getBigInteger("x0");
                BigInteger z = getBigInteger("z");
                getObject("gs");
                getObject("ps");

                // Generate zero-tester pzt
                final BigInteger zPowKappa = z.modPow(BigInteger.valueOf(parameters.getKappa()), x0);

                Accumulator<BigInteger> pzt = new BigIntegerAddAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    pzt.accumulate(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return getRandom(parameters.getBeta(), random)
                                    .multiply(getBigIntegerAt("gs", i).modInverse(getBigIntegerAt("ps", i)).multiply(zPowKappa).mod(getBigIntegerAt("ps", i)))
                                    .multiply(x0.divide(getBigIntegerAt("ps", i)));
                        }
                    });
                }

                putBigInteger("pzt", pzt.awaitResult().mod(x0));
            }
        }).submit(new ContextRunnable("xs") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                BigInteger zInv = getBigInteger("zInv");
                getObject("crtCoefficients");
                getObject("gs");

                // Quadratic re-randomization stuff
                for (int i = 0; i < parameters.getDelta(); i++) {
                    // xs[i] = encodeZero();
                    BigInteger xs = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++)
                        xs = xs.add(
                                getBigIntegerAt("gs", j).multiply(getRandom(parameters.getRho(), random))
                                        .multiply(getBigIntegerAt("crtCoefficients", j))
                        );
                    xs = xs.mod(x0);
                    putBigIntegerAt("xs", i, xs);

                    // xs[parameters.getDelta() + i] = encodeAt(1);
                    xs = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++) {
                        xs = xs.add(
                                getBigIntegerAt("gs", j).multiply(getRandom(parameters.getRho(), random))
                                        .add(getRandom(parameters.getAlpha(), random))
                                        .multiply(getBigIntegerAt("crtCoefficients", j))
                        );
                    }
                    xs = xs.multiply(zInv).mod(x0);
                    putBigIntegerAt("xs", parameters.getDelta() + i, xs);
                }

                putBoolean("xs", true);
            }
        });

        long start = System.currentTimeMillis();
        executor.awaitTermination();
        long end = System.currentTimeMillis();
        System.out.println("end = " + (end - start));
    }


    public static void main(String[] args) {
        String params = "./params/mm/ctl13/medium.properties";
        if (args.length > 0)
            params = args[0];

        CTL13MMMultiThreadLazyInstanceGenerator gen = new CTL13MMMultiThreadLazyInstanceGenerator(
                new SecureRandom(),
                PairingFactory.getInstance().loadParameters(params)
        );
        gen.generate();
    }

}
