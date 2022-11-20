package logic;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class FileChiphrator {
    public static final String algo = "AES/CBC/PKCS5Padding";
    private static String passPhrase;

    public FileChiphrator(String passPhrase) {
        FileChiphrator.passPhrase = passPhrase;
    }

    public void encriptFile(String algo, SecretKey key, IvParameterSpec iv, File inputFile, File encriptedFile) throws
            NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(encriptedFile)) {
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        }
    }

    public void decriptFile(String algo, SecretKey key, IvParameterSpec iv, File encriptedFile, File decriptedFile)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algo);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        try(FileInputStream fis = new FileInputStream(encriptedFile);
            FileOutputStream fos = new FileOutputStream(decriptedFile)) {
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        }
    }

    public IvParameterSpec generateIv() throws IOException {
        byte [] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        FileOutputStream fs = new FileOutputStream("paramFile");
        BufferedOutputStream bos = new BufferedOutputStream(fs);
        bos.write(iv);
        bos.close();
        return new IvParameterSpec(iv);
    }

    public IvParameterSpec readIv() throws IOException {
        byte[] fileData = new byte[16];
        DataInputStream dis = new DataInputStream(new FileInputStream("paramFile"));
        dis.readFully(fileData);
        dis.close();
        return new IvParameterSpec(fileData);
    }

    public String createHashString(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesOfMessage = s.getBytes(StandardCharsets.UTF_8);
            byte[] thedigest = md.digest(bytesOfMessage);
            StringBuilder hexString = new StringBuilder();
            for(byte bi : thedigest) {
                String hex = Integer.toHexString(0xFF & bi);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e) {
            return "";
        }
    }

    public SecretKey getKeyFromPassword2(String digest) {
       return convertStringToSecretKeyto(digest);
    }

    private  SecretKey convertStringToSecretKeyto(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}