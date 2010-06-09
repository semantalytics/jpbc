package it.unisa.dia.gas.jpbc;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: angelo
 * Date: Jun 7, 2010
 * Time: 1:19:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static String b() {

        try {
            int rBits = 160 / 4;
            int qBits = 512 / 4;
            CurveParams curveParams = new CurveParams();
            curveParams.load(Main.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"));

            //CurveGenerator curveGenerator = new TypeACurveGenerator(rBits, qBits);

            //Map<?, ?> curveParams = curveGenerator.generate();
            Pairing pairing = PairingFactory.getPairing((CurveParams) curveParams);
            //Pairing pairing = new PBCPairing((CurveParams) curveParams);

            String Message = "Hello World!";
            String address = "jxiaobao@gmail.com";

            Element Qid = pairing.getG1().newElement().setFromHash(address.getBytes(), 0, address.getBytes().length).getImmutable();

            Element s = pairing.getZr().newRandomElement();
            Element P = pairing.getG1().newRandomElement().getImmutable();
            Element Ppub = P.mulZn(s);        //Ppub = s*P
            Element Did = Qid.mulZn(s).getImmutable();  //Did = s*Qid

            Element r = pairing.getZr().newRandomElement();
            Element U = Qid.mulZn(r);       //U = r*Qid
            //
            MessageDigest sha1 = MessageDigest.getInstance("SHA");
            sha1.update(Message.getBytes());
            sha1.update(U.toBytes());
            byte[] hash1 = sha1.digest();

            Element h = pairing.getZr().newElement().setFromHash(hash1, 0, hash1.length);
            h.add(r);
            Element V = Did.mulZn(h);

            System.out.println("U = " + U);
            System.out.println("V = " + V);

            //sig = (U,V)
            //String Message2 = "Hello World!";
            MessageDigest sha2 = MessageDigest.getInstance("SHA");
            sha2.update(Message.getBytes());
            sha2.update(U.toBytes());
            byte[] hash2 = sha2.digest();
            Element h1 = pairing.getZr().newElement().setFromHash(hash2, 0, hash2.length);

            Element tmp = Qid.mulZn(h1).add(U); //tmp = h*Qid +U

            Element e1 = pairing.pairing(P, V);
            Element e2 = pairing.pairing(Ppub, tmp);

            System.out.println("e1 = " + e1);
            System.out.println("e2 = " + e2);

            if (e1.isEqual(e2)) {
                return "The signature is valid.";
            } else {
                return "The signature is NOT valid.";
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s\n", b());
        }
    }

}
