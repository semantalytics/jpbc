package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import org.bouncycastle.crypto.CipherParameters;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08Parameters implements CipherParameters, Serializable {
    private CurveParameters curveParams;
    private Element g;
    private ElementPowPreProcessing powG;

    private int[] attributeLengths;
    private int[] attributeLengthsInBytes;
    private int[] attributeNums;
    private int n;

    private int attributesLengthInBytes;


    public HHVEIP08Parameters(CurveParameters curveParams, Element g, int[] attributeLengths) {
        this.curveParams = curveParams;
        this.g = g;
        this.powG = g.pow();
        this.n = attributeLengths.length;
        this.attributeLengths = Arrays.copyOf(attributeLengths, attributeLengths.length);

        this.attributesLengthInBytes = 0;
        this.attributeLengthsInBytes = new int[n];
        this.attributeNums = new int[n];
        for (int i = 0; i < attributeLengths.length; i++) {
            int attributeLength = attributeLengths[i];

            attributeLengthsInBytes[i] = (int) Math.ceil((Math.log(attributeLength)/Math.log( 2 )+1) / 8);
            attributesLengthInBytes += attributeLengthsInBytes[i];

            attributeNums[i] = (int) Math.pow(2, attributeLength);
        }
    }

    public CurveParameters getCurveParams() {
        return curveParams;
    }

    public Element getG() {
        return g;
    }

    public ElementPowPreProcessing getPowG() {
        return powG;
    }

    public int getN() {
        return n;
    }

    public int getAttributesLengthInBytes() {
        return attributesLengthInBytes;
    }

    public int getAttributeLengthInBytesAt(int index) {
        return attributeLengthsInBytes[index];
    }

    public int getAttributeNumAt(int index) {
        return attributeNums[index];
    }
}