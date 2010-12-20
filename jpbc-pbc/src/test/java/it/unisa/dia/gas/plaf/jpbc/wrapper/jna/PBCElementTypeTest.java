package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;

import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementTypeTest extends TestCase {

    @Override
    protected void tearDown() throws Exception {
        System.out.println("gc");
        System.gc();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void test() {
        PBCPairingType pbcPairingType = new PBCPairingType(
                new CurveParams().load(PBCElementTypeTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties")).toString(" ")
        );


        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int field = random.nextInt(4);
//            System.out.println("field = " + field);
            PBCElementType pbcElementType = null;
            switch (field) {
                case 0:
                    pbcElementType = new PBCElementType(
                            PBCElementType.FieldType.G1,
                            pbcPairingType
                    );
                    break;
                case 1:
                    pbcElementType = new PBCElementType(
                            PBCElementType.FieldType.G2,
                            pbcPairingType
                    );
                    break;
                case 2:
                    pbcElementType = new PBCElementType(
                            PBCElementType.FieldType.GT,
                            pbcPairingType
                    );
                    break;
                case 3:
                    pbcElementType = new PBCElementType(
                            PBCElementType.FieldType.Zr,
                            pbcPairingType
                    );
                    break;
            }
//            System.out.println("pbcElementType = " + pbcElementType);
        }
    }
}
