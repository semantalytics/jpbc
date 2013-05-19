package it.unisa.dia.gas.jpbc;

/**
 * This interface lets the user to generate all the necessary parameters
 * to initialize an instance of Pairing interface.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface PairingParametersGenerator<P extends PairingParameters> {

    /**
     * Generates the parameters.
     *
     * @return a map with all the necessary parameters.
     * @since 1.3.0
     */
    P generate();

}
