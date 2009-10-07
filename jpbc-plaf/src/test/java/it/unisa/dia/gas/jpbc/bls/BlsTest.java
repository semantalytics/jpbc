package it.unisa.dia.gas.jpbc.bls;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BlsTest extends TestCase {

    public void testBls() {
        // Load pairing
        CurveParams curveParams = new CurveParams();
        curveParams.load(BlsTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"));
        Pairing pairing = PairingFactory.getPairing(curveParams);

        // Generate system parameters
        Element g = pairing.getG1().newRandomElement();

        // Generate the secret key
        Element x = pairing.getZr().newRandomElement();

        // Generate the corresponding public key
        Element pk = g.duplicate().powZn(x); // We need to duplicate g because it's a system parameter.

        // Map the hash of the message m to some element of G1

        byte[] hash = "ABCDEF".getBytes(); // Generate an hash from m (48-bit hash)
        Element h = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);

        // Generate the signature

        Element sig = h.powZn(x); // We can discard the value h, so we don't need to duplicate it.

        // Map again the hash of the message m

        hash = "ABCDEF".getBytes(); // Generate an hash from m (48-bit hash)
        h = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);

        // Verify the signature

        Element temp1 = pairing.pairing(sig, g);
        Element temp2 = pairing.pairing(h, pk);

        assertTrue(temp1.isEqual(temp2));
    }

}