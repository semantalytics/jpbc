package it.unisa.dia.gas.plaf.jpbc.util;

import java.math.BigDecimal;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Complex {
    protected BigDecimal re, im;


    public Complex() {
    }

    public Complex(Complex complex) {
        this.re = complex.re;
        this.im = complex.im;
    }


    public BigDecimal getRe() {
        return re;
    }

    public void setRe(BigDecimal re) {
        this.re = re;
    }

    public BigDecimal getIm() {
        return im;
    }

    public void setIm(BigDecimal im) {
        this.im = im;
    }

    public Complex negate() {
        re = re.negate();
        im = im.negate();

        return this;
    }

    public Complex set(Complex complex) {
        this.re = complex.re;
        this.im = complex.im;

        return this;
    }

    public Complex add(Complex value) {
        this.re = this.re.add(value.re);
        this.im = this.im.add(value.im);

        return this;
    }

    public Complex mul(Complex value) {
        throw new IllegalStateException("Not implemented yet!!!");
    }
}
