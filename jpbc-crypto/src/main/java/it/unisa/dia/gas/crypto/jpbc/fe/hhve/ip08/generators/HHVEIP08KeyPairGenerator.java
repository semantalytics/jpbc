package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.HHVEIP08PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private HHVEIP08KeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (HHVEIP08KeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        HHVEIP08Parameters parameters = param.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
        Element g = parameters.getG();
        ElementPowPreProcessing powG = parameters.getPowG();
        int n = parameters.getN();

        // Init Y
        Element y = pairing.getZr().newElement().setToRandom();
        Element Y = pairing.pairing(g, g).powZn(y);

        // Init
        List<List<ElementPowPreProcessing>> T = new ArrayList<List<ElementPowPreProcessing>>(n);
        List<List<Element>> t = new ArrayList<List<Element>>(n);

        List<List<ElementPowPreProcessing>> V = new ArrayList<List<ElementPowPreProcessing>>(n);
        List<List<Element>> v = new ArrayList<List<Element>>(n);

        for (int i = 0; i < n ; i++) {

            int howMany = parameters.getAttributeNumAt(i);
            List<ElementPowPreProcessing> T_i = new ArrayList<ElementPowPreProcessing>();
            List<Element> t_i = new ArrayList<Element>();

            List<ElementPowPreProcessing> V_i = new ArrayList<ElementPowPreProcessing>();
            List<Element> v_i = new ArrayList<Element>();

            for (int j = 0; j < howMany; j++) {
                Element t_j = pairing.getZr().newElement().setToRandom();
                T_i.add(powG.powZn(t_j).pow());
                t_i.add(t_j);

                Element v_j = pairing.getZr().newElement().setToRandom();
                V_i.add(powG.powZn(v_j).pow());
                v_i.add(v_j);
            }

            T.add(T_i);
            t.add(t_i);

            V.add(V_i);
            v.add(v_i);
        }

        return new AsymmetricCipherKeyPair(
            new HHVEIP08PublicKeyParameters(parameters, Y.getImmutable(), T, V),
            new HHVEIP08PrivateKeyParameters(parameters, y.getImmutable(), t, v)
        );
    }

}