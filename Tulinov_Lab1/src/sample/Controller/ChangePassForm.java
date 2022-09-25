package sample.Controller;

import animations.Shake;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import logic.PasswordCreator;
import sample.DBHandler;
import sample.User;

public class ChangePassForm {

    private static DBHandler dbHandler;

    @FXML
    private Label passIsNotGood;

    @FXML
    private Label oldPassMsg;

    @FXML
    private PasswordField oldPassField;

    @FXML
    private Label label;

    @FXML
    private PasswordField newPassField;

    @FXML
    private PasswordField passwordProofField;

    @FXML
    private Button enterButton;

    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        dbHandler = new DBHandler();

        cancelButton.setOnAction(event -> {
            System.out.println("Модальное окно смены пароля закрыто");
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        enterButton.setOnAction(event -> changePassword());
    }
    
    private boolean passAreSame(String p1, String p2) {
        char c;
        if (p1.length() != p2.length()) return false;

        for (int i = 0; i < p1.length(); i++) {
            c = p1.charAt(i);
            if (c != p2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean oldPassIsCorrect(String p1, DBHandler handler) {
        User user = Controller.getCurrentUser();
        String oldLogin = user.getLogin();
        String oldPass = user.getPassword();
        User old = new User(oldLogin, oldPass);
        String p2 = handler.getPassword(old);
        return passAreSame(p1, p2);
    }

    private void changePassword() {
        if (checkPassword(newPassField.getText().trim())) {
            String password = newPassField.getText().trim();
            String passwordProof = passwordProofField.getText().trim();
            String oldpassword = oldPassField.getText().trim();

            oldPassMsg.setVisible(false);
            label.setVisible(false);
            passIsNotGood.setVisible(false);
            if (oldPassIsCorrect(oldpassword, dbHandler)) {
                System.out.println("Старый пароль введен правильно");
                //oldPassMsg.setVisible(false);
                if (passAreSame(password, passwordProof)) {
                    Controller.getCurrentUser().setPassword(password);
                    dbHandler.changePassword(Controller.getCurrentUser());
                   // label.setVisible(false);
                    System.out.println("Пароль изменен");
                    Stage stage = (Stage) enterButton.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Пароли не совпадают");
                    label.setVisible(true);
                    Shake passwordProofShake = new Shake(passwordProofField);
                    passwordProofShake.playAnimation();
                }
            } else {
                System.out.println("Старый пароль введен неправильно");
                oldPassMsg.setVisible(true);
                Shake oldPasswordShake = new Shake(oldPassField);
                oldPasswordShake.playAnimation();
            }
        } else {
            System.out.println("Пароль не подходит под ограничения");
            passIsNotGood.setVisible(true);
        }

    }

    private boolean checkPassword(String password) {
        return Controller.getCurrentUser().getPasslimit() == 0 ||
                new PasswordCreator().checkPassword(password);
    }
}
