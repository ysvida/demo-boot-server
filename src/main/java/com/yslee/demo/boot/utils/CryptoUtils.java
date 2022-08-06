package com.yslee.demo.boot.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtils {

//    private static final Logger logger = LoggerFactory.getLogger(CryptoUtils.class);

    //    private static final String SECRET_KEY = "mySecretKey01234";
    private static final String PAD_STR = "0";

//    private static Key keySpec;

//    static {
//        byte[] keyBytes = new byte[16]; // AES128
//        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//        int len = secretKeyBytes.length;
//        if (len > keyBytes.length) {
//            len = keyBytes.length;
//        }
//        System.arraycopy(secretKeyBytes, 0, keyBytes, 0, len);
//        keySpec = new SecretKeySpec(keyBytes, "AES");
//    }

    public static String decryptFromFront(String encMessage, String inputKey, String iv) {
        String result;

        try {

            byte[] message = Base64.decodeBase64(encMessage);
            Key keySpec = getKeySpec(inputKey);
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(message);
            result = new String(decrypted, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String encryptToFront(String message, String inputKey, String iv) {
        String result;

        try {

            Key keySpec = getKeySpec(inputKey);
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
//            result = Base64.encodeBase64URLSafeString(encryptedBytes);
            result = Base64.encodeBase64String(encryptedBytes);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return result;
    }

    private static Key getKeySpec(String inputKey) {
        String secretKey = StringUtils.defaultString(inputKey);
        int keyLen = secretKey.length();
        if (keyLen > 16) {
            secretKey = secretKey.substring(0, 16);
        } else if (keyLen < 16) {
            secretKey = StringUtils.rightPad(secretKey, 16, PAD_STR);
        }
        byte[] keyBytes = new byte[16]; // AES128
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(secretKeyBytes, 0, keyBytes, 0, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

}
