package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.mt.MultiThreadExecutor;
import it.unisa.dia.gas.plaf.jpbc.util.mt.MultiThreadNoREduceExecutor;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.concurrent.Callable;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08SecretKeyGenerator {
    protected HVEIP08SecretKeyGenerationParameters param;
    protected int[] pattern;

    public void init(KeyGenerationParameters param) {
        this.param = (HVEIP08SecretKeyGenerationParameters) param;
        pattern = this.param.getPattern();

        if (pattern == null)
            throw new IllegalArgumentException("pattern cannot be null.");

        int n = this.param.getMasterSecretKey().getParameters().getN();
        if (pattern.length != n)
            throw new IllegalArgumentException("pattern length not valid.");
    }

    public CipherParameters generateKey() {
        final HVEIP08MasterSecretKeyParameters masterSecretKey = param.getMasterSecretKey();
        if (param.isAllStar()) {
            return new HVEIP08SecretKeyParameters(
                    masterSecretKey.getParameters(),
                    masterSecretKey.getParameters().getElementPowG().powZn(masterSecretKey.getY())
            );
        }

        Pairing pairing = PairingFactory.getPairing(masterSecretKey.getParameters().getCurveParameters());

        int n = masterSecretKey.getParameters().getN();
        int numNonStar = param.getNumNonStar();

        // generate a_i's
        final Element a[] = new Element[numNonStar];
        Element sum = pairing.getZr().newElement().setToZero();
        for (int i = 0; i < numNonStar - 1; i++) {
            a[i] = pairing.getZr().newElement().setToRandom();
            sum.add(a[i]);
        }
        a[numNonStar - 1] = masterSecretKey.getY().add(sum.negate());

        // generate key elements
        final ElementPow g = masterSecretKey.getParameters().getElementPowG();

        final Element[] Y = new Element[n];
        final Element[] L = new Element[n];


        if (masterSecretKey.isPreProcessed()) {
//            for (int i = 0, j = 0; i < n; i++) {
//                if (param.isStarAt(i)) {
//                    Y[i] = null;
//                    L[i] = null;
//                } else {
//                    Y[i] = g.powZn(a[j].duplicate().mul(masterSecretKey.getPreTAt(i, param.getPatternAt(i)))).getImmutable();
//                    L[i] = g.powZn(a[j].duplicate().mul(masterSecretKey.getPreVAt(i, param.getPatternAt(i)))).getImmutable();
//                    j++;
//                }
//            }
            MultiThreadExecutor executor = new MultiThreadNoREduceExecutor();
            for (int i = 0, j = 0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = null;
                    L[i] = null;
                } else {
                    final int finalI = i;
                    final int finalJ = j;
                    executor.submit(new Callable() {
                        public Object call() throws Exception {
                            Y[finalI] = g.powZn(a[finalJ].duplicate().mul(masterSecretKey.getPreTAt(finalI, param.getPatternAt(finalI)))).getImmutable();
                            L[finalI] = g.powZn(a[finalJ].duplicate().mul(masterSecretKey.getPreVAt(finalI, param.getPatternAt(finalI)))).getImmutable();
                            return null;
                        }
                    });
                    j++;
                }
            }
            executor.process();

        } else {
//            for (int i = 0, j = 0; i < n; i++) {
//                if (param.isStarAt(i)) {
//                    Y[i] = null;
//                    L[i] = null;
//                } else {
//                    Y[i] = g.powZn(a[j].duplicate().div(masterSecretKey.getTAt(i, param.getPatternAt(i)))).getImmutable();
//                    L[i] = g.powZn(a[j].duplicate().div(masterSecretKey.getVAt(i, param.getPatternAt(i)))).getImmutable();
//                    j++;
//                }
//            }
            MultiThreadExecutor executor = new MultiThreadNoREduceExecutor();
            for (int i = 0, j = 0; i < n; i++) {
                if (param.isStarAt(i)) {
                    Y[i] = null;
                    L[i] = null;
                } else {
                    final int finalI = i;
                    final int finalJ = j;
                    executor.submit(new Callable() {
                        public Object call() throws Exception {
                            Y[finalI] = g.powZn(a[finalJ].duplicate().div(masterSecretKey.getTAt(finalI, param.getPatternAt(finalI)))).getImmutable();
                            L[finalI] = g.powZn(a[finalJ].duplicate().div(masterSecretKey.getVAt(finalI, param.getPatternAt(finalI)))).getImmutable();
                            return null;
                        }
                    });
                    j++;
                }
            }
            executor.process();

        }

        return new HVEIP08SecretKeyParameters(masterSecretKey.getParameters(), pattern, Y, L);
    }

}