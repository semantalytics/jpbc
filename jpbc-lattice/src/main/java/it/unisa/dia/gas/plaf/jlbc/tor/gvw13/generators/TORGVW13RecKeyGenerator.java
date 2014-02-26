package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines.MP12HLP2SampleD;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2SampleParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13ReKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13RecodeParameters;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13RecKeyGenerator implements CipherParametersGenerator {
    private TORGVW13ReKeyGenerationParameters params;


    public CipherParametersGenerator init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13ReKeyGenerationParameters) keyGenerationParameters;

        return this;
    }

    public CipherParameters generateKey() {
        // Sample R1 from D_Z,s
        MP12HLP2PublicKeyParameters latticePk = (MP12HLP2PublicKeyParameters) params.getLeftPk().getLatticePublicKey();
        MatrixField<Field> RField = new MatrixField<Field>(
                latticePk.getParameters().getRandom(),
                latticePk.getZq(),
                latticePk.getM(),
                latticePk.getM()
        );
        MatrixElement R1 = RField.newElement();
        for (int i = 0; i < latticePk.getM(); i++) {
            for (int j = 0; j < latticePk.getM(); j++) {
                R1.getAt(i, j).set(latticePk.getSampler().sample());
            }
        }
        // Compute U
        MatrixElement U = (MatrixElement) ((MP12HLP2PublicKeyParameters) params.getTargetPk().getLatticePublicKey()).getA().duplicate().sub(
                ((MP12HLP2PublicKeyParameters) params.getRightPk().getLatticePublicKey()).getA().mul(R1)
        );

        // Sample R0

        MP12HLP2SampleD sampleD = new MP12HLP2SampleD();
        sampleD.init(new MP12HLP2SampleParameters(params.getLeftPk().getLatticePublicKey(), params.getLeftSk().getLatticePrivateKey()));
        MatrixElement R0 = RField.newElement();

        for (int i = 0; i < latticePk.getM(); i++) {
            R0.setColAt(i, sampleD.processElements(U.columnAt(i)));
        }

        // Compute R
        Element R = RField.unionByRow(R0, R1);

        return new TORGVW13RecodeParameters(params.getLeftPk().getParameters(), R);
    }


}
