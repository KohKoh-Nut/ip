package zabud.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import zabud.Zabud;

/**
 * Displays the Zabud JavaFX interface.
 */
public class Main extends Application {
    /**
     * Command application used by the GUI controller.
     */
    private final Zabud zabud = new Zabud();

    /**
     * Loads the main window and shows it on the primary stage.
     *
     * @param stage primary JavaFX stage.
     * @throws IOException if the FXML view cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = loader.load();
        loader.<MainWindow>getController().setZabud(zabud);

        stage.setScene(new Scene(root));
        stage.setTitle("Zabud");
        stage.setMinHeight(500);
        stage.setMinWidth(400);
        stage.show();
    }
}
