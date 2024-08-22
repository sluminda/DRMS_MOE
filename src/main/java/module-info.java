module com.dmb.drms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.codec;
    requires org.slf4j;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;


    opens com.dmb.drms to javafx.fxml;
    exports com.dmb.drms;

    opens com.dmb.drms.utils to javafx.fxml;
    exports com.dmb.drms.utils;

    opens com.dmb.drms.utils.sql to javafx.fxml;
    exports com.dmb.drms.utils.sql;

    opens com.dmb.drms.main_modules.user_registration to javafx.fxml;
    exports com.dmb.drms.main_modules.user_registration;

    opens com.dmb.drms.main_modules.extra to javafx.fxml;
    exports com.dmb.drms.main_modules.extra;

    opens com.dmb.drms.main_modules.table_views to javafx.fxml;
    exports com.dmb.drms.main_modules.table_views;
}