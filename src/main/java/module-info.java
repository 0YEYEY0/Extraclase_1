module com.example.extraclase_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.extraclase_1 to javafx.fxml;
    exports com.example.extraclase_1;
    exports sample;
    opens sample to javafx.fxml;
}