package com.dmb.drms.user_registration;

import com.dmb.drms.utils.AlertUtil;
import com.dmb.drms.utils.DBConnection;
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

public class UserRegistrationController {

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
        userRole.setItems(FXCollections.observableArrayList("Owner", "Super Admin", "Admin"));
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
                    PreparedStatement stmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS);

                    stmt.setString(1, name.getText().trim());
                    stmt.setString(2, userNameValue);
                    stmt.setString(3, nic.getText().trim());
                    stmt.setString(4, email.getText().trim());
                    stmt.setString(5, phone.getText().trim());
                    stmt.setString(6, userRole.getValue());
                    stmt.setString(7, hashedPassword);
                    stmt.setString(8, salt);

                    stmt.executeUpdate();

                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    int userId = 0;
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    }

                    insertUserPrivileges(conn, userId);
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
                    PreparedStatement stmt = conn.prepareStatement(updateUserSQL);

                    stmt.setString(1, name.getText().trim());
                    stmt.setString(2, username.getText().trim());
                    stmt.setString(3, nic.getText().trim());
                    stmt.setString(4, email.getText().trim());
                    stmt.setString(5, phone.getText().trim());
                    stmt.setString(6, userRole.getValue());
                    stmt.setInt(7, selectedUser.getUserID());

                    stmt.executeUpdate();
                    updateUserPrivileges(conn, selectedUser.getUserID());
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
                    PreparedStatement stmt = conn.prepareStatement(deleteUserSQL);
                    stmt.setInt(1, selectedUser.getUserID());

                    stmt.executeUpdate();
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
                    String fetchUsersSQL = "SELECT * FROM Users";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(fetchUsersSQL);

                    while (rs.next()) {
                        User user = new User(rs.getInt("User_ID"), rs.getString("Name"), rs.getString("User_Name"),
                                rs.getString("NIC"), rs.getString("Email"), rs.getString("Phone"),
                                rs.getString("User_Role"));
                        userData.add(user);
                    }
                } catch (SQLException e) {
                    logger.error("Database Error: Error loading users", e); // Log error to Logback
                    AlertUtil.showAlertError("Database Error", "Error loading users: " + e.getMessage());
                    throw e;
                }
                return null;
            }

            @Override
            protected void failed() {
                logger.error("Failed to load users"); // Log generic failure message
                AlertUtil.showAlertError("Database Error", "Failed to load users.");
            }
        };

        new Thread(task).start(); // Run the task in the background
    }

    private void insertUserPrivileges(Connection conn, int userId) throws SQLException {
        String[] selectedPrivileges = {
                dashboard.isSelected() ? "Dashboard" : null,
                dailyLetters.isSelected() ? "Daily Letters" : null,
                inquiry.isSelected() ? "Inquiry" : null,
                reports.isSelected() ? "Reports" : null,
                tableViews.isSelected() ? "Table Views" : null,
                masterTables.isSelected() ? "Master Tables" : null,
                userManagement.isSelected() ? "User Management" : null
        };

        String getModuleIdSQL = "SELECT M_ID FROM Modules WHERE M_Name = ?";
        String insertPrivilegeSQL = "INSERT INTO Privileges (User_ID, M_ID) VALUES (?, ?)";

        try (PreparedStatement moduleStmt = conn.prepareStatement(getModuleIdSQL);
             PreparedStatement privilegeStmt = conn.prepareStatement(insertPrivilegeSQL)) {

            for (String privilege : selectedPrivileges) {
                if (privilege != null) {
                    // Query the Modules table to get the M_ID for this privilege
                    moduleStmt.setString(1, privilege);
                    ResultSet rs = moduleStmt.executeQuery();

                    if (rs.next()) {
                        int moduleId = rs.getInt("M_ID");

                        // Insert privilege into Privileges table
                        privilegeStmt.setInt(1, userId);
                        privilegeStmt.setInt(2, moduleId);
                        privilegeStmt.addBatch();
                    }
                }
            }
            privilegeStmt.executeBatch(); // Execute all privilege inserts in a batch
        }
    }


    private void updateUserPrivileges(Connection conn, int userId) throws SQLException {
        String deletePrivilegesSQL = "DELETE FROM Privileges WHERE User_ID = ?";
        String insertPrivilegesSQL = "INSERT INTO Privileges (User_ID, M_ID) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = conn.prepareStatement(deletePrivilegesSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertPrivilegesSQL)) {

            // Delete existing privileges
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();

            // Insert new privileges
            String[] selectedPrivileges = {
                    dashboard.isSelected() ? "Dashboard" : null,
                    dailyLetters.isSelected() ? "Daily Letters" : null,
                    inquiry.isSelected() ? "Inquiry" : null,
                    reports.isSelected() ? "Reports" : null,
                    tableViews.isSelected() ? "Table Views" : null,
                    masterTables.isSelected() ? "Master Tables" : null,
                    userManagement.isSelected() ? "User Management" : null
            };

            String getModuleIdSQL = "SELECT M_ID FROM Modules WHERE M_Name = ?";

            try (PreparedStatement moduleStmt = conn.prepareStatement(getModuleIdSQL)) {
                for (String privilege : selectedPrivileges) {
                    if (privilege != null) {
                        moduleStmt.setString(1, privilege);
                        ResultSet rs = moduleStmt.executeQuery();

                        if (rs.next()) {
                            int moduleId = rs.getInt("M_ID");

                            insertStmt.setInt(1, userId);
                            insertStmt.setInt(2, moduleId);
                            insertStmt.addBatch();
                        }
                    }
                }
                insertStmt.executeBatch(); // Execute all privilege inserts in a batch
            }
        }
    }


    private boolean validateForm() {
        if (name.getText().trim().isEmpty() || username.getText().trim().isEmpty() || nic.getText().trim().isEmpty()
                || email.getText().trim().isEmpty() || phone.getText().trim().isEmpty() || userRole.getValue() == null) {
            AlertUtil.showAlertError("Validation Error", "All fields must be filled!");
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

    private void populateFormFields() {
        User selectedUser = userListTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            name.setText(selectedUser.getName());
            username.setText(selectedUser.getUsername());
            nic.setText(selectedUser.getNic());
            email.setText(selectedUser.getEmail());
            phone.setText(selectedUser.getPhone());
            userRole.setValue(selectedUser.getUserRole());

            // Load user privileges
            loadUserPrivileges(selectedUser.getUserID());
        }
    }


    private void loadUserPrivileges(int userId) {
        String getPrivilegesSQL = "SELECT M_Name FROM Modules m JOIN Privileges p ON m.M_ID = p.M_ID WHERE p.User_ID = ?";

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(getPrivilegesSQL)) {
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    // Clear all checkboxes first
                    dashboard.setSelected(false);
                    dailyLetters.setSelected(false);
                    inquiry.setSelected(false);
                    reports.setSelected(false);
                    tableViews.setSelected(false);
                    masterTables.setSelected(false);
                    userManagement.setSelected(false);

                    // Set checkboxes based on privileges
                    while (rs.next()) {
                        String moduleName = rs.getString("M_Name");
                        switch (moduleName) {
                            case "Dashboard":
                                dashboard.setSelected(true);
                                break;
                            case "Daily Letters":
                                dailyLetters.setSelected(true);
                                break;
                            case "Inquiry":
                                inquiry.setSelected(true);
                                break;
                            case "Reports":
                                reports.setSelected(true);
                                break;
                            case "Table Views":
                                tableViews.setSelected(true);
                                break;
                            case "Master Tables":
                                masterTables.setSelected(true);
                                break;
                            case "User Management":
                                userManagement.setSelected(true);
                                break;
                        }
                    }
                } catch (SQLException e) {
                    logger.error("Database Error: Error loading user privileges", e);
                    AlertUtil.showAlertError("Database Error", "Error loading user privileges: " + e.getMessage());
                    throw e;
                }
                return null;
            }
        };

        new Thread(task).start(); // Run the task in the background
    }
}
