package participants;

import deck.Card;
import deck.Deck;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents the dealer in a Blackjack game.
 *
 * <p>
 *     The dealer has a {@link Hand} that holds the cards. The cards are drawn according to the
 *     Blackjack rules (must hit until 17). The dealer interacts with the {@link Deck} to draw cards
 *     and update the game UI by adding the cards to an {@link HBox}.
 * </p>
 */

public class Dealer {
    /** The dealer's hand of cards.*/
    private final Hand hand = new Hand();

    /**
     * Returns the dealer's current hand.
     * @return the dealer's hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Prepares the dealer for a new game by clearing its current hand.
     */
    public void startRound() {
        hand.clearHand();
    }

    /**
     * Plays the dealer's turn according to Blackjack rules. Each drawn card is displayed in the given
     * {@link HBox} container.
     * <ul>
     *     <li>The dealer must hit until the hand's total value is at least 17.</li>
     *     <li>If the dealer busts but holds an Ace, the Ace's value changed from 11 to 1.</li>
     * </ul>
     * @param d The deck from which the dealer draws cards.
     * @param handBox The UI container where the drawn cards are displayed.
     */
    public void playTurn(Deck d, HBox handBox) {
        hand.changeAceIfNotSplit();

        while (hand.getSumOfValues() < 17) {
            ImageView card = hit(d);
            handBox.getChildren().add(card);
            hand.printCardsValue("dealer");

            if (hand.isBusted() && hand.getAceCount() > 0) {
                hand.changeAceValue();
            }
        }
    }

    /**
     * Draws a card from the given deck, adds it to dealer's hand and returns the card's image for display.
     * @param d The deck to draw from.
     * @return The {@link ImageView} representing the drawn card.
     */
    private ImageView hit(Deck d) {
        Card drawnCard = d.drawCard();
        hand.addCard(drawnCard);
        return drawnCard.getCardImage().getImage();
    }
}
