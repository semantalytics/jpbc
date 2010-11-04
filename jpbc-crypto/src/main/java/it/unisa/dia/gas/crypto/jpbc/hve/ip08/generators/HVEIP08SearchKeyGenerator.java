package it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.params.HVEIP08SearchKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.ip08.params.HVEIP08SearchKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SearchKeyGenerator {
    private HVEIP08SearchKeyGenerationParameters param;
    private byte[] attributePattern;

    public void init(KeyGenerationParameters param) {
        this.param = (HVEIP08SearchKeyGenerationParameters) param;
        this.attributePattern = this.param.getAttributePattern();
    }

    public CipherParameters generateKey() {
        if (attributePattern == null)
            throw new IllegalArgumentException("attributePattern cannot be null.");

        int length = param.getParameters().getParameters().getLength();
        if (attributePattern.length < length || attributePattern.length > length)
            throw new IllegalArgumentException("x length not valid.");

        // count how many star and non star

        int n = param.getParameters().getParameters().getN() ;
        int[] attributeLengths = param.getParameters().getParameters().getAttributeLengths();
        int starCount = 0, nonStarCount = 0;
        int blockOffset = 0;
        for (int yi = 0; yi < n; yi++) {
            int j = 0;
            for (int k = attributeLengths[yi] - 1; k >= 0; k--) {
                if (attributePattern[blockOffset + k] == -1) {
                    j = -1;
                    break;
                }
            }
            blockOffset += attributeLengths[yi];

            if (j == -1)
                starCount++;
        }
        nonStarCount = n - starCount;

        Pairing pairing = PairingFactory.getPairing(param.getParameters().getParameters().getCurveParams());

        if (nonStarCount == 0) {
            return new HVEIP08SearchKeyParameters(
                    param.getParameters().getParameters(),
                    param.getParameters().getParameters().getG().powZn(param.getParameters().getY())
                    );            
        } else {
            // generate a_is
            List<Element> a = new ArrayList<Element>(nonStarCount);
            Element sum = pairing.getZr().newElement().setToZero();
            for (int i = 0; i < nonStarCount - 1; i++) {
                Element ai = pairing.getZr().newElement().setToRandom();
                a.add(ai);

                sum.add(ai);
            }
            a.add(param.getParameters().getY().sub(sum));

            // Verify that the as sum to msk.y
            sum.setToZero();
            for (Element element : a) {
                sum.add(element);
            }
            if (!sum.isEqual(param.getParameters().getY()))
                throw new IllegalStateException();

            // generate Yis, Lis
            List<Element> Y = new ArrayList<Element>(length);
            List<Element> L = new ArrayList<Element>(length);

            Element g = param.getParameters().getParameters().getG();

            blockOffset = 0;
            for (int yi = 0, ai = 0; yi < n; yi++) {

                // Compute j
                int pow = 1;
                int j = 0;
                for (int k = attributeLengths[yi] - 1; k >= 0; k--) {
                    if (attributePattern[blockOffset + k] == -1) {
                        j = -1;
                        break;
                    }
                    j += attributePattern[blockOffset + k] * pow;
                    pow <<= 1;
                }
                blockOffset += attributeLengths[yi];

                if (j == -1) {
                    Y.add(null);
                    L.add(null);
                } else {

                    Y.add(
                            g.powZn(
                                    a.get(ai).duplicate().div(param.getParameters().getT().get(yi).get(j))
                            ).getImmutable()
                    );

                    L.add(
                            g.powZn(
                                    a.get(ai).duplicate().div(param.getParameters().getV().get(yi).get(j))
                            ).getImmutable()
                    );
                    ai++;
                }
            }

            return new HVEIP08SearchKeyParameters(param.getParameters().getParameters(), Y, L);
        }


    }

}