package sample.Controller;

import animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.FileChiphrator;
import logic.OneLetterAffineSubstitution;
import sample.DBHandler;
import sample.User;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller {

    private static User currentUser;

    private static int passTry = 0;
    public Label labelMsg11;

    @FXML
    private Label labelMsg1;

    @FXML
    private Label labelMsg;

    public static User getCurrentUser() {
        return currentUser;
    }

    @FXML
    private Menu menuRefItem;

    @FXML
    private MenuItem menuAboutItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button enterButton;

    @FXML
    private Button cancelButton;

    public static Button staticEnterButton;

    @FXML
    void initialize() {

        staticEnterButton = enterButton;
        cancelButton.setOnAction(event -> {
            loginField.setText("");
            passwordField.setText("");
        });

        enterButton.setOnAction(event -> login());

        closeMenuItem.setOnAction(actionEvent -> closeProgram());

        menuAboutItem.setOnAction(actionEvent -> {
            System.out.println("Нажали на Справку о программе");
            openNewModalScene("/sample/fxml/aboutDialog.fxml","О программе");
        });
    }

    private void closeProgram() {
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

    private void loginUser(String loginText, String password) {
        DBHandler dbHandler = new DBHandler();
        User user = dbHandler.getUser(loginText);
        String hash = OneLetterAffineSubstitution.encryptMessage(loginText.toCharArray(), password);
        if (hash.equals(user.getPassword())) {
            labelMsg.setVisible(true);
            labelMsg11.setVisible(false);
            System.out.println("Успех");
            currentUser = user;
            if (currentUser.getIsblocked() == 1)  {
                openNewModalScene("/sample/fxml/block.fxml", "Блокировка");
            } else {
                if ((currentUser.getIsfirstlogin() == 1)) {
                    openNewModalScene("/sample/fxml/proofPass.fxml", "Подтверждение пароля");
                    dbHandler.updateUserLog(currentUser);
                } else if (currentUser.isAdmin()) {
                    openNewScene(enterButton, "/sample/fxml/appAdmin.fxml");
                } else {
                    openNewScene(enterButton, "/sample/fxml/app.fxml");
                }
            }
        } else {
            System.out.println("Error: Введите пароль повторно");
            passTry++;
            labelMsg.setVisible(false);
            labelMsg11.setVisible(true);
            Shake shakePass = new Shake(passwordField);
            shakePass.playAnimation();
        }
    }

    private void login() {
        String loginText = loginField.getText().trim();
        String loginPassword = passwordField.getText().trim();

        if (passTry > 3) {
            closeProgram();
        }

        if (loginIsPreset(loginText)) {
            labelMsg.setVisible(true);
            labelMsg1.setVisible(false);
            loginUser(loginText, loginPassword);
        } else {
            System.out.println("Error: Введите логин повторно");
            Shake shakeLoginField = new Shake(loginField);
            labelMsg.setVisible(false);
            labelMsg1.setVisible(true);
            shakeLoginField.playAnimation();
        }
    }

    private boolean loginIsPreset(String login) {
        DBHandler dbHandler = new DBHandler();
        User user = new User();
        user.setLogin(login);
        ResultSet resultSet = dbHandler.getLogin(user);
        int counter = 0;
        try {
            while (resultSet.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter >= 1;
    }

    public void openNewScene(Button button, String window) {
        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
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
    }
}