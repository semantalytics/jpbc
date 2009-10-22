package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong;

import it.unisa.dia.gas.plaf.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.engines.UTMAStrongEngine;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.engines.UTMAStrongRandomizer;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators.UTMAStrongKeyPairGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators.UTMAStrongParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.params.ElGamalParameters;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongEngineTest extends TestCase {

    
    public void testEngineEncryptDecrypt() {
        // Generate public info

        UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator();
        utmaStrongParametersGenerator.init(getCurveParamas(), getElGamalParameters());
        UTMAStrongParameters utmaParameters =  utmaStrongParametersGenerator.generateParameters();

        // Generate Key-Pair

        UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
        utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        try {
            AsymmetricBlockCipher strongEngine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAStrongEngine(),
                    new PKCS7Padding()
            );

            // Encrypt

            strongEngine.init(true, keyPair.getPublic());
            byte[] cipherText = strongEngine.processBlock(messageAsBytes, 0, messageAsBytes.length);

            // Decrypt

            strongEngine.init(false, keyPair.getPrivate());
            byte[] plainText = strongEngine.processBlock(cipherText, 0, cipherText.length);

            assertEquals(message, new String(plainText));
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testEngineEncryptRandomizeDecrypt() {
        // Generate public info

        UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator();
        utmaStrongParametersGenerator.init(getCurveParamas(), getElGamalParameters());
        UTMAStrongParameters utmaParameters =  utmaStrongParametersGenerator.generateParameters();

        // Generate Key-Pair

        UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
        utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        try {
            AsymmetricBlockCipher strongEngine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAStrongEngine(),
                    new PKCS7Padding()
            );

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
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testEngineEncRandDecPubInfoFromFile() {
        // Load public info

        UTMAStrongParameters utmaParameters = null;
        try {
            UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator();
            utmaParameters = utmaStrongParametersGenerator.load(
                    this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/rfid/utma/strong/utmas.params")
            );
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        // Generate Key-Pair

        UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
        utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        try {
            AsymmetricBlockCipher strongEngine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAStrongEngine(),
                    new PKCS7Padding()
            );

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
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testEngineEncRandDecKeyPairFromFile() {
        // Generate Key-Pair

        AsymmetricCipherKeyPair keyPair = null;
        try {
            UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
            keyPair = utmaStrongKeyPairGenerator.load(
                    this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/rfid/utma/strong/utmas_keypair.params")
            );
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        try {
            AsymmetricBlockCipher strongEngine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAStrongEngine(),
                    new PKCS7Padding()
            );

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
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
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
        // Generate Elgamal Parameters
/*        ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        elGamalParametersGenerator.init(1024, 12, new SecureRandom());
        ElGamalParameters elGamalParameters = elGamalParametersGenerator.generateParameters();

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
*/
        
        // Generate and Store UTMA Strong Parameters

        try {
            // Load parameters, the curve and elgamal params
            CurveParams curveParams = new CurveParams();
            InputStream inputStream = UTMAStrongEngineTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties");
            curveParams.load(inputStream);
            inputStream.close();

            inputStream = UTMAStrongEngineTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/elgamal_1024.params");
            ObjectInputStream ooi = new ObjectInputStream(inputStream);
            BigInteger g = (BigInteger) ooi.readObject();
            BigInteger p = (BigInteger) ooi.readObject();
            Integer l = (Integer) ooi.readObject();
            ooi.close();

            // Generate and store the public info
            UTMAStrongParametersGenerator utmaStrongParametersGenerator = new UTMAStrongParametersGenerator();
            utmaStrongParametersGenerator.init(curveParams, new ElGamalParameters(p, g, l));
            UTMAStrongParameters utmaParameters =  utmaStrongParametersGenerator.generateParameters();

            FileOutputStream fileOutputStream = new FileOutputStream("utmas.params");
            utmaStrongParametersGenerator.store(fileOutputStream, utmaParameters);
            fileOutputStream.flush();
            fileOutputStream.close();

            // Generate and store a keypair
            UTMAStrongKeyPairGenerator utmaStrongKeyPairGenerator = new UTMAStrongKeyPairGenerator();
            utmaStrongKeyPairGenerator.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), utmaParameters));

//            System.out.println("STORE");

            AsymmetricCipherKeyPair keyPair = utmaStrongKeyPairGenerator.generateKeyPair();
            fileOutputStream = new FileOutputStream("utmas_keypair.params");
            utmaStrongKeyPairGenerator.store(fileOutputStream, keyPair);
            fileOutputStream.flush();
            fileOutputStream.close();

//            System.out.println("READ");

            inputStream = new FileInputStream("utmas_keypair.params");
            keyPair = utmaStrongKeyPairGenerator.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}