package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class applicationController implements Initializable {
    connectDb condb = new connectDb();
    @FXML
    private BorderPane showpane;
    @FXML
    private Label lblwelcome;
    @FXML
    private Label lblstatus;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnWorker;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(this.condb.isDatabaseConnected()){
            lblstatus.setText("Connected to database");
        }else{
            lblstatus.setText("Can not connect to database");
        }
        btnCustomer.setOnMouseClicked(this::onCustomerClick);
        btnWorker.setOnMouseClicked(this::onWorkerClick);
    }
    
    public void onWorkerClick(MouseEvent event) {
        lblwelcome.setVisible(false);
        loadUI("worker");
    }
    
    public void onCustomerClick(MouseEvent event) {
        lblwelcome.setVisible(false);
        loadUI("customer");
    }
    
    private void loadUI(String ui) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
        } catch (IOException e) {
            
        }
        showpane.setTop(root);
    }
    
}
