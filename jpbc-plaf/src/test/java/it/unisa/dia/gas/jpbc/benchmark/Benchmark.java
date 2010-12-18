package it.unisa.dia.gas.jpbc.benchmark;

/**
 * Angelo De Caro (decaro@dia.unisa.it)
 */
public class Benchmark {
    public static final String[] pairingBenchmarkNames = new String[]{
            "Pairing#pairing(in1, in2)",
            "Pairing#pairing(in1)",
            "PairingPreProcessing#pairing(in2)",
    };

    public static final String[] elementBenchmarkNames = new String[]{
            "Element#pow(BigInteger)",
            "Element#powZn(Element)",
            "Element#pow()",
            "ElementPowPreProcessing#pow(BigInteger)",
            "ElementPowPreProcessing#powZn(Element)",
            "Element#mul(BigInteger)",
            "Element#setToRandom()"
    };

    public static final String[] fieldNames = new String[]{
            "G1", "G2", "GT", "Zr"
    };

    private String[] curves;
    private double[][] pairingBenchmarks;
    private double[][][] elementBenchmarks;


    public Benchmark(String[] curves) {
        this.curves = curves;
        this.pairingBenchmarks = new double[3 + (5 * 4)][curves.length];
        this.elementBenchmarks = new double[fieldNames.length][elementBenchmarkNames.length][curves.length];
    }


    public double[][] getPairingBenchmarks() {
        return pairingBenchmarks;
    }

    public double[][][] getElementBenchmarks() {
        return elementBenchmarks;
    }

    
    public String toHTML() {
        StringBuffer buffer = new StringBuffer();

        buffer  .append("                <table>\n")
                .append("                    <tr>\n")
                .append("                        <th>Benchmark - Average Time (ms)</th>\n")
                .append("                        <th>Pairing Type</th>\n")
                .append("                    </tr>\n")
                .append("                    <tr>\n")
                .append("                        <th></th>\n");
        for (String curve : curves) {
            curve = curve.substring(curve.lastIndexOf('/')+1, curve.lastIndexOf('.'));

            buffer.append("                        <th><font style=\"font-weight: bold;color:green\">").append(curve).append("</strong></th>\n");
        }
        buffer.append("                    </tr>\n");

        for (int row = 0; row < pairingBenchmarkNames.length; row++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\"><font style=\"font-weight: bold;color:green\">").append(pairingBenchmarkNames[row]).append("</strong></th>\n");
            for (int col = 0; col < curves.length; col++) {
                buffer.append("                        <td>").append(pairingBenchmarks[row][col]).append("</td>\n");
            }
            buffer.append("                    </tr>\n");
        }

        for (int fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\">\n")
                    .append("                            <font style=\"font-weight: bold;color:black\">Element Pow (").append(fieldNames[fieldIndex]).append(")</strong>\n")
                    .append("                        </th>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                    </tr>\n");

            for (int row = 0; row < elementBenchmarkNames.length; row++) {
                buffer.append("                    <tr>\n")
                        .append("                        <th align=\"left\"><font style=\"font-weight: bold;color:green\">")
                        .append(elementBenchmarkNames[row])
                        .append("</strong></th>\n");
                for (int col = 0; col < curves.length; col++) {
                    buffer.append("                        <td>").append(elementBenchmarks[fieldIndex][row][col]).append("</td>\n");
                }
                buffer.append("                    </tr>\n");
            }
        }

        buffer.append("                </table>\n");

        return buffer.toString();
    }

    public String toHTML(Benchmark... benchmarks) {
        StringBuffer buffer = new StringBuffer();

        buffer  .append("                <table>\n")
                .append("                    <tr>\n")
                .append("                        <th>Benchmark - Average Time (ms)</th>\n")
                .append("                        <th>Pairing Type</th>\n")
                .append("                    </tr>\n")
                .append("                    <tr>\n")
                .append("                        <th></th>\n");
        for (String curve : curves) {
            curve = curve.substring(curve.lastIndexOf('/')+1, curve.lastIndexOf('.'));

            buffer.append("                        <th><font style=\"font-weight: bold;color:green\">").append(curve).append("</strong></th>\n");
        }
        buffer.append("                    </tr>\n");

        for (int row = 0; row < pairingBenchmarkNames.length; row++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\"><font style=\"font-weight: bold;color:green\">").append(pairingBenchmarkNames[row]).append("</strong></th>\n");
            for (int col = 0; col < curves.length; col++) {
                buffer.append("                        <td>").append(pairingBenchmarks[row][col]);

                if (benchmarks != null) {
                    buffer.append(" (");
                    for (int i = 0; i < benchmarks.length; i++) {
                        Benchmark benchmark = benchmarks[i];
                        buffer.append(benchmark.getPairingBenchmarks()[row][col]);
                        if (i+1 < benchmarks.length)
                            buffer.append(", ");
                    }
                    buffer.append(")");
                }

                buffer.append("</td>\n");
            }
            buffer.append("                    </tr>\n");
        }

        for (int fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++) {
            buffer.append("                    <tr>\n")
                    .append("                        <th align=\"left\">\n")
                    .append("                            <font style=\"font-weight: bold;color:black\">Element Pow (").append(fieldNames[fieldIndex]).append(")</strong>\n")
                    .append("                        </th>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                        <td></td>\n")
                    .append("                    </tr>\n");

            for (int row = 0; row < elementBenchmarkNames.length; row++) {
                buffer.append("                    <tr>\n")
                        .append("                        <th align=\"left\"><font style=\"font-weight: bold;color:green\">")
                        .append(elementBenchmarkNames[row])
                        .append("</strong></th>\n");
                for (int col = 0; col < curves.length; col++) {
                    buffer.append("                        <td>").append(elementBenchmarks[fieldIndex][row][col]);

                    if (benchmarks != null) {
                        buffer.append(" (");
                        for (int i = 0; i < benchmarks.length; i++) {
                            Benchmark benchmark = benchmarks[i];
                            buffer.append(benchmark.getElementBenchmarks()[fieldIndex][row][col]);
                            if (i+1 < benchmarks.length)
                                buffer.append(", ");
                        }
                        buffer.append(")");
                    }

                    buffer.append("</td>\n");
                }
                buffer.append("                    </tr>\n");
            }
        }

        buffer.append("                </table>\n");

        return buffer.toString();
    }
}
