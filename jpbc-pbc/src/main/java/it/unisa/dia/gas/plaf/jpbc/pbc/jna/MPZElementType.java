package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

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
        GMPLibrary.INSTANCE.__gmpz_init(this);
    }

    public String toString(int base) {
        return GMPLibrary.INSTANCE.__gmpz_get_str(null, base, this);
    }

    public void setFromString(String s, int base) {
        GMPLibrary.INSTANCE.__gmpz_set_str(this, s, base);
    }

    @Override
    protected void finalize() {
        if (isValid()) {
            GMPLibrary.INSTANCE.__gmpz_clear(this);

            super.finalize();
        }
    }

}
