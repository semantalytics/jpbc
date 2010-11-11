package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteElement;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingMap;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeATateNafProjectiveMillerPairingMap extends AbstractMillerPairingMap {
    protected TypeAPairing pairing;


    public TypeATateNafProjectiveMillerPairingMap(TypeAPairing pairing) {
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

        byte[] r = naf(pairing.r, (byte) 2);

        Point u = pairing.Fq2.newElement();
        for (int i = r.length - 2; i >= 0; i--) {
            encDbl(u, V, f, Q);
            f.square().mul(u);

            if (r[i] == 1) {
                encAdd(u, V, P, Q, f);
                f.mul(u);
            }
            if (r[i] == -1) {
                encAdd(u, V, nP, Q, f);
                f.mul(u);
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
    final void encDbl(Point u, JacobPoint P, Element f, Point Q) {
        //if(P.isInfinity())
        //	return;

        Element x = P.getX();
        Element y = P.getY();
        Element z = P.getZ();

        //t1=y^2
        Element t1 = y.duplicate().square();

        //t2=4xt1
        Element t2 = x.duplicate().mul(t1).twice().twice();

        //t4=z^2
        Element t4 = z.duplicate().square();

        //t5=3x^2+aZ^4
        Element t5 = x.duplicate().square().mul(3).add(t4.duplicate().square());

        //x3=t5^2-2t2
        Element x3 = t5.duplicate().square().sub(t2.duplicate().twice());

        //y3=t5(t2-x3)- 8t1^2
        Element y3 = t5.duplicate().mul(t2.duplicate().sub(x3)).sub(t1.duplicate().square().twice().twice().twice());

        //z3=2y1z1
        Element z3 = y.duplicate().mul(z).twice();

        P.setX(x3);
        P.setY(y3);
        P.setZ(z3);

        //Z3 t4 yQ i - (2 t1 - t5(t4 Xq + x1))
        u.getX().set(t4.duplicate().mul(Q.getX()).add(x).mul(t5).sub(t1).sub(t1));
        u.getY().set(z3.duplicate().mul(t4).mul(Q.getY()));
    }


    //used by Tate paring, add two point, save result in the first argument, return the value of f
    final void encAdd(Point u, JacobPoint A, Point P, Point Q, Element f) {
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


    //windowed naf form of BigInteger k, w is the window size
    final byte[] naf(BigInteger k, byte w) {
        // The window NAF is at most 1 element longer than the binary
        // representation of the integer k. byte can be used instead of short or
        // int unless the window width is larger than 8. For larger width use
        // short or int. However, a width of more than 8 is not efficient for
        // m = log2(q) smaller than 2305 Bits. Note: Values for m larger than
        // 1000 Bits are currently not used in practice.
        byte[] wnaf = new byte[k.bitLength() + 1];

        // 2^width as short and BigInteger
        short pow2wB = (short) (1 << w);
        BigInteger pow2wBI = BigInteger.valueOf(pow2wB);

        int i = 0;

        // The actual length of the WNAF
        int length = 0;

        // while k >= 1
        while (k.signum() > 0) {
            // if k is odd
            if (k.testBit(0)) {
                // k mod 2^width
                BigInteger remainder = k.mod(pow2wBI);

                // if remainder > 2^(width - 1) - 1
                if (remainder.testBit(w - 1)) {
                    wnaf[i] = (byte) (remainder.intValue() - pow2wB);
                } else {
                    wnaf[i] = (byte) remainder.intValue();
                }
                // wnaf[i] is now in [-2^(width-1), 2^(width-1)-1]

                k = k.subtract(BigInteger.valueOf(wnaf[i]));
                length = i;
            } else {
                wnaf[i] = 0;
            }

            // k = k/2
            k = k.shiftRight(1);
            i++;
        }

        length++;

        // Reduce the WNAF array to its actual length
        byte[] wnafShort = new byte[length];
        System.arraycopy(wnaf, 0, wnafShort, 0, length);
        return wnafShort;

    }


    public class JacobPoint {

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
