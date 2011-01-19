package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08Parameters implements CipherParameters, Serializable {
    private CurveParameters curveParams;
    private Element g;
    private int[] attributeLengths;
    private int n;

    private int length;


    public HVEIP08Parameters(CurveParameters curveParams, Element g, int[] attributeLengths) {
        this.curveParams = curveParams;
        this.g = g;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);
        this.n = attributeLengths.length;

        this.length = 0;
        for (int attributeLength : attributeLengths) {
            length += attributeLength;
        }
    }

    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public int getN() {
        return n;
    }

    public int[] getAttributeLengths() {
        return attributeLengths;
    }

    public int getLength() {
        return length;
    }

}