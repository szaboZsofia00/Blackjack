package deck;

import utilz.Constants.CardImage;

/**
 * Represents a single card used in the Blackjack game, typically French-suited cards.
 * <p>
 *     A card has a suit, a rank, a numerical value, and an associated image representations
 *     defined in {@link CardImage}.
 * </p>
 */
public class Card {

    private String suit;
    private String rank;
    private int value;
    private CardImage cardImage;

    /**
     * Creates a new {@code Card} instance with the specified suit, rank and value. The image is automatically
     * assigned based on the suit and rank.
     *
     * @param suit The suit of the card (spades, clubs, hearts, diamonds).
     * @param rank The rank of the card (e.g: ace, 3, king).
     * @param value The value of the card in the game.
     */
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
        this.cardImage = CardImage.create(rank, suit);
    }

    /**
     * Gets the suit of the card.
     *
     * @return The card's suit.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Gets the rank of the card.
     *
     * @return The card's rank.
     */
    public String getRank() {
        return rank;
    }

    /**
     * Gets the value of the card.
     *
     * @return The card's value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the cards.
     *
     * @param value The new value assigned to the card.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the card.
     *
     * @return A string describing the card in the following format: "{rank} of {suit}".
     */
    public String toString() {
        return rank + " of " + suit;
    }

    /**
     * Gets the {@link CardImage} associated with the card.
     *
     * @return The card's image representation.
     */
    public CardImage getCardImage() {
        return cardImage;
    }
}