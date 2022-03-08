package controller;

import DAO.ReportsDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Reports {
    public TextArea textArea;
    public Button report1;
    public Button report3;
    public Button report2;
    public Button exit;


    public void initialize(){
        textArea.setText("Choose from one of the reports below.");
    }

    public void onReport1(ActionEvent actionEvent) throws SQLException {
        textArea.setText(ReportsDAO.report1());
    }

    public void onReport2(ActionEvent actionEvent) throws SQLException {
        textArea.setText(ReportsDAO.report2());
    }

    public void onReport3(ActionEvent actionEvent) throws SQLException, ParseException {
        textArea.setText(ReportsDAO.report3());
    }



    public void onExit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
