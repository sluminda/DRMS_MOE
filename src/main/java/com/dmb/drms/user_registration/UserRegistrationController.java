package com.dmb.drms.user_registration;

import com.dmb.drms.MainApplication;
import com.dmb.drms.utils.AlertUtil;
import com.dmb.drms.utils.DBConnection;
import com.dmb.drms.utils.PasswordUtil;
import com.dmb.drms.utils.sql.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    @FXML
    private TextArea name;
    @FXML
    private TextField username;
    @FXML
    private TextField nic;
    @FXML
    private TextArea email;
    @FXML
    private TextField phone;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<String> userRole;
    @FXML
    private CheckBox dashboard;
    @FXML
    private CheckBox reports;
    @FXML
    private CheckBox dailyLetters;
    @FXML
    private CheckBox inquiry;
    @FXML
    private CheckBox userManagement;
    @FXML
    private Button insertUser;
    @FXML
    private Button deleteUser;
    @FXML
    private TableView<User> userListTable;
    @FXML
    private TableColumn<User, Integer> colID;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colNIC;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, String> colPhone;
    @FXML
    private TableColumn<User, String> colUserRole;

    private ObservableList<User> userData;

    @FXML
    public void initialize() {
        // Initialize ComboBox with user roles
        userRole.setItems(FXCollections.observableArrayList("Owner", "Super Admin", "Admin"));

        // Configure the TableView columns
        configureTableView();

        // Load users from the database
        loadUsers();

        // Set up listeners for table row clicks and button actions
        setupListeners();

        //Default Password
        password.setText("1234");
    }

    private void configureTableView() {
        colID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));

        // Initialize the ObservableList
        userData = FXCollections.observableArrayList();
        userListTable.setItems(userData);
    }

    private void loadUsers() {
        String query = "SELECT User_ID, Name, User_Name, NIC, Email, Phone, User_Role FROM Users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("User_ID"),
                        rs.getString("Name"),
                        rs.getString("User_Name"),
                        rs.getString("NIC"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("User_Role")
                );
                userData.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace to get more details
            AlertUtil.showAlertError("Database Error", "Failed to load users.");
        }
    }


    private void setupListeners() {
        userListTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !userListTable.getSelectionModel().isEmpty()) {
                populateForm(userListTable.getSelectionModel().getSelectedItem());
            }
        });

        insertUser.setOnAction(event -> insertUser());
        deleteUser.setOnAction(event -> deleteUser());
    }

    private void populateForm(User user) {
        name.setText(user.getName());
        username.setText(user.getUsername());
        nic.setText(user.getNic());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        userRole.setValue(user.getUserRole());

        loadUserPrivileges(user.getUserID());
    }

    private void loadUserPrivileges(int userID) {
        String query = "SELECT M_Name FROM Modules m " +
                "INNER JOIN Privileges p ON m.M_ID = p.M_ID WHERE p.User_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            clearPrivilegeCheckboxes();
            while (rs.next()) {
                String moduleName = rs.getString("M_Name");
                switch (moduleName) {
                    case "Dashboard":
                        dashboard.setSelected(true);
                        break;
                    case "Reports":
                        reports.setSelected(true);
                        break;
                    case "Daily Letters":
                        dailyLetters.setSelected(true);
                        break;
                    case "Inquiry":
                        inquiry.setSelected(true);
                        break;
                    case "User Management":
                        userManagement.setSelected(true);
                        break;
                    default:
                        // If a module name doesn't match, do nothing or log it
                        System.out.println("Unknown module: " + moduleName);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showAlertError("Database Error", "Failed to load user privileges.");
        }
    }

    private void clearPrivilegeCheckboxes() {
        dashboard.setSelected(false);
        reports.setSelected(false);
        dailyLetters.setSelected(false);
        inquiry.setSelected(false);
        userManagement.setSelected(false);
    }

    private void insertUser() {
        if (validateForm()) {
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(password.getText(), salt);

            String insertUserQuery = "INSERT INTO Users (Name, User_Name, NIC, Email, Phone, User_Role, Password_Hash, Password_Salt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, name.getText());
                ps.setString(2, username.getText());
                ps.setString(3, nic.getText());
                ps.setString(4, email.getText());
                ps.setString(5, phone.getText());
                ps.setString(6, userRole.getValue());
                ps.setString(7, hashedPassword);
                ps.setString(8, salt);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int userID = rs.getInt(1);
                    insertPrivileges(userID);
                    loadUsers();
                    AlertUtil.showAlertSuccess("Success", "User registered successfully.");
                }
            } catch (SQLException e) {
                AlertUtil.showAlertError("Database Error", "Failed to insert user.");
                e.printStackTrace();
            }
        }
    }

    private void insertPrivileges(int userID) {
        String insertPrivilegeQuery = "INSERT INTO privileges (User_ID, M_ID) VALUES (?, " +
                "(SELECT M_ID FROM Modules WHERE M_Name = ?))";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertPrivilegeQuery)) {

            if (dashboard.isSelected()) {
                ps.setInt(1, userID);
                ps.setString(2, "Dashboard");
                ps.addBatch();
            }
            if (reports.isSelected()) {
                ps.setInt(1, userID);
                ps.setString(2, "Reports");
                ps.addBatch();
            }
            if (dailyLetters.isSelected()) {
                ps.setInt(1, userID);
                ps.setString(2, "Daily Letters");
                ps.addBatch();
            }
            if (inquiry.isSelected()) {
                ps.setInt(1, userID);
                ps.setString(2, "Inquiry");
                ps.addBatch();
            }
            if (userManagement.isSelected()) {
                ps.setInt(1, userID);
                ps.setString(2, "User Management");
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.showAlertError("Database Error", "Failed to assign privileges.");
        }
    }

    private boolean validateForm() {
        if (name.getText().isEmpty() || username.getText().isEmpty() || nic.getText().isEmpty() ||
                email.getText().isEmpty() || phone.getText().isEmpty() || password.getText().isEmpty() ||
                userRole.getValue() == null) {
            AlertUtil.showAlertWarning("Validation Error", "Please fill in all required fields.");
            return false;
        }
        return true;
    }

    private void deleteUser() {
        User selectedUser = userListTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null && AlertUtil.showConfirmationAlert("Delete User", "Are you sure you want to delete this user?")) {
            String deleteQuery = "DELETE FROM Users WHERE User_ID = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.setInt(1, selectedUser.getUserID());
                ps.executeUpdate();
                userData.remove(selectedUser);
                AlertUtil.showAlertSuccess("Success", "User deleted successfully.");
            } catch (SQLException e) {
                logger.info("This is an informational message.");
                logger.debug("This is a debug message.");
                logger.error("This is an error message.", e);
            }
        } else {
            AlertUtil.showAlertWarning("No Selection", "Please select a user to delete.");
        }
    }
}
