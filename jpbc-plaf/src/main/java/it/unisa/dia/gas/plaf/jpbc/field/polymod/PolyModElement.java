package it.unisa.dia.gas.plaf.jpbc.field.polymod;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericPolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyUtils;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PolyModElement<E extends Element> extends GenericPolyElement<E> {
    protected PolyModField field;


    public PolyModElement(PolyModField field) {
        super(field);
        this.field = field;

        for (int i = 0; i < field.n; i++) {
            coeff.add((E) field.getTargetField().newElement());
        }
    }

    public PolyModElement(PolyModField field, List<E> coeff) {
        super(field);
        this.field = field;

        this.coeff = coeff;
    }


    public PolyModField getField() {
        return field;
    }

    public PolyModElement duplicate() {
        List<Element> duplicatedCoeff = new ArrayList<Element>(coeff.size());

        for (Element element : coeff) {
            duplicatedCoeff.add(element.duplicate());
        }

        return new PolyModElement(field, duplicatedCoeff);
    }

    public PolyModElement set(Element e) {
        PolyModElement<E> element = (PolyModElement<E>) e;
        
        for (int i = 0; i < coeff.size(); i++) {
            coeff.get(i).set(element.coeff.get(i));
        }

        return this;
    }

    public PolyModElement set(int value) {
        coeff.get(0).set(value);

        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
        /*
        polymod_field_data_ptr p = e->field->data;
        element_t *coeff = e->data;
        int i, n = p->n;
        element_set_si(coeff[0], x);
        for (i=1; i<n; i++) {
            element_set0(coeff[i]);
        }
        */
    }

    public PolyModElement set(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyModElement setToRandom() {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).setToRandom();
        }

        return this;
    }

    public PolyModElement setFromHash(byte[] hash) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytes(byte[] bytes) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setEncoding(byte[] bytes) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] getDecoding() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyModElement setToZero() {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public boolean isZero() {
        for (int i = 0; i < field.n; i++) {
            if (!coeff.get(i).isZero())
                return false;
        }
        return true;
    }

    public PolyModElement setToOne() {
        coeff.get(0).setToOne();

        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
    }

    public boolean isOne() {
        if (!coeff.get(0).isOne())
            return false;

        for (int i = 1; i < field.n; i++) {
            if (!coeff.get(i).isZero())
                return false;
        }

        return true;

        /*
        polymod_field_data_ptr p = e->field->data;
        element_t *coeff = e->data;
        int i, n = p->n;

        if (!element_is1(coeff[0])) return 0;
        for (i=1; i<n; i++) {
        if (!element_is0(coeff[i])) return 0;
        }
        return 1;
        */
    }

    public PolyModElement map(Element e) {
        coeff.get(0).set(e);
        for (int i = 1; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        return this;
        /*
        polymod_field_data_ptr p = f - > field - > data;
        element_t * coeff = f - > data;
        int i, n = p - > n;
        element_set(coeff[0], g);
        for (i = 1; i < n; i++) {
            element_set0(coeff[i]);
        }
        */
    }

    public PolyModElement twice() {
        return add(this);
    }

    public PolyModElement square() {
        switch (field.n) {
            case 3:
                PolyModElement<E> p0 = field.newElement();
                Element c0 = field.getTargetField().newElement();
                Element c2 = field.getTargetField().newElement();

                Element c3 = p0.coeff.get(0);
                Element c1 = p0.coeff.get(1);

                c3.set(coeff.get(0)).mul(coeff.get(1));
                c1.set(coeff.get(0)).mul(coeff.get(2));
                coeff.get(0).square();

                c2.set(coeff.get(1)).mul(coeff.get(2));
                c0.set(coeff.get(2)).square();
                coeff.get(2).set(coeff.get(1)).square();

                coeff.get(1).set(c3).add(c3);

                c1.add(c1);

                coeff.get(2).add(c1);
                p0.set(field.xpwr[1]);
                p0.polymodConstMul(c0);
                add(p0);

                c2.add(c2);
                p0.set(field.xpwr[0]);
                p0.polymodConstMul(c2);
                add(p0);

                return this;
                /*
                element_t *dst = res->data;
                element_t *src = e->data;
                polymod_field_data_ptr p = res->field->data;
                element_t p0;
                element_t c0, c2;
                element_ptr c1, c3;

                element_init(p0, res->field);
                element_init(c0, p->field);
                element_init(c2, p->field);

                c3 = p0->data;
                c1 = c3 + 1;

                element_mul(c3, src[0], src[1]);
                element_mul(c1, src[0], src[2]);
                element_square(dst[0], src[0]);

                element_mul(c2, src[1], src[2]);
                element_square(c0, src[2]);
                element_square(dst[2], src[1]);

                element_add(dst[1], c3, c3);

                element_add(c1, c1, c1);
                element_add(dst[2], dst[2], c1);

                polymod_const_mul(p0, c0, p->xpwr[1]);
                element_add(res, res, p0);

                element_add(c2, c2, c2);
                polymod_const_mul(p0, c2, p->xpwr[0]);
                element_add(res, res, p0);

                element_printf("polymod_square_degree3 = %B\n", res);

                element_clear(p0);
                element_clear(c0);
                element_clear(c2);
                */
            case 6:
                throw new IllegalStateException("Not Implemented yet!!!");
            default:
                squareInternal();
        }
        return this;
    }

    public PolyModElement invert() {
        setFromPolyTruncate(polyInvert(field.irred.getField().newElement().setFromPolyMod(this)));
        return this;

        /*
        polymod_field_data_ptr p = r->field->data;
        element_ptr minpoly = p->poly;
        element_t f, r1;

        element_init(f, minpoly->field);
        element_init(r1, minpoly->field);
        element_polymod_to_poly(f, e);

        poly_invert(r1, f, p->poly);

        element_poly_to_polymod_truncate(r, r1);

        element_clear(f);
        element_clear(r1);
        */
    }

    public PolyModElement negate() {
        for (Element e : coeff) {
            e.negate();
        }

        return this;
    }

    public PolyModElement add(Element e) {
        PolyModElement<E> element = (PolyModElement<E>) e;

        for (int i = 0; i < field.n; i++) {
            coeff.get(i).add(element.coeff.get(i));
        }

        return this;
    }

    public PolyModElement sub(Element e) {
        PolyModElement<E> element = (PolyModElement<E>) e;

        for (int i = 0; i < field.n; i++) {
            coeff.get(i).sub(element.coeff.get(i));
        }

        return this;
    }

    public PolyModElement mul(Element e) {
        PolyModElement<E> element = (PolyModElement<E>) e;

        switch (field.n) {
            case 3:
//                System.out.println("mul (e) = " + this);
//                System.out.println("mul (f) = " + element);

                PolyModElement<E> p0 = field.newElement();
                Element c3 = field.getTargetField().newElement();
                Element c4 = field.getTargetField().newElement();

                kar_poly_2(coeff, c3, c4, coeff, element.coeff, p0.coeff);

//                System.out.println("c3 = " + c3);
//                System.out.println("c4 = " + c4);

                p0.set(field.xpwr[0]).polymodConstMul(c3);
                add(p0);
                p0.set(field.xpwr[1]).polymodConstMul(c4);
                add(p0);

//                System.out.println("mul = " + this);

                return this;
            /*
            polymod_field_data_ptr p = res->field->data;
            element_t *dst = res->data, *s1 = e->data, *s2 = f->data;
            element_t c3, c4;
            element_t p0;

            element_init(p0, res->field);
            element_init(c3, p->field);
            element_init(c4, p->field);

            kar_poly_2(dst, c3, c4, s1, s2, p0->data);

            polymod_const_mul(p0, c3, p->xpwr[0]);
            element_add(res, res, p0);
            polymod_const_mul(p0, c4, p->xpwr[1]);
            element_add(res, res, p0);

            element_clear(p0);
            element_clear(c3);
            element_clear(c4);
            */
            case 6:
                throw new IllegalStateException("Not Implemented yet!!!");
//                f->mul = polymod_mul_degree6;
            default:

                Element[] high = new Element[field.n - 1];
                for (int i = 0, size = field.n - 1; i < size; i++) {
                    high[i] = field.getTargetField().newElement().setToZero();
                }
                PolyModElement<E> prod = field.newElement();
                p0 = field.newElement();
                Element c0 = field.getTargetField().newElement();

                for (int i = 0; i < field.n; i++) {
                    int ni = field.n - i;

                    int j = 0;
                    for (; j < ni; j++) {
                        c0.set(coeff.get(i)).mul(element.coeff.get(j));
                        prod.coeff.get(i + j).add(c0);
                    }
                    for (; j < field.n; j++) {
                        c0.set(coeff.get(i)).mul(element.coeff.get(j));
                        high[j - ni].add(c0);
                    }
                }

                for (int i = 0, size = field.n - 1; i < size; i++) {
                    p0.set(field.xpwr[i]).polymodConstMul(high[i]);
                    prod.add(p0);
                }

                set(prod);

                return this;
            /*
            polymod_field_data_ptr p = res->field->data;
            int n = p->n;
            element_t *dst;
            element_t *s1 = e->data, *s2 = f->data;
            element_t prod, p0, c0;
            int i, j;
            element_t *high; //coefficients of x^n,...,x^{2n-2}

            high = pbc_malloc(sizeof(element_t) * (n - 1));
            for (i=0; i<n-1; i++) {
                element_init(high[i], p->field);
                element_set0(high[i]);
            }
            element_init(prod, res->field);
            dst = prod->data;
            element_init(p0, res->field);
            element_init(c0, p->field);

            for (i=0; i<n; i++) {
                int ni = n - i;
                for (j=0; j<ni; j++) {
                    element_mul(c0, s1[i], s2[j]);
                    element_add(dst[i + j], dst[i + j], c0);
                }
                for (;j<n; j++) {
                    element_mul(c0, s1[i], s2[j]);
                    element_add(high[j - ni], high[j - ni], c0);
                }
            }

            for (i=0; i<n-1; i++) {
                polymod_const_mul(p0, high[i], p->xpwr[i]);
                element_add(prod, prod, p0);
                element_clear(high[i]);
            }
            pbc_free(high);

            element_set(res, prod);
            */
        }
    }

    public PolyModElement mul(int value) {
        for (int i = 0; i < field.n; i++) {
            coeff.get(i).mul(value);
        }
        
        return this;
        /*
        static void poly_mul_si(element_ptr f, element_ptr g, signed long int z)
        poly_element_ptr pf = f - > data;
        poly_element_ptr pg = g - > data;
        int i, n;

        n = pg - > coeff - > count;
        poly_alloc(f, n);
        for (i = 0; i < n; i++) {
            element_mul_si(pf - > coeff - > item[i], pg - > coeff - > item[i], z);
        }
        */
    }

    public PolyModElement mul(BigInteger value) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyModElement mulZn(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyModElement powZn(Element e) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public PolyModElement sqrt() {
        PolyField polyField = new PolyField(field);

        PolyElement f = polyField.newElement();
        PolyElement r = polyField.newElement();
        PolyElement s = polyField.newElement();
        Element e0 = field.newElement();

        f.ensureCoeffSize(3);
        f.getCoefficient(2).setToOne();
        f.getCoefficient(0).set(this).negate();

//        System.out.println("a = " + this);
//        System.out.println("f.getCoefficient(2) = " + f.getCoefficient(2));
//        System.out.println("f.getCoefficient(0) = " + f.getCoefficient(0));

        BigInteger z = field.getOrder().subtract(BigInteger.ONE).divide(BigIntegerUtils.TWO);

//        System.out.println("z = " + z);

        while (true) {
            int i;
            Element x;
            Element e1, e2;

            r.ensureCoeffSize(2);
            r.getCoefficient(1).setToOne();
            x = r.getCoefficient(0);
            x.setToRandom();
            e0.set(x).mul(x);

            if (e0.compareTo(this) == 0) {
                set(x);
                break;
            }
//            System.out.println("r = " + r);

            s.setToOne();
            for (i = z.bitLength() - 1; i >= 0; i--) {
                s.mul(s);
                
                if (s.getDegree() == 2) {
                    e1 = s.getCoefficient(0);
                    e2 = s.getCoefficient(2);
                    e0.set(e2).mul(this);
                    e1.add(e0);
                    s.ensureCoeffSize(2);
                    s.removeLeadingZeroes();
                }

                if (z.testBit(i)) {
                    s.mul(r);
                    if (s.getDegree() == 2) {
                        e1 = s.getCoefficient(0);
                        e2 = s.getCoefficient(2);
                        e0.set(e2).mul(this);
                        e1.add(e0);
                        s.ensureCoeffSize(2);
                        s.removeLeadingZeroes();
                    }
                }
            }

            if (s.getDegree() < 1)
                continue;

            e0.setToOne();
            e1 = s.getCoefficient(0);
            e2 = s.getCoefficient(1);
            e1.add(e0);
//            System.out.println("e2 = " + e2);
            e0.set(e2).invert();
//            System.out.println("e0 = " + e0);
            e0.mul(e1);
            e2.set(e0).mul(e0);

            if (e2.compareTo(this) == 0) {
                set(e0);
                break;
            }
        }

        return this;
        /*
        static void polymod_sqrt(element_ptr res, element_ptr a)
        
        field_t kx;
        element_t f;
        element_t r, s;
        element_t e0;
        mpz_t z;

        field_init_poly(kx, a - > field);
        mpz_init(z);
        element_init(f, kx);
        element_init(r, kx);
        element_init(s, kx);
        element_init(e0, a - > field);

        poly_alloc(f, 3);
        element_set1(poly_coeff(f, 2));
        element_neg(poly_coeff(f, 0), a);

        mpz_sub_ui(z, a - > field - > order, 1);
        mpz_divexact_ui(z, z, 2);
        for (; ;) {
            int i;
            element_ptr x;
            element_ptr e1, e2;

            poly_alloc(r, 2);
            element_set1(poly_coeff(r, 1));
            x = poly_coeff(r, 0);
            element_random(x);
            element_mul(e0, x, x);
            if (!element_cmp(e0, a)) {
                element_set(res, x);
                break;
            }
            element_set1(s);
            //TODO: this can be optimized greatly
            //since we know r has the form ax + b
            for (i = mpz_sizeinbase(z, 2) - 1; i >= 0; i--) {
                element_mul(s, s, s);
                if (poly_degree(s) == 2) {
                    e1 = poly_coeff(s, 0);
                    e2 = poly_coeff(s, 2);
                    element_mul(e0, e2, a);
                    element_add(e1, e1, e0);
                    poly_alloc(s, 2);
                    poly_remove_leading_zeroes(s);
                }
                if (mpz_tstbit(z, i)) {
                    element_mul(s, s, r);
                    if (poly_degree(s) == 2) {
                        e1 = poly_coeff(s, 0);
                        e2 = poly_coeff(s, 2);
                        element_mul(e0, e2, a);
                        element_add(e1, e1, e0);
                        poly_alloc(s, 2);
                        poly_remove_leading_zeroes(s);
                    }
                }
            }
            if (poly_degree(s) < 1) continue;
            element_set1(e0);
            e1 = poly_coeff(s, 0);
            e2 = poly_coeff(s, 1);
            element_add(e1, e1, e0);
            element_invert(e0, e2);
            element_mul(e0, e0, e1);
            element_mul(e2, e0, e0);
            if (!element_cmp(e2, a)) {
                element_set(res, e0);
                break;
            }
        }

        mpz_clear(z);
        element_clear(f);
        element_clear(r);
        element_clear(s);
        element_clear(e0);
        field_clear(kx);
        */
    }

    public boolean isSqr() {
        BigInteger z = field.getOrder().subtract(BigInteger.ONE).divide(BigIntegerUtils.TWO);
        return field.newElement().set(this).pow(z).isOne();

        /*
        mpz_init(z);
        mpz_sub_ui(z, e->field->order, 1);
        mpz_divexact_ui(z, z, 2);

        element_pow_mpz(e0, e, z);
        res = element_is1(e0);
        element_clear(e0);
        mpz_clear(z);
        return res;
        */
    }

    public int sign() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int compareTo(Element e) {
        PolyModElement<E> element = (PolyModElement<E>) e;

        for (int i = 0; i < field.n; i++) {
            if (coeff.get(i).compareTo(element.coeff.get(i)) != 0)
                return 1;
        }

        return 0;
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("[");
        for (Element e : coeff) {
            buffer.append(e).append(", ");
        }
        buffer.append("]");
        return buffer.toString();
    }


    public PolyModElement setFromPolyTruncate(PolyElement<E> element) {
        int n = element.getCoefficients().size();
        if (n > field.n)
            n = field.n;

        int i = 0;
        for (; i < n; i++) {
            coeff.get(i).set(element.getCoefficients().get(i));
        }

        for (; i < field.n; i++) {
            coeff.get(i).setToZero();
        }

        /*
        polymod_field_data_ptr p = e - > field - > data;
        element_t * coeff = e - > data;
        int i;
        int n;
        n = poly_coeff_count(f);
        if (n > p - > n) n = p - > n;

        for (i = 0; i < n; i++) {
            element_set(coeff[i], poly_coeff(f, i));
        }
        for (; i < p - > n; i++) {
            element_set0(coeff[i]);
        }
        */
//        throw new IllegalStateException("Not Implemented yet!!!");
        return this;
    }

    public PolyModElement polymodConstMul(Element e) {
        //a lies in R, e in R[x]
        for (int i = 0, n = coeff.size(); i < n; i++) {
            coeff.get(i).mul(e);
        }

        return this;

        /*
        element_t * coeff = e - > data,*dst = res - > data;
        int i, n = polymod_field_degree(e - > field);

        for (i = 0; i < n; i++) {
            element_mul(dst[i], coeff[i], a);
        }
        */
    }


    protected void squareInternal() {
        List<E> dst;
        List<E> src = coeff;

        int n = field.n;

        PolyModElement<E> prod, p0;
        Element c0;
        int i, j;

        Element high[] = new Element[n - 1];

        for (i = 0; i < n - 1; i++) {
            high[i] = field.getTargetField().newElement().setToZero();
        }

        prod = field.newElement();
        dst = prod.coeff;
        p0 = field.newElement();
        c0 = field.getTargetField().newElement();

        for (i = 0; i < n; i++) {
            int twicei = 2 * i;

            c0.set(src.get(i)).square();

            if (twicei < n) {
                dst.get(twicei).add(c0);
            } else {
                high[twicei - n].add(c0);
            }

            for (j = i + 1; j < n - i; j++) {
                c0.set(src.get(i)).mul(src.get(j));
                c0.add(c0);
                dst.get(i + j).add(c0);
            }

            for (; j < n; j++) {
                c0.set(src.get(i)).mul(src.get(j));
                c0.add(c0);
                high[i + j - n].add(c0);
            }
        }

        for (i = 0; i < n - 1; i++) {
            p0.set(field.xpwr[i]).polymodConstMul(high[i]);
            prod.add(p0);
        }

        set(prod);

        /*
        element_t * dst;
        element_t * src = e - > data;
        polymod_field_data_ptr p = res - > field - > data;
        int n = p - > n;

        element_t prod, p0, c0;
        int i, j;

        element_t * high; //coefficients of x^n,...,x^{2n-2}

        high = pbc_malloc(sizeof(element_t) * (n - 1));
        for (i = 0; i < n - 1; i++) {
            element_init(high[i], p - > field);
            element_set0(high[i]);
        }

        element_init(prod, res - > field);
        dst = prod - > data;
        element_init(p0, res - > field);
        element_init(c0, p - > field);

        for (i = 0; i < n; i++) {
            int twicei = 2 * i;
            element_square(c0, src[i]);
            if (twicei < n) {
                element_add(dst[twicei], dst[twicei], c0);
            } else {
                element_add(high[twicei - n], high[twicei - n], c0);
            }

            for (j = i + 1; j < n - i; j++) {
                element_mul(c0, src[i], src[j]);
                element_add(c0, c0, c0);
                element_add(dst[i + j], dst[i + j], c0);
            }
            for (; j < n; j++) {
                element_mul(c0, src[i], src[j]);
                element_add(c0, c0, c0);
                element_add(high[i + j - n], high[i + j - n], c0);
            }
        }

        for (i = 0; i < n - 1; i++) {
            polymod_const_mul(p0, high[i], p - > xpwr[i]);
            element_add(prod, prod, p0);
            element_clear(high[i]);
        }
        pbc_free(high);

        element_set(res, prod);
        element_clear(prod);
        element_clear(p0);
        element_clear(c0);
        */
    }

    //Karatsuba for degree 2 polynomials
    protected void kar_poly_2(List<E> dst, Element c3, Element c4, List<E> s1, List<E> s2, List<E> scratch) {
        Element c01, c02, c12;

        c12 = scratch.get(0);
        c02 = scratch.get(1);
        c01 = scratch.get(2);

        c3.set(s1.get(0)).add(s1.get(1));
        c4.set(s2.get(0)).add(s2.get(1));
        c01.set(c3).mul(c4);
        c3.set(s1.get(0)).add(s1.get(2));
        c4.set(s2.get(0)).add(s2.get(2));
        c02.set(c3).mul(c4);
        c3.set(s1.get(1)).add(s1.get(2));
        c4.set(s2.get(1)).add(s2.get(2));
        c12.set(c3).mul(c4);

//        System.out.println("c01 = " + c01);
//        System.out.println("c02 = " + c02);
//        System.out.println("c12 = " + c12);

//        System.out.println("s1.get(0) = " + s1.get(0));
//        System.out.println("s1.get(1) = " + s1.get(1));
//        System.out.println("s1.get(2) = " + s1.get(2));

//        System.out.println("s2.get(0) = " + s2.get(0));
//        System.out.println("s2.get(1) = " + s2.get(1));
//        System.out.println("s2.get(2) = " + s2.get(2));

        dst.get(1).set(s1.get(1)).mul(s2.get(1));

        //constant term
        dst.get(0).set(s1.get(0)).mul(s2.get(0));

        //coefficient of x^4
        c4.set(s1.get(2)).mul(s2.get(2));

        //coefficient of x^3
//        System.out.println("==================================");
//        System.out.println("dst.get(1) = " + dst.get(1));
//        System.out.println("c4 = " + c4);
//        System.out.println("c12 = " + c12);
//        System.out.println("==================================");

        c3.set(dst.get(1)).add(c4);
        c3.set(c3.getField().sub(c12, c3));


//        element_add(c3, dst[1], c4);
//        element_sub(c3, c12, c3);


        //coefficient of x^2
        dst.get(2).set(c4).add(dst.get(0));
        c02.sub(dst.get(2));
        dst.get(2).set(dst.get(1)).add(c02);

        //coefficient of x
        c01.sub(dst.get(0));
        dst.set(1, (E) dst.get(1).getField().sub(c01, dst.get(1)));


//        System.out.println("dst.get(0) = " + dst.get(0));
//        System.out.println("dst.get(1) = " + dst.get(1));
//        System.out.println("dst.get(2) = " + dst.get(2));

        /*
        element_ptr c01, c02, c12;

        c12 = scratch[0];
        c02 = scratch[1];
        c01 = scratch[2];

        element_add(c3, s1[0], s1[1]);
        element_add(c4, s2[0], s2[1]);
        element_mul(c01, c3, c4);
        element_add(c3, s1[0], s1[2]);
        element_add(c4, s2[0], s2[2]);
        element_mul(c02, c3, c4);
        element_add(c3, s1[1], s1[2]);
        element_add(c4, s2[1], s2[2]);
        element_mul(c12, c3, c4);

        element_mul(dst[1], s1[1], s2[1]);

        //constant term
        element_mul(dst[0], s1[0], s2[0]);

        //coefficient of x^4
        element_mul(c4, s1[2], s2[2]);

        //coefficient of x^3
        element_add(c3, dst[1], c4);
        element_sub(c3, c12, c3);

        //coefficient of x^2
        element_add(dst[2], c4, dst[0]);
        element_sub(c02, c02, dst[2]);
        element_add(dst[2], dst[1], c02);

        //coefficient of x
        element_sub(c01, c01, dst[0]);
        element_sub(dst[1], c01, dst[1]);
        */
    }

    protected PolyElement polyInvert(PolyElement f) {
        PolyField polyField = f.getField();

        PolyElement q = polyField.newElement();

        // TODO: why do we need the cast???
        PolyElement b0 = (PolyElement) polyField.newZeroElement();
        PolyElement b1 = (PolyElement) polyField.newOneElement();
        PolyElement b2 = polyField.newElement();

        PolyElement r0 = field.irred.duplicate();
        PolyElement r1 = f.duplicate();
        PolyElement r2 = polyField.newElement();

        Element inv = f.getField().getTargetField().newElement();

        while (true) {
            PolyUtils.div(q, r2, r0, r1);
            if (r2.isZero())
                break;

            b2.set(b1).mul(q);
            b2.set(polyField.sub(b0, b2));

            b0.set(b1);
            b1.set(b2);

            r0.set(r1);
            r1.set(r2);
        }

        inv.set(r1.getCoefficient(0)).invert();
        return PolyUtils.constMul(inv, b1);

        /*
        void poly_invert(element_ptr res, element_ptr f, element_ptr m)
        element_t q, r0, r1, r2;
        element_t b0, b1, b2;
        element_t inv;

        element_init(b0, res - > field);
        element_init(b1, res - > field);
        element_init(b2, res - > field);
        element_init(q, res - > field);
        element_init(r0, res - > field);
        element_init(r1, res - > field);
        element_init(r2, res - > field);
        element_init(inv, poly_base_field(res));
        element_set0(b0);
        element_set1(b1);
        element_set(r0, m);
        element_set(r1, f);

        for (; ;) {
            poly_div(q, r2, r0, r1);
            if (element_is0(r2)) break;
            element_mul(b2, b1, q);
            element_sub(b2, b0, b2);
            element_set(b0, b1);
            element_set(b1, b2);
            element_set(r0, r1);
            element_set(r1, r2);
        }
        element_invert(inv, poly_coeff(r1, 0));
        poly_const_mul(res, inv, b1);
        element_clear(inv);
        element_clear(q);
        element_clear(r0);
        element_clear(r1);
        element_clear(r2);
        element_clear(b0);
        element_clear(b1);
        element_clear(b2);
        */
    }

}