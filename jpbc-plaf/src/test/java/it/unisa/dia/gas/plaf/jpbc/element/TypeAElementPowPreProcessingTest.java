package it.unisa.dia.gas.plaf.jpbc.element;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeAElementPowPreProcessingTest extends ElementPowPreProcessingTest {

   protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"));
        return curveParams;
    }

}