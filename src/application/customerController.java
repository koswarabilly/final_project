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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class customerController implements Initializable {
    private int rowselected;
    private boolean edit;
    @FXML
    private TextField txtname;
    @FXML
    private TextField txtroom;
    @FXML
    private DatePicker checkindt;
    @FXML
    private DatePicker checkoutdt;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblcheckin;
    @FXML
    private Label lblcheckout;
    @FXML
    private TableView<Customer> tableCustomer;
    @FXML
    TableColumn<Customer, String> columName;
    @FXML
    TableColumn<Customer, String> columCIDate;
    @FXML
    TableColumn<Customer, String> columCODate;
    @FXML
    TableColumn<Customer, Integer> columnRoom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(txtname::requestFocus);
        columName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columCIDate.setCellValueFactory(new PropertyValueFactory<>("checkindt"));
        columCODate.setCellValueFactory(new PropertyValueFactory<>("checkoutdt"));
        columnRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        btnSave.setOnMouseClicked(this::onBtnSaveClick);
        btnCancel.setOnMouseClicked(this::onBtnCancelClick);
        tableCustomer.setOnMouseClicked(this::onRowClick);
        btnDelete.setOnMouseClicked(this::onBtnDeleteClick);
    }

    private String getNameFromInput() throws IllegalArgumentException {
        String name = txtname.getText().trim();
        if (name.equals("")) {
            throw new IllegalArgumentException("Name can't be nothing");
        }
        return name;
    }

    public void onBtnCancelClick(MouseEvent e) {
        edit=false;
        txtname.clear();
        checkindt.getEditor().clear();
        checkoutdt.getEditor().clear();
        checkindt.setVisible(true);
        checkoutdt.setVisible(true);
        lblcheckin.setVisible(false);
        lblcheckout.setVisible(false);
        txtroom.clear();
    }
    
    public void onBtnDeleteClick(MouseEvent e){
        edit=false;
        tableCustomer.getItems().remove(tableCustomer.getSelectionModel().getSelectedItem());
        txtname.clear();
        checkindt.getEditor().clear();
        checkoutdt.getEditor().clear();
        checkindt.setVisible(true);
        checkoutdt.setVisible(true);
        lblcheckin.setVisible(false);
        lblcheckout.setVisible(false);
        txtroom.clear();
    }

    public void onRowClick(MouseEvent e) {
        if (e.getClickCount() == 2) //Checking double click
        {
            edit=true;
            txtname.setText(tableCustomer.getSelectionModel().getSelectedItem().getName());
            lblcheckin.setVisible(true);
            lblcheckout.setVisible(true);
            lblcheckin.setText(tableCustomer.getSelectionModel().getSelectedItem().getCheckindt());
            lblcheckout.setText(tableCustomer.getSelectionModel().getSelectedItem().getCheckoutdt());
            checkindt.setVisible(false);
            checkoutdt.setVisible(false);
            txtroom.setText(tableCustomer.getSelectionModel().getSelectedItem().getRoom().toString());
            rowselected=tableCustomer.getSelectionModel().getSelectedIndex();
        }
    }

    public void onBtnSaveClick(MouseEvent event) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String name = "", checkindtx = "", checkoutdtx = "";
        Integer room = 0;
        try {
            name = getNameFromInput();
        } catch (IllegalArgumentException e) {
            messageBox("Hotel System", e.getMessage()).showAndWait();
            txtname.requestFocus();
        }
        try {
            checkindtx = checkindt.getValue().toString();
        } catch (NullPointerException e) {
            messageBox("Hotel System", "Check in date can't be nothing").showAndWait();
            txtname.requestFocus();
        }
        try {
            checkoutdtx = checkoutdt.getValue().toString();
        } catch (NullPointerException e) {
            messageBox("Hotel System", "Check out date can't be nothing").showAndWait();
            txtname.requestFocus();
        }
        try {
            room = Integer.valueOf(txtroom.getText());
            if (room < 0 || room > 400) {
                throw new IllegalArgumentException("Room available : 0 - 400");
            }
        } catch (NumberFormatException e) {
            messageBox("Hotel System", "Room only can have number").showAndWait();
            txtroom.selectAll();
            txtroom.requestFocus();
            return;
        } catch (IllegalArgumentException e) {
            messageBox("Hotel System", e.getMessage()).showAndWait();
            txtroom.selectAll();
            txtroom.requestFocus();
            txtroom.selectAll();
            return;
        }

        txtname.clear();
        checkindt.getEditor().clear();
        checkoutdt.getEditor().clear();
        txtroom.clear();
        if(edit==true){
            tableCustomer.getItems().set(rowselected, new Customer(name, checkindtx, checkoutdtx, room));
        }else{
            tableCustomer.getItems().add(new Customer(name, checkindtx, checkoutdtx, room));
        }
        edit=false;
        checkindt.setVisible(true);
        checkoutdt.setVisible(true);
        lblcheckin.setVisible(false);
        lblcheckout.setVisible(false);
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
