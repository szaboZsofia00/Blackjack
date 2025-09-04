package ui;

import deck.Card;
import game.Game;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import participants.Hand;
import utilz.Constants;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Represents the main game window for the Blackjack application.
 * <p>
 *     This class is responsible for creating and managing the UI layout, including player and dealer hand areas,
 *     player control buttons, overlay menu, evaluation messages, and game over messages. It interacts with the
 *     {@link Game} class to reflect game state changes on the UI.
 * </p>
 */
public class GameWindow {

    private final Stage stage;
    private final AnchorPane root;
    private final Scene scene;

    private Game game;

    private final int WIDTH = Menu.WIDTH;
    private final int HEIGHT = Menu.HEIGHT;

    private VBox menuOverlay;
    private HBox consoleBox;
    private VBox finishedHandBox;
    private HBox evaluationContainer;
    private HBox gameOverBox;

    private Button placeBetButton;

    private Button doubleDownButton;
    private Button splitButton;
    private Button surrenderButton;

    private HBox playerFirstHandBox;
    private HBox playerSecondHandBox;
    private HBox dealerHandBox;

    private final ArrayList<Text> resultTexts = new ArrayList<>();

    /**
     * Constructs a new {@code GameWindow}.
     * @param stage The primary JavaFX stage.
     * @param game The {@link Game} instance controlling the game logic.
     */
    public GameWindow(Stage stage, Game game) {
        this.stage = stage;
        this.root = new AnchorPane();
        this.scene = new Scene(root, WIDTH, HEIGHT);
        this.game = game;

        createStartingHandBoxes();
        createGameLayout();

        stage.setScene(scene);
    }

    //---------------------------- GETTERS -------------------------------//

    /** Returns the console containing the player's action buttons. */
    public HBox getConsoleBox() {
        return consoleBox;
    }
    /** Returns the box displaying the message when a hand is finished. */
    public VBox getFinishedHandBox() {
        return finishedHandBox;
    }
    /** Returns the evaluation container displaying the result of the round. */
    public HBox getEvaluationBox() {
        return evaluationContainer;
    }
    /** Returnds the game over window. */
    public HBox getGameOverBox() {
        return gameOverBox;
    }
    /** Returns HBox containing the player's first hand. */
    public HBox getPlayerFirstHandBox() {
        return playerFirstHandBox;
    }
    /** Returns HBox containing the dealer's hand. */
    public HBox getDealerHandBox() {
        return dealerHandBox;
    }
    /** Returns HBox containing the player's second hand. */
    public HBox getPlayerSecondHandBox() {
        return playerSecondHandBox;
    }
    /**
     * Updates the HBox for the player's second hand.
     * @param playerSecondHandBox The new hand box.
     */
    public void setPlayerSecondHandBox(HBox playerSecondHandBox) {
        this.playerSecondHandBox = playerSecondHandBox;
    }
    /** Returns the button used for placing bets. */
    public Button getPlaceBetButton() {
        return placeBetButton;
    }
    /** Returns the split button. */
    public Button getSplitButton() {
        return splitButton;
    }
    /** Returns the double down button. */
    public Button getDoubleDownButton() {
        return doubleDownButton;
    }
    /** Returns the surrender button. */
    public Button getSurrenderButton() {
        return surrenderButton;
    }
    /** Returns the list of text elements used to display results. */
    public ArrayList<Text> getResultTexts() {
        return resultTexts;
    }

    //------------------- WINDOW CREATION ------------------------//

    /**
     * Sets the background image for the game table.
     */
    private void createBackground() {
        Image bcgr = new Image("blackjack-table-layout2.png");
        BackgroundSize backgroundSize = new BackgroundSize(WIDTH, HEIGHT, false, false, false, false);
        root.setBackground(new Background(new BackgroundImage(
                bcgr, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));
    }

    /**
     * Creates a menu button that toggles the overlay menu.
     * @return The menu button.
     */
    private Button createMenuButton() {
        Button menuButton = createButton("Menu",() -> game.showHidePopUpWindows(menuOverlay));
        return menuButton;
    }

    /**
     * Creates the overlay menu containing options like starting a new game, going back to
     * the main menu, or quiting the game.
     * @param stage The primary stage.
     * @return The overlay menu container.
     */
    private VBox createOverlayMenu(Stage stage) {
        menuOverlay = createVBox(15, 200, 250);

        Button newGameButton = createButton("New Game",
                () -> new Game(stage,game.getNumOfDecks(),game.getInitialBalance()));
        Button mainMenuButton = createButton("Main Menu", () -> new Menu(stage));
        Button quitButton = createButton("Quit", stage::close);

        menuOverlay.getChildren().addAll(newGameButton, mainMenuButton, quitButton);

        AnchorPane.setTopAnchor(menuOverlay, (double) (HEIGHT / 2 - 150));
        AnchorPane.setLeftAnchor(menuOverlay, (double) (WIDTH / 2 - 100));
        return menuOverlay;
    }

    /**
     * Creates the console containing the player action buttons: hit, stand, double down,
     * split, and surrender.
     * @return The console container.
     */
    private HBox createPlayerConsole() {
        consoleBox = createHBox();

        Button hitButton = createButton("Hit", () -> game.getPlayer().hit());
        Button standButton = createButton("Stand", () -> game.getPlayer().stand());
        doubleDownButton = createButton("Double Down", () -> game.getPlayer().doubleDown());
        splitButton = createButton("Split", () -> game.getPlayer().split());
        surrenderButton = createButton("Surrender", () -> game.getPlayer().surrender());

        consoleBox.getChildren().addAll(hitButton, standButton, doubleDownButton, splitButton, surrenderButton);
        AnchorPane.setBottomAnchor(consoleBox, (double) 50);
        AnchorPane.setLeftAnchor(consoleBox, (double) (WIDTH / 2 - 320));
        return consoleBox;
    }

    /**
     * Creates the button for placing bets.
     * @return The button.
     */
    private Button createPlaceBetButton() {
        placeBetButton = createButton("Place a bet", game::placeBet);

        AnchorPane.setLeftAnchor(placeBetButton, (double) (120));
        AnchorPane.setTopAnchor(placeBetButton, (double) (HEIGHT / 2 + 100));
        return placeBetButton;
    }

    /**
     * Creates the evaluation window that displays the results of a round.
     * @param resultTexts The list that stores result text references.
     * @return The evaluation container.
     */
    private HBox createEvaluationWindow(ArrayList<Text> resultTexts) {
        evaluationContainer = createHBox();

        VBox evaluationBox = createVBox(5, 220, 220);
        evaluationBox.setVisible(true);
        Text txt1 = createStyledText("",15);
        Text txt2 = createStyledText("",15);
        evaluationBox.getChildren().addAll(txt1, txt2);

        resultTexts.add(txt1);
        resultTexts.add(txt2);

        Button newRoundButton = createButton("New Round", () -> game.setUpNewRound());
        evaluationContainer.getChildren().addAll(evaluationBox, newRoundButton);

        AnchorPane.setTopAnchor(evaluationContainer, (double) (HEIGHT / 2 - 170));
        AnchorPane.setRightAnchor(evaluationContainer, (double) (WIDTH / 2 - 170));
        return evaluationContainer;
    }

    /**
     * Creates a game-over window with a "Main Menu" button.
     * @param stage The primary stage.
     * @return The game-over container
     */
    private HBox createGameOverWindow(Stage stage) {
        gameOverBox = createHBox();

        VBox txtContainer = createVBox(5,220,220);
        txtContainer.setVisible(true);
        Text txt = createStyledText("GAME OVER",40);
        Text txt2 = createStyledText("You don't have enogh money.",20);
        txtContainer.getChildren().addAll(txt,txt2);

        Button menuButton = createButton("Main Menu", () -> new Menu(stage));
        gameOverBox.getChildren().addAll(txtContainer,menuButton);

        AnchorPane.setTopAnchor(gameOverBox, (double) (HEIGHT / 2 - 140));
        AnchorPane.setRightAnchor(gameOverBox, (double) (WIDTH / 2 - 170));
        return gameOverBox;
    }

    /**
     * Creates a message box that shows if a hand was finished.
     * @param playerActiveHandIndex The index of the finished hand.
     * @return The message box.
     */
    private VBox createFinishedHandMsg(int playerActiveHandIndex) {
        finishedHandBox = createVBox(5,-1,-1);
        finishedHandBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2");
        Text msg = createStyledText("Hand " + (playerActiveHandIndex+1) + " is finished.", 12);
        finishedHandBox.getChildren().add(msg);

        AnchorPane.setBottomAnchor(finishedHandBox, (double) 90);
        AnchorPane.setLeftAnchor(finishedHandBox, (double) (WIDTH / 2 - 30));
        return finishedHandBox;
    }

    /**
     * Assembles all UI components into the root layout.
     */
    private void createGameLayout() {
        createBackground();

        Button menuButton = createMenuButton();
        VBox menuOverlayed = createOverlayMenu(stage);
        HBox playerConsole = createPlayerConsole();
        Button placeBetButton = createPlaceBetButton();
        HBox evaluationBox = createEvaluationWindow(resultTexts);
        HBox gameOverBox = createGameOverWindow(stage);
        VBox finishedHandMsg = createFinishedHandMsg(game.getPlayerActiveHandIndex());
        VBox chipConsole = ChipStackUI.createChipConsole(game.getInitialBalance());

        root.getChildren().addAll(menuButton,menuOverlayed,playerConsole,
                placeBetButton,evaluationBox,gameOverBox,finishedHandMsg,chipConsole);

    }

    /**
     * Creates and positions the initial player and dealer hand boxes.
     */
    private void createStartingHandBoxes() {
        playerFirstHandBox = createHandBox(HEIGHT - 300, WIDTH / 2 - 280,600);
        dealerHandBox = createHandBox(30, WIDTH / 2 - 180,400);
    }

    // ----------- HELPERS -----------------//

    /**
     * Creates a styles VBox container (not visible), with the given width and height.
     * @param spacing Spacing between children.
     * @param width The preferred width.
     * @param height The preferred height.
     * @return The VBox.
     */
    private VBox createVBox(double spacing, double width, double height) {
        VBox box = new VBox(spacing);
        box.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 8");
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(width);
        box.setPrefHeight(height);
        box.setVisible(false);
        return box;
    }

    /**
     * Creates a styled HBox container (not visible).
     * @return The HBox.
     */
    private HBox createHBox() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        box.setVisible(false);
        return box;
    }

    /**
     * Creates a button with the given label and action.
     * @param buttonName The button label.
     * @param action The action to execute on click.
     * @return The button.
     */
    private Button createButton(String buttonName, Runnable action) {
        Button button = new Button(buttonName);
        button.setPrefSize(100, 25);
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Creates a styled text with the specified content and font size.
     * @param s The text content.
     * @param size The font size.
     * @return The styled Text.
     */
    private Text createStyledText(String s, int size) {
        Text txt = new Text(s);
        txt.setFont(Font.font(size));
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setStyle("-fx-font-weight: bold");
        return txt;
    }

    /**
     * Creates and positions an HBox for representing a hand.
     * @param fromTop Distance from the top anchor.
     * @param fromLeft Distance from the left anchor.
     * @param width The preferred width.
     * @return The hand box.
     */
    public HBox createHandBox(int fromTop, int fromLeft, int width) {
        HBox handBox = new HBox(-20);
        handBox.setAlignment(Pos.CENTER);
        handBox.setPrefWidth(width);
        handBox.setPrefHeight(150);

        root.getChildren().add(handBox);

        AnchorPane.setTopAnchor(handBox, (double) fromTop);
        AnchorPane.setLeftAnchor(handBox, (double) fromLeft);

        return handBox;
    }

    // -------------- GAME INTERACTION -----------------------//

    /**
     * Starts a round by displaying the player's and dealer's initial hand. In the dealer's case
     * one card is shown, one is hidden.
     * @param playerHand The player's hand.
     * @param dealerHand The dealer's hand.
     */
    public void startRound(Hand playerHand, Hand dealerHand) {
        showCards(playerHand, playerFirstHandBox);
        showInitialDealerHand(dealerHand);
    }

    /**
     * Displays all cards in a hand in the given HBox.
     * @param h The hand.
     * @param handBox The UI container.
     */
    private void showCards(Hand h, HBox handBox) {
        for (Card c: h.getCards()) {
            ImageView cardImg = c.getCardImage().getImage();
            handBox.getChildren().add(cardImg);
        }
    }

    /**
     * Displays the dealer's initial hand. One visible card and one facedown card.
     * @param dealerHand The dealer's hand.
     */
    private void showInitialDealerHand(Hand dealerHand) {
        ImageView dealerCardImg = dealerHand.getCards().getFirst().getCardImage().getImage();
        ImageView blankCardImg = Constants.CardImage.BACK.getImage();
        dealerHandBox.getChildren().addAll(dealerCardImg,blankCardImg);
    }

    /**
     * Handles the UI update when the player splits their hand. Disable certain actions (according
     * to the casino rules) and creates a second hand box.
     */
    public void split() {
        splitButton.setDisable(true);
        doubleDownButton.setDisable(true);
        surrenderButton.setDisable(true);

        playerFirstHandBox.setPrefWidth(300);
        playerSecondHandBox = createHandBox(HEIGHT - 300, (WIDTH / 2 + 20), 300);
        playerFirstHandBox.getChildren().clear();
        showCards(game.getPlayer().getHands().get(0), playerFirstHandBox);
        showCards(game.getPlayer().getHands().get(1), playerSecondHandBox);
    }

    /**
     * Updates the dealer's hand displa.
     */
    public void dealerMove() {
        dealerHandBox.getChildren().clear();
        showCards(game.getDealer().getHand(), dealerHandBox);
    }
}
