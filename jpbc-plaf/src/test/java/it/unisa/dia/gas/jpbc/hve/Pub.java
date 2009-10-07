package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
*/
public class Pub {
    protected Pairing pairing;
    protected Element g;
    protected int n;

    protected Pk pk;
    protected Msk msk;


    public Pub(Pairing pairing, int n) {
        this.pairing = pairing;
        this.n = n;

        setup();
    }


    public void setup() {
        // Peek the generator
        this.g = pairing.getG1().newElement().setToRandom();

        // Init Y
        Element y = pairing.getZr().newElement().setToRandom();
        Element Y = pairing.pairing(g, g).powZn(y);

        // Init
        List<Element> T = new ArrayList<Element>(n);
        List<Element> t = new ArrayList<Element>(n);
        List<Element> V = new ArrayList<Element>(n);
        List<Element> v = new ArrayList<Element>(n);
        List<Element> R = new ArrayList<Element>(n);
        List<Element> r = new ArrayList<Element>(n);
        List<Element> M = new ArrayList<Element>(n);
        List<Element> m = new ArrayList<Element>(n);

        for (int i = 0; i < n ; i++) {
            // t_i, T_i
            Element t_i = pairing.getZr().newElement().setToRandom();
            t.add(t_i);
            T.add(g.duplicate().powZn(t_i));
//            System.out.println("t[" + i + "]  = " + t.get(i));
//            System.out.println("T[" + i + "]  = " + T.get(i));

            // v_i, V_i
            Element v_i = pairing.getZr().newElement().setToRandom();
            v.add(v_i);
            V.add(g.duplicate().powZn(v_i));
//            System.out.println("v[" + i + "]  = " + v.get(i));
//            System.out.println("V[" + i + "]  = " + V.get(i));

            // r_i, R_i
            Element r_i = pairing.getZr().newElement().setToRandom();
            r.add(r_i);
            R.add(g.duplicate().powZn(r_i));
//            System.out.println("r[" + i + "]  = " + r.get(i));
//            System.out.println("R[" + i + "]  = " + R.get(i));

            // m_i, M_i
            Element m_i = pairing.getZr().newElement().setToRandom();
            m.add(m_i);
            M.add(g.duplicate().powZn(m_i));
//            System.out.println("m[" + i + "]  = " + m.get(i));
//            System.out.println("M[" + i + "]  = " + M.get(i));
        }

        pk = new Pk(pairing, g, Y, T, V, R, M);
        msk = new Msk(y, t, v, r, m);
    }

    public Key keyGen(byte[] y) {
        if (y.length < n || y.length > n)
            throw new IllegalArgumentException("x length not valid.");

        // count how many star and non star
        int starCount = 0, nonStarCount = 0;
        for (byte yi : y) {
            if (yi == -1)
                starCount++;
        }
        nonStarCount = y.length - starCount;

        // generate a_is
        List<Element> a = new ArrayList<Element>(nonStarCount);
        Element sum = pairing.getZr().newElement().setToZero();
        for (int i = 0; i < nonStarCount - 1; i++) {
            Element ai = pairing.getZr().newElement().setToRandom();
            a.add(ai);

            sum.add(ai);
        }
        a.add(msk.y.duplicate().sub(sum));

        // Verify that the as sum to msk.y
        sum.setToZero();
        for (Element element : a) {
            sum.add(element);
        }
        if (!sum.isEqual(msk.y))
            throw new IllegalStateException();

        // generate Yis, Lis
        List<Element> Y = new ArrayList<Element>(n);
        List<Element> L = new ArrayList<Element>(n);

        for (int yi = 0, ai = 0; yi < n; yi++) {
            switch (y[yi]) {
                case  0:
                    Y.add(
                            g.duplicate().powZn(
                                    a.get(ai).duplicate().div(msk.r.get(yi))
                            )
                    );
                    L.add(
                            g.duplicate().powZn(
                                    a.get(ai).duplicate().div(msk.m.get(yi))
                            )
                    );

                    ai++;
                    break;

                case 1:
                    Y.add(
                            g.duplicate().powZn(
                                    a.get(ai).duplicate().div(msk.t.get(yi))
                            )
                    );
                    L.add(
                            g.duplicate().powZn(
                                    a.get(ai).duplicate().div(msk.v.get(yi))
                            )
                    );

                    ai++;
                    break;

                case -1:
                    Y.add(null);
                    L.add(null);
            }
        }

        return new Key(Y, L);
    }

}
