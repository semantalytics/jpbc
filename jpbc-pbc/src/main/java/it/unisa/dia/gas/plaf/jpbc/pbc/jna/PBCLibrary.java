package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface PBCLibrary extends Library {

    int pbc_pairing_sizeof();

    int pbc_element_sizeof();

    int pbc_pairing_pp_sizeof();

    int pbc_mpz_sizeof();

    int pbc_element_pp_sizeof();


    void pbc_pairing_init_inp_buf(Pointer pairing, String buf, int len);

    void pbc_pairing_pp_init(Pointer p, Pointer in1, Pointer pairing);

    void pbc_pairing_pp_clear(Pointer p);

    void pbc_pairing_pp_apply(Pointer out, Pointer in2, Pointer p);

    void pbc_pairing_apply(Pointer out, Pointer in1, Pointer in2, Pointer pairing);

    int pbc_pairing_is_symmetric(Pointer pairing);

    int pbc_pairing_length_in_bytes_G1(Pointer pairing);

    int pbc_pairing_length_in_bytes_x_only_G1(Pointer pairing);

    int pbc_pairing_length_in_bytes_compressed_G1(Pointer pairing);

    int pbc_pairing_length_in_bytes_G2(Pointer pairing);

    int pbc_pairing_length_in_bytes_compressed_G2(Pointer pairing);

    int pbc_pairing_length_in_bytes_x_only_G2(Pointer pairing);

    int pbc_pairing_length_in_bytes_GT(Pointer pairing);

    int pbc_pairing_length_in_bytes_Zr(Pointer pairing);

    void pbc_pairing_clear(Pointer pairing);


    void pbc_element_init_G1(Pointer element, Pointer pairing);

    void pbc_element_init_G2(Pointer element, Pointer pairing);

    void pbc_element_init_GT(Pointer element, Pointer pairing);

    void pbc_element_init_Zr(Pointer element, Pointer pairing);

    void pbc_element_init_same_as(Pointer e, Pointer e2);

    int pbc_element_snprint(Pointer s, int n, Pointer e);

    int pbc_element_set_str(Pointer e, String s, int base);

    void pbc_element_set0(Pointer e);

    void pbc_element_set1(Pointer e);

    void pbc_element_set_si(Pointer e, long i);

    void pbc_element_set_mpz(Pointer e, MPZElementType z);

    void pbc_element_set(Pointer e, Pointer a);

    void pbc_element_add_ui(Pointer n, Pointer a, long b);

    void pbc_element_to_mpz(MPZElementType z, Pointer e);

    void pbc_element_from_hash(Pointer e, Pointer data, int len);

    void pbc_element_add(Pointer n, Pointer a, Pointer b);

    void pbc_element_sub(Pointer n, Pointer a, Pointer b);

    void pbc_element_mul(Pointer n, Pointer a, Pointer b);

    void pbc_element_mul_mpz(Pointer n, Pointer a, MPZElementType z);

    void pbc_element_mul_si(Pointer n, Pointer a, long z);

    void pbc_element_mul_zn(Pointer c, Pointer a, Pointer z);

    void pbc_element_div(Pointer n, Pointer a, Pointer b);

    void pbc_element_double(Pointer n, Pointer a);

    void pbc_element_halve(Pointer n, Pointer a);

    void pbc_element_square(Pointer n, Pointer a);

    void pbc_element_pow_mpz(Pointer x, Pointer a, MPZElementType n);

    void pbc_element_pow_zn(Pointer x, Pointer a, Pointer n);

    void pbc_element_neg(Pointer n, Pointer a);

    void pbc_element_invert(Pointer n, Pointer a);

    void pbc_element_random(Pointer e);

    int pbc_element_is1(Pointer n);

    int pbc_element_is0(Pointer n);

    int pbc_element_cmp(Pointer a, Pointer b);

    int pbc_element_is_sqr(Pointer a);

    int pbc_element_sgn(Pointer a);

    int pbc_element_sign(Pointer a);

    void pbc_element_sqrt(Pointer a, Pointer b);

    int pbc_element_length_in_bytes(Pointer e);

    int pbc_element_to_bytes(byte[] data, Pointer e);

    int pbc_element_from_bytes(Pointer e, byte[] data);

    void pbc_element_clear(Pointer element);


    void pbc_curve_x_coord(Pointer out, Pointer element);

    void pbc_curve_y_coord(Pointer out, Pointer element);


    void pbc_element_pp_init(Pointer p, Pointer element);

    void pbc_element_pp_clear(Pointer p);

    void pbc_element_pp_pow(Pointer out, Pointer power, Pointer p);


    int element_length_in_bytes_x_only(Pointer element);

    int element_to_bytes_x_only(byte[] data, Pointer element);

    int element_from_bytes_x_only(Pointer element, byte[] data);


    int element_length_in_bytes_compressed(Pointer element);
    
    int element_to_bytes_compressed(byte[] data, Pointer element);

    int element_from_bytes_compressed(Pointer element, byte[] data);

}
