package it.unisa.dia.gas.plaf.jpbc.element;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeAElementPowPreProcessingTest extends ElementPowPreProcessingTest {

   protected CurveParameters getCurveParameters() {
       return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");
   }

}