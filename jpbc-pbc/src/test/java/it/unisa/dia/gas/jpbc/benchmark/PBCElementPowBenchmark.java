package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPowBenchmark {

    protected Pairing pairing;
    protected String curveParamsFileName;
    protected int times;


    public PBCElementPowBenchmark(String curveParamsFileName, int times) {
        this.curveParamsFileName = curveParamsFileName;
        this.times = times;
    }


    public void setUp() {
        pairing = new PBCPairing(getCurveParams());
    }

    public void testPowPreProcessing() {
        System.out.println("ElementPowBenchmark.testPowPreProcessing");

        if (pairing == null)
            return;

        long t1 = 0;
        long t2 = 0;

        for (int i = 0; i < times; i++) {
            Element e1 = pairing.getZr().newElement().setToRandom();
            Element e2 = e1.duplicate();
            ElementPowPreProcessing ppp = e1.pow();
            BigInteger n = pairing.getZr().newElement().setToRandom().toBigInteger();

            long start = System.currentTimeMillis();
            ppp.pow(n);
            long end = System.currentTimeMillis();
            t1 += (end - start);

            start = System.currentTimeMillis();
            e2.duplicate().pow(n);
            end = System.currentTimeMillis();
            t2 += (end - start);
        }

        System.out.println("t1 = " + t1/* / (double) times*/);
        System.out.println("t2 = " + t2/* / (double) times*/);
    }

    public void testPowPreProcessingZn() {
        System.out.println("ElementPowBenchmark.testPowPreProcessingZn");

        if (pairing == null)
            return;

        long t1 = 0;
        long t2 = 0;

        for (int i = 0; i < times; i++) {
            Element e1 = pairing.getZr().newElement().setToRandom();
            Element e2 = e1.duplicate();
            ElementPowPreProcessing ppp = e1.pow();
            Element n = pairing.getZr().newElement().setToRandom();

            long start = System.currentTimeMillis();
            ppp.powZn(n);
            long end = System.currentTimeMillis();
            t1 += (end - start);

            start = System.currentTimeMillis();
            e2.duplicate().powZn(n);
            end = System.currentTimeMillis();
            t2 += (end - start);
        }

        System.out.println("t1 = " + t1/* / (double) times*/);
        System.out.println("t2 = " + t2/* / (double) times*/);
    }


    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();

        try {
            File curveFile = new File(curveParamsFileName);
            if (curveFile.exists()) {
                curveParams.load(curveFile.toURI().toURL().openStream());

            } else
                curveParams.load(this.getClass().getClassLoader().getResourceAsStream(curveParamsFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return curveParams;
    }


    public static void main(String[] args) {
        PBCElementPowBenchmark test = new PBCElementPowBenchmark(args[0],
                                                                 Integer.parseInt(args[1]));

        System.out.printf("PBCElementPowBenchmark{%s %s}\n", args[0], args[1]);
        test.setUp();
        test.testPowPreProcessing();
        test.testPowPreProcessingZn();
    }

}
