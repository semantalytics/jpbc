#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <assert.h>
#include <stdio.h>
#include <math.h>

#include <gmp.h>


int gmp_mpz_sizeof() {
    return sizeof(mpz_t);
}

int gmp_mpz_sign(mpz_ptr value) {
    return mpz_sgn(value);
}
