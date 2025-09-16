package gui;

import chatbot.Bubbles;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Bubbles bubbles;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image bubblesImage = new Image(this.getClass().getResourceAsStream("/images/Bubbles.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Bubbles instance */
    public void setBubbles(Bubbles b) {
        bubbles = b;

        // I asked ChatGPT on how to show the welcome message cleanly
        dialogContainer.getChildren().add(
                DialogBox.getBubblesDialog(bubbles.run(), bubblesImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing bubbles' reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (!input.isEmpty()) {
            String response = bubbles.getResponse(input);
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getBubblesDialog(response, bubblesImage)
            );

            if (input.equalsIgnoreCase("bye")) {
                Platform.exit();
            }
        }

        userInput.clear();
    }
}
