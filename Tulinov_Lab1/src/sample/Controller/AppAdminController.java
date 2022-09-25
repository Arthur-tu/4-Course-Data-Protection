package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AppAdminController {
    @FXML
    private Button listUsersButton;

    @FXML
    private Button createNewUserButton;

    @FXML
    private Button cancelProgramButton;

    @FXML
    private Button changePassButton;

    @FXML
    void initialize() {
        createNewUserButton.setOnAction(event -> openNewModalScene("/sample/fxml/newUserForm.fxml",
                "Добавление нового пользователя"));

        cancelProgramButton.setOnAction(event -> closeProgram());

        changePassButton.setOnAction(event -> openNewModalScene("/sample/fxml/changePassForm.fxml",
                "Смена пароля"));

        listUsersButton.setOnAction(event -> openNewModalScene("/sample/fxml/listViewForm.fxml",
                "Список пользователей"));
    }

    private void closeProgram() {
        System.out.println("Завершение работы программы");
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
