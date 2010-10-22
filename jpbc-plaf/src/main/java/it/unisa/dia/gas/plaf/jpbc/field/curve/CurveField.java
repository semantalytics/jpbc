package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.generic.GenericFieldOver;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveField<F extends Field> extends GenericFieldOver<F, CurveElement> {

    public static <F extends Field> CurveField<F> newCurveFieldJ(Element j, BigInteger order, BigInteger cofac) {
        // Assumes j != 0, 1728

        Element a, b;

        a = j.getField().newElement();
        b = j.getField().newElement();

        a.set(1728).sub(j).invert().mul(j);

        //b = 2 j / (1728 - j)
        b.set(a).add(a);
        //a = 3 j / (1728 - j)
        a.add(b);

        return new CurveField<F>(a, b, order, cofac);
    }

    protected Element a, b;
    protected CurveElement gen, genNoCofac;
    protected BigInteger order, cofac;

    // A non-NULL quotient_cmp means we are working with the quotient group of
    // order #E / quotient_cmp, and the points are actually coset
    // representatives. Thus for a comparison, we must multiply by quotient_cmp
    // before comparing.
    protected BigInteger quotient_cmp = null;


    public CurveField(Element a, Element b, BigInteger order, BigInteger cofac) {
        super((F) a.getField());

        this.a = a;
        this.b = b;
        this.order = order;
        this.cofac = cofac;

        initGen();
    }


    public CurveField(Element a, Element b, BigInteger order, BigInteger cofac, BigInteger genNoCofac) {
        super((F) a.getField());

        this.a = a;
        this.b = b;
        this.order = order;
        this.cofac = cofac;

        initGen(genNoCofac);
    }

    public CurveField(Element b, BigInteger order, BigInteger cofac) {
        this(b.getField().newZeroElement(), b, order, cofac);
    }


    public CurveElement newElement() {
        return new CurveElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public CurveElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int getLengthInBytes() {
        return getTargetField().getLengthInBytes() * 2;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurveField)) return false;

        CurveField that = (CurveField) o;

        if (!a.equals(that.a)) return false;
        if (!b.equals(that.b)) return false;
        if (cofac != null ? !cofac.equals(that.cofac) : that.cofac != null) return false;
        if (!order.equals(that.order)) return false;

        return true;
    }

    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        result = 31 * result + order.hashCode();
        result = 31 * result + (cofac != null ? cofac.hashCode() : 0);
        return result;
    }


    public Element getA() {
        return a;
    }

    public Element getB() {
        return b;
    }

    public BigInteger getQuotient_cmp() {
        return quotient_cmp;
    }

    public void setQuotient_cmp(BigInteger quotient_cmp) {
        this.quotient_cmp = quotient_cmp;
    }

    /**
     * Existing points are invalidated as this mangles c.
     * Consider the curve E′ given by Y^2 = X^3 + a v^2 X + v^3 b, which
     * we call the (quadratic) twist of the curve E, where
     * v is a quadratic nonresidue in Fq
     *
     * @return the twisted curve.
     */
    public CurveField twist() {
        Element nqr = getTargetField().getNqr();

        a.mul(nqr).mul(nqr);
        b.mul(nqr).mul(nqr).mul(nqr);

        initGen();

        return this;
    }

    public CurveElement getGenNoCofac() {
        return genNoCofac;
    }

    public CurveElement getGen() {
        return gen;
    }

    protected void initGen() {
        genNoCofac = getCurveRandomNoCofacSolvefory();
        if (cofac != null) {
            gen = genNoCofac.duplicate().mul(cofac);
        } else {
            gen = genNoCofac.duplicate();
        }
    }

    protected void initGen(BigInteger genNoCofac) {
        if (genNoCofac == null) {
            this.genNoCofac = getCurveRandomNoCofacSolvefory();
        } else {
            CurveElement element = new CurveElement(this);
            element.setFromBytes(genNoCofac.toByteArray());
            this.genNoCofac = element;
        }

        if (cofac != null) {
            gen = this.genNoCofac.duplicate().mul(cofac);
        } else {
            gen = this.genNoCofac.duplicate();
        }
    }

    protected CurveElement getCurveRandomNoCofacSolvefory() {
        //TODO: with 0.5 probability negate y-coord

        Element t = targetField.newElement();

        CurveElement element = new CurveElement(this);
        element.infFlag = 0;

        do {
            t.set(element.getX().setToRandom()).square().add(a).mul(element.getX()).add(b);
        } while (!t.isSqr());
        element.getY().set(t.sqrt());

        return element;
    }

    @Override
    public CurveElement[] twice(CurveElement[] elements) {
        int i;
        int n = elements.length;

        Element[] table = new Element[n];  //a big problem?
        Element e0, e1, e2;
        CurveElement q, r;

        q = elements[0];
        e0 = elements[0].getX().getField().newElement();
        e1 = elements[0].getX().getField().newElement();
        e2 = elements[0].getX().getField().newElement();

        for (i = 0; i < n; i++) {
            q = elements[i];
            r = elements[i];
            table[i] = q.getY().getField().newElement();

            if (q.infFlag != 0) {
                r.infFlag = 1;
                continue;
            }
            if (q.getY().isZero()) {
                r.infFlag = 1;
                continue;
            }
        }

        //to compute 1/2y multi. see Cohen's GTM139 Algorithm 10.3.4
        for (i = 0; i < n; i++) {
            q = elements[i];
            table[i].set(q.getY()).twice();
            if (i > 0)
                table[i].mul(table[i - 1]);
        }
        e2.set(table[n-1]).invert();
        for (i = n - 1; i > 0; i--) {
            q = elements[i];
            table[i].set(table[i-1]).mul(e2);
            e2.mul(q.getY()).twice(); //e2=e2*2y_j
        }
        table[0].set(e2); //e2 no longer used.

        for (i = 0; i < n; i++) {
            q = elements[i];
            r = elements[i];
            if (r.infFlag != 0) continue;

            //e2=lambda = (3x^2 + a) / 2y
            e2.set(q.getX()).square().mul(3).add(a).mul(table[i]); //Recall that table[i]=1/2y_i

            //x1 = lambda^2 - 2x
            e1.set(q.getX()).twice();
            e0.set(e2).square().sub(e1);

            //y1 = (x - x1)lambda - y
            e1.set(q.getX()).sub(e0) .mul(e2).sub(q.getY());

            r.getX().set(e0);
            r.getY().set(e1);
            r.infFlag = 0;
        }

        return elements;
    }


/*
//compute c_i=a_i+b_i at one time.
static void multi_add(element_ptr c[], element_ptr a[], element_ptr b[], int n){
 int i;
 element_t* table=malloc(sizeof(element_t)*n);  //a big problem?
 point_ptr p, q, r;
 element_t e0, e1, e2;
 curve_data_ptr cdp = a[0]->field->data;

 p = a[0]->data;
 q = b[0]->data;
 element_init(e0, p->x->field);
 element_init(e1, p->x->field);
 element_init(e2, p->x->field);

 element_init(table[0], p->x->field);
 element_sub(table[0], q->x, p->x);
 for(i=1; i<n; i++){
   p = a[i]->data;
   q = b[i]->data;
   element_init(table[i], p->x->field);
   element_sub(table[i], q->x, p->x);
   element_mul(table[i], table[i], table[i-1]);
 }
 element_invert(e2, table[n-1]);
 for(i=n-1; i>0; i--){
   p = a[i]->data;
   q = b[i]->data;
   element_mul(table[i], table[i-1], e2);
   element_sub(e1, q->x, p->x);
   element_mul(e2,e2,e1); //e2=e2*(x2_j-x1_j)
 }
 element_set(table[0],e2); //e2 no longer used.

 for(i=0; i<n; i++){
   p = a[i]->data;
   q = b[i]->data;
   r = c[i]->data;
   if (p->inf_flag) {
     curve_set(c[i], b[i]);
     continue;
   }
   if (q->inf_flag) {
     curve_set(c[i], a[i]);
     continue;
   }
   if (!element_cmp(p->x, q->x)) { //a[i]=b[i]
     if (!element_cmp(p->y, q->y)) {
       if (element_is0(p->y)) {
         r->inf_flag = 1;
         continue;
       } else {
         double_no_check(r, p, cdp->a);
         continue;
       }
     }
     //points are inverses of each other
     r->inf_flag = 1;
     continue;
   } else {
     //lambda = (y2-y1)/(x2-x1)
     element_sub(e2, q->y, p->y);
     element_mul(e2, e2, table[i]);
     //x3 = lambda^2 - x1 - x2
     element_square(e0, e2);
     element_sub(e0, e0, p->x);
     element_sub(e0, e0, q->x);
     //y3 = (x1-x3)lambda - y1
     element_sub(e1, p->x, e0);
     element_mul(e1, e1, e2);
     element_sub(e1, e1, p->y);
     element_set(r->x, e0);
     element_set(r->y, e1);
     r->inf_flag = 0;
   }
 }
 element_clear(e0);
 element_clear(e1);
 element_clear(e2);
 for(i=0; i<n; i++){
   element_clear(table[i]);
 }
 free(table);
}

    */

}