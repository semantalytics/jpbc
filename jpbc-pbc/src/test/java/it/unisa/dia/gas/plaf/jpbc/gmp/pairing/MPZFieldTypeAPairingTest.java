package it.unisa.dia.gas.plaf.jpbc.gmp.pairing;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.gmp.field.MPZField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairingTest;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class MPZFieldTypeAPairingTest extends TypeAPairingTest {

    @Override
    protected void setUp() throws Exception {
        CurveParams curveParams = getCurveParams();
        if (curveParams != null) {
            pairing = new TypeAPairing(curveParams) {
                @Override
                protected Field initFp(BigInteger order) {
                    return new MPZField(order);
                }
            };

            assertNotNull(pairing.getG1());
            assertNotNull(pairing.getG2());
            assertNotNull(pairing.getGT());
            assertNotNull(pairing.getZr());
        } else
            pairing = null;
    }
}
