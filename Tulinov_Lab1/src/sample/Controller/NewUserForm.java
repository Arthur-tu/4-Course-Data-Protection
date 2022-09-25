package sample.Controller;

import animations.Shake;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.User;

import java.util.Set;

public class NewUserForm {

    private static  Set<String> loginSet;
    private static DBHandler dbHandler;

    @FXML
    private Label loginIsPresentLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField loginField;

    @FXML
    private Button registrButton;

    @FXML
    void initialize() {
        dbHandler = new DBHandler();
        loginSet = dbHandler.getLoginUsers();
        loginIsPresentLabel.setVisible(false);

        registrButton.setOnAction(event -> trySignUpNewUser());

        cancelButton.setOnAction(event -> {
            System.out.println("Модальное окно регистрации закрыто");
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private void signUpNewUser() {
        String login = loginField.getText().trim();
        dbHandler.signUpUser(new User(login, new String("")));
        loginSet.add(login);
    }

    private void trySignUpNewUser() {
        if (!loginSet.contains(loginField.getText().trim())) {
            loginIsPresentLabel.setVisible(false);
            signUpNewUser();
            System.out.println("Пользователь добавлен");
        } else {
            loginIsPresentLabel.setVisible(true);
            Shake shakeLogin = new Shake(registrButton);
            shakeLogin.playAnimation();
            System.out.println("Такой логин уже есть");
        }
    }
}