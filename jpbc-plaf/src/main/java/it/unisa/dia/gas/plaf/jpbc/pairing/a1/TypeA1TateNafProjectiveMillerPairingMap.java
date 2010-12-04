package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1TateNafProjectiveMillerPairingMap extends AbstractMillerPairingMap {
    protected TypeA1Pairing pairing;


    public TypeA1TateNafProjectiveMillerPairingMap(TypeA1Pairing pairing) {
        this.pairing = pairing;
    }


    @Override
    protected void millerStep(Point out, Element a, Element b, Element c, Element Qx, Element Qy) {
    }

    /**
     * in1, in2 are from E(F_q), out from F_q^2
     */
    public Element pairing(Point P, Point Q) {
        Point f = pairing.Fq2.newOneElement();

        JacobPoint V = new JacobPoint(P.getX(), P.getY(), P.getX().getField().newOneElement());
        Point nP = (Point) P.duplicate().negate();

        byte[] r = BigIntegerUtils.naf(pairing.r, (byte) 2);

        Point u = pairing.Fq2.newElement();
        for (int i = r.length - 2; i >= 0; i--) {
            twice(u, V, Q);
            f.square().mul(u);

            switch (r[i]){
                case 1:
                    add(u, V, P, Q);
                    f.mul(u);
                    break;
                case -1:
                    add(u, V, nP, Q);
                    f.mul(u);
                    break;
            }
        }

        Point out = pairing.Fq2.newElement();
        tatePow(out, f, pairing.phikOnr);
        return new GTFiniteElement(this, (GTFiniteField) pairing.getGT(), out);
    }

    public void finalPow(Element element) {
    }


    final void tatePow(Point out, Point in, BigInteger cofactor) {
        Element in1 = in.getY();
        //simpler but slower:
        //element_pow_mpz(out, f, tateExp);

        //1. Exponentiate by q-1
        //which is equivalent to the following

        Point temp = (Point) in.duplicate().invert();
        in1.negate();
        in.mul(temp);

        //2. Exponentiate by (q+1)/r

        //Instead of:
        //	element_pow_mpz(out, in, cofactor);
        //we use Lucas sequences (see "Compressed Pairings", Scott and Barreto)
        lucasOdd(out, in, temp, cofactor);
    }

    //used by tate pairing, point doubling in Jacobian coordinates, and return the value of f
    final void twice(Point u, JacobPoint P, Point Q) {
        //if(P.isInfinity())
        //	return;

        Element x = P.getX();
        Element y = P.getY();
        Element z = P.getZ();

        //t1 = y^2
        Element t1 = y.duplicate().square();

        //t2 = 4 x t1 = 4 x y^2
        Element t2 = x.duplicate().mul(t1).twice().twice();

        //t4 = z^2
        Element t4 = z.duplicate().square();

        //t5 = 3 x^2 + a t4^2 = 3 x^2 + a z^4
        Element t5 = x.duplicate().square().mul(3).add(t4.duplicate().square());

        //x3 = (3 x^2 + a z^4)^2 - 2 (4 x y^2)
        Element x3 = t5.duplicate().square().sub(t2.duplicate().twice());

        //y3 = 3 x^2 + a z^4 (4 x y^2 - x3) - 8 y^4
        Element y3 = t5.duplicate().mul(t2.duplicate().sub(x3)).sub(t1.duplicate().square().twice().twice().twice());

        //z3 = 2 y z
        Element z3 = y.duplicate().mul(z).twice();

        P.setX(x3);
        P.setY(y3);
        P.setZ(z3);

        // (2 y z * z^2 * Q.y)i - (2 y^2 - ((3 x^2 + a z^4) (z^2 Q.x + x)))
        u.getX().set(t4.duplicate().mul(Q.getX()).add(x).mul(t5).sub(t1).sub(t1));
        u.getY().set(z3.duplicate().mul(t4).mul(Q.getY()));
    }


    //used by Tate paring, add two point, save result in the first argument, return the value of f
    final void add(Point u, JacobPoint A, Point P, Point Q) {
        Element x1 = A.getX();
        Element y1 = A.getY();
        Element z1 = A.getZ();

        Element x = P.getX();
        Element y = P.getY();

        //t1=z1^2
        Element t1 = z1.duplicate().square();
        //t2=z1t1
        Element t2 = z1.duplicate().mul(t1);
        //t3=xt1
        Element t3 = x.duplicate().mul(t1);
        //t4=Yt2
        Element t4 = y.duplicate().mul(t2);
        //t5=t3-x1
        Element t5 = t3.duplicate().sub(x1);
        //t6=t4-y1
        Element t6 = t4.duplicate().sub(y1);
        //t7=t5^2
        Element t7 = t5.duplicate().square();
        //t8=t5t7
        Element t8 = t5.duplicate().mul(t7);
        //t9=x1t7
        Element t9 = x1.duplicate().mul(t7);

        //x3=t6^2-(t8+2t9)
        Element x3 = t6.duplicate().square().sub(t8.duplicate().add(t9.duplicate().twice()));

        //y3=t6(t9-x3)-y1t8
        Element y3 = t6.duplicate().mul(t9.duplicate().sub(x3)).sub((y1.duplicate().mul(t8)));

        //z3=z1t5
        Element z3 = z1.duplicate().mul(t5);

        A.setX(x3);
        A.setY(y3);
        A.setZ(z3);

        //z3 yq i -(z3Y-t6(xq+x))
        u.getX().set(Q.getX().duplicate().add(x).mul(t6).sub(z3.duplicate().mul(y)));
        u.getY().set(z3.duplicate().mul(Q.getY()));
    }


    public static class JacobPoint {

        private Element x;
        private Element y;
        private Element z;

        public JacobPoint(Element x, Element y, Element z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Element getX() {
            return this.x;
        }

        public Element getY() {
            return this.y;
        }

        public Element getZ() {
            return this.z;
        }

        public boolean isInfinity() {
            //return this.equals(JacobPoint.INFINITY);
            return this.z.isZero();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((x == null) ? 0 : x.hashCode());
            result = prime * result + ((y == null) ? 0 : y.hashCode());
            result = prime * result + ((z == null) ? 0 : z.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            JacobPoint other = (JacobPoint) obj;
            if (x == null) {
                if (other.x != null)
                    return false;
            } else if (!x.equals(other.x))
                return false;
            if (y == null) {
                if (other.y != null)
                    return false;
            } else if (!y.equals(other.y))
                return false;
            if (z == null) {
                if (other.z != null)
                    return false;
            } else if (!z.equals(other.z))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "," + z + "]";
        }

        public void setX(Element newX) {
            this.x = newX;
        }

        public void setY(Element newY) {
            this.y = newY;
        }

        public void setZ(Element newZ) {
            this.z = newZ;
        }


    }

}
