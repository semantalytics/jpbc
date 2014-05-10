package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params;

import it.unisa.dia.gas.crypto.circuit.ArithmeticCircuit;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14SecretKeyGenerationParameters extends KeyGenerationParameters {

    private BNS14PublicKeyParameters publicKeyParameters;
    private BNS14MasterSecretKeyParameters masterSecretKeyParameters;
    private ArithmeticCircuit circuit;


    public BNS14SecretKeyGenerationParameters(
            BNS14PublicKeyParameters publicKeyParameters,
            BNS14MasterSecretKeyParameters masterSecretKeyParameters,
            ArithmeticCircuit circuit) {
        super(null, 0);

        this.publicKeyParameters = publicKeyParameters;
        this.masterSecretKeyParameters = masterSecretKeyParameters;
        this.circuit = circuit;
    }


    public BNS14PublicKeyParameters getPublicKeyParameters() {
        return publicKeyParameters;
    }

    public BNS14MasterSecretKeyParameters getMasterSecretKeyParameters() {
        return masterSecretKeyParameters;
    }

    public ArithmeticCircuit getCircuit() {
        return circuit;
    }
}