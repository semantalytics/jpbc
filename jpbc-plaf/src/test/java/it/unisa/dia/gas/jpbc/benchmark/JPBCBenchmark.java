package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class JPBCBenchmark {
    protected int times;

    public JPBCBenchmark(int times) {
        this.times = times;
    }


    public Benchmark benchmark(String[] curves) {
        Benchmark benchmark = new Benchmark(curves);

        double[][] pairingBenchmarks = benchmark.getPairingBenchmarks();

        for (int col = 0; col < curves.length; col++) {
            System.out.printf("Curve = %s...\n", curves[col]);
            printStatMemory();

            Pairing pairing = getPairing(curves[col]);

            int t1 = 0, t2 = 0, t3 = 0;
            for (int i = 0; i < times; i++) {
                Element g = pairing.getG1().newElement().setToRandom();
                Element h = pairing.getG2().newElement().setToRandom();

                long start = System.currentTimeMillis();
                pairing.pairing(g, h);
                long end = System.currentTimeMillis();
                t1 += Math.abs((end - start));

                start = System.currentTimeMillis();
                PairingPreProcessing ppp = pairing.pairing(g);
                end = System.currentTimeMillis();
                t2 += Math.abs((end - start));

                start = System.currentTimeMillis();
                ppp.pairing(h);
                end = System.currentTimeMillis();
                t3 += Math.abs((end - start));
            }

            printStatMemory();
            System.gc();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {}
            printStatMemory();

            pairingBenchmarks[0][col] = (double) t1 / times;
            pairingBenchmarks[1][col] = (double) t2 / times;
            pairingBenchmarks[2][col] = (double) t3 / times;
            System.out.printf("finished.\n");
        }

        // Element Pow Benchmarks
        System.out.println("Element Pow Benchmark...");

        double[][][] elementBenchmarks = benchmark.getElementBenchmarks();

        for (int col = 0; col < curves.length; col++) {
            System.out.printf("Curve = %s\n", curves[col]);
            printStatMemory();

            Pairing pairing = getPairing(curves[col]);
            Field[] fields = new Field[]{
                    pairing.getG1(),
                    pairing.getG2(),
                    pairing.getGT(),
                    pairing.getZr()
            };

            for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
                System.out.printf("Field %s...", Benchmark.fieldNames[fieldIndex]);
                printStatMemory();

                long t1 = 0, t2 = 0,t3 = 0,t4 = 0, t5 = 0, t6 = 0, t7 = 0;
                for (int i = 0; i < times; i++) {
                    Element e1 = fields[fieldIndex].newRandomElement();

                    BigInteger n = pairing.getZr().newRandomElement().toBigInteger();
                    Element n1 = pairing.getZr().newRandomElement();

                    long start = System.currentTimeMillis();
                    e1.duplicate().pow(n);
                    long end = System.currentTimeMillis();
                    t1 += Math.abs((end - start));

                    start = System.currentTimeMillis();
                    e1.duplicate().powZn(n1);
                    end = System.currentTimeMillis();
                    t2 += Math.abs((end - start));

                    start = System.currentTimeMillis();
                    ElementPowPreProcessing ppp = e1.pow();
                    end = System.currentTimeMillis();
                    t3 += Math.abs((end - start));

                    start = System.currentTimeMillis();
                    ppp.pow(n);
                    end = System.currentTimeMillis();
                    t4 += Math.abs((end - start));

                    start = System.currentTimeMillis();
                    ppp.powZn(n1);
                    end = System.currentTimeMillis();
                    t5 += Math.abs((end - start));

                    start = System.currentTimeMillis();
                    e1.duplicate().mul(n);
                    end = System.currentTimeMillis();
                    t6 += Math.abs((end - start));

                    start = System.currentTimeMillis();
                    e1.setToRandom();
                    end = System.currentTimeMillis();
                    t7 += Math.abs((end - start));
                }

                elementBenchmarks[fieldIndex][0][col] = (double ) t1 / times;
                elementBenchmarks[fieldIndex][1][col] = (double ) t2 / times;
                elementBenchmarks[fieldIndex][2][col] = (double ) t3 / times;
                elementBenchmarks[fieldIndex][3][col] = (double ) t4 / times;
                elementBenchmarks[fieldIndex][4][col] = (double ) t5 / times;
                elementBenchmarks[fieldIndex][5][col] = (double ) t6 / times;
                elementBenchmarks[fieldIndex][6][col] = (double ) t7 / times;

                printStatMemory();
                System.gc();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {}
                printStatMemory();
                System.out.printf("finished.\n");
            }

        }
        printStatMemory();

        return benchmark;
    }

    protected CurveParams getCurveParams(String curve) {
        CurveParams curveParams = new CurveParams();

        try {
            File curveFile = new File(curve);
            if (curveFile.exists()) {
                curveParams.load(curveFile.toURI().toURL().openStream());
            } else
                curveParams.load(getClass().getClassLoader().getResourceAsStream(curve));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return curveParams;
    }

    protected Pairing getPairing(String curve) {
        return PairingFactory.getPairing(getCurveParams(curve));
    }

    protected void printStatMemory() {
        System.out.printf("Memory. Max = %d, Free = %d, Total = %d\n",
                Runtime.getRuntime().maxMemory(),
                Runtime.getRuntime().freeMemory(),
                Runtime.getRuntime().totalMemory());
    }

    public static void main(String[] args) {
        PairingFactory.getInstance().setUsePBCWhenPossible(false);
        
        JPBCBenchmark benchmark = new JPBCBenchmark(Integer.parseInt(args[0]));
        String[] curves = Arrays.copyOfRange(args, 1, args.length);

        System.out.printf("JPBC Benchmark.\n");
        System.out.printf("#Times = %s\n", args[0]);
        for (String curve : curves) {
            System.out.printf("Curve = %s\n", curve);
        }
        System.out.printf("Results: \n %s\n", benchmark.benchmark(curves).toHTML());
        System.out.printf("JPBC Benchmark. Finished.\n");
    }
}
