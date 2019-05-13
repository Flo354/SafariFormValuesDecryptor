package com.flo354.decrypt.safari.formvalues;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * @author Florian Pradines <florian.pradines@gmail.com>
 */
public class FormValueDecryptor {

    private static byte[] derivePassword(String password) throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(new String(Hex.decodeHex(password)).toCharArray(), "someSalt".getBytes(), 1000, 128);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    private static byte[] decrypt(byte[] key, byte[] cipher) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipherInstance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        cipherInstance.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        return cipherInstance.doFinal(cipher);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java -jar program.jar PASSWORD_HEXADECIMAL PATH_TO_INPUT_FILE PATH_TO_OUTPUT_FILE");
            return;
        }

        System.out.println("[+] Generating decryption key from password");
        byte[] key =  derivePassword(args[0]);
        System.out.println("\t[+] Decryption key: " + Hex.encodeHexString(key));

        System.out.println("[+] Reading content of file: " + args[1]);
        byte[] cipher = Files.readAllBytes(Paths.get(args[1]));

        System.out.println("[+] Decrypting content");
        byte[] plaintext = decrypt(key, cipher);

        System.out.println("[+] Writing content to: " + args[2]);
        OutputStream os = new FileOutputStream(new File(args[2]));
        os.write(plaintext);
        os.close();
    }
}
