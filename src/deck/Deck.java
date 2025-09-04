package deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one or more decks of a standard French-suited playing card.
 */
public class Deck {

    /** All cards, every deck. Initially same as {@code playingDeck}, but this used as a copy when reshuffling the deck.*/
    private List<Card> cards = new ArrayList<>();
    /** Cards currently in play. Initially same as {@link cards}, but this used for the game.*/
    private List<Card> playingDeck;
    /** Number of cards in the complete deck. */
    private final int totalCards;
    /** The threshold at which the deck will be reshuffled. */
    private final int reshuffleThreshold;

    private final String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
    private final String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

    /**
     * Creates a new {@code Deck} containing the specified number of standard deck.
     *
     * <p>
     *     Automatically shuffles the deck and calculate the reshuffle threshold.
     * </p>
     *
     * @param numberOfDecks The number of standard 52-card decks used in the game.
     */
    public Deck(int numberOfDecks) {

        createMultipleDecks(createDeckOfCards(),numberOfDecks);

        totalCards = cards.size();
        reshuffleThreshold = (int) (0.3 * totalCards);

        playingDeck = new ArrayList<>(cards);
        shuffle(playingDeck);
    }

    /**
     * Creates a single 52-card deck.
     *
     * @return A list of {@link Card} objects representing a deck.
     */
    private List<Card> createDeckOfCards() {
        List<Card> tmpCards = new ArrayList<>();

        for (String s: suits) {
            for (String r: ranks) {
                int value;
                if (r.equals("Jack") || r.equals("Queen") || r.equals("King")) {
                    value = 10;
                } else if (r.equals("Ace")) {
                    value = 11; // later need to be changed if necessary to 1
                } else {
                    value = Integer.parseInt(r);
                }
                tmpCards.add(new Card(s,r,value));
            }
        }
        return tmpCards;
    }

    /**
     * Creates multiple decks by duplicating the given deck a specified number of times.
     *
     * @param deck The deck to duplicate.
     * @param numberOfDecks The number of copies to create.
     */
    private void createMultipleDecks(List<Card> deck, int numberOfDecks) {

        for (int i=0; i<numberOfDecks; i++) {
            for(Card c: deck) {
                cards.add(new Card(c.getSuit(),c.getRank(),c.getValue()));
            }
        }
    }

    /** Shuffles the given list of cards.
     *
     * <p>
     *     With this the game is similar to the real-life version, by determining the order of the cards
     *     in the beginning of the game.
     * </p>
     *
     * @param deck List of cards to shuffle.
     */
    private void shuffle(List<Card> deck) {
        Collections.shuffle(deck);
    }

    /**
     * Resetting the playing deck to the original full deck and shuffle it.
     */
    private void reshuffle() {
        playingDeck = new ArrayList<>(cards);
        shuffle(playingDeck);
    }

    /**
     * Draws the top card from the playing deck.
     *
     * <p>
     *     If the number of the remaining cards are under the reshuffle threshold, the deck is automatically
     *     reshuffled.
     * </p>
     *
     * @return The drawn {@link Card}.
     */
    public Card drawCard() {
        if (playingDeck.size() < reshuffleThreshold) {
            System.out.println("The deck is too low. The dealer's gonna reshuffle the deck.");
            reshuffle();
        }

        Card drawnCard = playingDeck.getFirst();
        playingDeck.removeFirst();
        return drawnCard;
    }
}
