package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PolyUtils {


    public static PolyElement constMul(Element a, PolyElement poly) {
        int n = poly.getCoefficients().size();

        PolyElement res = poly.getField().newElement();
        res.ensureCoeffSize(n);

        for (int i = 0; i < n; i++) {
            res.getCoefficient(i).set(a).mul(poly.getCoefficient(i));
        }
        res.removeLeadingZeroes();

        return res;

        /*
        int i, n = poly_coeff_count(poly);
        poly_alloc(res, n);
        for (i=0; i<n; i++) {
        element_mul(poly_coeff(res, i), a, poly_coeff(poly, i));
        }
        poly_remove_leading_zeroes(res);
        */
    }

    public static void div(Element quot, Element rem, PolyElement a, PolyElement b) {
        if (b.isZero())
            throw new IllegalArgumentException("BUG! division by zero!");

        int n = b.getDegree();
        int m = a.getDegree();

        if (n > m) {
            rem.set(a);
            quot.setToZero();

            return;
        }

        PolyElement r = a.getField().newElement();
        PolyElement q = a.getField().newElement();
        Element binv = a.getField().getTargetField().newElement();
        Element e0 = a.getField().getTargetField().newElement();
        r.set(a);
        int k = m - n;
        q.ensureCoeffSize(k + 1);
        binv.set(b.getCoefficient(n)).invert();
        while (k >= 0) {
            Element qe = q.getCoefficient(k);
            qe.set(binv).mul(r.getCoefficient(m));
            for (int i = 0; i <= n; i++) {
                e0.set(qe).mul(b.getCoefficient(i));
                r.getCoefficient(i + k).sub(e0);
            }
            k--;
            m--;
        }
        r.removeLeadingZeroes();
        quot.set(q);
        rem.set(r);

        /*
       void poly_div(element_ptr quot, element_ptr rem, element_ptr a, element_ptr b) {
       poly_element_ptr pq, pr;
       poly_field_data_ptr pdp = a - > field - > data;
       element_t q, r;
       element_t binv, e0;
       element_ptr qe;
       int m, n;
       int i, k;

       if (element_is0(b)) {
           fprintf(stderr, "BUG! division by zero!\n");
           exit(1);
       }
       n = poly_degree(b);
       m = poly_degree(a);
       if (n > m) {
           element_set(rem, a);
           element_set0(quot);
           return;
       }
       element_init(r, a - > field);
       element_init(q, a - > field);
       element_init(binv, pdp - > field);
       element_init(e0, pdp - > field);
       pq = q - > data;
       pr = r - > data;
       element_set(r, a);
       k = m - n;
       poly_alloc(q, k + 1);
       element_invert(binv, poly_coeff(b, n));
       while (k >= 0) {
           qe = pq - > coeff - > item[k];
           element_mul(qe, binv, pr - > coeff - > item[m]);
           for (i = 0; i <= n; i++) {
               element_mul(e0, qe, poly_coeff(b, i));
               element_sub(pr - > coeff - > item[i + k], pr - > coeff - > item[i + k], e0);
           }
           k--;
           m--;
       }
       poly_remove_leading_zeroes(r);
       element_set(quot, q);
       element_set(rem, r);

       element_clear(q);
       element_clear(r);
       element_clear(e0);
       element_clear(binv);
        */
    }

}
