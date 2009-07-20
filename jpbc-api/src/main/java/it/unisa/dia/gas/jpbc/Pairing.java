package it.unisa.dia.gas.jpbc;

/**
 * This interface gives access to the pairing functions.
 * Pairings involve three groups of prime order r called G1, G2, and GT.
 * The pairing is a bilinear map that takes two elements as input, one from G1 and one from G2, and outputs an element of GT.
 * Sometimes G1 and G2 are the same group (i.e. the pairing is symmetric) so their elements can be mixed freely.
 * In this case the #isSymmetric() function returns true.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Pairing {

    /**
     * Returns true if this pairing is symmetric, false otherwise.
     *
     * @return true if this pairing is symmetric, false otherwise.
     */
    boolean isSymmetric();

    /**
     * Return the G1 field.
     *
      * @return the G1 field.
     */
    Field<? extends Point> getG1();

    /**
     * Return the G2 field.
     *
      * @return the G2 field.
     */
    Field<? extends Point> getG2();

    /**
     * Return the GT field which is the group of rth roots of unity.
     *
      * @return the GT field.
     */
    Field getGT();

    /**
     * Return the Zr field.
     *
      * @return the Zr field.
     */
    Field getZr();

    /**
     * Applies the bilinear map. It returns e(g1, g2). g1 must be in the group G1, g2 must be in the group G2.
     *
     * @param g1 an element from G1.
     * @param g2 an element from G2.
     * @return an element from GT whose value is assigned by this map applied to g1 and g2.
     */
    Element pairing(Element g1, Element g2);

}
