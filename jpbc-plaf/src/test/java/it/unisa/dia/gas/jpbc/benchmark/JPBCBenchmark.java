package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class JPBCBenchmark {
    protected Pairing pairing;
    protected String curveParamsFileName;
    protected int times;


    public JPBCBenchmark(String curveParamsFileName, int times) {
        this.curveParamsFileName = curveParamsFileName;
        this.times = times;
    }

    public void setUp() {
        PairingFactory.getInstance().setUsePBCWhenPossible(false);
        pairing = PairingFactory.getPairing(getCurveParams());
    }


    public void benchmarkPairing() {
        if (pairing == null)
            return;

        long elapsed = 0;

        for (int i = 0; i < times; i++) {
            Element g = pairing.getG1().newElement().setToRandom();
            Element h = pairing.getG2().newElement().setToRandom();

            long start = System.currentTimeMillis();

            pairing.pairing(g, h);

            long end = System.currentTimeMillis();
            elapsed += (end - start);
        }

        System.out.printf("Pairing#pairing(in1, in2)                    = %f\n", (double) elapsed / times);
    }

    public void benchmarkPairingPreProcessing() {
        if (pairing == null)
            return;

        long t1 = 0;
        long t2 = 0;

        for (int i = 0; i < times; i++) {
            Element g = pairing.getG1().newElement().setToRandom();
            Element h = pairing.getG2().newElement().setToRandom();

            long start = System.currentTimeMillis();

            PairingPreProcessing ppp = pairing.pairing(g);

            long end = System.currentTimeMillis();
            t1 += (end - start);

            start = System.currentTimeMillis();

            ppp.pairing(h);

            end = System.currentTimeMillis();
            t2 += (end - start);
        }

        System.out.printf("Pairing#pairing(in1)                         = %f\n", (double) t1 / times);
        System.out.printf("PairingPreProcessing#pairing                 = %f\n", (double) t2 / times);
    }

    public void benchmarkPow() {
        if (pairing == null)
            return;

        pow(pairing.getG1(), "G1");
        pow(pairing.getG2(), "G2");
        pow(pairing.getGT(), "GT");
        pow(pairing.getZr(), "Zr");
    }

    public void benchmarkPowPreProcessing() {
        if (pairing == null)
            return;

        powPreProcessing(pairing.getG1(), "G1");
        powPreProcessing(pairing.getG2(), "G2");
        powPreProcessing(pairing.getGT(), "GT");
        powPreProcessing(pairing.getZr(), "Zr");
    }


    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();

        try {
            File curveFile = new File(curveParamsFileName);
            if (curveFile.exists()) {
                curveParams.load(curveFile.toURI().toURL().openStream());
            } else
                curveParams.load(getClass().getClassLoader().getResourceAsStream(curveParamsFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return curveParams;
    }

    protected void pow(Field field, String label) {
        if (pairing == null)
            return;

        long t2 = 0;
        long t3 = 0;

        for (int i = 0; i < times; i++) {
            Element e1 = field.newRandomElement();

            BigInteger n = pairing.getZr().newRandomElement().toBigInteger();

            long start = System.currentTimeMillis();
            e1.duplicate().pow(n);
            long end = System.currentTimeMillis();
            t2 += (end - start);

            Element n1 = pairing.getZr().newRandomElement();

            start = System.currentTimeMillis();
            e1.duplicate().powZn(n1);
            end = System.currentTimeMillis();
            t3 += (end - start);
        }

        System.out.printf("(" + label + ") Element#pow(BigInteger)                 = %f\n", (double) t2 / times);
        System.out.printf("(" + label + ") Element#powZn(Element)                  = %f\n", (double) t3 / times);
    }

    protected void powPreProcessing(Field field, String label) {
        if (pairing == null)
            return;

        long t1 = 0;
        long t2 = 0;
        long t3 = 0;

        for (int i = 0; i < times; i++) {
            Element e1 = field.newRandomElement();

            long start = System.currentTimeMillis();
            ElementPowPreProcessing ppp = e1.pow();
            long end = System.currentTimeMillis();
            t1 += (end - start);

            BigInteger n = pairing.getZr().newRandomElement().toBigInteger();

            start = System.currentTimeMillis();
            ppp.pow(n);
            end = System.currentTimeMillis();
            t2 += (end - start);

            Element n1 = pairing.getZr().newRandomElement();

            start = System.currentTimeMillis();
            ppp.powZn(n1);
            end = System.currentTimeMillis();
            t3 += (end - start);
        }

        System.out.printf("(" + label + ") Element#pow()                           = %f\n", (double) t1 / times);
        System.out.printf("(" + label + ") ElementPowPreProcessing#pow(BigInteger) = %f\n", (double) t2 / times);
        System.out.printf("(" + label + ") ElementPowPreProcessing#powZn(Element)  = %f\n", (double) t3 / times);
    }



    public static void main(String[] args) {
        JPBCBenchmark benchmark = new JPBCBenchmark(args[0], Integer.parseInt(args[1]));
        System.out.printf("PBC Benchmark{%s %s}\n", args[0], args[1]);
        benchmark.setUp();
        benchmark.benchmarkPairing();
        benchmark.benchmarkPairingPreProcessing();
        benchmark.benchmarkPow();
        benchmark.benchmarkPowPreProcessing();
        System.out.printf("PBC Benchmark. Finished.\n");
    }

}
