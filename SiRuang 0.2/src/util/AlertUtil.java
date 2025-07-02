package util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public interface AlertUtil {

    // Informasi biasa
    public static void show(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Error / validasi
    public static void error(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Terjadi Kesalahan");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Sukses eksplisit
    public static void success(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Berhasil");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Konfirmasi dengan aksi lanjut jika YES
    public static void confirm(String message, Runnable onYes) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Konfirmasi");
            alert.showAndWait().ifPresent(res -> {
                if (res == ButtonType.YES) {
                    onYes.run();
                }
            });
        });
    }
}
