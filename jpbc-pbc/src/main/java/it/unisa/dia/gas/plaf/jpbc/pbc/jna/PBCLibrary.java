package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface PBCLibrary extends Library {
    public static PBCLibrary INSTANCE = (PBCLibrary) Native.loadLibrary("jpbc-pbc", PBCLibrary.class);

    int pbc_pairing_sizeof();

    int pbc_element_sizeof();

    int pbc_pairing_pp_sizeof();

    int pbc_mpz_sizeof();

    void pbc_pairing_init_inp_buf(PBCPairingType pairing, String buf, int len);

    void pbc_pairing_pp_init(PBCPairingPPType p, PBCElementType in1, PBCPairingType pairing);

    void pbc_pairing_pp_clear(PBCPairingPPType p);

    void pbc_pairing_pp_apply(PBCElementType out, PBCElementType in2, PBCPairingPPType p);

    void pbc_pairing_apply(PBCElementType out, PBCElementType in1, PBCElementType in2, PBCPairingType pairing);

    int pbc_pairing_is_symmetric(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_G1(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_x_only_G1(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_compressed_G1(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_G2(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_compressed_G2(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_x_only_G2(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_GT(PBCPairingType pairing);

    int pbc_pairing_length_in_bytes_Zr(PBCPairingType pairing);

    void pbc_pairing_clear(PBCPairingType pairing);


    void pbc_element_init_G1(PBCElementType element, PBCPairingType pairing);

    void pbc_element_init_G2(PBCElementType element, PBCPairingType pairing);

    void pbc_element_init_GT(PBCElementType element, PBCPairingType pairing);

    void pbc_element_init_Zr(PBCElementType element, PBCPairingType pairing);

    void pbc_element_init_same_as(PBCElementType e, PBCElementType e2);

    int pbc_element_snprint(Pointer s, int n, PBCElementType e);

    int pbc_element_set_str(PBCElementType e, String s, int base);

    void pbc_element_set0(PBCElementType e);

    void pbc_element_set1(PBCElementType e);

    void pbc_element_set_si(PBCElementType e, long i);

    void pbc_element_set_mpz(PBCElementType e, MPZElementType z);

    void pbc_element_set(PBCElementType e, PBCElementType a);

    void pbc_element_add_ui(PBCElementType n, PBCElementType a, long b);

    void pbc_element_to_mpz(MPZElementType z, PBCElementType e);

    void pbc_element_from_hash(PBCElementType e, Pointer data, int len);

    void pbc_element_add(PBCElementType n, PBCElementType a, PBCElementType b);

    void pbc_element_sub(PBCElementType n, PBCElementType a, PBCElementType b);

    void pbc_element_mul(PBCElementType n, PBCElementType a, PBCElementType b);

    void pbc_element_mul_mpz(PBCElementType n, PBCElementType a, MPZElementType z);

    void pbc_element_mul_si(PBCElementType n, PBCElementType a, long z);

    void pbc_element_mul_zn(PBCElementType c, PBCElementType a, PBCElementType z);

    void pbc_element_div(PBCElementType n, PBCElementType a, PBCElementType b);

    void pbc_element_double(PBCElementType n, PBCElementType a);

    void pbc_element_halve(PBCElementType n, PBCElementType a);

    void pbc_element_square(PBCElementType n, PBCElementType a);

    void pbc_element_pow_mpz(PBCElementType x, PBCElementType a, MPZElementType n);

    void pbc_element_pow_zn(PBCElementType x, PBCElementType a, PBCElementType n);

    void pbc_element_neg(PBCElementType n, PBCElementType a);

    void pbc_element_invert(PBCElementType n, PBCElementType a);

    void pbc_element_random(PBCElementType e);

    int pbc_element_is1(PBCElementType n);

    int pbc_element_is0(PBCElementType n);

    int pbc_element_cmp(PBCElementType a, PBCElementType b);

    int pbc_element_is_sqr(PBCElementType a);

    int pbc_element_sgn(PBCElementType a);

    int pbc_element_sign(PBCElementType a);

    void pbc_element_sqrt(PBCElementType a, PBCElementType b);

    int pbc_element_length_in_bytes(PBCElementType e);

    int pbc_element_to_bytes(byte[] data, PBCElementType e);

    int pbc_element_from_bytes(PBCElementType e, byte[] data);

    void pbc_element_clear(PBCElementType element);
    
}
