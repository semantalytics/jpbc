package it.unisa.dia.gas.plaf.jpbc.pairing.f;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeFCurveGenerator implements CurveGenerator {
    protected int rBits; // The number of bits in r, the order of the subgroup G1


    public TypeFCurveGenerator(int rBits) {
        this.rBits = rBits;
    }


    public Map generate() {
        //36 is a 6-bit number
        int xbit = (rBits - 6) / 4;

        //TODO: use binary search to find smallest appropriate x
        BigInteger q, r, b;

        BigInteger x = BigInteger.ZERO.setBit(xbit);
        BigInteger t;
        for (; ;) {
            t = x.multiply(x).multiply(BigInteger.valueOf(6)).add(BigInteger.ONE);
            q = tryminusx(x);
            r = q.subtract(t).add(BigInteger.ONE);

            if (q.isProbablePrime(10) && r.isProbablePrime(10)) break;

            q = tryplusx(x);
            r = q.subtract(t).add(BigInteger.ONE);

            if (q.isProbablePrime(10) && r.isProbablePrime(10)) break;

            x = x.add(BigInteger.ONE);
        }

        Field Fq = new NaiveField(q);
        Element e1 = Fq.newElement();
        for (; ;) {
            e1.setToRandom();

            Field curveField = new CurveField(e1, r, null);
            Element point = curveField.newRandomElement().mul(r);
            if (point.isZero())
                break;
        }

        b = e1.toBigInteger();

        Field Fq2 = new QuadraticField(Fq);
        BigInteger beta = Fq.getNqr().toBigInteger();

        PolyField Fq2x = new PolyField(Fq2);
        PolyElement<Point> f = Fq2x.newElement();

        // Find an irreducible polynomial of the form f = x^6 + alpha.
        // Call poly_set_coeff1() first so we can use element_item() for the other
        // coefficients.
        f.ensureSize(7);
        f.getCoefficient(6).setToOne();
        for (; ;) {
            f.getCoefficient(0).setToRandom();

            if (f.isIrriducible())
                break;
        }

        //extend F_q^2 using f = x^6 + alpha
        //see if sextic twist contains a subgroup of order r
        //if not, it's the wrong twist: replace alpha with alpha^5
        e1 = Fq2.newElement().set(b).mul(f.getCoefficient(0)).negate();

        Field curveField = new CurveField(e1, r, null);
        Element point = curveField.newRandomElement();

        //I'm not sure what the #E'(F_q^2) is, but
        //it definitely divides n_12 = #E(F_q^12). It contains a
        //subgroup of order r if and only if
        //(n_12 / r^2)P != O for some (in fact most) P in E'(F_q^6)
        BigInteger z0 = q.pow(12).add(BigInteger.ONE);

        BigInteger z1 = BigIntegerUtils.pbc_mpz_trace_n(q, t, 12);
        z1 = z0.subtract(z1);
        z0 = r.multiply(r);
        z1 = z1.divide(z0);

        point.mul(z1);
        if (point.isZero()) {
            z0 = BigInteger.valueOf(5);
            f.getCoefficient(0).pow(z0);
        }

        BigInteger alpha0 = f.getCoefficient(0).getX().toBigInteger();
        BigInteger alpha1 = f.getCoefficient(0).getY().toBigInteger();

        // Store parameters
        CurveParams params = new CurveParams();
        params.put("type", "f");
        params.put("q", q.toString());
        params.put("r", r.toString());
        params.put("b", b.toString());
        params.put("beta", beta.toString());
        params.put("alpha0", alpha0.toString());
        params.put("alpha1", alpha1.toString());

        return params;
    }


    protected BigInteger tryminusx(BigInteger x) {
        //36x4 - 36x3 + 24x2 - 6x + 1
        //= ((36(x - 1)x + 24)x - 6)x + 1

        return x.subtract(BigInteger.ONE)
                .multiply(x)
                .multiply(BigInteger.valueOf(36))
                .add(BigInteger.valueOf(24))
                .multiply(x)
                .subtract(BigInteger.valueOf(6))
                .multiply(x)
                .add(BigInteger.ONE);
    }

    protected BigInteger tryplusx(BigInteger x) {
        //36x4 + 36x3 + 24x2 + 6x + 1
        //= ((36(x + 1)x + 24)x + 6)x + 1
        return x.add(BigInteger.ONE)
                .multiply(x)
                .multiply(BigInteger.valueOf(36))
                .add(BigInteger.valueOf(24))
                .multiply(x)
                .add(BigInteger.valueOf(6))
                .multiply(x)
                .add(BigInteger.ONE);
    }


    public static void main(String[] args) {
        if (args.length < 1)
            throw new IllegalArgumentException("Too few arguments. Usage <rbits>");

        if (args.length > 1)
            throw new IllegalArgumentException("Too many arguments. Usage <rbits>");

        Integer rBits = Integer.parseInt(args[0]);

        CurveGenerator generator = new TypeFCurveGenerator(rBits);
        CurveParams curveParams = (CurveParams) generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}