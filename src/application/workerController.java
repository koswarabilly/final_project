package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class workerController implements Initializable {
    private int rowselected;
    private boolean edit;
    @FXML
    private TextField txtid;
    @FXML
    private TextField txtname;
    @FXML
    private TextField txtposition;
    @FXML
    private DatePicker since;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblsince;
    @FXML
    private TableView<Worker> tableWorker;
    @FXML
    TableColumn<Worker, String> columId;
    @FXML
    TableColumn<Worker, String> columName;
    @FXML
    TableColumn<Worker, String> columPosition;
    @FXML
    TableColumn<Worker, String> columnSince;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(txtid::requestFocus);
        columName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        columnSince.setCellValueFactory(new PropertyValueFactory<>("since"));
        btnSave.setOnMouseClicked(this::onBtnSaveClick);
        btnCancel.setOnMouseClicked(this::onBtnCancelClick);
        tableWorker.setOnMouseClicked(this::onRowClick);
        btnDelete.setOnMouseClicked(this::onBtnDeleteClick);
    }

    private String getNameFromInput() throws IllegalArgumentException {
        String name = txtname.getText().trim();
        if (name.equals("")) {
            throw new IllegalArgumentException("Name can't be nothing");
        }
        return name;
    }
    
    private String getIdFromInput() throws IllegalArgumentException {
        String id = txtid.getText().trim();
        if (id.equals("")) {
            throw new IllegalArgumentException("Id can't be nothing");
        }
        return id;
    }
    
    private String getPositionFromInput() throws IllegalArgumentException {
        String pos = txtposition.getText().trim();
        if (pos.equals("")) {
            throw new IllegalArgumentException("Position can't be nothing");
        }
        return pos;
    }

    public void onBtnCancelClick(MouseEvent e) {
        edit=false;
        txtname.clear();
        since.getEditor().clear();
        txtid.clear();
        txtposition.clear();
        since.setVisible(true);
        lblsince.setVisible(false);
    }
    
    public void onBtnDeleteClick(MouseEvent e){
        edit=false;
        tableWorker.getItems().remove(tableWorker.getSelectionModel().getSelectedItem());
        txtname.clear();
        txtname.clear();
        since.getEditor().clear();
        txtid.clear();
        txtposition.clear();
        since.setVisible(true);
        lblsince.setVisible(false);
    }

    public void onRowClick(MouseEvent e) {
        if (e.getClickCount() == 2) //Checking double click
        {
            edit=true;
            txtname.setText(tableWorker.getSelectionModel().getSelectedItem().getName());
            lblsince.setVisible(true);
            lblsince.setText(tableWorker.getSelectionModel().getSelectedItem().getSince());
            since.setVisible(false);
            txtposition.setText(tableWorker.getSelectionModel().getSelectedItem().getPosition());
            txtid.setText(tableWorker.getSelectionModel().getSelectedItem().getId());
            rowselected=tableWorker.getSelectionModel().getSelectedIndex();
        }
    }

    public void onBtnSaveClick(MouseEvent event) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String name = "", sincex = "",id="",position="";
        try {
            name = getNameFromInput();
        } catch (IllegalArgumentException e) {
            messageBox("Hotel System", e.getMessage()).showAndWait();
            txtname.requestFocus();
        }
        try {
            id = getIdFromInput();
        } catch (IllegalArgumentException e) {
            messageBox("Hotel System", e.getMessage()).showAndWait();
            txtname.requestFocus();
        }
        try {
            position = getPositionFromInput();
        } catch (IllegalArgumentException e) {
            messageBox("Hotel System", e.getMessage()).showAndWait();
            txtname.requestFocus();
        }
        try {
            sincex = since.getValue().toString();
        } catch (NullPointerException e) {
            messageBox("Hotel System", "Check out date can't be nothing").showAndWait();
            txtname.requestFocus();
        }
        
        if(edit==true){
            tableWorker.getItems().set(rowselected, new Worker(id, name, position, sincex));
        }else{
            tableWorker.getItems().add(new Worker(id, name, position, sincex));
        }

        txtname.clear();
        since.getEditor().clear();
        txtid.clear();
        txtposition.clear();

        
        txtname.requestFocus();
    }

    private Alert messageBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }
}
