package it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params.GVW13SecretKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13SecretKeyGenerator {
    private GVW13SecretKeyGenerationParameters param;

    private Circuit circuit;

    public void init(KeyGenerationParameters param) {
        this.param = (GVW13SecretKeyGenerationParameters) param;

        this.circuit = this.param.getCircuit();
    }

    public CipherParameters generateKey() {
        GVW13MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        GVW13PublicKeyParameters pk = param.getPublicKeyParameters();

        Circuit circuit = this.circuit;
        int n = circuit.getN();
        int q = circuit.getQ();
        int qMinus1 = q - 1;

        // Generate key pairs
        AsymmetricCipherKeyPairGenerator tor = param.getPublicKeyParameters().getParameters().getTorKeyPairGenerater();
        CipherParametersGenerator reKeyGen = param.getPublicKeyParameters().getParameters().getTorReKeyPairGenerater();

        CipherParameters[] publicKeys = new CipherParameters[q * 2];
        CipherParameters[] secretKeys = new CipherParameters[q * 2];

        for (int i = 0, size = 2 * qMinus1; i < size; i++) {
            AsymmetricCipherKeyPair keyPair = tor.generateKeyPair();
            publicKeys[i] = keyPair.getPublic();
            secretKeys[i] = keyPair.getPrivate();
        }

        AsymmetricCipherKeyPair keyPair = tor.generateKeyPair();
        publicKeys[2 * qMinus1] = keyPair.getPublic();
        publicKeys[2 * qMinus1 + 1] = pk.getCipherParametersAt(n - 1, 1);

        secretKeys[2 * qMinus1] = keyPair.getPrivate();

        // encode the circuit
        Map<Integer, CipherParameters[]> keys = new HashMap<Integer, CipherParameters[]>();

        keys.put(-1, new CipherParameters[]{null});

        for (Circuit.Gate gate : circuit) {
            int index = gate.getIndex();
            int depth = gate.getDepth();

            switch (gate.getType()) {
                case INPUT:
                    break;

                case OR:
                    CipherParameters[] recKeys = new CipherParameters[4];
                    int left = gate.getInputIndexAt(0);
                    int right = gate.getInputIndexAt(1);

                    int b0 = 0, b1 = 0;
                    for (int i = 0; i < 4; i++) {
                        CipherParameters leftTorPK = (left < n)
                                ? pk.getCipherParametersAt(left, b0)
                                : publicKeys[2 * (left - n) + b0];
                        CipherParameters leftTorSK = (left < n)
                                ? msk.getCipherParametersAt(left, b0)
                                : secretKeys[2 * (left - n) + b0];

                        CipherParameters rightTorPK = (right < n)
                                ? pk.getCipherParametersAt(right, b1)
                                : publicKeys[2 * (left - n) + b1];

                        int target = b0 == 1 || b1 == 1 ? 1 : 0;
                        CipherParameters targetTorPK = (index < n)
                                ? pk.getCipherParametersAt(index, target)
                                : publicKeys[2 * (left - n) + target];


                        recKeys[i] = reKeyGen.init(
                                msk.getParameters().getReKeyPairGenerationParameters(leftTorPK, leftTorSK, rightTorPK, targetTorPK)
                        ).generateKey();


                        if (b1 == 0)
                            b1++;
                        else if (b0 == 0) {
                            b0++; b1 = 0;
                        } else
                            break;
                    }

                    keys.put(index, recKeys);
                    break;

                case AND:
                    recKeys = new CipherParameters[4];
                    left = gate.getInputIndexAt(0);
                    right = gate.getInputIndexAt(1);

                    b0 = 0; b1 = 0;
                    for (int i = 0; i < 4; i++) {
                        CipherParameters leftTorPK = (left < n)
                                ? pk.getCipherParametersAt(left, b0)
                                : publicKeys[2 * (left - n) + b0];
                        CipherParameters leftTorSK = (left < n)
                                ? msk.getCipherParametersAt(left, b0)
                                : secretKeys[2 * (left - n) + b0];

                        CipherParameters rightTorPK = (right < n)
                                ? pk.getCipherParametersAt(right, b1)
                                : publicKeys[2 * (left - n) + b1];

                        int target = b0 == 1 && b1 == 1 ? 1 : 0;
                        CipherParameters targetTorPK = (index < n)
                                ? pk.getCipherParametersAt(index, target)
                                : publicKeys[2 * (left - n) + target];


                        recKeys[i] = reKeyGen.init(
                                msk.getParameters().getReKeyPairGenerationParameters(leftTorPK, leftTorSK, rightTorPK, targetTorPK)
                        ).generateKey();


                        if (b1 == 0)
                            b1++;
                        else if (b0 == 0) {
                            b0++; b1 = 0;
                        } else
                            break;
                    }

                    keys.put(index, recKeys);
                    break;
            }
        }

        return new GVW13SecretKeyParameters(
                param.getPublicKeyParameters().getParameters(), circuit, keys
        );
    }

}