package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HIPLOSTW10Parameters extends IPLOSTW10Parameters {

    private int[] hierarchy;

    public HIPLOSTW10Parameters(CurveParameters curveParams, Element g, int n) {
        super(curveParams, g, n);
    }

}