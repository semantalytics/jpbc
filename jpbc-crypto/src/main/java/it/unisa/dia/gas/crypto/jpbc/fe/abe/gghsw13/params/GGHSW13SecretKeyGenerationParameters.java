package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import it.unisa.dia.gas.crypto.circuit.BooleanCircuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13SecretKeyGenerationParameters extends KeyGenerationParameters {

    private final GGHSW13PublicKeyParameters publicKeyParameters;
    private final GGHSW13MasterSecretKeyParameters masterSecretKeyParameters;
    private final BooleanCircuit circuit;

    public GGHSW13SecretKeyGenerationParameters(
            final GGHSW13PublicKeyParameters publicKeyParameters,
            final GGHSW13MasterSecretKeyParameters masterSecretKeyParameters,
            final BooleanCircuit circuit) {

        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public GGHSW13PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public GGHSW13MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public BooleanCircuit getCircuit() {
        return circuit;
    }
}