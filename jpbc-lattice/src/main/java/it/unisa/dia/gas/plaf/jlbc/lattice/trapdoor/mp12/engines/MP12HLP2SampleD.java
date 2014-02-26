package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2SampleParameters;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2SampleD extends MP12PLP2SampleD {

    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;

    public ElementCipher init(CipherParameters param) {
        AsymmetricCipherKeyPair keyPair = ((MP12HLP2SampleParameters) param).getKeyPair();

        pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();

        super.init(pk);

        return this;
    }

    public Element processElements(Element... input) {
        Element z = super.processElements(input[0]);

        Element z1 = sk.getR().mul(z);

        return VectorField.union(z1, z);
    }

}
