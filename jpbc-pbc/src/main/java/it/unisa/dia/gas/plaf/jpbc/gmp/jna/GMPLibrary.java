package it.unisa.dia.gas.plaf.jpbc.gmp.jna;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface GMPLibrary extends Library {

    void __gmpz_init(Pointer op);

    void __gmpz_set_str(Pointer op, String str, int base);

    String __gmpz_get_str(String str, int base, Pointer op);

    void __gmpz_clear(Pointer op);
    
}
