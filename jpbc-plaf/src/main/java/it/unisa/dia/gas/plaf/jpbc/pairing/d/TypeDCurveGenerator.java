package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.polymod.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.util.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDCurveGenerator implements CurveGenerator {

    protected int bitlimit;
    protected int discriminant;
    protected BigInteger D3;
    protected PellEquation pellEquation;


    public TypeDCurveGenerator(int discriminant) {
        this();
        setDiscriminant(discriminant);
    }

    public TypeDCurveGenerator() {
        this.bitlimit = 500;
    }



    public Map generate() {
        Map[] curves = findCurves();
        if (curves == null || curves.length == 0)
            throw new IllegalStateException("Cannot find valid curves. Try another discriminant.");

        return curves[0];
    }


    public int getDiscriminant() {
        return discriminant;
    }

    public void setDiscriminant(int discriminant) {
        BigInteger D3 = BigInteger.valueOf(discriminant * 3);
        if (BigIntegerUtils.isPerfectSquare(D3)) {
            //(the only squares that differ by 8 are 1 and 9,
            //which we get if U=V=1, D=3, but then l is not an integer)
            throw new IllegalArgumentException("Invalid discriminant. 3*D is a perfect square.");
        }

        this.discriminant = discriminant;
        this.D3 = D3;
        this.pellEquation = new PellEquation(D3, -8);
   }

    /**
     * Finds the next valid discriminant starting from the previous discriminant value.
     */
    public void nextDiscriminant() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    /**
     * Finds all the feasible curve for the current discriminant.
     * @return
     */
    public Map[] findCurves() {
        List<Map> curves = new ArrayList<Map>();


        Map params = new LinkedHashMap();

        BigInteger t0, t1, t2;

        int found_count = 0;

        pellEquation.solve();

        int n;
        n = pellEquation.count;
        if (n != 0) {
            boolean found = false;

            // Copy pell equation solution
            BigInteger[] x = pellEquation.x.clone();
            BigInteger[] y = pellEquation.y.clone();

            while (!found) {
                for (int i = 0; i < n; i++) {
                    //element_printf("%Zd, %Zd\n", ps->x[i], ps->y[i]);

                    if (mnt_step2(params, x[i]) == 0)
                        found_count++;

                    // compute next solution as follows
                    // if p, q is current solution
                    // compute new solution p', q' via
                    // (p + q sqrt{3D})(t + u sqrt{3D}) = p' + q' sqrt(3D)
                    // where t, u is min. solution to Pell equation

                    t0 = pellEquation.minx.multiply(y[i]);
                    t1 = pellEquation.miny.multiply(x[i]);
                    t1 = t1.multiply(D3);
                    t0 = t0.add(t1);

                    if (2 * t0.bitLength() > bitlimit + 10) {
                        found = true;
                        break;
                    }

                    t2 = pellEquation.minx.multiply(y[i]);
                    t1 = pellEquation.miny.multiply(x[i]);
                    t2 = t2.add(t1);

                    x[i] = t0;
                    y[i] = t2;
                }
            }
        }

        return curves.toArray(new Map[curves.size()]);
    }



    protected int mnt_step2(Map params, BigInteger U) {
        int d;
        BigInteger n, l, q;
        BigInteger p;
        BigInteger r, cofac;

        l = U.mod(BigIntegerUtils.SIX);
        if (l.compareTo(BigInteger.ONE) == 0) {
            l = U.subtract(BigInteger.ONE);
            d = 1;
        } else if (l.compareTo(BigIntegerUtils.FIVE) == 0) {
            l = U.add(BigInteger.ONE);
            d = -1;
        } else {
            return 1;
        }

        l = l.divide(BigIntegerUtils.THREE);

        q = l.multiply(l);
        q = q.add(BigInteger.ONE);

        if (!q.isProbablePrime(10)) {
            return 1;
        }

        if (d < 0) {
            n = q.subtract(l);
        } else {
            n = q.add(l);
        }


        {
            cofac = BigInteger.ONE;
            r = n;
            p = BigIntegerUtils.TWO;

            if (!r.isProbablePrime(10)) {
                for (; ;) {
                    if (BigIntegerUtils.isDivisible(r, p)) {
                        do {
                            cofac = cofac.multiply(p);
                            r = r.divide(p);
                        } while (BigIntegerUtils.isDivisible(r, p));
                    }

                    if (r.isProbablePrime(10))
                        break;

                    //TODO: use a table of primes instead?
                    p = p.nextProbablePrime();
                    if (p.bitLength() > 16) {
                        // printf("has 16+ bit factor\n");
                        return 1;
                    }
                }
            }
        }

        params.put("k", 6);
        params.put("D", discriminant);
        params.put("q", q);
        params.put("r", r);
        params.put("h", cofac);
        params.put("n", n);

        return 0;
    }

    protected void d_param_from_cm(Map param) {
        Field<? extends Element> Fq;
        PolyModField Fqd;
        PolyField Fqx;
        PolyElement irred;
        PolyModElement nqr;
        int d = 3;
        int i;

//        compute_cm_curve(param, cm);

        Fq = new NaiveField((BigInteger) param.get("q"));
        Fqx = new PolyField<Field>(Fq);

        irred = Fqx.newElement();
        do {
            irred.setToRandomMonic(3);
        } while (!irred.isIrriducible());

        Fqd = new PolyModField<Field>(irred, null);

        //find a quadratic nonresidue of Fqd lying in Fq
        nqr = Fqd.newElement();
        do {
            nqr.getCoefficient(0).setToRandom();
        } while (nqr.isSqr());

        for (i = 0; i < d; i++) {
            param.put("coeff" + i, irred.getCoefficient(i).toBigInteger().toString());
        }
        param.put("nqr", nqr.getCoefficient(0).toBigInteger());
    }

    /**
     * computes a curve and sets fp to the field it is defined over
     * using the complex multiplication method, where cm holds
     * the appropriate information (e.g. discriminant, field order)
     *
     * @param param
     */
    protected void compute_cm_curve(Map param) {
/*        List coefflist;
        Element root;
        PolyElement hp;
        Field<? extends Element> fp;
        PolyField fpx;
        int i, n;
        Field<? extends Element> cc;

        fp = new NaiveField((BigInteger) param.get("q"));
        fpx = new PolyField(fp);


        hp = fpx.newElement();

        darray_init(coefflist);

        hilbert_poly(coefflist, cm - > D);

        n = coefflist - > count;
        poly_alloc(hp, n);
        for (i = 0; i < n; i++) {
            element_set_mpz(poly_coeff(hp, i), coefflist - > item[i]);
        }

        hilbert_poly_clear(coefflist);

        darray_clear(coefflist);
        //TODO: remove x = 0, 1728 roots
        //TODO: what if there's no roots?
        //printf("hp ");
        //element_out_str(stdout, 0, hp);
        //printf("\n");

        element_init(root, fp);
        findroot(root, hp);
        //printf("root = ");
        //element_out_str(stdout, 0, root);
        //printf("\n");
        element_clear(hp);
        field_clear(fpx);

        //the root is the j-invariant of our desired curve
        field_init_curve_j(cc, root, cm - > n, NULL);
        element_clear(root);

        //we may need to twist it however
        {
            element_t P;

            //pick a random point P and see if it has the right order
            element_init(P, cc);
            element_random(P);
            element_mul_mpz(P, P, cm - > n);
            //element_printf("P = %B", P);
            //if not, we twist the curve
            if (!element_is0(P)) {
                twist_curve(cc);
            }
            element_clear(P);
        }

        mpz_set(param - > q, cm - > q);
        mpz_set(param - > n, cm - > n);
        mpz_set(param - > h, cm - > h);
        mpz_set(param - > r, cm - > r);
        element_to_mpz(param - > a, curve_field_a_coeff(cc));
        element_to_mpz(param - > b, curve_field_b_coeff(cc));
        param - > k = cm - > k;
        {
            mpz_t z;
            mpz_init(z);
            //compute order of curve in F_q^k
            //n = q - t + 1 hence t = q - n + 1
            mpz_sub(z, param - > q, param - > n);
            mpz_add_ui(z, z, 1);
            compute_trace_n(z, param - > q, z, param - > k);
            mpz_pow_ui(param - > nk, param - > q, param - > k);
            mpz_sub_ui(z, z, 1);
            mpz_sub(param - > nk, param - > nk, z);
            mpz_mul(z, param - > r, param - > r);
            mpz_divexact(param - > hk, param - > nk, z);
            mpz_clear(z);
        }
        field_clear(cc);
        field_clear(fp);*/
    }


    /**
     * solves x^2 - d y^2 = n
     * D not a square
     * (for square D, observe (x+Dy)(x-Dy) = N and look at factors of N)
     */
    public class PellEquation {
        protected BigInteger D;
        protected int N;

        protected int count;
        protected BigInteger minx, miny;
        protected BigInteger[] x, y;


        public PellEquation(BigInteger D, int n) {
            this.D = D;
            this.N = n;
        }


        public void solve() {
            //TODO: brute force for small D
            List L;
            int i, f, n, sgnN = N > 0 ? 1 : -1;

            //find square factors of N
            List<Integer> listf = new ArrayList<Integer>();

            f = 1;
            while (true) {
                n = f * f;
                if (n > Math.abs(N))
                    break;

                if (Math.abs(N) % n == 0) {
                    listf.add(f);
                }
                f++;
            }

            //a0, twice_a0 don't change once initialized
            //a1 is a_i every iteration
            //P0, P1 become P_{i-1}, P_i every iteration
            //similarly for Q0, Q1
            BigInteger a0, twice_a0, a1;
            BigInteger P0, P1;
            BigInteger Q0, Q1;
            //variables to compute the convergents
            BigInteger p0, p1, pnext;
            BigInteger q0, q1, qnext;

            int d;

            List<BigInteger> listp, listq;
            BigInteger zptr;

            L = new ArrayList();

            listp = new ArrayList<BigInteger>();
            listq = new ArrayList<BigInteger>();

            a0 = BigIntegerUtils.sqrt(D);
            P0 = BigInteger.ZERO;
            Q0 = BigInteger.ONE;

            P1 = a0;
            Q1 = a0.multiply(a0);
            Q1 = D.subtract(Q1);
            a1 = a0.add(P1);
            a1 = a1.divide(Q1);
            twice_a0 = a0.add(a0);

            p0 = a0;
            q0 = BigInteger.ONE;
            p1 = a0.multiply(a1);
            p1 = p1.add(BigInteger.ONE);
            q1 = a1;

            d = -1;
            for (; ;) {
                if (d == sgnN) {
                    for (i = 0; i < listf.size(); i++) {
                        f = listf.get(i);

                        if (Q1.compareTo(BigInteger.valueOf(Math.abs(N) / (f * f))) == 0) {
                            zptr = p0;
                            zptr = p0.multiply(BigInteger.valueOf(f));
                            listp.add(zptr);

                            zptr = q0;
                            zptr = q0.multiply(BigInteger.valueOf(f));
                            listq.add(zptr);
                        }
                    }
                }

                if (twice_a0.compareTo(a1) == 0 && d == 1)
                    break;

                //compute more of the continued fraction expansion
                P0 = P1;
                P1 = a1.multiply(Q1);
                P1 = P1.subtract(P0);
                Q0 = Q1;
                Q1 = P1.multiply(P1);
                Q1 = D.subtract(Q1);
                Q1 = Q1.divide(Q0);
                a1 = a0.add(P1);
                a1 = a1.divide(Q1);

                //compute next convergent
                pnext = a1.multiply(p1);
                pnext = pnext.add(p0);
                p0 = p1;
                p1 = pnext;

                qnext = a1.multiply(q1);
                qnext = qnext.add(q0);
                q0 = q1;
                q1 = qnext;

                d = -d;
            }
            listf.clear();

            minx = p0;
            miny = q0;
            count = listp.size();
            if (count != 0) {
                x = new BigInteger[n];
                y = new BigInteger[n];
                for (i = 0; i < n; i++) {
                    x[i] = listp.get(i);
                    y[i] = listq.get(i);
                }
            }
        }
    }

}
