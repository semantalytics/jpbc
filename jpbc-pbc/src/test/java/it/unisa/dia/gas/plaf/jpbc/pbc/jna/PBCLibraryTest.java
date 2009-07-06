package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCLibraryTest extends TestCase {

    public void testSize() {
        assertNotSame(0, PBCLibrary.INSTANCE.pbc_pairing_sizeof());
        assertNotSame(0, PBCLibrary.INSTANCE.pbc_element_sizeof());
    }
}
