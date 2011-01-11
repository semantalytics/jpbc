package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEDelegateSecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08SearchKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08SearchKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
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
    private int[] attributesPattern;

    public void init(KeyGenerationParameters params) {
        this.params = params;

        if (params instanceof HHVEIP08SearchKeyGenerationParameters) {
            HHVEIP08SearchKeyGenerationParameters param = (HHVEIP08SearchKeyGenerationParameters) params;

            attributesPattern = param.getAttributesPattern();

            if (attributesPattern == null)
                throw new IllegalArgumentException("attributesPattern cannot be null.");

            int n = param.getParameters().getParameters().getN();
            if (attributesPattern.length != n)
                throw new IllegalArgumentException("attributesPattern length not valid.");
        } else
            throw new IllegalStateException("invalid params");

    }

    public CipherParameters generateKey() {
        //TODO: verify that param is dirrent from null...


        if (params instanceof HHVEIP08SearchKeyGenerationParameters) {
            HHVEIP08SearchKeyGenerationParameters param = (HHVEIP08SearchKeyGenerationParameters) params;

            // count how many star and non star

            Pairing pairing = PairingFactory.getPairing(param.getParameters().getParameters().getCurveParams());

            if (param.isAllStar()) {
                return new HHVEIP08SearchKeyParameters(
                        param.getParameters().getParameters(),
                        param.getParameters().getParameters().getG().powZn(param.getParameters().getY())
                );
            } else {
                int n = param.getParameters().getParameters().getN();

                // generate a_is
                Element a[] = new Element[n];
                Element sum = pairing.getZr().newElement().setToZero();
                for (int i = 0; i < n - 1; i++) {
                    a[i] = pairing.getZr().newElement().setToRandom();
                    sum.add(a[i]);
                }
                a[n - 1] = param.getParameters().getY().sub(sum);

                // generate key elements
                Element g = param.getParameters().getParameters().getG();

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
                        for (int j = 0, size = (int) Math.pow(2, param.getParameters().getParameters().getAttributeLengthAt(i)); j < size; j++) {
                            SYList.add(g.powZn(a[i].duplicate().div(param.getParameters().getTAt(i, j))).getImmutable());
                            SLList.add(g.powZn(a[i].duplicate().div(param.getParameters().getVAt(i, j))).getImmutable());
                        }
                        SY.add(SYList);
                        SL.add(SLList);
                    } else {
                        Y[i] = g.powZn(a[i].duplicate().div(param.getParameters().getTAt(i, param.getPatternAt(i)))).getImmutable();
                        L[i] = g.powZn(a[i].duplicate().div(param.getParameters().getVAt(i, param.getPatternAt(i)))).getImmutable();

                        SY.add(null);
                        SL.add(null);
                    }
                }

                return new HHVEIP08SearchKeyParameters(param.getParameters().getParameters(), attributesPattern, Y, L, SY, SL);
            }
        } else {
            HHVEDelegateSecretKeyGenerationParameters param = (HHVEDelegateSecretKeyGenerationParameters) params;

            return null;  //TODO
        }
    }

}