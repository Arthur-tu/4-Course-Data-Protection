package sample;

import logic.FileChiphrator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TestFileChifrator {
    public static void main(String[] args) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        FileChiphrator fileChiphrator = new FileChiphrator("123");
        IvParameterSpec ivParameterSpec = fileChiphrator.generateIv();
        String digest = fileChiphrator.createHashString("123");
        SecretKey keyOnPassPhrase = fileChiphrator.getKeyFromPassword2(digest);

        File inputFile = new File(
                "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\users.ibd");
        File encriptedFile = new File
                ("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\encriptedFile.ibd");

        fileChiphrator.encriptFile(FileChiphrator.algo, keyOnPassPhrase,ivParameterSpec,inputFile,encriptedFile);
        System.out.println("Файл зашифрован");
    }
}
