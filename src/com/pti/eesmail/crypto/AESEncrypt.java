/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pti.eesmail.crypto;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Agustinus Agri
 */
public class AESEncrypt {

    private static final String ALGORITHM = "AES";
    private static final String startingKey = "TheBestKeysEverInTheHumanHistory";

    private static IvParameterSpec ivSpecial = null;

//    static
//    {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//    }
    public static String encrypt(String Data) throws Exception {
        Key key = generateKey(startingKey);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        String decryptedValue = null;
        Key key = generateKey(startingKey);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey(String keyToHash) throws Exception {
//        byte[] byteKey = get_SHA_1_SecurePassword(keyToHash, getSalt());
        byte[] byteKey = keyToHash.getBytes("UTF-8");
        byteKey = Arrays.copyOf(byteKey, 16);
        byte[] hashing = hashing(byteKey);
        hashing = Arrays.copyOf(hashing, 16);
        Key key = new SecretKeySpec(hashing, ALGORITHM);
        return key;
    }

    private static byte[] hashing(byte[] rawByte) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(rawByte);
        return md.digest();

    }

    /*
        Masih gagal implementasi salt.
    Error : given block not properly padded
    Penyebeb : salt
    */
    private static byte[] get_SHA_1_SecurePassword(String passwordToHash, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String generatedPassword = null;
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(salt.getBytes("UTF-8"));
            bytes = md.digest(passwordToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        
        String algorithm = sr.getAlgorithm();
        System.out.println(algorithm);
        
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

}
