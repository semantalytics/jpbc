package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08Parameters implements CipherParameters, Serializable {
    private CurveParams curveParams;
    private Element g;
    private int[] attributeLengths;
    private int n;

    private int length;


    public HVEIP08Parameters(CurveParams curveParams, Element g, int[] attributeLengths) {
        this.curveParams = curveParams;
        this.g = g;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);
        this.n = attributeLengths.length;

        this.length = 0;
        for (int attributeLength : attributeLengths) {
            length += attributeLength;
        }
    }

    public CurveParams getCurveParams() {
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