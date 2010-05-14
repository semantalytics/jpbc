package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCBenchmark extends JPBCBenchmark {

    public PBCBenchmark(int times) {
        super(times);
    }


    @Override
    public Benchmark benchmark(String[] curves) {
        if (!PBCLibraryProvider.isAvailable())
            return null;
        return super.benchmark(curves);
    }

    protected Pairing getPairing(String curve) {
        return PairingFactory.getPairing(getCurveParams(curve));
    }                 

    
    public static void main(String[] args) {
        PBCBenchmark benchmark = new PBCBenchmark(Integer.parseInt(args[0]));
        String[] curves = Arrays.copyOfRange(args, 1, args.length);

        System.out.printf("PBC Benchmark.\n");
        System.out.printf("#Times = %s\n", args[0]);
        for (String curve : curves) {
            System.out.printf("Curve = %s\n", curve);
        }
        System.out.printf("Results: \n %s\n", benchmark.benchmark(curves).toHTML());
        System.out.printf("PBC Benchmark. Finished.\n");
    }
}