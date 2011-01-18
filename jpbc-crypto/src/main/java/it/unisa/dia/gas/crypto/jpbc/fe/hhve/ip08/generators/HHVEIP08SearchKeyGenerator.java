package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08SearchKeyGenerator {
    private KeyGenerationParameters params;
    private int[] pattern;

    public void init(KeyGenerationParameters params) {
        this.params = params;

        if (params instanceof HHVEIP08SearchKeyGenerationParameters) {
            HHVEIP08SearchKeyGenerationParameters param = (HHVEIP08SearchKeyGenerationParameters) params;

            pattern = param.getPattern();

            if (pattern == null)
                throw new IllegalArgumentException("pattern cannot be null.");

            int n = param.getPrivateKey().getParameters().getN();
            if (pattern.length != n)
                throw new IllegalArgumentException("pattern length not valid.");
        } else if (params instanceof HHVEIP08DelegateSecretKeyGenerationParameters) {
            HHVEIP08DelegateSecretKeyGenerationParameters param = (HHVEIP08DelegateSecretKeyGenerationParameters) params;

            pattern = param.getPattern();

            if (pattern == null)
                throw new IllegalArgumentException("pattern cannot be null.");

            int n = param.getPublicKey().getParameters().getN();
            if (pattern.length != n)
                throw new IllegalArgumentException("pattern length not valid.");
        } else
            throw new IllegalStateException("invalid params");

    }

    public CipherParameters generateKey() {
        //TODO: verify that param is dirrent from null...


        if (params instanceof HHVEIP08SearchKeyGenerationParameters) {
            HHVEIP08SearchKeyGenerationParameters param = (HHVEIP08SearchKeyGenerationParameters) params;
            HHVEIP08PrivateKeyParameters privateKey = param.getPrivateKey();

            Pairing pairing = PairingFactory.getPairing(privateKey.getParameters().getCurveParams());
            int n = privateKey.getParameters().getN();

            // generate a_is
            Element a[] = new Element[n];
            Element sum = pairing.getZr().newElement().setToZero();
            for (int i = 0; i < n - 1; i++) {
                a[i] = pairing.getZr().newElement().setToRandom();
                sum.add(a[i]);
            }
//                a[n - 1] = param.getPrivateKey().getY().sub(sum);
            a[n - 1] = sum.negate();

            // generate key elements
            ElementPowPreProcessing g = privateKey.getParameters().getPowG();

            Element[] Y = new Element[n];
            Element[] L = new Element[n];
            List<List<Element>> SY = new ArrayList<List<Element>>();
            List<List<Element>> SL = new ArrayList<List<Element>>();

            for (int i = 0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = g.powZn(a[i]).getImmutable();
                    L[i] = g.powZn(a[i]).getImmutable();

                    List<Element> SYList = new ArrayList<Element>();
                    List<Element> SLList = new ArrayList<Element>();
                    for (int j = 0, size = privateKey.getParameters().getAttributeNumAt(i); j < size; j++) {
                        SYList.add(g.powZn(a[i].duplicate().div(privateKey.getTAt(i, j))).getImmutable());
                        SLList.add(g.powZn(a[i].duplicate().div(privateKey.getVAt(i, j))).getImmutable());
                    }
                    SY.add(SYList);
                    SL.add(SLList);
                } else {
                    Y[i] = g.powZn(a[i].duplicate().div(privateKey.getTAt(i, param.getPatternAt(i)))).getImmutable();
                    L[i] = g.powZn(a[i].duplicate().div(privateKey.getVAt(i, param.getPatternAt(i)))).getImmutable();

                    SY.add(null);
                    SL.add(null);
                }
            }

            if (param.isAllStar())
//                        privateKey.getParameters().getG().powZn(privateKey.getY())
                return new HHVEIP08SearchKeyParameters(
                        privateKey.getParameters(), Y, L, SY, SL
                );
            else
                return new HHVEIP08SearchKeyParameters(privateKey.getParameters(), pattern, Y, L, SY, SL);
        } else {
            HHVEIP08DelegateSecretKeyGenerationParameters param = (HHVEIP08DelegateSecretKeyGenerationParameters) params;

            HHVEIP08PublicKeyParameters publicKey = param.getPublicKey();
            HHVEIP08SearchKeyParameters searchKey = param.getSearchKey();

            Pairing pairing = PairingFactory.getPairing(publicKey.getParameters().getCurveParams());
            int n = publicKey.getParameters().getN();
            Element z = pairing.getZr().newRandomElement();

            // generate key elements
            Element[] Y = new Element[n];
            Element[] L = new Element[n];
            List<List<Element>> SY = new ArrayList<List<Element>>();
            List<List<Element>> SL = new ArrayList<List<Element>>();

            for (int i = 0; i < n; i++) {
                if (searchKey.isStar(i) && param.isStarAt(i)) {
                    // Copy
                    Y[i] = searchKey.getYAt(i).powZn(z).getImmutable();
                    L[i] = searchKey.getLAt(i).powZn(z).getImmutable();

                    List<Element> SYList = new ArrayList<Element>();
                    List<Element> SLList = new ArrayList<Element>();
                    for (int j = 0, size = param.getPublicKey().getParameters().getAttributeNumAt(i); j < size; j++) {
                        SYList.add(searchKey.getSYAt(i, j).powZn(z).getImmutable());
                        SLList.add(searchKey.getSLAt(i, j).powZn(z).getImmutable());
                    }
                    SY.add(SYList);
                    SL.add(SLList);
                } else if (searchKey.getPatternAt(i) == param.getPatternAt(i)) {
                    // copy
                    Y[i] = searchKey.getYAt(i).powZn(z).getImmutable();
                    L[i] = searchKey.getLAt(i).powZn(z).getImmutable();

                    SY.add(null);
                    SL.add(null);
                } else if (searchKey.isStar(i) && !param.isStarAt(i)) {
                    // set
                    Y[i] = searchKey.getSYAt(i, param.getPatternAt(i)).powZn(z).getImmutable();
                    L[i] = searchKey.getSLAt(i, param.getPatternAt(i)).powZn(z).getImmutable();

                    SY.add(null);
                    SL.add(null);
                } else
                    throw new IllegalArgumentException("It's nor a minor...");
            }

            return new HHVEIP08SearchKeyParameters(publicKey.getParameters(), pattern, Y, L, SY, SL);
        }
    }

}