package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MPZElementType extends Memory {

    public static MPZElementType fromBigInteger(BigInteger bigInteger) {
        MPZElementType element = new MPZElementType();
        GMPLibrary.INSTANCE.__gmpz_init(element);
        GMPLibrary.INSTANCE.__gmpz_set_str(element, bigInteger.toString(), 2);

        return element;
    }


    public MPZElementType() {
        super(PBCLibrary.INSTANCE.pbc_mpz_sizeof());
    }

    @Override
    protected void finalize() {
        System.out.println("MPZElementType S: finalize!!");
        GMPLibrary.INSTANCE.__gmpz_clear(this);
        super.finalize();
        System.out.println("MPZElementType E: finalize!!");
    }
}
