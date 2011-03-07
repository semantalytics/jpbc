package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SearchKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SearchKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08AttributesOnlySearchKeyGenerator {
    protected HVEIP08SearchKeyGenerationParameters param;
    protected int[] pattern;

    public void init(KeyGenerationParameters param) {
        this.param = (HVEIP08SearchKeyGenerationParameters) param;
        pattern = this.param.getPattern();

        if (pattern == null)
            throw new IllegalArgumentException("pattern cannot be null.");

        int n = this.param.getPrivateKey().getParameters().getN();
        if (pattern.length != n)
            throw new IllegalArgumentException("pattern length not valid.");
    }

    public CipherParameters generateKey() {
        HVEIP08PrivateKeyParameters privateKey = param.getPrivateKey();
        if (param.isAllStar())
            return new HVEIP08SearchKeyParameters(privateKey.getParameters());

        Pairing pairing = PairingFactory.getPairing(privateKey.getParameters().getCurveParams());

        int n = privateKey.getParameters().getN();
        int numNonStar = param.getNumNonStar();

        // generate a_i's
        Element a[] = new Element[numNonStar];
        Element sum = pairing.getZr().newElement().setToZero();
        for (int i = 0; i < numNonStar - 1; i++) {
            a[i] = pairing.getZr().newElement().setToRandom();
            sum.add(a[i]);
        }
        a[numNonStar - 1] = sum.negate();

        // generate key elements
        ElementPow g = privateKey.getParameters().getElementPowG();

        Element[] Y = new Element[n];
        Element[] L = new Element[n];

        if (privateKey.isPreProcessed()) {
            for (int i = 0, j=0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = null;
                    L[i] = null;
                } else {
                    Y[i] = g.powZn(a[j].duplicate().mul(privateKey.getPreTAt(i, param.getPatternAt(i)))).getImmutable();
                    L[i] = g.powZn(a[j].duplicate().mul(privateKey.getPreVAt(i, param.getPatternAt(i)))).getImmutable();
                    j++;
                }
            }
        } else {
            for (int i = 0, j=0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = null;
                    L[i] = null;
                } else {
                    Y[i] = g.powZn(a[j].duplicate().div(privateKey.getTAt(i, param.getPatternAt(i)))).getImmutable();
                    L[i] = g.powZn(a[j].duplicate().div(privateKey.getVAt(i, param.getPatternAt(i)))).getImmutable();
                    j++;
                }
            }
        }

        return new HVEIP08SearchKeyParameters(privateKey.getParameters(), pattern, Y, L);
    }

}