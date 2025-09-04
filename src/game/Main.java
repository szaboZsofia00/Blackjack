package game;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.Menu;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new Menu(stage);
    }
}