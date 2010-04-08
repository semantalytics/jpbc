package it.unisa.dia.gas.jpbc.benchmark;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCjPBCBenchmark {
    protected int times;


    public PBCjPBCBenchmark(int times) {
        this.times = times;
    }


    public String benchmark(String[] curves) {
        PBCBenchmark pbcBenchmark = new PBCBenchmark(times);
        JPBCBenchmark jpbcBenchmark = new JPBCBenchmark(times);

        Benchmark benchmarkPBC = pbcBenchmark.benchmark(curves);
        Benchmark benchmarkJPBC = jpbcBenchmark.benchmark(curves);

        return benchmarkJPBC.toHTML(benchmarkPBC);
    }


    public static void main(String[] args) {
        PBCjPBCBenchmark benchmark = new PBCjPBCBenchmark(Integer.parseInt(args[0]));
        String[] curves = Arrays.copyOfRange(args, 1, args.length);

        System.out.printf("PBCjPBC Benchmark.\n");
        System.out.printf("#Times = %s\n", args[0]);
        for (String curve : curves) {
            System.out.printf("Curve = %s\n", curve);
        }
        System.out.printf("Results: \n %s\n", benchmark.benchmark(curves));
        System.out.printf("PBCjPBC Benchmark. Finished.\n");
    }
}