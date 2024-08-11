module com.dmb.drms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.dmb.drms to javafx.fxml;
    exports com.dmb.drms;

    opens com.dmb.drms.utils to javafx.fxml;
    exports com.dmb.drms.utils;
}