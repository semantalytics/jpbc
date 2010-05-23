package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCLibraryTest extends TestCase {

    public void testSize() {
        System.out.println(System.getProperty("java.library.path"));

        // Check for link library
        if (!PBCLibraryProvider.isAvailable())
            return;
                
        assertTrue(PBCLibraryProvider.isAvailable());

        System.out.println("pbc_pairing_sizeof() = " + PBCLibraryProvider.getPbcLibrary().pbc_pairing_sizeof());
        System.out.println("pbc_pairing_pp_sizeof() = " + PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_sizeof());
        System.out.println("pbc_mpz_sizeof() = " + PBCLibraryProvider.getPbcLibrary().pbc_mpz_sizeof());
        System.out.println("pbc_element_sizeof() = " + PBCLibraryProvider.getPbcLibrary().pbc_element_sizeof());
        System.out.println("pbc_element_pp_sizeof() = " + PBCLibraryProvider.getPbcLibrary().pbc_element_pp_sizeof());

        assertNotSame(0, PBCLibraryProvider.getPbcLibrary().pbc_pairing_sizeof());
        assertNotSame(0, PBCLibraryProvider.getPbcLibrary().pbc_element_sizeof());
        assertNotSame(0, PBCLibraryProvider.getPbcLibrary().pbc_mpz_sizeof());
        assertNotSame(0, PBCLibraryProvider.getPbcLibrary().pbc_element_pp_sizeof());
        assertNotSame(0, PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_sizeof());
    }
}
