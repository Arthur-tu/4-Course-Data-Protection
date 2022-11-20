package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import logic.FileChiphrator;
import sample.Config;
import sample.DBHandler;
import sample.Main;
import sample.User;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class PassphraseController{
    @FXML
    private PasswordField passwordProofField;
    @FXML
    private Button enterButton;
    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        cancelButton.setOnAction(actionEvent -> closeProgram());

        enterButton.setOnAction(actionEvent -> checkPassPhrase());
    }

    private void checkPassPhrase() {
        String passPhrase = passwordProofField.getText();
        passwordProofField.setText("");
        IvParameterSpec ivParameterSpec = null;

        FileChiphrator fileChiphrator = new FileChiphrator(passPhrase);
        try {
             ivParameterSpec = fileChiphrator.readIv();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String digest = fileChiphrator.createHashString(passPhrase);
        SecretKey keyOnPassPhrase = fileChiphrator.getKeyFromPassword2(digest);

        File encriptedFile = new File
                ("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\encriptedFile.ibd");
        File decriptedFile = new File
                ("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\decriptedFile.ibd");

        try {
            fileChiphrator.decriptFile(FileChiphrator.algo, keyOnPassPhrase, ivParameterSpec, encriptedFile, decriptedFile);
            if (passPhaseIsPresent()) {
                System.out.println("Файл расшифрован");
                Main.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/sample/fxml/sample.fxml")), 540, 360));
            } else {
                closeProgram();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ключ неверный");
            closeProgram();
        }
    }

    private boolean passPhaseIsPresent() {
        DBHandler dbHandler = new DBHandler();
        User user = dbHandler.getUser("admin");
        return user.isAdmin();
    }

    private void closeProgram() {
        System.out.println("Парольная фраза не подтверждена. Завершение работы...");
        System.exit(1);
    }
}
