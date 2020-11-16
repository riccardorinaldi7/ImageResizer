package sample;

import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

public class Controller {
    public void openInfoDialog(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("L'applicazione sembra funzionare correttamente");
        alert.showAndWait();
    }
}
