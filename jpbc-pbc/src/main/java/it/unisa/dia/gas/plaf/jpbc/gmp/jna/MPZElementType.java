package it.unisa.dia.gas.plaf.jpbc.gmp.jna;

import com.sun.jna.Memory;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MPZElementType extends Memory {

    public static MPZElementType fromBigInteger(BigInteger bigInteger) {
        MPZElementType element = new MPZElementType();
        element.init();
        element.setFromString(bigInteger.toString(), 10);

        return element;
    }


    public MPZElementType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_mpz_sizeof());
    }


    public void init() {
        GMPLibraryProvider.getGmpLibrary().__gmpz_init(this);
    }

    public String toString(int base) {
        return GMPLibraryProvider.getGmpLibrary().__gmpz_get_str(null, base, this);
    }

    public void setFromString(String s, int base) {
        GMPLibraryProvider.getGmpLibrary().__gmpz_set_str(this, s, base);
    }

    public MPZElementType duplicate() {
        // TODO: finish implementation...
        MPZElementType copy = new MPZElementType();
        copy.init();

        return copy;
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            GMPLibraryProvider.getGmpLibrary().__gmpz_clear(this);

            super.finalize();
        }
    }

}
