package application;

import static application.customerController.deleteData;
import static application.customerController.insertData;
import static application.customerController.updateData;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class workerController implements Initializable {
    private int rowselected;
    private boolean edit;
    private String tempname;
    private connectDb dc;
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
    private Button btnRefresh;
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
    
    private ObservableList<Worker> data;

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
        this.dc = new connectDb();
        btnRefresh.setOnAction(this::loadData);
        btnRefresh.fire();
    }
    
    private void loadData(ActionEvent ev) {
        try {
            Connection conn = connectDb.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM worker");
            while (rs.next()) {
                this.data.add(new Worker(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException e) {
            System.err.println("Error " + e);
        }
        columName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        columnSince.setCellValueFactory(new PropertyValueFactory<>("since"));
        this.tableWorker.setItems(null);
        this.tableWorker.setItems(this.data);
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
        try {
            Connection conn = connectDb.getConnection();
            deleteData(conn, this.tempname);
            btnRefresh.fire();
        } catch (SQLException evv) {
            System.err.println("Error " + evv);
        }
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
            this.tempname = tableWorker.getSelectionModel().getSelectedItem().getName();
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
            try {
                Connection conn = connectDb.getConnection();
                updateData(conn, this.tempname, name, position);
                btnRefresh.fire();
            } catch (SQLException e) {
                System.err.println("Error " + e);
            }
        }else{
            try {
                Connection conn = connectDb.getConnection();
                Statement stmt = null;
                stmt = conn.createStatement();
                insertData(stmt, name, id, position, sincex);
                btnRefresh.fire();
            } catch (SQLException e) {
                System.err.println("Error " + e);
            }
        }

        txtname.clear();
        since.getEditor().clear();
        txtid.clear();
        txtposition.clear();

        
        txtname.requestFocus();
    }
    
    public static void insertData(Statement stmt, String name, String id, String position, String since) {
        try {
            String sql = "Insert Into worker(id,name,position,since) values ('" + id + "', '" + name + "','" + position + "','" + since + "')";
            stmt.execute(sql);
            System.out.println("Data " + name + "(" + position + ") inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateData(Connection conn, String oldname, String newname, String newposition) {
        try {
            String sql = "update worker set name = ?,position = ? where name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newname);
            stmt.setString(2, newposition);
            stmt.setString(3, oldname);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(Connection conn, String name) {
        try {
            String sql = "delete from worker where name = ?";
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
