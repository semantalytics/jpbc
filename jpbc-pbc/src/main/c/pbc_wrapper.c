#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <assert.h>
#include <pbc/pbc.h>
#include <pbc/pbc_field.h>
#include <pbc/pbc_pairing.h>

void pairingInit(pairing_t pairing, const char *buf, size_t len) {
    pairing_init_inp_buf (pairing, buf, len);
}

void pairingClear(pairing_t pairing) {
    pairing_clear(pairing);
}

void elementInitG1(element_t element, pairing_t pairing) {
    element_init_G1(element, pairing);
}

void elmentClear(element_t element) {
    element_clear(element);
}