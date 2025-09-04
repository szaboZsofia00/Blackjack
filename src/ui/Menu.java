package ui;

import game.Game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Represents the main menu of the Blackjack game.
 * <p>
 * The menu provides button for starting the game, accessing the settings, viewing rules.
 * and closing the application.
 * </p>
 */

public class Menu {

    private final Scene scene;
    private final Stage stage;
    private final StackPane root;

    private VBox settingsBox;
    private VBox rulesBox;

    private int numOfDecks;
    private int initialBudget;

    /** The fixed width of the application window. Default is {@value}. */
    public static final int WIDTH = 1000;
    /** The fixed height of the application window. Default is {@value}. */
    public static final int HEIGHT = 700;

    /**
     * Creates a new Menu for the Blackjack game.
     *
     * @param stage The primary stage where the menu will be displayed.
     */
    public Menu(Stage stage) {
        this.stage = stage;
        root = new StackPane();
        scene = new Scene(root);

        numOfDecks = 6;
        initialBudget = 1000;

        createStage();
        createMenu();
        createSettingsWindow();
        createRulesWindow();
    }

    /**
     *Configures and displays the primary stage with fixed sizes.
     */
    private void createStage() {

        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Creates the menu layout and adds buttons for starting the game, accessing settings, reading rules and
     * closing the application.
     */
    private void createMenu() {

        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);

        Button startGameButton = createButton("Start Game", () -> new Game(stage,numOfDecks,initialBudget));
        Button settingsButton = createButton("Settings", () -> activateSettingsWindow(true));
        Button rulesButton = createButton("Rules", () -> activateRulesWindow(true));
        Button quitButton = createButton("Quit", stage::close);

        menu.getChildren().addAll(startGameButton,settingsButton,rulesButton,quitButton);
        root.getChildren().add(menu);
    }

    /**
     * Creates a button with a given name and fixed size.
     *
     * @param buttonName The label displayed on the button.
     * @return A Button instance with the given label.
     */
    private Button createButton(String buttonName, Runnable action) {
        Button button = new Button(buttonName);
        button.setPrefSize(200, 50);
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Creates a box for the settings window. The user can chane the number of decks use in the game
     * between 1 and 8. Also can change the initial balance of the player between 1000 and 10 000.
     */
    private void createSettingsWindow() {
        /*
        surrender:
         - make this optional (in settings) because only some of the casinos allow this
         - not available in n=2 decks game
         */

        settingsBox = new VBox(40);
        settingsBox.setAlignment(Pos.TOP_LEFT);
        settingsBox.setMaxSize(350,450);
        settingsBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 4");
        settingsBox.setPadding(new Insets(40, 20, 40, 20));
        settingsBox.setVisible(false);

        HBox numOfDeckBox = new HBox(10);
        Label numOfDeckLabel = new Label("Number of decks: ");
        Slider numOfDeckSlider = new Slider(1,8,numOfDecks);
        numOfDeckSlider.setMajorTickUnit(1);
        numOfDeckSlider.setMinorTickCount(0);
        numOfDeckSlider.setSnapToTicks(true);
        numOfDeckSlider.setShowTickMarks(true);
        numOfDeckSlider.setShowTickLabels(true);
        numOfDeckBox.getChildren().addAll(numOfDeckLabel,numOfDeckSlider);

        HBox budgetBox = new HBox(10);
        Label budgetLabel = new Label("Initial balance: ");
        Slider budgetSlider = new Slider(1000,10000,initialBudget);
        budgetSlider.setMajorTickUnit(1000);
        budgetSlider.setMinorTickCount(0);
        budgetSlider.setSnapToTicks(true);
        budgetSlider.setShowTickMarks(true);
        budgetSlider.setShowTickLabels(true);
        budgetBox.getChildren().addAll(budgetLabel,budgetSlider);

        VBox buttonBox = new VBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            numOfDecks = (int) numOfDeckSlider.getValue();
            initialBudget = (int) budgetSlider.getValue();

            settingsBox.setVisible(false);
        });
        buttonBox.getChildren().add(saveButton);

        settingsBox.getChildren().addAll(numOfDeckBox, budgetBox, buttonBox);
        AnchorPane.setTopAnchor(settingsBox, (double) (HEIGHT / 2 - 150));
        AnchorPane.setLeftAnchor(settingsBox, (double) (WIDTH / 2 - 150));
        root.getChildren().add(settingsBox);
    }

    /**
     * Show or hide the settings window.
     * @param activate True to show, false to hide.
     */
    private void activateSettingsWindow(boolean activate) {
        settingsBox.setVisible(activate);
    }

    /**
     * Loads a file from the resource path using the given filename.
     * @param filename The name of the resource file to load.
     * @return A {@link File} object pointing to the resource, or {@code null} if the file is not null.
     */
    private File readInFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(filename);

        if (resource == null) {
            System.out.println("rules.txt is not found!");
            return null;
        }
        File file = new File(resource.getFile());
        return file;
    }

    /**
     * Returns a {@link StringBuilder} by reading the content of the resource file.
     * @param filename The name of the resource file to read.
     * @return A {@link StringBuilder} containing the file's contents, or {@code null} if the file could not be loaded.
     */
    private StringBuilder createStringFromFile(String filename) {
        File file = readInFile(filename);
        StringBuilder sb = new StringBuilder();

        if (file == null) {
            return null;
        }

        try(Scanner sc = new Scanner(file)) {
            while(sc.hasNextLine()) {
                sb.append(sc.nextLine()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return sb;
    }

    /**
     * Creates a box for the rules window. It contains the basic rules for the Blackjack game.
     */
    private void createRulesWindow() {
        StringBuilder content = createStringFromFile("rules.txt");

        if(content == null) {
            content = new StringBuilder("Rules file not found!");
        }

        rulesBox = new VBox(40);
        rulesBox.setAlignment(Pos.TOP_LEFT);
        rulesBox.setMaxSize(500,470);
        rulesBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2");
        rulesBox.setVisible(false);

        Label txt = new Label(content.toString());
        txt.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(txt);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(500, 400);
        scrollPane.setPadding(new Insets(40, 20, 40, 20));

        VBox buttonBox = new VBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button menuButton = new Button("Main Menu");
        menuButton.setOnAction(e -> {
            rulesBox.setVisible(false);
        });
        buttonBox.getChildren().add(menuButton);

        rulesBox.getChildren().addAll(scrollPane, buttonBox);
        root.getChildren().add(rulesBox);
    }

    /**
     * Show/hide the rules window.
     * @param activate True to show, false to hide.
     */
    private void activateRulesWindow(boolean activate) {
        rulesBox.setVisible(activate);
    }

}
