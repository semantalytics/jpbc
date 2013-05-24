package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MapParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectOutput;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;
import it.unisa.dia.gas.plaf.jpbc.util.mt.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class MTCTL13MMInstanceGenerator extends CTL13MMInstanceGenerator {


    public MTCTL13MMInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters) {
        super(random, parameters, true);
    }

    public MTCTL13MMInstanceGenerator(SecureRandom random, PairingParameters parameters) {
        super(random, parameters);
    }


    public PairingParameters generate() {
        if (storeGeneratedInstance) {
            try {
                PairingParameters params = load();
                if (params != null)
                    return params;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task() {
            public void run() {
                // Generate CRT modulo x0
                final BigInteger[] ps = new BigInteger[parameters.getN()];

                Accumulator<BigInteger> x0 = new BigIntegerMulAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    x0.submit(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return (ps[i] = BigInteger.probablePrime(parameters.getEta(), random));
                        }
                    });
                }
                x0.process();

                put("ps", ps);
                put("x0", x0.getResult());
            }
        }).addTask(new Task() {
            public void run() {
                // Generate g_i's
                BigInteger[] gs = new BigInteger[parameters.getN()];
                for (int i = 0; i < parameters.getN(); i++) {
                    gs[i] = BigInteger.probablePrime(parameters.getAlpha(), random);
                }

                put("gs", gs);
            }
        }).addTask(new Task() {
            public void run() {
                // Generate CRT Coefficients
                final BigInteger x0 = getBigInteger("x0");
                final BigInteger[] ps = getBigIntegers("ps");

                final BigInteger[] crtCoefficients = new BigInteger[parameters.getN()];

                Pool executor = new MultiThreadNoReduceExecutor();
                for (int i = 0; i < parameters.getN(); i++) {
                    executor.submit(new MultiThreadExecutor.IndexRunnable(i) {
                        public void run() {
                            BigInteger temp = x0.divide(ps[i]);
                            crtCoefficients[i] = temp.modInverse(ps[i]).multiply(temp);
                        }
                    });
                }
                executor.process();

                put("crtCoefficients", crtCoefficients);
            }
        }).addTask(new Task() {
            public void run() {
                BigInteger x0 = getBigInteger("x0");

                // Generate z
                BigInteger z, zInv;
                do {
                    z = BigIntegerUtils.getRandom(x0, random);
                    zInv = z.modInverse(x0);
                } while (zInv.equals(BigInteger.ZERO));

                put("z", z);
                put("zInv", zInv);
            }
        }).addTask(new Task() {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                BigInteger[] crtCoefficients = getBigIntegers("crtCoefficients");
                BigInteger[] gs = getBigIntegers("gs");

                // Generate xp_i's
                BigInteger[] xsp = new BigInteger[parameters.getEll()];
                for (int i = 0; i < parameters.getEll(); i++) {

                    // xsp[i] = encodeAt(0);
                    xsp[i] = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++) {
                        xsp[i] = xsp[i].add(
                                gs[j].multiply(getRandom(parameters.getRho(), random))
                                        .add(getRandom(parameters.getAlpha(), random))
                                        .multiply(crtCoefficients[j])
                        );
                    }
                    xsp[i] = xsp[i].mod(x0);

                }

                put("xsp", xsp);
            }
        }).addTask(new Task() {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                final BigInteger[] crtCoefficients = getBigIntegers("crtCoefficients");
                final BigInteger[] gs = getBigIntegers("gs");
                BigInteger zInv = getBigInteger("zInv");

                // Generate y = encodeOneAt(1)
                Accumulator<BigInteger> y = new BigIntegerAddAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    y.submit(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return gs[i].multiply(getRandom(parameters.getRho(), random))
                                    .add(BigInteger.ONE)
                                    .multiply(crtCoefficients[i]);
                        }
                    });
                }
                y.process();

                put("y", y.getResult().multiply(zInv).mod(x0));
            }
        }).addTask(new Task() {
            public void run() {
                final BigInteger x0 = getBigInteger("x0");
                final BigInteger[] ps = getBigIntegers("ps");
                final BigInteger[] gs = getBigIntegers("gs");
                BigInteger z = getBigInteger("z");

                // Generate zero-tester pzt
                final BigInteger zPowKappa = z.modPow(BigInteger.valueOf(parameters.getKappa()), x0);

                Accumulator<BigInteger> pzt = new BigIntegerAddAccumulator();
                for (int i = 0; i < parameters.getN(); i++) {
                    pzt.accumulate(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return getRandom(parameters.getBeta(), random)
                                    .multiply(gs[i].modInverse(ps[i]).multiply(zPowKappa).mod(ps[i]))
                                    .multiply(x0.divide(ps[i]));
                        }
                    });
                }
                put("pzt", pzt.process().getResult().mod(x0));
            }
        }).addTask(new Task() {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                BigInteger[] crtCoefficients = getBigIntegers("crtCoefficients");
                BigInteger[] gs = getBigIntegers("gs");
                BigInteger zInv = getBigInteger("zInv");

                // Quadratic re-randomization stuff
                BigInteger[] xs = new BigInteger[2 * parameters.getDelta()];
                for (int i = 0; i < parameters.getDelta(); i++) {
//            xs[i] = encodeZero();
                    xs[i] = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++)
                        xs[i] = xs[i].add(
                                gs[j].multiply(getRandom(parameters.getRho(), random))
                                        .multiply(crtCoefficients[j])
                        );
                    xs[i] = xs[i].mod(x0);

//            xs[parameters.getDelta() + i] = encodeAt(1);
                    int index = parameters.getDelta() + i;
                    xs[index] = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++) {
                        xs[index] = xs[index].add(
                                gs[j].multiply(getRandom(parameters.getRho(), random))
                                        .add(getRandom(parameters.getAlpha(), random))
                                        .multiply(crtCoefficients[j])
                        );
                    }
                    xs[index] = xs[index].multiply(zInv).mod(x0);
                }

                put("xs", xs);
            }
        });

        long start = System.currentTimeMillis();
        taskManager.process();
        long end = System.currentTimeMillis();
        System.out.println("end = " + (end - start));

        if (storeGeneratedInstance)
            store(taskManager.getContext());

        MapParameters mapParameters = new MapParameters();
        mapParameters.put("params", parameters);
        mapParameters.putAll(taskManager.getContext());

        return mapParameters;
    }


    protected void store(Map<String, Object> values) {
        try {
            // file name
            String fileName = String.format(
                    "MTCTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                    parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                    parameters.getRho(), parameters.getDelta(),
                    parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound());

            PairingObjectOutput dos = new PairingObjectOutput(fileName);

            dos.writeBigInteger((BigInteger) values.get("x0"));
            dos.writeBigInteger((BigInteger) values.get("y"));
            dos.writeBigInteger((BigInteger) values.get("pzt"));
            dos.writeBigInteger((BigInteger) values.get("z"));
            dos.writeBigInteger((BigInteger) values.get("zInv"));

            dos.writeBigIntegers((BigInteger[]) values.get("xsp"));
            dos.writeBigIntegers((BigInteger[]) values.get("crtCoefficients"));
            dos.writeBigIntegers((BigInteger[]) values.get("xs"));
            dos.writeBigIntegers((BigInteger[]) values.get("gs"));
            dos.writeBigIntegers((BigInteger[]) values.get("ps"));

            dos.flush();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MTCTL13MMInstanceGenerator gen = new MTCTL13MMInstanceGenerator(new SecureRandom(), CTL13MMInstanceParameters.SMALL);
        gen.generate();
    }

}
