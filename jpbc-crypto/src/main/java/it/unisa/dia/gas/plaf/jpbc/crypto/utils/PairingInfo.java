package it.unisa.dia.gas.plaf.jpbc.crypto.utils;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingInfo {

    public static void main(String[] args) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(args[0]);

        Pairing pairing = PairingFactory.getInstance().initPairing(curveParams);
        System.out.println("pairing.isSymmetric() = " + pairing.isSymmetric());
        System.out.println("pairing.getZr().getLengthInBytes() = " + pairing.getZr().getLengthInBytes());
        System.out.println("pairing.getG1().getLengthInBytes() = " + pairing.getG1().getLengthInBytes());
        System.out.println("pairing.getG2().getLengthInBytes() = " + pairing.getG2().getLengthInBytes());
        System.out.println("pairing.getGT().getLengthInBytes() = " + pairing.getGT().getLengthInBytes());
        System.out.println("pairing.getG1().newElement().getLengthInBytesCompressed() = " + ((Point) pairing.getG1().newElement()).getLengthInBytesCompressed());
        System.out.println("pairing.getG2().newElement().getLengthInBytesCompressed() = " + ((Point) pairing.getG2().newElement()).getLengthInBytesCompressed());
    }

}
