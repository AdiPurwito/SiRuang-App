package util;

import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;
import java.util.stream.Collectors;

public class AutoCompleteTextField {

    public static void attachTo(TextField textField, List<String> sourceData) {
        ContextMenu suggestions = new ContextMenu();
        suggestions.setAutoHide(true);
        suggestions.setHideOnEscape(true);

        // Navigasi index manual
        final int[] currentIndex = {-1};

        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (suggestions.isShowing()) {
                if (event.getCode() == KeyCode.DOWN) {
                    currentIndex[0]++;
                    moveSelection(suggestions, currentIndex[0]);
                    event.consume();
                } else if (event.getCode() == KeyCode.UP) {
                    currentIndex[0]--;
                    moveSelection(suggestions, currentIndex[0]);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER) {
                    if (currentIndex[0] >= 0 && currentIndex[0] < suggestions.getItems().size()) {
                        MenuItem selected = suggestions.getItems().get(currentIndex[0]);
                        textField.setText(selected.getText());
                        suggestions.hide();
                        event.consume();
                    }
                }
            }
        });

        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentIndex[0] = -1;
            if (newText == null || newText.isEmpty()) {
                suggestions.hide();
            } else {
                List<String> filtered = sourceData.stream()
                        .filter(item -> item.toLowerCase().contains(newText.toLowerCase()))
                        .collect(Collectors.toList());

                if (!filtered.isEmpty()) {
                    List<MenuItem> items = filtered.stream().map(s -> {
                        MenuItem item = new MenuItem(s);
                        item.setOnAction(e -> {
                            textField.setText(s);
                            suggestions.hide();
                        });
                        return item;
                    }).collect(Collectors.toList());

                    suggestions.getItems().setAll(items);
                    if (!suggestions.isShowing()) {
                        suggestions.show(textField, Side.BOTTOM, 0, 0);
                    }
                } else {
                    suggestions.hide();
                }
            }
        });

        textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                suggestions.hide();
            }
        });
    }

    private static void moveSelection(ContextMenu menu, int index) {
        int total = menu.getItems().size();
        for (int i = 0; i < total; i++) {
            MenuItem item = menu.getItems().get(i);
            item.setStyle("-fx-background-color: transparent;");
        }

        if (index >= 0 && index < total) {
            MenuItem item = menu.getItems().get(index);
            item.setStyle("-fx-background-color: #cce5ff;"); // highlight biru muda
        }
    }
}
