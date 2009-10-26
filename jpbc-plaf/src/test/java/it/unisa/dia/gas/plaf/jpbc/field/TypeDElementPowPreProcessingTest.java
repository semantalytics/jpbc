package it.unisa.dia.gas.plaf.jpbc.field;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDElementPowPreProcessingTest extends ElementPowPreProcessingTest {

   protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"));
        return curveParams;
    }

}