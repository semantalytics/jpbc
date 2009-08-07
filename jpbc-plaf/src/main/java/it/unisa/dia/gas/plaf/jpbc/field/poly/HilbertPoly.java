package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.plaf.jpbc.util.BigDecimalUtils;
import it.unisa.dia.gas.plaf.jpbc.util.Complex;

import static java.lang.Math.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.LinkedList;
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
        int B = (int) floor(sqrt((double) D / 3.0));
        int a = 0, b = D % 2, t = 0, d = 0, h = 1, jcount = 1;

        // Compute required precision.
        boolean step1 = true, step2 = true;
        for (; ;) {
            if (step1) {
                t = (b * b + D) / 4;
                a = b;

                if (a <= 1) {
                    a = 1;
                    step2 = false;
                }
            }
            step1 = true;

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

            a++;
            if (a * a <= t) {
                step1 = false;
                continue;
            } else {
                b += 2;
                if (b > B)
                    break;
            }

        }

        System.out.printf("modulus: %f\n", exp(3.14159265358979 * sqrt(D)) * d * 0.5);
        d *= sqrt(D) * 3.14159265358979 / log(2);
        precision_init(d + 34);
        System.out.printf("class number %d, %d bit precision", h, (int) d + 34);


        BigDecimal sqrtD = new BigDecimal(sqrt(D));
        b = D % 2;
        h = 0;
        List<Complex> Pz = new LinkedList<Complex>();

        BigDecimal f0;

        int step = 0;
        step1 = true;
        boolean running = true;

        while (running) {
            if (step1) {
                t = (b * b + D) / 4;
                if (b > 1) {
                    a = b;
                } else {
                    a = 1;
                }
            }
            step1 = false;

            if (step == 0 && t % a == 0)
                step = 2;

            switch (step) {
                case 0:
                    while (t % a != 0) {
                        a++;
                        if (a * a > t) {
                            step = 3;
                            break;
                        }
                    }
                    break;

                case 1:
                    do {
                        a++;
                        if (a * a > t) {
                            step = 3;
                            break;
                        }
                    } while (t % a != 0);
                    break;

                case 2:
                    // a, b, t/a are coeffs of an appropriate primitive reduced positive
                    // definite form.
                    // Compute j((-b + sqrt{-D})/(2a)).
                    h++;
                    System.out.printf("[%d/%d] a b c = %d %d %d", h, jcount, a, b, t / a);
                    f0 = BigDecimal.ONE;
                    f0 = f0.divide(BigDecimal.valueOf(2 * a));
                    Complex alpha = new Complex();
                    alpha.setIm(sqrtD.multiply(f0));
                    f0 = f0.multiply(BigDecimal.valueOf(b));
                    alpha.setRe(f0.negate());

                    Complex j = new Complex();
                    compute_j(j, alpha);

                    /*
                                    if (0) {
                                        int i;
                                        for (i = Pz - > count - 1; i >= 0; i--) {
                                            printf("P %d = ", i);
                                            mpc_out_str(stdout, 10, 4, Pz - > item[i]);
                                            printf("\n");
                                        }
                                    }
                    */
                    if (a == b || a * a == t || b == 0) {
                        // P *= X - j
                        int i, n;
                        Complex p0 = new Complex(j);
                        p0.negate();

                        n = Pz.size();
                        if (n != 0) {
                            Complex z1 = new Complex(Pz.get(0));
                            Pz.get(0).set(z1).add(p0);

                            Complex z0 = new Complex();
                            for (i = 1; i < n; i++) {
                                z0.set(z1).mul(p0);
                                z1.set(Pz.get(i));
                                Pz.get(i).add(z0);
                            }
                            p0.mul(z1);
                        }
                        Pz.add(p0);
                    } else {
                        // P *= X^2 - 2 Re(j) X + |j|^2
                        int i, n;

                        Complex p0 = new Complex(), p1 = new Complex();

                        // p1 = - 2 Re(j)
                        f0 = j.getRe().multiply(BigDecimal.valueOf(2)).negate();
                        p1.setRe(f0);

                        // p0 = |j|^2
                        f0 = j.getRe().multiply(j.getRe());
                        p0.setRe(j.getIm().multiply(j.getIm()));
                        p0.setRe(p0.getRe().add(f0));

                        n = Pz.size();
                        if (n == 0) {
                        } else if (n == 1) {
                            Complex z1 = new Complex(Pz.get(0));
                            Pz.get(0).add(p1);
                            p1.mul(z1);
                            p1.add(p0);
                            p0.mul(z1);
                        } else {
                            Complex z2 = new Complex(Pz.get(0));
                            Complex z1 = new Complex(Pz.get(1));

                            Pz.get(0).set(z2).add(p1);
                            Complex z0 = new Complex(z2).mul(p1);
                            Pz.get(1).set(z1).add(z0).add(p0);

                            for (i = 2; i < n; i++) {
                                z0.set(z1).mul(p1);
                                alpha.set(z2).mul(p0);
                                z2.set(z1);
                                z1.set(Pz.get(i));
                                alpha.add(z0);
                                Pz.get(i).set(z1).add(alpha);
                            }
                            z0.set(z2).mul(p0);
                            p1.mul(z1);
                            p1.add(z0);
                            p0.mul(z1);
                        }
                        Pz.add(p1);
                        Pz.add(p0);
                    }
                    step = 1;
                    break;

                case 3:
                    b += 2;
                    if (b > B)
                        running = false;
            }

        }
/*
        // Round polynomial and assign.
        int k = 0;
        {
            *arr = pbc_malloc(sizeof(mpz_t) * (Pz - > count + 1));
            int i;

            for (i = Pz.size()- 1; i >= 0; i--) {
                if (Pz.get(i).getRe().signum() < 0)
                    f0 = new BigDecimal(-0.5);
                else
                    f0 = new BigDecimal(0.5);

                f0 = f0.add(Pz.get(i).getRe());


                mpf_add(f0, f0, mpc_re(Pz - > item[i]));
                mpz_init(( * arr)[k]);
                mpz_set_f(( * arr)[k], f0);
                k++;
            }

            mpz_init(( * arr)[k]);
            mpz_set_ui(( * arr)[k], 1);
            k++;
        }

        precision_clear();
        return k;
  */
        /*       int a, b;
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

// Compute required precision.
b = D % 2;
for (;;) {
  t = (b*b + D) / 4;
  a = b;
  if (a <= 1) {
    a = 1;
    goto step535_4;
  }
step535_3:
  if (!(t % a)) {
    jcount++;
    if ((a == b) || (a*a == t) || !b) {
      d += 1.0 / ((double) a);
      h++;
    } else {
      d += 2.0 / ((double) a);
      h+=2;
    }
  }
step535_4:
  a++;
  if (a * a <= t) {
    goto step535_3;
  } else {
    b += 2;
    if (b > B) break;
  }
}

//printf("modulus: %f\n", exp(3.14159265358979 * sqrt(D)) * d * 0.5);
d *= sqrt(D) * 3.14159265358979 / log(2);
precision_init(d + 34);
pbc_info("class number %d, %d bit precision", h, (int) d + 34);

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
for (;;) {
  t = (b*b + D) / 4;
  if (b > 1) {
    a = b;
  } else {
    a = 1;
  }
step3:
  if (t % a) {
step4:
    a++;
    if (a * a <= t) goto step3;
  } else {
    // a, b, t/a are coeffs of an appropriate primitive reduced positive
    // definite form.
    // Compute j((-b + sqrt{-D})/(2a)).
    h++;
    pbc_info("[%d/%d] a b c = %d %d %d", h, jcount, a, b, t/a);
    mpf_set_ui(f0, 1);
    mpf_div_ui(f0, f0, 2 * a);
    mpf_mul(mpc_im(alpha), sqrtD, f0);
    mpf_mul_ui(f0, f0, b);
    mpf_neg(mpc_re(alpha), f0);

    compute_j(j, alpha);
if (0) {
int i;
for (i=Pz->count - 1; i>=0; i--) {
  printf("P %d = ", i);
  mpc_out_str(stdout, 10, 4, Pz->item[i]);
  printf("\n");
}
}
    if (a == b || a * a == t || !b) {
      // P *= X - j
      int i, n;
      mpc_ptr p0;
      p0 = (mpc_ptr) pbc_malloc(sizeof(mpc_t));
      mpc_init(p0);
      mpc_neg(p0, j);
      n = Pz->count;
      if (n) {
        mpc_set(z1, Pz->item[0]);
        mpc_add(Pz->item[0], z1, p0);
        for (i=1; i<n; i++) {
          mpc_mul(z0, z1, p0);
          mpc_set(z1, Pz->item[i]);
          mpc_add(Pz->item[i], z1, z0);
        }
        mpc_mul(p0, p0, z1);
      }
      darray_append(Pz, p0);
    } else {
      // P *= X^2 - 2 Re(j) X + |j|^2
      int i, n;
      mpc_ptr p0, p1;
      p0 = (mpc_ptr) pbc_malloc(sizeof(mpc_t));
      p1 = (mpc_ptr) pbc_malloc(sizeof(mpc_t));
      mpc_init(p0);
      mpc_init(p1);
      // p1 = - 2 Re(j)
      mpf_mul_ui(f0, mpc_re(j), 2);
      mpf_neg(f0, f0);
      mpf_set(mpc_re(p1), f0);
      // p0 = |j|^2
      mpf_mul(f0, mpc_re(j), mpc_re(j));
      mpf_mul(mpc_re(p0), mpc_im(j), mpc_im(j));
      mpf_add(mpc_re(p0), mpc_re(p0), f0);
      n = Pz->count;
      if (!n) {
      } else if (n == 1) {
        mpc_set(z1, Pz->item[0]);
        mpc_add(Pz->item[0], z1, p1);
        mpc_mul(p1, z1, p1);
        mpc_add(p1, p1, p0);
        mpc_mul(p0, p0, z1);
      } else {
        mpc_set(z2, Pz->item[0]);
        mpc_set(z1, Pz->item[1]);
        mpc_add(Pz->item[0], z2, p1);
        mpc_mul(z0, z2, p1);
        mpc_add(Pz->item[1], z1, z0);
        mpc_add(Pz->item[1], Pz->item[1], p0);
        for (i=2; i<n; i++) {
          mpc_mul(z0, z1, p1);
          mpc_mul(alpha, z2, p0);
          mpc_set(z2, z1);
          mpc_set(z1, Pz->item[i]);
          mpc_add(alpha, alpha, z0);
          mpc_add(Pz->item[i], z1, alpha);
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
  b+=2;
  if (b > B) break;
}

// Round polynomial and assign.
int k = 0;
{
  *arr = pbc_malloc(sizeof(mpz_t) * (Pz->count + 1));
  int i;
  for (i=Pz->count - 1; i>=0; i--) {
    if (mpf_sgn(mpc_re(Pz->item[i])) < 0) {
      mpf_set_d(f0, -0.5);
    } else {
      mpf_set_d(f0, 0.5);
    }
    mpf_add(f0, f0, mpc_re(Pz->item[i]));
    mpz_init((*arr)[k]);
    mpz_set_f((*arr)[k], f0);
    k++;
    mpc_clear(Pz->item[i]);
    pbc_free(Pz->item[i]);
  }
  mpz_init((*arr)[k]);
  mpz_set_ui((*arr)[k], 1);
  k++;
}
darray_clear(Pz);
mpc_clear(z0);
mpc_clear(z1);
mpc_clear(z2);
mpf_clear(f0);
mpf_clear(sqrtD);
mpc_clear(alpha);
mpc_clear(j);

precision_clear();
return k;       */

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

    // Computes j = j(tau).
    protected void compute_j(Complex j, Complex tau) {
/*        mpc_t h;
        mpc_t z0;
        mpc_init(h);
        mpc_init(z0);
        compute_h(h, tau);
        //mpc_mul_ui(z0, h, 256);
        mpc_mul_2exp(z0, h, 8);
        mpc_add_ui(z0, z0, 1);
        mpc_pow_ui(z0, z0, 3);
        mpc_div(j, z0, h);
        mpc_clear(z0);
        mpc_clear(h);*/
    }

}
