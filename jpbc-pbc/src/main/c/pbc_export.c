#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <assert.h>
#include <pbc/pbc.h>
#include <pbc/pbc_field.h>
#include <pbc/pbc_pairing.h>
#include <stdio.h>


int pbc_pairing_sizeof() {
    return sizeof(pairing_t);
}

int pbc_element_sizeof() {
    return sizeof(element_t);
}

int pbc_pairing_pp_sizeof() {
    return sizeof(pairing_pp_t);
}

int pbc_mpz_sizeof() {
    return sizeof(mpz_t);
}

int pbc_element_pp_sizeof() {
    return sizeof(element_pp_t);
}

void pbc_pairing_init_inp_buf(pairing_t pairing, const char *buf, size_t len) {
    pairing_init_set_buf (pairing, buf, len);
    //pairing_init_inp_buf (pairing, buf, len);
}

void pbc_pairing_pp_init(pairing_pp_t p, element_t in1, pairing_t pairing) {
    pairing_pp_init(p, in1, pairing);
}

void pbc_pairing_pp_clear(pairing_pp_t p) {
    pairing_pp_clear(p);
}

void pbc_pairing_pp_apply(element_t out, element_t in2, pairing_pp_t p) {
    pairing_pp_apply(out, in2, p);
}

void pbc_pairing_apply(element_t out, element_t in1, element_t in2, pairing_t pairing) {
    pairing_apply(out, in1, in2, pairing);
}

int pbc_pairing_is_symmetric(pairing_t pairing) {
    return pairing_is_symmetric(pairing);
}

int pbc_pairing_length_in_bytes_G1(pairing_t pairing) {
    return pairing_length_in_bytes_G1(pairing);
}

int pbc_pairing_length_in_bytes_x_only_G1(pairing_t pairing) {
    return pairing_length_in_bytes_x_only_G1(pairing);
}

int pbc_pairing_length_in_bytes_compressed_G1(pairing_t pairing) {
    return pairing_length_in_bytes_compressed_G1(pairing);
}

int pbc_pairing_length_in_bytes_G2(pairing_t pairing) {
    return pairing_length_in_bytes_G2(pairing);
}

int pbc_pairing_length_in_bytes_compressed_G2(pairing_t pairing) {
    return pairing_length_in_bytes_compressed_G2(pairing);
}

int pbc_pairing_length_in_bytes_x_only_G2(pairing_t pairing) {
    return pairing_length_in_bytes_x_only_G2(pairing);
}

int pbc_pairing_length_in_bytes_GT(pairing_t pairing) {
    return pairing_length_in_bytes_GT(pairing);
}

int pbc_pairing_length_in_bytes_Zr(pairing_t pairing) {
    return pairing_length_in_bytes_Zr(pairing);
}

void pbc_pairing_clear(pairing_t pairing) {
    pairing_clear(pairing);
}


void pbc_element_init_G1(element_t element, pairing_t pairing) {
    element_init_G1(element, pairing);
}

void pbc_element_init_G2(element_t element, pairing_t pairing) {
    element_init_G2(element, pairing);
}

void pbc_element_init_GT(element_t element, pairing_t pairing) {
    element_init_GT(element, pairing);
}

void pbc_element_init_Zr(element_t element, pairing_t pairing) {
    element_init_Zr(element, pairing);
}

void pbc_element_init_same_as(element_t e, element_t e2) {
    element_init_same_as(e, e2);
}

int pbc_element_snprint(char *s, size_t n, element_t e) {
    return element_snprint(s, n, e);
}

int pbc_element_set_str(element_t e, char *s, int base) {
    return element_set_str(e, s, base);
}

void pbc_element_set0(element_t e) {
    element_set0(e);
}

void pbc_element_set1(element_t e) {
    element_set1(e);
}

void pbc_element_set_si(element_t e, signed long int i) {
    element_set_si(e, i);
}

void pbc_element_set_mpz(element_t e, mpz_t z) {
    element_set_mpz(e, z);
}

void pbc_element_set(element_t e, element_t a) {
    element_set(e, a);
}

void pbc_element_add_ui(element_t n, element_t a, unsigned long int b) {
    element_add_ui(n, a, b);
}

void pbc_element_to_mpz(mpz_t z, element_t e) {
    element_to_mpz(z, e);
}

void pbc_element_from_hash(element_t e, void *data, int len) {
    element_from_hash(e, data, len);
}

void pbc_element_add(element_t n, element_t a, element_t b) {
    element_add(n, a, b);
}

void pbc_element_sub(element_t n, element_t a, element_t b) {
    element_sub(n, a, b);
}

void pbc_element_mul(element_t n, element_t a, element_t b) {
    element_mul(n, a, b);
}

void pbc_element_mul_mpz(element_t n, element_t a, mpz_t z) {
    element_mul_mpz(n, a, z);
}

void pbc_element_mul_si(element_t n, element_t a, signed long int z) {
    element_mul_si(n, a, z);
}

void pbc_element_mul_zn(element_t c, element_t a, element_t z) {
    element_mul_zn(c, a, z);
}

void pbc_element_div(element_t n, element_t a, element_t b) {
    element_div(n, a, b);
}

void pbc_element_double(element_t n, element_t a) {
    element_double(n, a);
}

void pbc_element_halve(element_t n, element_t a) {
    element_halve(n, a);
}

void pbc_element_square(element_t n, element_t a) {
    element_square(n, a);
}

void pbc_element_pow_mpz(element_t x, element_t a, mpz_t n) {
    element_pow_mpz(x, a, n);
}

void pbc_element_pow_zn(element_t x, element_t a, element_t n) {
    element_pow_zn(x, a, n);
}

void pbc_element_neg(element_t n, element_t a) {
    element_neg(n, a);
}

void pbc_element_invert(element_t n, element_t a) {
    element_invert(n, a);
}

void pbc_element_random(element_t e) {
    element_random(e);
}

int pbc_element_is1(element_t n) {
    return element_is1(n);
}

int pbc_element_is0(element_t n) {
    return element_is0(n);
}

int pbc_element_cmp(element_t a, element_t b) {
    return element_cmp(a, b);
}

int pbc_element_is_sqr(element_t a) {
    return element_is_sqr(a);
}

int pbc_element_sgn(element_t a) {
    return element_sgn(a);
}

int pbc_element_sign(element_t a) {
    return element_sign(a);
}

void pbc_element_sqrt(element_t a, element_t b) {
    element_sqrt(a, b);
}

int pbc_element_length_in_bytes(element_t e) {
    return element_length_in_bytes(e);
}

int pbc_element_to_bytes(unsigned char *data, element_t e) {
    return element_to_bytes(data, e);
}

int pbc_element_from_bytes(element_t e, unsigned char *data) {
    return element_from_bytes(e, data);
}

void pbc_element_clear(element_t element) {
    element_clear(element);
}


void pbc_curve_x_coord(element_t out, element_t element) {
    // TODO: what happen to the memory used by out?
    element_ptr x = element_x(element);

    element_init_same_as(out, x);
    element_set(out, x);
    out[0] = *x;
}

void pbc_curve_y_coord(element_t out, element_t element) {
    // TODO: what happen to the memory used by out?
    element_ptr y = element_y(element);

    element_init_same_as(out, y);
    element_set(out, y);
    out[0] = *y;
}


void pbc_element_pp_init(element_pp_t p, element_t in) {
    element_pp_init(p, in);
}

void pbc_element_pp_clear(element_pp_t p) {
    element_pp_clear(p);
}

void pbc_element_pp_pow(element_t out, mpz_ptr power, element_pp_t p) {
    element_pp_pow(out, power, p);
}

void pbc_element_pp_pow_zn(element_t out, element_t power, element_pp_t p) {
    element_pp_pow_zn(out, power, p);
}


void pbc_field_order(element_t element, mpz_t order) {
    //gmp_fprintf(stderr, "element->field->order = %Zd\n", element->field->order);
    mpz_set(order, element->field->order);
    //gmp_fprintf(stderr, "order = %Zd\n", order);
}