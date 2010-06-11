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

    long s1 = System.currentTimeMillis();
	CurveGenerator curveGenerator = new TypeACurveGenerator(rBits, qBits);
    Map<?, ?> curveParams = curveGenerator.generate();
    long e1 = System.currentTimeMillis();


    long s2 = System.currentTimeMillis();
    Pairing pairing = PairingFactory.getPairing((CurveParams) curveParams);
    long e2 = System.currentTimeMillis();

    
	Element g = pairing.getG1().newRandomElement().getImmutable();
	
	Element x = pairing.getZr().newRandomElement();
	
	Element pk = g.powZn(x); 
	
	byte[] hashcode = "jxiaobao@gmail.com".getBytes(); 
	Element h1 = pairing.getG1().newElement().setFromHash(hashcode, 0, hashcode.length);

	Element sig = h1.powZn(x);  

	byte[] hashcode1 = "jxiaobao@gmail.com".getBytes();
	Element h2 = pairing.getG1().newElement().setFromHash(hashcode1, 0, hashcode1.length);
	
    long s3 = System.currentTimeMillis();
	Element temp1 = pairing.pairing(sig,g);
    long e3 = System.currentTimeMillis();
	Element temp2 = pairing.pairing(h2, pk); 


	if(temp1.isEqual(temp2)){
		return "The signature is valid." + (e1-s1) + " - " + (e2-s2) + " - " + (e3-s3);
		}
	else{
		return "The signature is NOT valid." + (e1-s1) + " - " + (e2-s2) + " - " + (e3-s3);
		} 
}
}