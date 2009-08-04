package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingBenchmark extends PairingBenchmark {

    public PBCPairingBenchmark(String curveParamsFileName, int times) {
        super(curveParamsFileName, times);
    }

    @Override
    public void setUp() {
        pairing = new PBCPairing(getCurveParams());
    }


    public static void main(String[] args) {
        PBCPairingBenchmark test = new PBCPairingBenchmark(args[0],
                                                           Integer.parseInt(args[1]));

        System.out.printf("PBCPairingBenchmark{%s %s}\n", args[0], args[1]);
        test.setUp();
        test.testPairingPreProcessing();
        System.out.printf("PBCPairingBenchmark. Finished.\n");
    }

}