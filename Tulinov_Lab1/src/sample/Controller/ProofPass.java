package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sample.Controller.Controller;
import sample.DBHandler;
import sample.User;

import java.io.IOException;

public class ProofPass {
    @FXML
    private Label label;
    @FXML
    private PasswordField passwordProofField;
    @FXML
    private Button enterButton;
    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        label.setVisible(false);
        cancelButton.setOnAction(event -> {
            System.out.println("Модальное окно подтверждения закрыто");
            Stage stage = (Stage) enterButton.getScene().getWindow();
            stage.close();
            System.exit(1);
        });

        enterButton.setOnAction(event -> {
            String pass = passwordProofField.getText();
            User user = Controller.getCurrentUser();
            String oldPass = user.getPassword();

            if (pass.equals(oldPass)) {
                DBHandler dbHandler = new DBHandler();
                System.out.println("Пароль подтвержден");
                Controller.getCurrentUser().setIsfirstlogin((short) 0);
                dbHandler.updateUserLog(Controller.getCurrentUser());
                Stage stage = (Stage) enterButton.getScene().getWindow();
                stage.close();
                if (isAdmin(Controller.getCurrentUser())) {
                    openNewScene(Controller.staticEnterButton, "/sample/fxml/appAdmin.fxml");
                } else {
                    openNewScene(Controller.staticEnterButton, "/sample/fxml/app.fxml");
                }
            } else {
                System.out.println("Пароль не подтвержден");
                label.setVisible(true);
            }
        });

    }

    private boolean isAdmin(User user) {
        return user.getLogin().equals("admin");
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
}
