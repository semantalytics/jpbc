package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Engine {
    protected Pk pk;
    protected Key key;


    public Engine(Pk pk, Key key) {
        this.pk = pk;
        this.key = key;
    }


    public CypherText enc(Element M, byte[] x) {
        Element s = pk.pairing.getZr().newElement().setToRandom();

        Element omega = pk.Y.duplicate().powZn(s.duplicate().negate()).mul(M);
        Element C0 = pk.g.duplicate().powZn(s);

        List<Element> X = new ArrayList<Element>();
        List<Element> W = new ArrayList<Element>();
        for (int i = 0; i < x.length; i++) {
            Element si = pk.pairing.getZr().newElement().setToRandom();
            Element sMinusSi = s.duplicate().sub(si);
            
            switch (x[i]) {
                case 0:
                    X.add(pk.R.get(i).duplicate().powZn(sMinusSi));
                    W.add(pk.M.get(i).duplicate().powZn(si));

                    break;

                case 1:
                    X.add(pk.T.get(i).duplicate().powZn(sMinusSi));
                    W.add(pk.V.get(i).duplicate().powZn(si));

                    break;

                default:
                    throw new IllegalArgumentException("Invalid x");
            }

        }

        return new CypherText(omega, C0, X, W);
    }

    public Element dec(CypherText cypherText) {

        Element result = cypherText.omega.duplicate();

        for (int i = 0; i < key.L.size(); i++) {

            if (key.L.get(i) != null) {
                result.mul(
                        pk.pairing.pairing(cypherText.X.get(i), key.Y.get(i))
                ).mul(
                        pk.pairing.pairing(cypherText.W.get(i), key.L.get(i))      
                );
            }

        }

        return result;
    }

}
