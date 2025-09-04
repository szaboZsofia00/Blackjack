package participants;

import deck.Card;
import deck.Deck;
import game.ChipStack;
import game.Game;
import ui.GameWindow;
import utilz.Constants.*;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;

/**
 * Represents a player in a Blackjack game.
 * <p>
 *     A player can perform standard Blackjack moves such as hitting, standing, doubling down,
 *     splitting ore surrendering. The player manages their balance, places bets, and after a
 *     split can have multiple hands.
 * </p>
 * <p>
 *     This class interacts with the {@link Deck} to draw their cards and interacts with the {@link ChipStack} to
 *     manage their balance. It's also coordinates with the {@link Game} logic and updates the {@link GameWindow}
 *     UI to reflect the player's actions.
 * </p>
 */
public class Player {

    /** The list of hands the player curretnly holds.*/
    private List<Hand> hands = new ArrayList<>();
    /** The index of the player's currently active hand.*/
    private int playerActiveHandIndex = 0;

    /** The deck from which the player draws cards. */
    private final Deck deck;
    /** The game that controls flow and results. */
    private final Game game;
    /** The user interface for displaying the player's actions.*/
    private final GameWindow ui;

    private ChipStack cs;

    /**
     * Creates a new player with the playing deck, game reference, and chip stack that controls the player's balance.
     * @param deck The deck used for drawing cards.
     * @param game The game instance controlling flow.
     * @param cs The player's available money.
     */
    public Player(Deck deck, Game game, ChipStack cs) {

        this.deck = deck;
        this.game = game;
        this.cs = cs;

        this.ui = game.getUi();
    }

    /**
     * Returns the list of the player's hands.
     * @return The player's hands.
     */
    public List<Hand> getHands() {
        return hands;
    }

    /**
     * Updates the player's balance by adding the specified amount.
     * @param amount The amount to add.
     */
    public void updateBalance(int amount) {
        int balance = cs.getBalance();
        balance += amount;
        cs.setBalance(balance);
    }

    /**
     * Returns the index of the currently active hand.
     * @return The active hand index.
     */
    public int getPlayerActiveHandIndex() {
        return playerActiveHandIndex;
    }

    /**
     * Change the index of the currently active hand.
     * @param playerActiveHandIndex The new hand index.
     */
    public void setPlayerActiveHandIndex(int playerActiveHandIndex) {
        this.playerActiveHandIndex = playerActiveHandIndex;
    }


    /**
     * Starts a new round for the player by clearing the previous hands, creating a new
     * initial hand, and placing the given bet.
     * @param bet The amount to bet for the round.
     */
    public void startRound(int bet) {
        for(Hand h:hands) h.clearHand();
        hands.clear();

        Hand firstHand = new Hand();
        hands.add(firstHand);

        placeBet(bet, firstHand);
    }

    /**
     * Places a bet on the given hand, reducing the player's balance.
     * @param bet The bet amount.
     * @param h The hand to assign the bet to.
     */
    private void placeBet(int bet, Hand h) {
        h.setCurrentBet(bet);
    }

    /**
     * Draws a card from the deck, adds it to the given hand, and returns the card's
     * image for display.
     * @param h The hand to add the card to.
     * @return The {@link ImageView} representing the drawn card.
     */
    private ImageView drawACard(Hand h) {
        Card card = deck.drawCard();
        h.addCard(card);
        return card.getCardImage().getImage();
    }

    /**
     * Perform a "hit" action. Draws a card for the active hand, updates the UI, and checks
     * if the hand is finished (blackjack or busted).
     */
    public void hit() {
        Hand hand = hands.get(playerActiveHandIndex);
        ImageView card = drawACard(hand);

        if (playerActiveHandIndex == 0) {
            ui.getPlayerFirstHandBox().getChildren().add(card);
        } else {
            ui.getPlayerSecondHandBox().getChildren().add(card);
        }

        game.setRes(playerActiveHandIndex,hand.evaluateHand());

        if(game.getRes().get(playerActiveHandIndex) != Result.NONE) {
            game.showHidePopUpWindows(ui.getFinishedHandBox(),true);
            game.moveToNextHandOrDealer();
        }

        ui.getSplitButton().setDisable(true);
        ui.getSurrenderButton().setDisable(true);
        ui.getDoubleDownButton().setDisable(true);
    }

    /**
     * Performs a "stand" action. The player end their turn for the active hand, and passes
     * to the next hand or the dealer.
     */
    public void stand() {
        Hand hand = hands.get(playerActiveHandIndex);
        game.setRes(playerActiveHandIndex,hand.evaluateHand());
        hand.changeAceIfNotSplit();

        if(game.getRes().get(playerActiveHandIndex).equals(Result.NONE)) {
            game.showHidePopUpWindows(ui.getFinishedHandBox(),true);
            game.moveToNextHandOrDealer();
        }
    }

    /**
     * Performs a "double down" action. The player's bet is doubled, exactly one additional
     * card is drawn, and the active hand ends.
     */
    public void doubleDown() {
        int balance = cs.getBalance();
        Hand hand = hands.get(playerActiveHandIndex);
        ImageView card = drawACard(hand);

        int bet = hand.getCurrentBet();
        cs.increaseBet(bet);
        balance -= bet;
        cs.setBalance(balance);
        hand.setCurrentBet(2 * bet);

        ui.getPlayerFirstHandBox().getChildren().add(card);

        game.setRes(playerActiveHandIndex,hand.evaluateHand());
        hand.changeAceIfNotSplit();
        game.showHidePopUpWindows(ui.getFinishedHandBox(),true);
        game.moveToNextHandOrDealer();
    }

    /**
     * Performs a "split" action. The player's hand is split into two separate hands, each
     * with its own bet and an additional card.
     */
    public void split() {
        int balance = cs.getBalance();
        Hand firstHand = hands.getFirst();
        Hand secondHand = new Hand();
        hands.add(secondHand);

        Card c = firstHand.removeCard();
        secondHand.addCard(c);

        int currentBet = firstHand.getCurrentBet();
        balance -= currentBet;
        secondHand.setCurrentBet(currentBet);

        for (Hand h: hands) {
            h.addCard(deck.drawCard());
        }

        playerActiveHandIndex = 0;
        game.getcS().setBalance(balance);

        ui.split();

        game.setRes(0,firstHand.evaluateHand());
        game.setRes(1,secondHand.evaluateHand());

        if(game.getRes().get(0) != Result.NONE) {
            game.showHidePopUpWindows(ui.getFinishedHandBox(),true);
            game.moveToNextHandOrDealer();
        }

    }

    /**
     * Performs a "surrender" action. The player gives up the current hand, losing half their bet
     * and ending their hand.
     */
    public void surrender() {
        Hand hand = hands.get(playerActiveHandIndex);
        hand.setResult(Result.SURRENDERED);
        game.setRes(playerActiveHandIndex, Result.SURRENDERED);
        game.moveToNextHandOrDealer();
    }
}
