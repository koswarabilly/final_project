package application;

import static db.dbCreate.insertData;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private String tempname;
    private connectDb dc;
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
    private Button btnRefresh;
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

    private ObservableList<Customer> data;

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
        this.dc = new connectDb();
        btnRefresh.setOnAction(this::loadData);
        btnRefresh.fire();
    }

    private void loadData(ActionEvent ev) {
        try {
            Connection conn = connectDb.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                this.data.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        columName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columCIDate.setCellValueFactory(new PropertyValueFactory<>("checkindt"));
        columCODate.setCellValueFactory(new PropertyValueFactory<>("checkoutdt"));
        columnRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        this.tableCustomer.setItems(null);
        this.tableCustomer.setItems(this.data);
    }

    private String getNameFromInput() throws IllegalArgumentException {
        String name = txtname.getText().trim();
        if (name.equals("")) {
            throw new IllegalArgumentException("Name can't be nothing");
        }
        return name;
    }

    public void onBtnCancelClick(MouseEvent e) {
        edit = false;
        txtname.clear();
        checkindt.getEditor().clear();
        checkoutdt.getEditor().clear();
        checkindt.setVisible(true);
        checkoutdt.setVisible(true);
        lblcheckin.setVisible(false);
        lblcheckout.setVisible(false);
        txtroom.clear();
    }

    public void onBtnDeleteClick(MouseEvent e) {
        edit = false;
        try {
            Connection conn = connectDb.getConnection();
            deleteData(conn, this.tempname);
            btnRefresh.fire();
        } catch (SQLException evv) {
            System.err.println("Error " + evv);
        }
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
            edit = true;
            txtname.setText(tableCustomer.getSelectionModel().getSelectedItem().getName());
            lblcheckin.setVisible(true);
            lblcheckout.setVisible(true);
            lblcheckin.setText(tableCustomer.getSelectionModel().getSelectedItem().getCheckindt());
            lblcheckout.setText(tableCustomer.getSelectionModel().getSelectedItem().getCheckoutdt());
            checkindt.setVisible(false);
            checkoutdt.setVisible(false);
            txtroom.setText(tableCustomer.getSelectionModel().getSelectedItem().getRoom().toString());
            rowselected = tableCustomer.getSelectionModel().getSelectedIndex();
            this.tempname = tableCustomer.getSelectionModel().getSelectedItem().getName();
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
            if(this.edit==false) messageBox("Hotel System", "Check in date can't be nothing").showAndWait();
            txtname.requestFocus();
        }
        try {
            checkoutdtx = checkoutdt.getValue().toString();
        } catch (NullPointerException e) {
            if(this.edit==false) messageBox("Hotel System", "Check out date can't be nothing").showAndWait();
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
        if (edit == true) {
            try {
                Connection conn = connectDb.getConnection();
                updateData(conn, this.tempname, name, room);
                btnRefresh.fire();
            } catch (SQLException e) {
                System.err.println("Error " + e);
            }
        } else {
            try {
                Connection conn = connectDb.getConnection();
                Statement stmt = null;
                stmt = conn.createStatement();
                insertData(stmt, name, checkindtx, checkoutdtx, room);
                btnRefresh.fire();
            } catch (SQLException e) {
                System.err.println("Error " + e);
            }
        }
        edit = false;
        checkindt.setVisible(true);
        checkoutdt.setVisible(true);
        lblcheckin.setVisible(false);
        lblcheckout.setVisible(false);
        txtname.requestFocus();
    }

    public static void insertData(Statement stmt, String name, String checkindt, String checkoutdt, int room) {
        try {
            String sql = "Insert Into customer(name,checkindt,checkoutdt,room) values ('" + name + "', '" + checkindt + "','" + checkoutdt + "'," + room + ")";
            stmt.execute(sql);
            System.out.println("Data " + name + "(" + room + ") inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateData(Connection conn, String oldname, String newname, int newroom) {
        try {
            String sql = "update customer set name = ?,room = ? where name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newname);
            stmt.setInt(2, newroom);
            stmt.setString(3, oldname);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(Connection conn, String name) {
        try {
            String sql = "delete from customer where name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Alert messageBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert;
    }
}
