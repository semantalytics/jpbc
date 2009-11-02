package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCBenchmark {
    protected int times;

    public PBCBenchmark(int times) {
        this.times = times;
    }


    public String benchmark(String[] curves) {
        StringBuffer buffer = new StringBuffer();

        benchmark(buffer, curves);

        return buffer.toString();
    }


    protected void benchmark(StringBuffer buffer, String[] curves) {
        // Pairing Benchmarks
        System.out.println("Pairing Benchmark...");

        String[] pairingBenchmarkNames = new String[]{
                "Pairing#pairing(in1, in2)",
                "Pairing#pairing(in1)",
                "PairingPreProcessing#pairing(in2)",
        };

        String[] elementBenchmarkNames = new String[]{
                "Element#pow(BigInteger)",
                "Element#powZn(Element)",
                "Element#pow()",
                "ElementPowPreProcessing#pow(BigInteger)",
                "ElementPowPreProcessing#powZn(Element)"
        };

        double[][] pairingBenchmarks = new double[3 + (5 * 4)][curves.length];

        for (int col = 0; col < curves.length; col++) {
            System.out.printf("Curve = %s...", curves[col]);

            Pairing pairing = getPairing(curves[col]);

            int t1 = 0, t2 = 0, t3 = 0;
            for (int i = 0; i < times; i++) {
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

            }

            pairingBenchmarks[0][col] = (double) t1 / times;
            pairingBenchmarks[1][col] = (double) t2 / times;
            pairingBenchmarks[2][col] = (double) t3 / times;
            System.out.printf("finished.\n");
        }

        System.out.println("Element Pow Benchmark...");
        // Element Pow Benchmarks
        String[] fieldNames = new String[]{
                "G1", "G2", "GT", "Zr"
        };
        double[][][] elementPowBenchmarks = new double[fieldNames.length][elementBenchmarkNames.length][curves.length];

        for (int col = 0; col < curves.length; col++) {
            System.out.printf("Curve = %s\n", curves[col]);

            Pairing pairing = getPairing(curves[col]);
            Field[] fields = new Field[]{
                    pairing.getG1(),
                    pairing.getG2(),
                    pairing.getGT(),
                    pairing.getZr()
            };

            for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
                System.out.printf("Field %s...", fieldNames[fieldIndex]);

                long t1 = 0, t2 = 0,t3 = 0,t4 = 0, t5 = 0;
                for (int i = 0; i < times; i++) {
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
                }

                elementPowBenchmarks[fieldIndex][0][col] = (double ) t1 / times;
                elementPowBenchmarks[fieldIndex][1][col] = (double ) t2 / times;
                elementPowBenchmarks[fieldIndex][2][col] = (double ) t3 / times;
                elementPowBenchmarks[fieldIndex][3][col] = (double ) t4 / times;
                elementPowBenchmarks[fieldIndex][4][col] = (double ) t5 / times;
                System.out.printf("finished.\n");
            }
        }


        buffer  .append("                <table>\n")
                .append("                    <tr>\n")
                .append("                        <th>Benchmark - Average Time (ms)</th>\n")
                .append("                        <th>Pairing Type</th>\n")
                .append("                    </tr>\n")
                .append("                    <tr>\n")
                .append("                        <th></th>\n");
        for (String curve : curves) {
            curve = curve.substring(curve.lastIndexOf('/')+1, curve.lastIndexOf('.'));

            buffer.append("                        <th><strong style=\"color:green\">").append(curve).append("</strong></th>\n");
        }
        buffer.append("                    </tr>\n");

        for (int row = 0; row < pairingBenchmarkNames.length; row++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\"><strong style=\"color:green\">").append(pairingBenchmarkNames[row]).append("</strong></th>\n");
            for (int col = 0; col < curves.length; col++) {
                buffer.append("                        <td>").append(pairingBenchmarks[row][col]).append("</td>\n");
            }
            buffer.append("                    </tr>\n");
        }

        for (int fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\">\n")
                    .append("                            <strong style=\"color:black\">Element Pow (").append(fieldNames[fieldIndex]).append(")</strong>\n")
                    .append("                        </th>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                    </tr>\n");
            
            for (int row = 0; row < elementBenchmarkNames.length; row++) {
                buffer.append("                    <tr>\n")
                        .append("                        <th align=\"left\"><strong style=\"color:green\">")
                        .append(elementBenchmarkNames[row])
                        .append("</strong></th>\n");
                for (int col = 0; col < curves.length; col++) {
                    buffer.append("                        <td>").append(elementPowBenchmarks[fieldIndex][row][col]).append("</td>\n");
                }
                buffer.append("                    </tr>\n");
            }
        }

        buffer.append("                </table>\n");
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
        return new PBCPairing(getCurveParams(curve));
    }                 

    
    public static void main(String[] args) {
        PBCBenchmark benchmark = new PBCBenchmark(Integer.parseInt(args[0]));
        String[] curves = Arrays.copyOfRange(args, 1, args.length);

        System.out.printf("PBC Benchmark.\n");
        System.out.printf("#Times = %s\n", args[0]);
        for (String curve : curves) {
            System.out.printf("Curve = %s\n", curve);
        }
        System.out.printf("Results: \n %s\n", benchmark.benchmark(curves));
        System.out.printf("PBC Benchmark. Finished.\n");
    }
}