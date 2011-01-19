package it.unisa.dia.gas.plaf.jpbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeFCurveGeneratorPairingTest extends CurveGeneratorPairingTest {

    @Override
    protected CurveGenerator getCurveGenerator() {
        CurveGenerator gen = new TypeFCurveGenerator(160);

        Pairing pairing = PairingFactory.getPairing(gen.generate());

        Element P = pairing.getG1().newRandomElement();
        Element Q = pairing.getG2().newRandomElement();

        Element res = pairing.pairing(P, Q);

        byte[] r = res.toBytes();
        for(byte a : r){
            System.out.print(String.format("%02X", a));
        }

        System.out.println();

        return new TypeFCurveGenerator(160);
    }

}
