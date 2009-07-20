package it.unisa.dia.gas.jpbc;

import java.util.Map;

/**
 * This interface lets the user to generate all the necessary curve parameters
 * to initialize an instance of Pairing interface.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface CurveGenerator {

    /**
     * Generates the curve parameters.
     *
     * @return a map with all the necessary parameters.
     */
    Map generate();

}
