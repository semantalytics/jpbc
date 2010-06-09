package it.unisa.dia.gas.jpbc.android.jpbcset;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.util.Map;


public class BLS {
public String init(){
	
	int rBits = 160;
	int qBits = 512;
	CurveGenerator curveGenerator = new TypeACurveGenerator(rBits, qBits);
    curveGenerator.toString();
    //CurveParams curveParams;
    Map<?, ?> curveParams = curveGenerator.generate();
    Pairing pairing = PairingFactory.getPairing((CurveParams) curveParams);
	
    
	Element g = pairing.getG1().newRandomElement().getImmutable();
	
	Element x = pairing.getZr().newRandomElement();
	
	Element pk = g.powZn(x); 
	
	byte[] hashcode = "jxiaobao@gmail.com".getBytes(); 
	Element h1 = pairing.getG1().newElement().setFromHash(hashcode, 0, hashcode.length);

	Element sig = h1.powZn(x);  

	byte[] hashcode1 = "jxiaobao@gmail.com".getBytes();
	Element h2 = pairing.getG1().newElement().setFromHash(hashcode1, 0, hashcode1.length);
	
	Element temp1 = pairing.pairing(sig,g);   
	Element temp2 = pairing.pairing(h2, pk); 
	if(temp1.isEqual(temp2)){ 
		return "The signature is valid.";
		}
	else{
		return "The signature is NOT valid.";
		} 
}
}