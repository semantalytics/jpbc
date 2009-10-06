package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingBenchmark {

    protected Pairing pairing;
    protected String curveParamsFileName;
    protected int times;


    public PairingBenchmark(String curveParamsFileName, int times) {
        this.curveParamsFileName = curveParamsFileName;
        this.times = times;
    }

    public void setUp() {
        pairing = PairingFactory.getPairing(getCurveParams());
    }


    public void testPairing() {
        if (pairing == null)
            return;

        long elapsed = 0;

        for (int i = 0; i < times; i++) {
            Element g = pairing.getG1().newElement().setToRandom();
            Element h = pairing.getG2().newElement().setToRandom();

            long start = System.currentTimeMillis();
            Element r2 = pairing.pairing(g, h);
            long end = System.currentTimeMillis();
            elapsed += (end - start);
        }

        System.out.printf("[testPairing] Pairing (ms) = %d, Mean (ms) = %f\n", elapsed, (double) elapsed / times);
    }

    public void testPairingPreProcessing() {
        if (pairing == null)
            return;

        long el1 = 0;
        long el2 = 0;

        for (int i = 0; i < times; i++) {
            Element g = pairing.getG1().newElement().setToRandom();

            long start = System.currentTimeMillis();
            PairingPreProcessing ppp = pairing.pairing(g);
            long end = System.currentTimeMillis();
            el1 += (end - start);

            Element h = pairing.getG2().newElement().setToRandom();

            start = System.currentTimeMillis();
            Element r2 = pairing.pairing(g, h);
            end = System.currentTimeMillis();
            el2 += (end - start);
        }

        System.out.printf("[testPairingPreProcessing] PreProcessing (ms) = %d, Mean (ms) = %f\n", el1, (double) el1 / times);
        System.out.printf("[testPairingPreProcessing] Pairing (ms) = %d, Mean (ms) = %f\n", el2, (double) el2 / times);
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


    public static void main(String[] args) {
        PairingBenchmark test = new PairingBenchmark(args[0],
                                                     Integer.parseInt(args[1]));
        System.out.printf("PairingBenchmark{%s %s}\n", args[0], args[1]);
        test.setUp();
        test.testPairing();
        test.testPairingPreProcessing();
        System.out.printf("PairingBenchmark. Finished.\n");
    }

}
