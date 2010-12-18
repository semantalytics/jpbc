package it.unisa.dia.gas.jpbc.android.benchmark;

import android.util.Log;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AndroidBenchmark {
    private static final String TAG = "AndroidBenchmark";

    protected int times;
    protected boolean running = false;


    public AndroidBenchmark(int times) {
        this.times = times;
    }


    public Benchmark benchmark(String[] curves) {
        Log.i(TAG, "Benchmarking...\n");

        this.running = true;
        Benchmark benchmark = new Benchmark(curves);

        double[][] pairingBenchmarks = benchmark.getPairingBenchmarks();

        for (int col = 0; col < curves.length && running; col++) {
            Log.i(TAG, "Curve = " + curves[col]);

            Pairing pairing = getPairing(curves[col]);

            int t1 = 0, t2 = 0, t3 = 0;
            for (int i = 0; i < times && running; i++) {
                Element g = pairing.getG1().newElement().setToRandom();
                Element h = pairing.getG2().newElement().setToRandom();

                long start = System.currentTimeMillis();
                pairing.pairing(g, h);
                long end = System.currentTimeMillis();
                t1 += (end - start);

                start = System.currentTimeMillis();
                PairingPreProcessing ppp = pairing.pairing(g);
                end = System.currentTimeMillis();
                t2 += (end - start);

                start = System.currentTimeMillis();
                ppp.pairing(h);
                end = System.currentTimeMillis();
                t3 += (end - start);

                System.gc();
            }

            if (!running)
                return null;

            pairingBenchmarks[0][col] = (double) t1 / times;
            pairingBenchmarks[1][col] = (double) t2 / times;
            pairingBenchmarks[2][col] = (double) t3 / times;
            Log.i(TAG, "Finished.\n");
        }

        // Element Pow Benchmarks
        Log.i(TAG, "Element Pow Benchmark...");

        double[][][] elementBenchmarks = benchmark.getElementBenchmarks();

        for (int col = 0; col < curves.length && running; col++) {
            Log.i(TAG, "Curve = " + curves[col]);

            Pairing pairing = getPairing(curves[col]);
            Field[] fields = new Field[]{
                    pairing.getG1(),
                    pairing.getG2(),
                    pairing.getGT(),
                    pairing.getZr()
            };

            for (int fieldIndex = 0; fieldIndex < fields.length && running; fieldIndex++) {
                Log.i(TAG, "Field " + Benchmark.fieldNames[fieldIndex]);

                long t1 = 0, t2 = 0,t3 = 0,t4 = 0, t5 = 0, t6 = 0, t7 = 0;
                for (int i = 0; i < times && running; i++) {
                    Element e1 = fields[fieldIndex].newRandomElement();

                    BigInteger n = pairing.getZr().newRandomElement().toBigInteger();
                    Element n1 = pairing.getZr().newRandomElement();

                    long start = System.currentTimeMillis();
                    e1.duplicate().pow(n);
                    long end = System.currentTimeMillis();
                    t1 += (end - start);

                    start = System.currentTimeMillis();
                    e1.duplicate().powZn(n1);
                    end = System.currentTimeMillis();
                    t2 += (end - start);

                    start = System.currentTimeMillis();
                    ElementPowPreProcessing ppp = e1.pow();
                    end = System.currentTimeMillis();
                    t3 += (end - start);

                    start = System.currentTimeMillis();
                    ppp.pow(n);
                    end = System.currentTimeMillis();
                    t4 += (end - start);

                    start = System.currentTimeMillis();
                    ppp.powZn(n1);
                    end = System.currentTimeMillis();
                    t5 += (end - start);

                    start = System.currentTimeMillis();
                    e1.duplicate().mul(n);
                    end = System.currentTimeMillis();
                    t6 += (end - start);

                    start = System.currentTimeMillis();
                    e1.setToRandom();
                    end = System.currentTimeMillis();
                    t7 += (end - start);

                    System.gc();
                }

                if (!running)
                    return null;

                elementBenchmarks[fieldIndex][0][col] = (double ) t1 / times;
                elementBenchmarks[fieldIndex][1][col] = (double ) t2 / times;
                elementBenchmarks[fieldIndex][2][col] = (double ) t3 / times;
                elementBenchmarks[fieldIndex][3][col] = (double ) t4 / times;
                elementBenchmarks[fieldIndex][4][col] = (double ) t5 / times;
                elementBenchmarks[fieldIndex][5][col] = (double ) t6 / times;
                elementBenchmarks[fieldIndex][6][col] = (double ) t7 / times;
                Log.i(TAG, "Finished.\n");
            }
        }

        Log.i(TAG, "Benchmarking Finished.\n");
        this.running = false;

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


    public void stop() {
        Log.i(TAG, "Stop.\n");

        this.running = false;
    }
}
