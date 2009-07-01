package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.plaf.jpbc.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HilbertPoly {
    protected int D;

    protected BigDecimal pi, eulere, recipeulere, epsilon, negepsilon;
    protected static List fact;


    public HilbertPoly(int D) {
        this.D = D;
    }


    /**
     * returns darray of mpz's that are coefficients of H_D(x)
     * (see Cohen: note my D is -D in his notation)
     *
     * @return
     */
    public List<BigInteger> getHilbertPoly() {
/*        int a;
        int t;
        int B = floor(sqrt((double) D / 3.0));
        mpc_t alpha;
        mpc_t j;
        mpf_t sqrtD;
        mpf_t f0;
        darray_t Pz;
        mpc_t z0, z1, z2;
        double d = 1.0;
        int h = 1;
        int jcount = 1;

        //first compute required precision
        int b = D % 2;

        boolean step1 = true, step2 = true;

        while (true) {
            if (step1) {
                t = (b * b + D) / 4;
                a = b;

                if (a <= 1) {
                    a = 1;
                    step2 = false; //goto step535_4;
                }
            }
            step1 = true;

//            step535_3:

            if (step2) {
                if (t % a == 0) {
                    jcount++;
                    if ((a == b) || (a * a == t) || b == 0) {
                        d += 1.0 / ((double) a);
                        h++;
                    } else {
                        d += 2.0 / ((double) a);
                        h += 2;
                    }
                }
            }
            step2 = true;

//            step535_4:

            a++;
            if (a * a <= t) {
//                    goto step535_3;
                step1 = false;
            } else {
                b += 2;
                if (b > B) break;
            }
        }

        //printf("modulus: %f\n", exp(3.14159265358979 * sqrt(D)) * d * 0.5);
        d *= Math.sqrt(D) * 3.14159265358979 / Math.log(2);
        precision_init(d + 34);
        fprintf(stderr, "class number %d, %d bit precision\n", h, (int) d + 34);

        darray_init(Pz);
        mpc_init(alpha);
        mpc_init(j);
        mpc_init(z0);
        mpc_init(z1);
        mpc_init(z2);
        mpf_init(sqrtD);
        mpf_init(f0);

        mpf_sqrt_ui(sqrtD, D);
        b = D % 2;
        h = 0;
        for (; ;) {
            t = (b * b + D) / 4;
            if (b > 1) {
                a = b;
            } else {
                a = 1;
            }
            step3:
            if (t % a) {
                step4:
                a++;
                if (a * a <= t)
                    goto step3;
            } else {
                //a, b, t/a are coeffs of an appropriate
                //primitive reduced positive definite form
                //compute j((-b + sqrt{-D})/(2a))
                h++;
                fprintf(stderr, "[%d/%d] a b c = %d %d %d\n", h, jcount, a, b, t / a);
                mpf_set_ui(f0, 1);
                mpf_div_ui(f0, f0, 2 * a);
                mpf_mul(mpc_im(alpha), sqrtD, f0);
                mpf_mul_ui(f0, f0, b);
                mpf_neg(mpc_re(alpha), f0);

                compute_j(j, alpha);
                if (0) {
                    int i;
                    for (i = Pz - > count - 1; i >= 0; i--) {
                        printf("P %d = ", i);
                        mpc_out_str(stdout, 10, 4, Pz - > item[i]);
                        printf("\n");
                    }
                }
                if (a == b || a * a == t || !b) {
                    //P *= X - j
                    int i, n;
                    mpc_ptr p0;
                    p0 = (mpc_ptr) pbc_malloc(sizeof(mpc_t));
                    mpc_init(p0);
                    mpc_neg(p0, j);
                    n = Pz - > count;
                    if (n) {
                        mpc_set(z1, Pz - > item[0]);
                        mpc_add(Pz - > item[0], z1, p0);
                        for (i = 1; i < n; i++) {
                            mpc_mul(z0, z1, p0);
                            mpc_set(z1, Pz - > item[i]);
                            mpc_add(Pz - > item[i], z1, z0);
                        }
                        mpc_mul(p0, p0, z1);
                    }
                    darray_append(Pz, p0);
                } else {
                    //P *= X^2 - 2 Re(j) X + |j|^2
                    int i, n;
                    mpc_ptr p0, p1;
                    p0 = (mpc_ptr) pbc_malloc(sizeof(mpc_t));
                    p1 = (mpc_ptr) pbc_malloc(sizeof(mpc_t));
                    mpc_init(p0);
                    mpc_init(p1);
                    //p1 = - 2 Re(j)
                    mpf_mul_ui(f0, mpc_re(j), 2);
                    mpf_neg(f0, f0);
                    mpf_set(mpc_re(p1), f0);
                    //p0 = |j|^2
                    mpf_mul(f0, mpc_re(j), mpc_re(j));
                    mpf_mul(mpc_re(p0), mpc_im(j), mpc_im(j));
                    mpf_add(mpc_re(p0), mpc_re(p0), f0);
                    n = Pz - > count;
                    if (!n) {
                    } else if (n == 1) {
                        mpc_set(z1, Pz - > item[0]);
                        mpc_add(Pz - > item[0], z1, p1);
                        mpc_mul(p1, z1, p1);
                        mpc_add(p1, p1, p0);
                        mpc_mul(p0, p0, z1);
                    } else {
                        mpc_set(z2, Pz - > item[0]);
                        mpc_set(z1, Pz - > item[1]);
                        mpc_add(Pz - > item[0], z2, p1);
                        mpc_mul(z0, z2, p1);
                        mpc_add(Pz - > item[1], z1, z0);
                        mpc_add(Pz - > item[1], Pz - > item[1], p0);
                        for (i = 2; i < n; i++) {
                            mpc_mul(z0, z1, p1);
                            mpc_mul(alpha, z2, p0);
                            mpc_set(z2, z1);
                            mpc_set(z1, Pz - > item[i]);
                            mpc_add(alpha, alpha, z0);
                            mpc_add(Pz - > item[i], z1, alpha);
                        }
                        mpc_mul(z0, z2, p0);
                        mpc_mul(p1, p1, z1);
                        mpc_add(p1, p1, z0);
                        mpc_mul(p0, p0, z1);
                    }
                    darray_append(Pz, p1);
                    darray_append(Pz, p0);
                }
                goto step4;
            }
            b += 2;
            if (b > B) break;
        }

        //round polynomial
        {
            int i;
            mpz_ptr coeff;
            for (i = Pz - > count - 1; i >= 0; i--) {
                coeff = (mpz_ptr) pbc_malloc(sizeof(mpz_t));
                mpz_init(coeff);
                if (mpf_sgn(mpc_re(Pz - > item[i])) < 0) {
                    mpf_set_d(f0, -0.5);
                } else {
                    mpf_set_d(f0, 0.5);
                }
                mpf_add(f0, f0, mpc_re(Pz - > item[i]));
                mpz_set_f(coeff, f0);
                darray_append(P, coeff);
                mpc_clear(Pz - > item[i]);
                pbc_free(Pz - > item[i]);
            }
            coeff = (mpz_ptr) pbc_malloc(sizeof(mpz_t));
            mpz_init(coeff);
            mpz_set_ui(coeff, 1);
            darray_append(P, coeff);
        }
        darray_clear(Pz);
        mpc_clear(z0);
        mpc_clear(z1);
        mpc_clear(z2);
        mpf_clear(f0);
        mpf_clear(sqrtD);
        mpc_clear(alpha);
        mpc_clear(j);

        precision_clear();*/
        throw new IllegalStateException("Not Implemente yet!!!");
    }


    protected void precision_init(int prec) {
        int i;
        BigDecimal f0;

        MathContext precisionMathContext = new MathContext(prec);

        MathContext precisionTwoMathContext = new MathContext(2);
        epsilon = new BigDecimal(1, precisionTwoMathContext);
        epsilon = epsilon.divide(BigDecimalUtils.TWO, precisionMathContext);
        negepsilon = epsilon.negate();

        negepsilon = new BigDecimal(0, precisionTwoMathContext);

        recipeulere = new BigDecimal(0, precisionMathContext);
        pi = new BigDecimal(0, precisionMathContext);
        eulere = new BigDecimal(1, precisionMathContext);
        f0 = new BigDecimal(1, precisionMathContext);

        fact = new ArrayList();


        for (i = 1; ; i++) {
            f0 = f0.divide(BigDecimal.valueOf(i));

            if (f0.compareTo(epsilon) < 0) {
                break;
            }

            eulere = eulere.add(f0);
        }

        recipeulere = new BigDecimal(1, precisionMathContext).divide(eulere);

        pi = BigDecimalUtils.compute_pi(prec);
    }
}
