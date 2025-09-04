package game;

import deck.Deck;
import participants.Dealer;
import participants.Hand;
import participants.Player;
import ui.GameWindow;
import utilz.Constants.*;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Represents the game logic and the flow of a Blackjack game.
 * <p>
 *    The {@code Game} class coordinates interactions between the {@link Player}, {@link Dealer},
 *    {@link Deck}, and {@link ChipStack}, while updating the {@link GameWindow} UI.
 * </p>
 * <p>
 *     The game instance manages the game rounds, evaluating results, and handles payouts. It also
 *     controls turn progression between the player (and their hands) and the dealer.
 * </p>
 */
public class Game {

    private final Deck deck;
    private final Dealer dealer;
    private final Player player;
    private final ChipStack cS;

    private final GameWindow ui;

    private final int numOfDecks;
    private final int initialBalance;
    private int playerActiveHandIndex;

    /** The result for each of the player's hand. */
    private ArrayList<Result> res = new ArrayList<>();


    /**
     * Constructs a new Blackjack game with the specified number of decks and the initial balance.
     * @param stage The primary stage for displaying the UI.
     * @param numOfDecks The number of decks used in the game.
     * @param initialBalance The player's starting balance.
     */
    public Game(Stage stage, int numOfDecks, int initialBalance) {

        this.numOfDecks = numOfDecks;
        this.initialBalance = initialBalance;

        this.ui = new GameWindow(stage, this);

        this.cS = new ChipStack(initialBalance);
        this.deck = new Deck(numOfDecks);
        this.dealer = new Dealer();
        this.player = new Player(deck,this,cS);
        this.playerActiveHandIndex = player.getPlayerActiveHandIndex();

        res.add(Result.NONE);
        res.add(Result.NONE);
    }

    //---------------------- HELPERS----------------------------//

    /**
     * Returns the result message for the given outcome.
     * @param result The round result.
     * @param handIndex The index of the hand that the result belongs to.
     * @return The result message string.
     */
    private String resultMsg(Result result, int handIndex) {
        return switch (result) {
            case WIN, BLACKJACK -> "Hand " + (handIndex + 1) + ": YOU WON";
            case LOSE -> "Hand " + (handIndex + 1) + ": YOU LOST";
            case TIE -> "Hand " + (handIndex + 1) + ": IT'S A TIE";
            case SURRENDERED -> "Hand " + (handIndex + 1) + ": \nYOU SURRENDERED";
            default -> "";
        };
    }

    //------------------ UI CONTOL HELPERS------------------------//

    /** Toggles the visibility of the given {@link VBox}. */
    public void showHidePopUpWindows(VBox box) {
        box.setVisible(!box.isVisible());
    }

    /**
     * Set the visibility of the given {@link VBox}.
     * @param box The box to show/hide.
     * @param show Whether to show the box or not. True to show, false to hide.
     */
    public void showHidePopUpWindows(VBox box, boolean show) { box.setVisible(show); }

    /** Toggles the visibility of the given {@link HBox}. */
    public void showHidePopUpWindows(HBox box) {
        box.setVisible(!box.isVisible());
    }

    /**
     * Set the visibility of the given {@link HBox}.
     * @param box The box to show/hide.
     * @param show Whether to show the box or not. True to show, false to hide.
     */
    public void showHidePopUpWindows(HBox box, boolean show) { box.setVisible(show); }

    /** Toggles the visibility of a button. */
    private void showHideButtons(Button btn) { btn.setVisible(!btn.isVisible()); }

    /**
     * Displays or hides the evaluation window that shows the result of the round for all hands.
     * @param activate True to show, false to hide the window.
     */
    private void activateEvaluationWindow(boolean activate) {
        for (int i = 0; i < (playerActiveHandIndex + 1); i++) {
            Result result = res.get(i);
            String msg = resultMsg(result, i);
            ui.getResultTexts().get(i).setText(msg);
        }
        ui.getEvaluationBox().setVisible(activate);
    }


    //------------------- ROUND SETUP & FLOW ---------------//

    /** Resets th game state and UI for a new round. */
    public void setUpNewRound() {
        activateEvaluationWindow(false);
        showHidePopUpWindows(ui.getConsoleBox(),false);

        res.set(0, Result.NONE);
        res.set(1, Result.NONE);

        for (Text txt : ui.getResultTexts()) {
            txt.setText("");
        }

        ui.getPlayerFirstHandBox().getChildren().clear();
        ui.getPlayerFirstHandBox().setPrefWidth(600);
        ui.getDealerHandBox().getChildren().clear();
        if (ui.getPlayerSecondHandBox() != null) {
            ui.getPlayerSecondHandBox().getChildren().clear();
            ui.setPlayerSecondHandBox(null);
        }


        cS.setBet(0);
        cS.toggleStackControl();

        ui.getDoubleDownButton().setDisable(false);
        ui.getSurrenderButton().setDisable(false);

        if(cS.getBalance() < 5) showHidePopUpWindows(ui.getGameOverBox());
        else showHideButtons(ui.getPlaceBetButton());
    }

    /**
     * Starts a new round by dealing two cards for the player and dealer, and updating the UI.
     */
    private void startRound() {
        int bet = cS.getBet();
        player.startRound(bet);
        dealer.startRound();

        Hand playerHand = player.getHands().getFirst();
        Hand dealerHand = dealer.getHand();

        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard()); //player cannot see this

        ui.startRound(playerHand,dealerHand);
    }

    /**
     * Handles placing a bet, updating ui, deals initial cards and checks for immediate
     * results.
     */
    public void placeBet() {
        if (cS.getBet() > 0) {

            cS.toggleStackControl();
            showHideButtons(ui.getPlaceBetButton());

            startRound();

            if(cS.getBet() > cS.getBalance()) {
                ui.getSplitButton().setDisable(true);
                ui.getDoubleDownButton().setDisable(true);
            } else {
                ui.getSplitButton().setDisable(!player.getHands().getFirst().canSplit());
            }

            cS.setBalance(cS.getBalance());

            res.set(0,player.getHands().getFirst().evaluateHand());
            if(res.getFirst() != Result.NONE) {
                showHidePopUpWindows(ui.getConsoleBox(),false);
                activateEvaluationWindow(true);
                payOut();
            } else {
                showHidePopUpWindows(ui.getConsoleBox(),true);
            }

        }
    }

    /**
     * Moves the game flow to the next hand if the player has split, or to the dealer's turn
     * if no more hands remain.
     */
    public void moveToNextHandOrDealer() {
        if(playerActiveHandIndex + 1 < player.getHands().size()) {
            playerActiveHandIndex++;
            player.setPlayerActiveHandIndex(playerActiveHandIndex);
            if (res.get(playerActiveHandIndex) != Result.NONE) {
                showHidePopUpWindows(ui.getFinishedHandBox(),true);
                moveToNextHandOrDealer();
            }
        } else {
            showHidePopUpWindows(ui.getConsoleBox(),true);

            ui.dealerMove();

            if(res.getFirst() != Result.SURRENDERED) dealer.playTurn(deck,ui.getDealerHandBox());

            for (int i = 0; i < playerActiveHandIndex+1; i++) {
                System.out.println(i);
                if(res.get(i).equals(Result.NONE)) {
                    res.set(i,evaluateRound(i));
                }
            }

            activateEvaluationWindow(true);
            payOut();
            showHidePopUpWindows(ui.getFinishedHandBox(),false);

            playerActiveHandIndex = 0;
            player.setPlayerActiveHandIndex(playerActiveHandIndex);

        }
    }

    /**
     * Evaluate the outcome of the player's hand against the dealer's.
     * @param i The index of the player's hand.
     * @return The result of the hand (win, lose, tie, etc.)
     */
    private Result evaluateRound(int i) {

        Result res = Result.NONE;

        Hand dealerHand = dealer.getHand();
        Hand playerHand = player.getHands().get(i);

        if(dealerHand.isBlackjack()) {
            playerHand.setResult(Result.LOSE);
            res = Result.LOSE;
        } else if (dealerHand.isBusted()) {
            playerHand.setResult(Result.WIN);
            res = Result.WIN;
        } else if (playerHand.getSumOfValues() == dealerHand.getSumOfValues()) {
            playerHand.setResult(Result.TIE);
            res = Result.TIE;
        } else if (playerHand.getSumOfValues() > dealerHand.getSumOfValues()) {
            playerHand.setResult(Result.WIN);
            res = Result.WIN;
        } else if (playerHand.getSumOfValues() < dealerHand.getSumOfValues()) {
            playerHand.setResult(Result.LOSE);
            res = Result.LOSE;
        } else {
            System.out.println("Not defined case.");
            System.out.println(playerHand.getResult());
        }
        return res;
    }

    /**
     * Pays out winnings to the player based on the results of their hands.
     */
    private void payOut() {
        for (Hand h: player.getHands()) {
            int bet = h.getCurrentBet();

            if(h.getResult() == Result.BLACKJACK) {
                player.updateBalance(bet*5/2);
            }
            else if(h.getResult() == Result.WIN) {
                player.updateBalance(bet*2);
            }
            else if(h.getResult() == Result.TIE) {
                player.updateBalance(bet);
            }
            else if(h.getResult() == Result.SURRENDERED) {
                player.updateBalance(bet/2);
            }
        }
    }

    //---------------------------------------------------------------------//

    /**
     * Returns the number of decks used in the game-
     * @return Number of decks.
     */
    public int getNumOfDecks() {
        return numOfDecks;
    }

    /**
     * Returns the initial balance of the player.
     * @return The initial balance.
     */
    public int getInitialBalance() {
        return initialBalance;
    }

    /**
     * Returns the game window.
     * @return Game window.
     */
    public GameWindow getUi() {
        return ui;
    }

    /**
     * Returns the chip stack
     * @return Chip stack.
     */
    public ChipStack getcS() {
        return cS;
    }

    /**
     * Returns the player.
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the dealer.
     * @return The dealer.
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * Returns the currently active hand's index.
     * @return The active hand's index.
     */
    public int getPlayerActiveHandIndex() {
        return playerActiveHandIndex;
    }

    /**
     * Returns the results of the player's hands
     * @return The list of results.
     */
    public ArrayList<Result> getRes() {
        return res;
    }

    /**
     * Sets the result of a specified hand.
     * @param index The hand index.
     * @param result The result to set.
     */
    public void setRes(int index, Result result) {
        res.set(index, result);
    }
}
