package participants;

import deck.Card;
import utilz.Constants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the hand of the participants in the Blackjack game.
 *
 * <p>
 *     A hand contains the drawn {@link Card}, and counts the drawn Aces. If the hand is connected to a {@link Player}
 *     than it saves the player's bet, and finally the result of the hand.
 * </p>
 */
public class Hand {

    /** The {@link Card}s in the participant's hand.*/
    private List<Card> cards = new ArrayList<>();
    /** The current bet of the hand. Only used with the player.*/
    private int currentBet;

    /** The number of Aces in the hand.*/
    private int aceCount = 0;
    /** The result of the hand. Initially {@code NONE}. Only used with the player.*/
    private Result result = Result.NONE;

    //------------------- GETTERS & SETTERS -------------------//

    /**
     * Get the list of {@link Card} in the {@code Hand}.
     * @return The list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Get the current bet of the {@code Hand}.
     * @return The current bet.
     */
    public int getCurrentBet() {
        return currentBet;
    }

    /**
     * Updates the current bet to the given value.
     * @param newBet The new bet value.
     */
    public void setCurrentBet(int newBet) {
        this.currentBet = newBet;
    }

    /**
     * Get the result of the {@code Hand}.
     * @return The {@link Result} of the hand.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Updates the {@code Hand}'s result to the given values.
     * @param result The new {@link Result}.
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Get the sum of the values of the cards currently holding in the {@code Hand}.
     * @return The sum of values.
     */
    public int getSumOfValues() {
        int sum = 0;

        for(Card c : cards) {
            sum += c.getValue();
        }
        return sum;
    }

    /**
     * Get the number of Aces currently in the {@code Hand}.
     * @return The number of Aces.
     */
    public int getAceCount() {
        return aceCount;
    }

    //************************//

    /**
     * Add a {@link Card} to the hand. If the card is an Ace than raise the {@code aceCount}.
     * @param c The card to add.
     */
    public void addCard(Card c) {
        cards.add(c);
        if (c.getRank().equals("Ace")) {
            aceCount++;
        }
    }

    /**
     * Remove the second {@link Card} in the hand. Checking if its and Ace and changing the {@code aceCount}
     * according to it. Used in splitting the cards.
     * @return The removed card.
     */
    public Card removeCard() {
        Card c = cards.get(1);
        cards.remove(1);
        if ("Ace".equals(c.getRank())) aceCount--;
        return c;
    }

    /**
     * Printing out the {@link Card}s' values sum and the cards itself.
     * @param participant Player or dealer.
     */
    public void printCardsValue(String participant) {
        System.out.println("----------------------------------");
        System.out.print("The " + participant + "'s hand: ");
        System.out.println(getSumOfValues());
        for (Card c: cards) System.out.println(c.toString());
    }

    /**
     * Delete the cards from the hand and set the bet to the default 0.
     */
    public void clearHand() {
        cards.clear();
        currentBet = 0;
    }

    /**
     * Check if the {@code Hand} is a Blackjack hand, so it contains 2 cards and their sum is 21.
     * @return True if it's Blackjack, otherwise False.
     */
    public boolean isBlackjack() {
        return (this.getSumOfValues() == 21 && cards.size() == 2);
    }

    /**
     * Check if the {@code Hand}'s value is 21 but not a Blackjack (more than 2 cards).
     * @return True if 21, otherwise False.
     */
    private boolean isTwentyOne() { return (this.getSumOfValues() == 21) && cards.size() > 2; }

    /**
     * Check if the {@code Hand} is busted, so the value of the cards is more than 21.
     * @return True if busted, otherwise False,
     */
    public boolean isBusted() {
        return this.getSumOfValues() > 21;
    }

    /**
     * Check if the {@code Hand} can be split, so there are only two cards in the hand and there values are the same.
     * @return True if it can be split, otherwise False.
     */
    public boolean canSplit() {
        return cards.size() == 2 &&
                cards.get(0).getValue() == cards.get(1).getValue();
    }

    /**
     * Change the Ace's value from 11 to 1, if there are any in the {@code Hand}.
     */
    public void changeAceValue() {
        for (Card c: cards) {
            if ("Ace".equals(c.getRank()) && c.getValue() == 11) {
                c.setValue(1);
                aceCount--;
                return;
            }
        }
        System.out.println("There are no Aces to change in this hand.");
    }

    /**
     * Evaluate the {@code Hand}. Check if it is a Blackjack or the hand is already busted.
     * @return The {@link Result} of the hand.
     */
    public Result evaluateHand() {
        if(isBlackjack()) {
            result = Result.BLACKJACK;
            System.out.println("You won! Blackjack!");
        } else if (isTwentyOne()) {
            result = Result.WIN;
            System.out.println("You won!");
        } else if (isBusted() && aceCount == 0) {
            result = Result.LOSE;
            System.out.println("Busted!");
        } else if (isBusted() && aceCount >0 && !canSplit()) {
            System.out.println("We have to change the value of your Ace card.");
            changeAceValue();
            System.out.println("The sum of values: "+getSumOfValues());
        }
        System.out.println("result in hand.evaluateHand: " + result);
        return result;
    }

    /**
     * Checking if the {@code Hand} is busted and contains an Ace (valued 11). If it is, change the value of the Ace to 1.
     */
    public void changeAceIfNotSplit() {
        if(isBusted() && aceCount > 0 && canSplit()) changeAceValue();
    }

}
