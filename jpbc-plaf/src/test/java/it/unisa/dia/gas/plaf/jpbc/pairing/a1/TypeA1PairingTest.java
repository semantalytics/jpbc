package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1PairingTest {

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties");
    }

/*
    public void testOrthogonality() {
        CurveParameters curveParameters = getCurveParameters();
        BigInteger p0 = curveParameters.getBigInteger("n0");
        BigInteger p1 = curveParameters.getBigInteger("n1");
        BigInteger p2 = curveParameters.getBigInteger("n2");

        Element gen = pairing.getG1().newRandomElement().getImmutable();
        Element gen0 = gen.pow(p1.multiply(p2)).getImmutable();
        Element gen1 = gen.pow(p0.multiply(p2)).getImmutable();
        Element gen2 = gen.pow(p0.multiply(p1)).getImmutable();
        Element gen01 = gen.pow(p2).getImmutable();
        Element gen02 = gen.pow(p1).getImmutable();
        Element gen12 = gen.pow(p0).getImmutable();

        Element e0 = gen0.powZn(pairing.getZr().newRandomElement());
        Element e1 = gen1.powZn(pairing.getZr().newRandomElement());
        Element e2 = gen2.powZn(pairing.getZr().newRandomElement());

        Element e01 = gen01.powZn(pairing.getZr().newRandomElement());
        Element e02 = gen02.powZn(pairing.getZr().newRandomElement());
        Element e12 = gen12.powZn(pairing.getZr().newRandomElement());

        assertEquals(true, pairing.pairing(e0, e1).isOne());
        assertEquals(true, pairing.pairing(e0, e2).isOne());
        assertEquals(true, pairing.pairing(e1, e2).isOne());
        assertEquals(true, pairing.pairing(e1, e0).isOne());
        assertEquals(true, pairing.pairing(e2, e0).isOne());
        assertEquals(true, pairing.pairing(e2, e1).isOne());

        assertEquals(false, pairing.pairing(e01, e02).isOne());
        assertEquals(false, pairing.pairing(e01, e12).isOne());
        assertEquals(false, pairing.pairing(e12, e02).isOne());
        assertEquals(false, pairing.pairing(e02, e01).isOne());
        assertEquals(false, pairing.pairing(e12, e01).isOne());
        assertEquals(false, pairing.pairing(e02, e12).isOne());

        assertEquals(true, pairing.pairing(e01, e2).isOne());
        assertEquals(true, pairing.pairing(e02, e1).isOne());
        assertEquals(true, pairing.pairing(e12, e0).isOne());
        assertEquals(true, pairing.pairing(e2, e01).isOne());
        assertEquals(true, pairing.pairing(e1, e02).isOne());
        assertEquals(true, pairing.pairing(e0, e12).isOne());
    }
*/

}