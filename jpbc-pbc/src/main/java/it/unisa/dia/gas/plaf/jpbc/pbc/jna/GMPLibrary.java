package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface GMPLibrary extends Library {
    public static GMPLibrary INSTANCE = (GMPLibrary) Native.loadLibrary("gmp", GMPLibrary.class);

    void __gmpz_init(Pointer pointer);

    void __gmpz_set_str(Pointer pointer, String str, int base);

    void __gmpz_clear(Pointer pointer);
}
