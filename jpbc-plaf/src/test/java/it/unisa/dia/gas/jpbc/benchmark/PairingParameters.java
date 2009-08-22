package it.unisa.dia.gas.jpbc.benchmark;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingParameters {

    public static void main(String[] args) {
        System.out.println(new BigInteger("2094476214847295281570670320144695883131009753607350517892357").bitLength());

        CurveParams curveParams = new CurveParams();
        curveParams.load(PairingParameters.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_603_181.properties"));

        Pairing pairing = PairingFactory.getPairing(curveParams);

        System.out.println("G1 FixedLengthInBytes = " + pairing.getG1().getLengthInBytes());
        System.out.println("G2 FixedLengthInBytes = " + pairing.getG2().getLengthInBytes());
        System.out.println("GT FixedLengthInBytes = " + pairing.getGT().getLengthInBytes());
        System.out.println("Zr FixedLengthInBytes = " + pairing.getZr().getLengthInBytes());

        System.out.println("-----------------------------------");
        curveParams.load(PairingParameters.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"));
        pairing = PairingFactory.getPairing(curveParams);


        System.out.println("G1 FixedLengthInBytes = " + pairing.getG1().getLengthInBytes());
        System.out.println("G2 FixedLengthInBytes = " + pairing.getG2().getLengthInBytes());
        System.out.println("GT FixedLengthInBytes = " + pairing.getGT().getLengthInBytes());
        System.out.println("Zr FixedLengthInBytes = " + pairing.getZr().getLengthInBytes());

    }
}
