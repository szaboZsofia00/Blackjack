package game;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ui.ChipStackUI;

/**
 * Represent the player's chip stack (balance and bet) in the Blackjack game.
 * <p>
 *     This class manages the player's balance and bet amounts, and communicate with the
 *     {@link ChipStackUI} to keep the UI consistent with the current state of the chips.
 * </p>
 * <p>
 *     Both {@code balance} and {@code bet} are declared as {@code static}, meaning there is
 *     only one shared chip stack across the game. Because only one player appears in the game.
 *     In a real life scenario this also true. Even with multiple players every one of them plays
 *     against the dealer/bank.
 * </p>
 */
public class ChipStack {

    /** The player's total available balance. */
    private static int balance;
    /** The player's current active bet. */
    private static int bet;

    /**
     * Creates a new chip stack with the given starting balance.
     * @param balance The initial balance of the player.
     */
    public ChipStack(int balance) {
        ChipStack.balance = balance;
        bet = 0;
    }

    /**
     * Updates the balance to a new value and refreshes the UI.
     * @param newBalance The new balance amount.
     */
    public void setBalance(int newBalance) {
        balance = newBalance;
        ChipStackUI.setBalance(balance);
    }

    /**
     * Returns the current balance.
     * @return The player's balance.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Increases the current bet by a given amount, if there is sufficient balance.
     * Deduces the balance with the bet. Also updates the UI.
     * @param newBet The amount to increase the bet with.
     */
    public static void increaseBet(int newBet) {
        if (newBet <= balance) {
            bet += newBet;
            ChipStackUI.setBet(bet);
            balance -= newBet;
        }
    }

    /**
     * Updates the current bet to a new amount and updates the UI.
     * @param newBet The new bet amount.
     */
    public void setBet(int newBet) {
        bet = newBet;
        ChipStackUI.setBet(bet);
    }

    /**
     * Returns the current bet.
     * @return The current bet.
     */
    public static int getBet() {
        return bet;
    }

    /**
     * Enables or disable the chip stacks and restart box in the UI. Used after
     * starting a round (placing the bet) to prevent further changes.
     */
    public void toggleStackControl() {
        for(StackPane stack: ChipStackUI.getChipStacks()) {
            stack.setDisable(!stack.isDisabled());
        }
        VBox restartBox = ChipStackUI.getRestartBox();
        restartBox.setDisable(!restartBox.isDisabled());
    }

    /**
     * Cancels the current bet and restores it to the balance. Resets the bet to zero.
     */
    public static void restartBet() {
        balance += bet;
        bet = 0;
    }
}
