#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <assert.h>
#include <pbc/pbc.h>
#include <pbc/pbc_field.h>
#include <pbc/pbc_pairing.h>


int sizeOfPairing() {
    return sizeof(pairing_t);
}

int sizeOfElement() {
    return sizeof(element_t);
}

void pairingInit(pairing_t pairing, const char *buf, size_t len) {
    pairing_init_inp_buf (pairing[0], buf, len);
}

void pairingClear(pairing_t pairing) {
    pairing_clear(pairing[0]);
}

void elementInitG1(element_t element, pairing_t pairing) {
    element_init_G1(element, pairing[0]);
}

void elementClear(element_t element) {
    element_clear(element);
}
