package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong;

import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.engines.UTMAStrongEngine;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.engines.UTMAStrongRandomizer;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators.UTMAStrongKeyPairGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators.UTMAStrongParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongEngineTest extends TestCase {

    public void testEngineEncryptDecrypt() {
        // Generate public info

        UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator(new ElGamalKeyPairGenerator());
        utmaStrongParametersGenerator.init(getCurveParamas(),
                                           new ElGamalKeyGenerationParameters(
                                                   new SecureRandom(),
                                                   getElGamalParameters())
        );
        UTMAStrongParameters utmaParameters =  utmaStrongParametersGenerator.generateParameters();

        // Generate Key-Pair

        UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
        utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        UTMAStrongEngine strongEngine = new UTMAStrongEngine(new ElGamalEngine());

        // Encrypt

        strongEngine.init(true, keyPair.getPublic());
        byte[] cipherText = strongEngine.processBlock(messageAsBytes, 0, messageAsBytes.length);

        // Decrypt

        strongEngine.init(false, keyPair.getPrivate());
        byte[] plainText = strongEngine.processBlock(cipherText, 0, cipherText.length);

        assertEquals(message, new String(plainText).trim());
    }

    public void testEngineEncryptRandomizeDecrypt() {
        // Generate public info

        UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator(new ElGamalKeyPairGenerator());
        utmaStrongParametersGenerator.init(getCurveParamas(),
                                           new ElGamalKeyGenerationParameters(
                                                   new SecureRandom(),
                                                   getElGamalParameters())
        );
        UTMAStrongParameters utmaParameters =  utmaStrongParametersGenerator.generateParameters();

        // Generate Key-Pair

        UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
        utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        UTMAStrongEngine strongEngine = new UTMAStrongEngine(new ElGamalEngine());

        // Encrypt
        strongEngine.init(true, keyPair.getPublic());
        byte[] cipherText = strongEngine.processBlock(messageAsBytes, 0, messageAsBytes.length);

        // Randomize
        UTMAStrongRandomizer randomizer = new UTMAStrongRandomizer(new ElGamalEngine());
        randomizer.init(keyPair.getPublic());
        for (int i = 0; i < 10; i++) {
            cipherText = randomizer.processBlock(cipherText, 0, cipherText.length);
        }

        // Decrypt
        strongEngine.init(false, keyPair.getPrivate());
        byte[] plainText = strongEngine.processBlock(cipherText, 0, cipherText.length);

        assertEquals(message, new String(plainText).trim());
    }

    

    protected CurveParams getCurveParamas() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));
        return curveParams;
    }

    protected ElGamalParameters getElGamalParameters() {
        try {
            ObjectInputStream ooi = new ObjectInputStream(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/elgamal_1024.params"));

            BigInteger g = (BigInteger) ooi.readObject();
            BigInteger p = (BigInteger) ooi.readObject();
            Integer l = (Integer) ooi.readObject();
            ooi.close();

            return new ElGamalParameters(p, g, l);            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        elGamalParametersGenerator.init(1024, 12, new SecureRandom());
        ElGamalParameters elGamalParameters = elGamalParametersGenerator.generateParameters();

        // Write to a file
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("elgamal_1024.params"));
            oos.writeObject(elGamalParameters.getG());
            oos.writeObject(elGamalParameters.getP());
            oos.writeObject(elGamalParameters.getL());
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}