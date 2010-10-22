package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1PairingTest extends PairingTest {

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"));
        return curveParams;

/*        TypeA1CurveGenerator generator = new TypeA1CurveGenerator(3, 128);
        return (CurveParams) generator.generate();*/
    }

    public void testOrthogonality() {
        CurveParams curveParams = getCurveParams();
        BigInteger p0 = curveParams.getBigInteger("n0");
        BigInteger p1 = curveParams.getBigInteger("n1");
        BigInteger p2 = curveParams.getBigInteger("n2");

        Element gen = pairing.getG1().newRandomElement().getImmutable();
        Element gen0 = gen.pow(p1.multiply(p2)).getImmutable();
        Element gen1 = gen.pow(p0.multiply(p2)).getImmutable();
        Element gen2 = gen.pow(p0.multiply(p1)).getImmutable();
        Element gen01 = gen.pow(p2).getImmutable();
        Element gen02 = gen.pow(p1).getImmutable();
        Element gen12 = gen.pow(p0).getImmutable();

        Element e0 = gen0.pow(pairing.getZr().newRandomElement().toBigInteger());
        Element e1 = gen1.pow(pairing.getZr().newRandomElement().toBigInteger());
        Element e2 = gen2.pow(pairing.getZr().newRandomElement().toBigInteger());

        Element e01 = gen01.pow(pairing.getZr().newRandomElement().toBigInteger());
        Element e02 = gen02.pow(pairing.getZr().newRandomElement().toBigInteger());
        Element e12 = gen12.pow(pairing.getZr().newRandomElement().toBigInteger());

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

}