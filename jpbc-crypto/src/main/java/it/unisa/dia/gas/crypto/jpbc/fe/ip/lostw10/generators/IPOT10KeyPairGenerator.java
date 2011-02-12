package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.product.ProductPairing;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import static it.unisa.dia.gas.plaf.jpbc.util.ElementUtils.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private IPOT10KeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (IPOT10KeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        IPOT10Parameters parameters = param.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
        Element g = parameters.getG();
        int n = parameters.getN();
        int N = 2 * parameters.getN() + 3;
        Pairing vectorPairing = new ProductPairing(param.getRandom(), pairing, N);

        Element sigma = pairing.pairing(g, g);

        // Generate canonical base
        Vector[] canonicalBase = new Vector[N];
        for (int i = 0; i < N; i++) {
            canonicalBase[i] = (Vector) vectorPairing.getG1().newZeroElement();
            canonicalBase[i].getAt(i).set(g);
        }

        // Sample a uniform transformation
        Element[][] linearTransformation = sampleUniformTransformation(pairing.getZr(), N);

        // Generate base B
        Element[] tempB = new Vector[N];
        for (int i = 0; i < N; i++) {
            tempB[i] = canonicalBase[0].duplicate().mulZn(linearTransformation[i][0]);
            for (int j = 1; j < N; j++) {
                tempB[i].add(canonicalBase[j].duplicate().mulZn(linearTransformation[i][j]));
            }
        }

        // Reduce tempB to B
        Element[] B = new Vector[n + 2];
        System.arraycopy(tempB, 0, B, 0, n);
        B[n] = tempB[N-3];
        B[n + 1] = tempB[N-1];


        // Generate base B*
        linearTransformation = invert(transpose(linearTransformation));

        Element[] tempBstar = new Vector[N];
        for (int i = 0; i < N; i++) {
            tempBstar[i] = canonicalBase[0].duplicate().mulZn(linearTransformation[i][0]);
            for (int j = 1; j < N; j++) {
                tempBstar[i].add(canonicalBase[j].duplicate().mulZn(linearTransformation[i][j]));
            }
        }

        // Reduce tempBstar to Bstar
        Element[] Bstar = new Vector[n + 2];
        System.arraycopy(tempBstar, 0, Bstar, 0, n);
        Bstar[n] = tempBstar[N-3];
        Bstar[n + 1] = tempBstar[N-2];

        return new AsymmetricCipherKeyPair(
            new IPOT10PublicKeyParameters(parameters, B, sigma),
            new IPOT10PrivateKeyParameters(parameters, Bstar)
        );
    }


}