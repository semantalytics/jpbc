package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDPairingTest extends PairingTest {

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeDPairing(typeProperties());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected Properties typeProperties() {
        Properties properties = new Properties();
        try {
//            properties.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"));
            properties.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_159.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }


    public static void main(String[] args) {
        try {
            TypeDPairingTest test = new TypeDPairingTest();
            test.setUp();
            test.pairingBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}