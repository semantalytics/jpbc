package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.Pool;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerAddAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerMulAccumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.task.Task;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.task.TaskManager;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils.IndexCallable;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMMultiThreadInstanceGenerator extends CTL13MMInstanceGenerator {


    public CTL13MMMultiThreadInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters) {
        super(random, parameters);
    }

    public CTL13MMMultiThreadInstanceGenerator(SecureRandom random, PairingParameters parameters) {
        super(random, parameters);
    }

    public CTL13MMMultiThreadInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters, boolean storeGeneratedInstance) {
        super(random, parameters, storeGeneratedInstance);
    }

    public CTL13MMMultiThreadInstanceGenerator(SecureRandom random, PairingParameters parameters, boolean storeGeneratedInstance) {
        super(random, parameters, storeGeneratedInstance);
    }


    public void generateInternal(MutablePairingParameters mapParameters) {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("x0+ps") {
            public void run() {
                // Generate CRT modulo x0
                Accumulator<BigInteger> x0 = new BigIntegerMulAccumulator();

                final BigInteger[] ps = new BigInteger[parameters.getN()];
                for (int i = 0; i < parameters.getN(); i++) {
                    x0.submit(new IndexCallable<BigInteger>(i) {
                        public BigInteger call() throws Exception {
                            return (ps[i] = BigInteger.probablePrime(parameters.getEta(), random));
                        }
                    });
                }
                x0.awaitTermination();

                put("ps", ps);
                put("x0", x0.getResult());
            }

        }).addTask(new Task("gs") {
            public void run() {
                // Generate g_i's
                BigInteger[] gs = new BigInteger[parameters.getN()];
                for (int i = 0; i < parameters.getN(); i++) {
                    gs[i] = BigInteger.probablePrime(parameters.getAlpha(), random);
                }

                put("gs", gs);
            }
        }).addTask(new Task("crtCoefficients") {
            public void run() {
                // Generate CRT Coefficients
                final BigInteger x0 = getBigInteger("x0");
                final BigInteger[] ps = getBigIntegers("ps");

                final BigInteger[] crtCoefficients = new BigInteger[parameters.getN()];

                Pool executor = new PoolExecutor();
                for (int i = 0; i < parameters.getN(); i++) {
                    executor.submit(new ExecutorServiceUtils.IndexRunnable(i) {
                        public void run() {
                            BigInteger temp = x0.divide(ps[i]);
                            crtCoefficients[i] = temp.modInverse(ps[i]).multiply(temp);
                        }
                    });
                }
                executor.awaitTermination();

                put("crtCoefficients", crtCoefficients);
            }
        }).addTask(new Task("z+zInv") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");

                // Generate z
                BigInteger z, zInv;
                do {
                    z = getRandom(x0, random);
                    zInv = z.modInverse(x0);
                } while (zInv.equals(BigInteger.ZERO));

                put("z", z);
                put("zInv", zInv);
            }
        }).addTask(new Task("xsp") {
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
        }).addTask(new Task("y") {
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
                put("y", y.awaitResult().multiply(zInv).mod(x0));
            }
        }).addTask(new Task("pzt") {
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
                put("pzt", pzt.awaitResult().mod(x0));
            }
        }).addTask(new Task("xs") {
            public void run() {
                BigInteger x0 = getBigInteger("x0");
                BigInteger[] crtCoefficients = getBigIntegers("crtCoefficients");
                BigInteger[] gs = getBigIntegers("gs");
                BigInteger zInv = getBigInteger("zInv");

                // Quadratic re-randomization stuff
                BigInteger[] xs = new BigInteger[2 * parameters.getDelta()];
                for (int i = 0; i < parameters.getDelta(); i++) {
                    // xs[i] = encodeZero();
                    xs[i] = BigInteger.ZERO;
                    for (int j = 0; j < parameters.getN(); j++)
                        xs[i] = xs[i].add(
                                gs[j].multiply(getRandom(parameters.getRho(), random))
                                        .multiply(crtCoefficients[j])
                        );
                    xs[i] = xs[i].mod(x0);

                    // xs[parameters.getDelta() + i] = encodeAt(1);
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

        mapParameters.putObject("params", parameters);
        mapParameters.putObject("x0", taskManager.get("x0"));
        mapParameters.putObject("y", taskManager.get("y"));
        mapParameters.putObject("pzt", taskManager.get("pzt"));
        mapParameters.putObject("z", taskManager.get("z"));
        mapParameters.putObject("zInv", taskManager.get("zInv"));
        mapParameters.putObject("xsp", taskManager.get("xsp"));
        mapParameters.putObject("crtCoefficients", taskManager.get("crtCoefficients"));
        mapParameters.putObject("xs", taskManager.get("xs"));
        mapParameters.putObject("gs", taskManager.get("gs"));
        mapParameters.putObject("ps", taskManager.get("ps"));
    }


    public static void main(String[] args) {
        CTL13MMMultiThreadInstanceGenerator gen = new CTL13MMMultiThreadInstanceGenerator(new SecureRandom(),
                PairingFactory.getInstance().loadParameters("./params/mm/ctl13/toy.properties")
        );
        gen.generate();
    }

}
