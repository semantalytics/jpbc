package it.unisa.dia.gas.jpbc;

/**
 * This interface gives access to the pairing functions.
 * Pairings involve three groups of prime order r called G1, G2, and GT.
 * The pairing is a bilinear map that takes two elements as input, one from G1 and one from G2, and outputs an element of GT.
 * Sometimes G1 and G2 are the same group (i.e. the pairing is symmetric) so their elements can be mixed freely.
 * In this case the #isSymmetric() function returns true.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface Pairing {

    /**
     * Returns true if this pairing is symmetric, false otherwise.
     *
     * @return true if this pairing is symmetric, false otherwise.
     * @since 1.0.0
     */
    boolean isSymmetric();

    /**
     * Return the G1 group.
     *
      * @return the G1 group.
     * @since 1.0.0
     */
    Field<? extends Point> getG1();

    /**
     * Return the G2 group.
     *
     * @return the G2 group.
     * @since 1.0.0
     */
    Field<? extends Point> getG2();

    /**
     * Return the GT group which is the group of rth roots of unity.
     *
     * @return the GT group.
     * @since 1.0.0
     */
    Field getGT();

    /**
     * Return the Zr group.
     *
     * @return the Zr group.
     * @since 1.0.0
     */
    Field getZr();

    /**
     * Applies the bilinear map. It returns e(in1, in2). g1 must be in the group G1, g2 must be in the group G2.
     *
     * @param in1 an element from G1.
     * @param in2 an element from G2.
     * @return an element from GT whose value is assigned by this map applied to in1 and in2.
     * @since 1.0.0
     */
    Element pairing(Element in1, Element in2);

    /**
     * Get ready to perform a pairing whose first input is in1, returns the results of time-saving pre-computation.
     *
     * @param in1 the first input of a pairing execution, used to pre-compute the pairing.
     *
     * @return the results of time-saving pre-computation.
     * @since 1.0.0
     */
    PairingPreProcessing pairing(Element in1);

    /**
     * Returns true given (g, g^x, h, h^x) or (g, g^x, h, h^-x)
     * order is important: a, b are from G1, c, d are from G2
     *
     * @param a eventually g
     * @param b eventually g^x
     * @param c eventually h
     * @param d eventually h^x or h^-x
     * @return <tt>true</tt> given (g, g^x, h, h^x) or (g, g^x, h, h^-x), <tt>false</tt> otherwise.
     */
    boolean isAlmostCoddh(Element a, Element b, Element c, Element d);
}
