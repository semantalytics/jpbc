package it.unisa.dia.gas.jpbc;

/**
 * Common interface for all pre-processing interfaces.
 *
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface PreProcessing {

    /**
     * Converts the proprocessing information to bytes.
     *
     * @return the bytes written.
     * @since 1.2.0
     */
    byte[] toBytes();

}
