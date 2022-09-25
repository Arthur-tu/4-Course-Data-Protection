package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
        System.out.println("Регистрация успешна");
    }
}
