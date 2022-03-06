package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class Reports {
    public TextArea textArea;
    public Button report1;
    public Button report3;
    public Button report2;
    public Button exit;

    public void onReport1(ActionEvent actionEvent) {
    }

    public void onReport3(ActionEvent actionEvent) {
    }

    public void onReport2(ActionEvent actionEvent) {
    }

    public void onExit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
