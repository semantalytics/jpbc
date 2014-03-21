package it.unisa.dia.gas.crypto.jlbc.fe.abe.gvw13.params;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private GVW13PublicKeyParameters publicKeyParameters;
    private GVW13MasterSecretKeyParameters masterSecretKeyParameters;
    private Circuit circuit;


    public GVW13SecretKeyGenerationParameters(
            GVW13PublicKeyParameters publicKeyParameters,
            GVW13MasterSecretKeyParameters masterSecretKeyParameters,
            Circuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GVW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GVW13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public Circuit getCircuit() {
        return circuit;
    }
}