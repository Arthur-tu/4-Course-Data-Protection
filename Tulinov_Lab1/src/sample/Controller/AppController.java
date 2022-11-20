package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.FileChiphrator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppController {
    @FXML
    private Button changePass;
    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        cancelButton.setOnAction(event -> closeProgram());

        changePass.setOnAction(event ->
                openNewModalScene("/sample/fxml/changePassForm.fxml", "Смена пароля")
        );
    }

    protected void closeProgram() {
        IvParameterSpec ivParameterSpec = null;
        System.out.println("Завершение работы программы");
        FileChiphrator fileChiphrator = new FileChiphrator("123");
        File inputFile = new File(
                "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\decriptedFile.ibd");
        File encriptedFile = new File
                ("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\encriptedFile.ibd");
        try {
            ivParameterSpec = fileChiphrator.generateIv();
        }catch (Exception e ) {
            e.printStackTrace();
        }

        String digest = fileChiphrator.createHashString("123");
        SecretKey keyOnPassPhrase = fileChiphrator.getKeyFromPassword2(digest);
        try {
            fileChiphrator.encriptFile(FileChiphrator.algo, keyOnPassPhrase, ivParameterSpec, inputFile, encriptedFile);
        } catch (Exception e) {
            System.out.println("Ошибка шифрования");
            e.printStackTrace();
        }
        System.out.println("Файл зашифрован");
        try {
            Files.delete(Paths.get("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Data\\tulinovdp\\decriptedFile.ibd"));
        } catch (Exception e ) {
            System.out.println("Ошибка удаления файла");
            e.printStackTrace();
        }
        System.out.println("Расшифрованный файл удален");
        System.exit(1);
    }

    public void openNewModalScene(String window, String title) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        System.out.println("Регистрация успешна");
    }
}
