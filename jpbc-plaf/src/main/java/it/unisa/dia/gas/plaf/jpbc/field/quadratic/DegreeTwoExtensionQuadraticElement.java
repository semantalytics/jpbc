package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DegreeTwoExtensionQuadraticElement<E extends Element> extends QuadraticElement<E> {

    protected DegreeTwoExtensionQuadraticField field;
    protected Element e0, e1, e2;



    public DegreeTwoExtensionQuadraticElement(DegreeTwoExtensionQuadraticField field) {
        super(field);
        this.field = field;

        this.x = (E) field.getTargetField().newElement();
        this.y = (E) field.getTargetField().newElement();

        this.e0 = field.getTargetField().newElement();
        this.e1 = field.getTargetField().newElement();
        this.e2 = field.getTargetField().newElement();
    }

    public DegreeTwoExtensionQuadraticElement(DegreeTwoExtensionQuadraticElement element) {
        super(element.field);
        this.field = element.field;

        this.x = (E) element.x.duplicate();
        this.y = (E) element.y.duplicate();

        this.e0 = field.getTargetField().newElement();
        this.e1 = field.getTargetField().newElement();
        this.e2 = field.getTargetField().newElement();
    }


    public DegreeTwoExtensionQuadraticElement duplicate() {
        return new DegreeTwoExtensionQuadraticElement(this);
    }

    public DegreeTwoExtensionQuadraticElement square() {
        e0.set(x).add(y).mul(e1.set(x).sub(y));
        e1.set(x).mul(y).twice()/*add(e1)*/;

        x.set(e0);
        y.set(e1);

        return this;

        /*
        fq_data_ptr p = a->data;
        fq_data_ptr r = n->data;
        element_t e0, e1;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        //Re(n) = x^2 - y^2 = (x+y)(x-y)
        element_add(e0, p->x, p->y);
        element_sub(e1, p->x, p->y);
        element_mul(e0, e0, e1);
        //Im(n) = 2xy
        element_mul(e1, p->x, p->y);
        element_add(e1, e1, e1);
        element_set(r->x, e0);
        element_set(r->y, e1);
        element_clear(e0);
        element_clear(e1);
         */
    }

    public DegreeTwoExtensionQuadraticElement invert() {
        e0.set(x).square().add(e1.set(y).square()).invert();

        x.mul(e0);
        y.mul(e0.negate());

        return this;

        /*
        fq_data_ptr p = a->data;
        fq_data_ptr r = n->data;
        element_t e0, e1;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_square(e0, p->x);
        element_square(e1, p->y);
        element_add(e0, e0, e1);
        element_invert(e0, e0);
        element_mul(r->x, p->x, e0);
        element_neg(e0, e0);
        element_mul(r->y, p->y, e0);

        element_clear(e0);
        element_clear(e1);
        */
    }

    public DegreeTwoExtensionQuadraticElement mul(Element e) {
        DegreeTwoExtensionQuadraticElement element = (DegreeTwoExtensionQuadraticElement) e;

        e0.set(x).add(y);
        e1.set(element.x).add(element.y);
        e2.set(e0).mul(e1);

        e0.set(x).mul(element.x);
        e1.set(y).mul(element.y);
        e2.sub(e0);

        x.set(e0).sub(e1);
        y.set(e2).sub(e1);

        /*
        fq_data_ptr p = a->data;
        fq_data_ptr q = b->data;
        fq_data_ptr r = n->data;
        element_t e0, e1, e2;

        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_init(e2, e0->field);
        Naive way
        element_mul(e0, p->x, q->x);
        element_mul(e1, p->y, q->y);
        element_sub(e0, e0, e1);
        element_mul(e1, p->x, q->y);
        element_mul(e2, p->y, q->x);
        element_add(e1, e1, e2);
        element_set(r->x, e0);
        element_set(r->y, e1);

        //Karatsuba:
        element_add(e0, p->x, p->y);
        element_add(e1, q->x, q->y);
        element_mul(e2, e0, e1);
        element_mul(e0, p->x, q->x);
        element_sub(e2, e2, e0);
        element_mul(e1, p->y, q->y);
        element_sub(r->x, e0, e1);
        element_sub(r->y, e2, e1);

        element_clear(e0);
        element_clear(e1);
        element_clear(e2);
        */
        return this;
    }

    public boolean isSqr() {

        /*
        //x + yi is a square <=> x^2 + y^2 is (in the base field)

        // Proof: (=>) if x+yi = (a+bi)^2,
        // then a^2 - b^2 = x, 2ab = y,
        // thus (a^2 + b^2)^2 = (a^2 - b^2)^2 + (2ab)^2 =  x^2 + y^2
        // (<=) Suppose A^2 = x^2 + y^2
        // then if there exist a, b satisfying:
        //   a^2 = (+-A + x)/2, b^2 = (+-A - x)/2
        // then (a + bi)^2 = x + yi.
        // We show that exactly one of (A + x)/2, (-A + x)/2
        // is a quadratic residue (thus a, b do exist).
        // Suppose not. Then the product
        // (x^2 - A^2) / 4 is some quadratic residue, a contradiction
        // since this would imply x^2 - A^2 = -y^2 is also a quadratic residue,
        // but we know -1 is not a quadratic residue.
        fq_data_ptr p = e->data;
        element_t e0, e1;
        int result;
        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_square(e0, p->x);
        element_square(e1, p->y);
        element_add(e0, e0, e1);
        result = element_is_sqr(e0);
        element_clear(e0);
        element_clear(e1);
        return result;
        */
        
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public DegreeTwoExtensionQuadraticElement sqrt() {
        /*
        fq_data_ptr p = e->data;
        fq_data_ptr r = n->data;
        element_t e0, e1, e2;

        //if (a+bi)^2 = x+yi then
        //2a^2 = x +- sqrt(x^2 + y^2)
        //(take the sign such that a exists) and 2ab = y
        //[thus 2b^2 = - (x -+ sqrt(x^2 + y^2))]
        element_init(e0, p->x->field);
        element_init(e1, e0->field);
        element_init(e2, e0->field);
        element_square(e0, p->x);
        element_square(e1, p->y);
        element_add(e0, e0, e1);
        element_sqrt(e0, e0);
        //e0 = sqrt(x^2 + y^2)
        element_add(e1, p->x, e0);
        element_set_si(e2, 2);
        element_invert(e2, e2);
        element_mul(e1, e1, e2);
        //e1 = (x + sqrt(x^2 + y^2))/2
        if (!element_is_sqr(e1)) {
        element_sub(e1, e1, e0);
        //e1 should be a square
        }
        element_sqrt(e0, e1);
        element_add(e1, e0, e0);
        element_invert(e1, e1);
        element_mul(r->y, p->y, e1);
        element_set(r->x, e0);
        element_clear(e0);
        element_clear(e1);
        element_clear(e2);
        */
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public boolean isEqual(Element e) {
        if (e == this)
            return true;

        DegreeTwoExtensionQuadraticElement element = (DegreeTwoExtensionQuadraticElement) e;

        return x.isEqual(element.x) && y.isEqual(element.y);
    }

    public String toString() {
        return String.format("{x=%s,y=%s}", x, y);
    }

}
