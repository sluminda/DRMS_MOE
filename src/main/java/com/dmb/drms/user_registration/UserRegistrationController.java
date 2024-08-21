package com.dmb.drms.user_registration;

import com.dmb.drms.utils.AlertUtil;
import com.dmb.drms.utils.DBConnection;
import com.dmb.drms.utils.MainAppController;
import com.dmb.drms.utils.PasswordUtil;
import com.dmb.drms.utils.sql.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UserRegistrationController extends MainAppController {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

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
    private CheckBox dailyLetters;
    @FXML
    private CheckBox inquiry;
    @FXML
    private CheckBox reports;
    @FXML
    private CheckBox tableViews;
    @FXML
    private CheckBox masterTables;
    @FXML
    private CheckBox userManagement;
    @FXML
    private Button insertUser;
    @FXML
    private Button updateUser;
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

    private final ObservableList<User> userData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        userRole.setItems(FXCollections.observableArrayList("Super Admin", "Admin"));
        password.setText("1234");
        loadUserData();

        userListTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                populateFormFields();
            }
        });

        colID.setCellValueFactory(cellData -> cellData.getValue().userIDProperty().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        colNIC.setCellValueFactory(cellData -> cellData.getValue().nicProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colPhone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        colUserRole.setCellValueFactory(cellData -> cellData.getValue().userRoleProperty());

        userListTable.setItems(userData);
    }

    @FXML
    private void insertUser() {
        if (!validateForm()) {
            return;
        }

        String userNameValue = username.getText().trim();
        String passwordValue = password.getText().trim();

        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(passwordValue, salt);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DBConnection.getConnection()) {
                    conn.setAutoCommit(false); // Start transaction

                    String insertUserSQL = "INSERT INTO Users (Name, User_Name, NIC, Email, Phone, User_Role, Password_Hash, Password_Salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {

                        stmt.setString(1, name.getText().trim());
                        stmt.setString(2, userNameValue);
                        stmt.setString(3, nic.getText().trim());
                        stmt.setString(4, email.getText().trim());
                        stmt.setString(5, phone.getText().trim());
                        stmt.setString(6, userRole.getValue());
                        stmt.setString(7, hashedPassword);
                        stmt.setString(8, salt);

                        stmt.executeUpdate();

                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int userId = generatedKeys.getInt(1);
                                insertUserPrivileges(conn, userId);
                            }
                        }
                    }
                    conn.commit(); // Commit transaction
                } catch (SQLException e) {
                    logger.error("Database Error: Error inserting user", e); // Log error to Logback
                    AlertUtil.showAlertError("Database Error", "Error inserting user: " + e.getMessage());
                    throw e;
                }
                return null;
            }

            @Override
            protected void succeeded() {
                AlertUtil.showAlertSuccess("Success", "User registered successfully!");
                clearForm();
                loadUserData();
            }

            @Override
            protected void failed() {
                logger.error("Failed to register user"); // Log generic failure message
                AlertUtil.showAlertError("Database Error", "Failed to register user.");
            }
        };

        new Thread(task).start(); // Run the task in the background
    }

    @FXML
    private void updateUser() {
        User selectedUser = userListTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtil.showAlertError("Selection Error", "No user selected for updating!");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DBConnection.getConnection()) {
                    conn.setAutoCommit(false); // Start transaction

                    String updateUserSQL = "UPDATE Users SET Name = ?, User_Name = ?, NIC = ?, Email = ?, Phone = ?, User_Role = ? WHERE User_ID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateUserSQL)) {

                        stmt.setString(1, name.getText().trim());
                        stmt.setString(2, username.getText().trim());
                        stmt.setString(3, nic.getText().trim());
                        stmt.setString(4, email.getText().trim());
                        stmt.setString(5, phone.getText().trim());
                        stmt.setString(6, userRole.getValue());
                        stmt.setInt(7, selectedUser.getUserID());

                        stmt.executeUpdate();
                        updateUserPrivileges(conn, selectedUser.getUserID());
                    }
                    conn.commit(); // Commit transaction
                } catch (SQLException e) {
                    logger.error("Database Error: Error updating user", e); // Log error to Logback
                    AlertUtil.showAlertError("Database Error", "Error updating user: " + e.getMessage());
                    throw e;
                }
                return null;
            }

            @Override
            protected void succeeded() {
                AlertUtil.showAlertSuccess("Success", "User updated successfully!");
                clearForm();
                loadUserData();
            }

            @Override
            protected void failed() {
                logger.error("Failed to update user"); // Log generic failure message
                AlertUtil.showAlertError("Database Error", "Failed to update user.");
            }
        };

        new Thread(task).start(); // Run the task in the background
    }

    @FXML
    private void deleteUser() {
        User selectedUser = userListTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtil.showAlertError("Selection Error", "No user selected for deletion!");
            return;
        }

        boolean confirmed = AlertUtil.showConfirmationAlert("Confirm Deletion", "Are you sure you want to delete this user?");
        if (!confirmed) return;

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DBConnection.getConnection()) {
                    String deleteUserSQL = "DELETE FROM Users WHERE User_ID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(deleteUserSQL)) {
                        stmt.setInt(1, selectedUser.getUserID());
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    logger.error("Database Error: Error deleting user", e); // Log error to Logback
                    AlertUtil.showAlertError("Database Error", "Error deleting user: " + e.getMessage());
                    throw e;
                }
                return null;
            }

            @Override
            protected void succeeded() {
                AlertUtil.showAlertSuccess("Success", "User deleted successfully!");
                clearForm();
                loadUserData();
            }

            @Override
            protected void failed() {
                logger.error("Failed to delete user"); // Log generic failure message
                AlertUtil.showAlertError("Database Error", "Failed to delete user.");
            }
        };

        new Thread(task).start(); // Run the task in the background
    }

    private void loadUserData() {
        userData.clear();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DBConnection.getConnection()) {
                    String fetchUsersSQL = "SELECT User_ID, Name, User_Name, NIC, Email, Phone, User_Role FROM Users";
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(fetchUsersSQL)) {

                        while (rs.next()) {
                            User user = new User(rs.getInt("User_ID"), rs.getString("Name"), rs.getString("User_Name"),
                                    rs.getString("NIC"), rs.getString("Email"), rs.getString("Phone"),
                                    rs.getString("User_Role"));
                            userData.add(user);
                        }
                    }
                } catch (SQLException e) {
                    logger.error("Database Error: Error loading user data. SQLState: {}, ErrorCode: {}, Message: {}",
                            e.getSQLState(), e.getErrorCode(), e.getMessage(), e); // Log error with more context
                    AlertUtil.showAlertError("Database Error", "Error loading user data: " + e.getMessage());
                    throw e;
                }
                return null;
            }
        };

        new Thread(task).start(); // Run the task in the background
    }


    private void insertUserPrivileges(Connection conn, int userId) throws SQLException {
        String insertPrivilegeSQL = "INSERT INTO Privileges (User_ID, M_ID) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertPrivilegeSQL)) {
            if (dashboard.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 1); // Assuming M_ID for Dashboard is 1
                stmt.addBatch();
            }
            if (dailyLetters.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 2); // Assuming M_ID for Daily Letters is 2
                stmt.addBatch();
            }
            if (inquiry.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 3); // Assuming M_ID for Inquiry is 3
                stmt.addBatch();
            }
            if (reports.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 4); // Assuming M_ID for Reports is 4
                stmt.addBatch();
            }
            if (tableViews.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 5); // Assuming M_ID for Table Views is 5
                stmt.addBatch();
            }
            if (masterTables.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 6); // Assuming M_ID for Master Tables is 6
                stmt.addBatch();
            }
            if (userManagement.isSelected()) {
                stmt.setInt(1, userId);
                stmt.setInt(2, 7); // Assuming M_ID for User Management is 7
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }


    private void updateUserPrivileges(Connection conn, int userId) throws SQLException {
        String deletePrivilegeSQL = "DELETE FROM Privileges WHERE User_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deletePrivilegeSQL)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }

        insertUserPrivileges(conn, userId);
    }

    private void populateFormFields() {
        User selectedUser = userListTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            name.setText(selectedUser.getName());
            username.setText(selectedUser.getUsername());
            nic.setText(selectedUser.getNic());
            email.setText(selectedUser.getEmail());
            phone.setText(selectedUser.getPhone());
            userRole.setValue(selectedUser.getUserRole());
            loadUserPrivileges(selectedUser.getUserID());
        }
    }

    private void loadUserPrivileges(int userId) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DBConnection.getConnection()) {
                    String fetchPrivilegesSQL = "SELECT M_ID FROM Privileges WHERE User_ID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(fetchPrivilegesSQL)) {
                        stmt.setInt(1, userId);
                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                int moduleId = rs.getInt("M_ID");
                                switch (moduleId) {
                                    case 1:
                                        dashboard.setSelected(true);
                                        break;
                                    case 2:
                                        dailyLetters.setSelected(true);
                                        break;
                                    case 3:
                                        inquiry.setSelected(true);
                                        break;
                                    case 4:
                                        reports.setSelected(true);
                                        break;
                                    case 5:
                                        tableViews.setSelected(true);
                                        break;
                                    case 6:
                                        masterTables.setSelected(true);
                                        break;
                                    case 7:
                                        userManagement.setSelected(true);
                                        break;
                                    default:
                                        logger.warn("Unknown module ID: {}", moduleId);
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    logger.error("Database Error: Error loading user privileges", e); // Log error to Logback
                    AlertUtil.showAlertError("Database Error", "Error loading user privileges: " + e.getMessage());
                    throw e;
                }
                return null;
            }

            @Override
            protected void succeeded() {
                logger.info("User privileges loaded successfully");
            }

            @Override
            protected void failed() {
                logger.error("Failed to load user privileges"); // Log generic failure message
                AlertUtil.showAlertError("Database Error", "Failed to load user privileges.");
            }
        };

        new Thread(task).start(); // Run the task in the background
    }


    private boolean validateForm() {
        if (name.getText().isEmpty() || username.getText().isEmpty() || nic.getText().isEmpty() || email.getText().isEmpty()
                || phone.getText().isEmpty() || password.getText().isEmpty() || userRole.getValue() == null) {
            AlertUtil.showAlertError("Validation Error", "Please fill in all fields.");
            return false;
        }
        return true;
    }

    @FXML
    private void clearForm() {
        name.clear();
        username.clear();
        nic.clear();
        email.clear();
        phone.clear();
        userRole.getSelectionModel().clearSelection();
        dashboard.setSelected(false);
        dailyLetters.setSelected(false);
        inquiry.setSelected(false);
        reports.setSelected(false);
        tableViews.setSelected(false);
        masterTables.setSelected(false);
        userManagement.setSelected(false);
    }
}
