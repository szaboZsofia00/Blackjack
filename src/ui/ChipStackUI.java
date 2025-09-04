package ui;

import game.ChipStack;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the user interface for managing the player's chip stack.
 * <p>
 *     This class creates and manages the betting UI components such as chip stacks, balance display, current bet
 *     display, and restart functionality.
 * </p>
 */
public class ChipStackUI {

    /** Text node displaying the player's current balance. */
    private static Text budgetText;
    /** Text node displaying the player's current bet. */
    private static Text betText;
    /** Container for the restart button. */
    private static VBox restartBox;
    /** Collection of all chip stack UI components. */
    private static List<StackPane> chipStacks = new ArrayList<>();

    //--------------------- UI UPDATE METHODS ----------------------------//

    /**
     * Updates the displayed balance text.
     * @param balance The player's new balance.
     */
    public static void setBalance(int balance) {
        budgetText.setText("Budget: $" + balance);
    }

    /**
     * Updates the displayed bet text.
     * @param bet The bet amount to display.
     */
    public static void setBet(int bet) {
        betText.setText("Your bet: $" + bet);
    }

    /**
     * Returns the list of chip stacks currently in the UI.
     * @return The list of chip stack UI elements.
     */
    public static List<StackPane> getChipStacks() {
        return chipStacks;
    }

    /**
     * Returns the restart button container.
     * @return The restart button's container.
     */
    public static VBox getRestartBox() {
        return restartBox;
    }

    // ------------------- UI CREATION METHODS --------------------//

    /**
     * Creates a single chip stack (stacked chip images and label) with a click listener that
     * increases the bet in {@link ChipStack}.
     * @param path The path to the chip image.
     * @param value The chip's values.
     * @return A VBox containing the chip stack and its label.
     */
    private static VBox createSingleStack(String path, int value) {
        StackPane stack = new StackPane();

        stack.setStyle("-fx-cursor: hand");
        stack.setOnMouseClicked(e -> {
            ChipStack.increaseBet(value);
            System.out.println(ChipStack.getBet()); /**/
        });

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.3);
        stack.setOnMouseEntered(e -> stack.setEffect(dropShadow));
        stack.setOnMouseExited(e -> stack.setEffect(null));

        int count = 10;
        for (int i=0; i< count; i++) {
            ImageView chip = new ImageView(new Image(path));
            chip.setFitHeight(30);
            chip.setFitWidth(30);
            chip.setTranslateY(-i * 5);

            stack.getChildren().add(chip);
        }

        chipStacks.add(stack);

        Text valueText = new Text("$" + value);
        valueText.setFont(Font.font(12));
        valueText.setStyle("-fx-fill: white");

        VBox stackWithValue = new VBox(5);
        stackWithValue.getChildren().addAll(stack,valueText);
        stackWithValue.setStyle("-fx-alignment: center");

        return stackWithValue;
    }

    /**
     * Creates an HBox containing all available chip stacks (red, blue, green, black).
     * @return An HBox with chip stacks.
     */
    private static HBox createAllStack() {

        HBox stacks = new HBox(5);

        VBox redStack = createSingleStack("/chips/red_chip.png",5);
        VBox blueStack = createSingleStack("/chips/blue_chip.png",10);
        VBox greenStack = createSingleStack("/chips/green_chip.png", 25);
        VBox blackStack = createSingleStack("/chips/black_chip.png", 100);

        stacks.getChildren().addAll(redStack,blueStack, greenStack,blackStack);
        return stacks;
    }

    /**
     * Creates a container showing the chip stacks and the current balance.
     * @param balance The starting balance.
     * @return A VBox container.
     */
    private static VBox createStackAndBudgetBox(int balance) {

        VBox budgetBox = new VBox(5);
        budgetBox.setAlignment(Pos.CENTER);

        budgetText = new Text("Budget: $" + balance);
        budgetText.setFont(Font.font(14));
        budgetText.setStyle("-fx-fill: white");

        HBox stack = createAllStack();

        budgetBox.getChildren().addAll(stack,budgetText);
        return budgetBox;
    }

    /**
     * Creates a container for the bet display and bet restart button.
     * @return An HBox container.
     */
    private static HBox createBetBox() {
        HBox betBox = new HBox(10);
        betBox.setAlignment(Pos.CENTER);

        VBox restartBox = createRestartButton();

        betText = new Text("Your bet: $" + 0);
        betText.setFont(Font.font(14));
        betText.setStyle("-fx-fill: white");

        betBox.getChildren().addAll(betText,restartBox);
        return betBox;
    }

    /**
     * Creates a restart button, which resets the bet when clicked on it.
     * @return A VBox container.
     */
    private static VBox createRestartButton() {
        restartBox = new VBox();
        restartBox.setAlignment(Pos.CENTER);
        restartBox.setStyle("-fx-cursor: hand");

        restartBox.setOnMouseClicked(e -> {
            ChipStack.restartBet();
            betText.setText("Your bet: $" + 0);
        });

        ImageView restart = new ImageView(new Image("restart.png"));
        restart.setFitWidth(20);
        restart.setFitHeight(20);

        restartBox.getChildren().add(restart);
        return restartBox;
    }

    /**
     * Creates the entire chip console, containing the chip stacks, the bet and balance displays and the restart
     * button.
     * @param balance The starting balance.
     * @return A VBox containing all chip console UI components.
     */
    public static VBox createChipConsole(int balance) {
        VBox console = new VBox(65);
        console.setAlignment(Pos.CENTER);

        HBox betBox = createBetBox();
        VBox moneyBox = createStackAndBudgetBox(balance);
        console.getChildren().addAll(betBox, moneyBox);


        AnchorPane.setBottomAnchor(console, (double) 70);
        AnchorPane.setRightAnchor(console, (double) 15);

        return console;
    }

}
