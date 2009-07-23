package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPowBenchmark extends ElementPowBenchmark {

    public PBCElementPowBenchmark(String curveParamsFileName, int times) {
        super(curveParamsFileName, times);
    }

    @Override
    public void setUp() {
        pairing = new PBCPairing(getCurveParams());
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
