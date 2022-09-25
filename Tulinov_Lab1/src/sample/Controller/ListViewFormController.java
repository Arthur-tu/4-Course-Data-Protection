package sample.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.DBHandler;
import sample.User;

import java.util.ArrayList;

public class ListViewFormController {
    private static int counter = 0;

    @FXML
    private Button listUsersPrevButton;

    @FXML
    private CheckBox isblocked;

    @FXML
    private CheckBox islimited;

    @FXML
    private Button saveButton;

    @FXML
    private Button listUsersButton;

    @FXML
    private Button cancelProgramButton;

    @FXML
    private Button enterButton;

    @FXML
    private Label userNameLabel;

    @FXML
    void initialize() {
        DBHandler dbHandler = new DBHandler();
        ArrayList<User> list = dbHandler.getListUsers();
        userNameLabel.setText(list.get(0).getLogin());
        setParams(list, counter);

        enterButton.setOnAction(event -> {
            System.out.println("Модальное окно просмотра закрыто");
            Stage stage = (Stage) enterButton.getScene().getWindow();
            stage.close();
        });

        cancelProgramButton.setOnAction(event -> {
            System.out.println("Модальное окно просмотра закрыто");
            Stage stage = (Stage) cancelProgramButton.getScene().getWindow();
            stage.close();
        });


        listUsersButton.setOnAction(event -> {
            if (counter == list.size() - 1) counter = -1;
            counter++;
            userNameLabel.setText(list.get(counter).getLogin());

            setParams(list, counter);
        });

        listUsersPrevButton.setOnAction(event -> {
            if (counter == 0) counter = list.size();
            counter--;
            boolean flag2 = false;
            if (list.get(counter).getPasslimit() == 1) {
                flag2 = true;
            } else {
                flag2 = false;
            }
            islimited.setSelected(flag2);
            if (list.get(counter).getIsblocked() == 1) {
                flag2 = true;
            } else {
                flag2 = false;
            }
            isblocked.setSelected(flag2);
            userNameLabel.setText(list.get(counter).getLogin());
        });

        saveButton.setOnAction(event -> {
            saveUser(dbHandler, list);
        });

    }

        private void saveUser(DBHandler dbHandler, ArrayList<User> list) {
            short block, limit;
            list.get(counter).setIsblocked(isblocked.isSelected() ? (short) 1 : (short) 0);

            list.get(counter).setIsLimited(islimited.isSelected() ? (short) 1 : (short) 0);

            dbHandler.updateUser(list.get(counter));
            System.out.println("Данные обновлены");
        }

        private void setParams(ArrayList<User> list, int x) {
            boolean flag = false;
            if (list.get(x).getPasslimit() == 1) {
                flag = true;
            } else {
                flag = false;
            }
            islimited.setSelected(flag);
            if (list.get(x).getIsblocked() == 1) {
                flag = true;
            } else {
                flag = false;
            }
            isblocked.setSelected(flag);
        }
    }
