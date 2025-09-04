package utilz;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Creating constants used throughout the Blackjack game including the potential results of the game,
 * and card image mappings.
 */
public class Constants {

    /**
     * Represents the possible results of the player's hand.
     */
    public enum Result {
        /** No result determined yet. */
        NONE,
        /** Player has Blackjack (an Ace and a 10-valued card as the first two cards). */
        BLACKJACK,
        /** The player wins the hand. */
        WIN,
        /** The hand ends in a tie. */
        TIE,
        /** The player loses the hand. */
        LOSE,
        /** The player gives up the hand. */
        SURRENDERED
    }

    /**
     * Represents card images used in the game, mapped to their corresponding file name.
     */
    public enum CardImage {
        BACK("cardBack.png"),
        ACE_OF_SPADES("aS.png"),
        TWO_OF_SPADES("2S.png"),
        THREE_OF_SPADES("3S.png"),
        FOUR_OF_SPADES("4S.png"),
        FIVE_OF_SPADES("5S.png"),
        SIX_OF_SPADES("6S.png"),
        SEVEN_OF_SPADES("7S.png"),
        EIGHT_OF_SPADES("8S.png"),
        NINE_OF_SPADES("9S.png"),
        TEN_OF_SPADES("10S.png"),
        JACK_OF_SPADES("jS.png"),
        QUEEN_OF_SPADES("qS.png"),
        KING_OF_SPADES("kS.png"),
        ACE_OF_CLUBS("aC.png"),
        TWO_OF_CLUBS("2C.png"),
        THREE_OF_CLUBS("3C.png"),
        FOUR_OF_CLUBS("4C.png"),
        FIVE_OF_CLUBS("5C.png"),
        SIX_OF_CLUBS("6C.png"),
        SEVEN_OF_CLUBS("7C.png"),
        EIGHT_OF_CLUBS("8C.png"),
        NINE_OF_CLUBS("9C.png"),
        TEN_OF_CLUBS("10C.png"),
        JACK_OF_CLUBS("jC.png"),
        QUEEN_OF_CLUBS("qC.png"),
        KING_OF_CLUBS("kC.png"),
        ACE_OF_DIAMONDS("aD.png"),
        TWO_OF_DIAMONDS("2D.png"),
        THREE_OF_DIAMONDS("3D.png"),
        FOUR_OF_DIAMONDS("4D.png"),
        FIVE_OF_DIAMONDS("5D.png"),
        SIX_OF_DIAMONDS("6D.png"),
        SEVEN_OF_DIAMONDS("7D.png"),
        EIGHT_OF_DIAMONDS("8D.png"),
        NINE_OF_DIAMONDS("9D.png"),
        TEN_OF_DIAMONDS("10D.png"),
        JACK_OF_DIAMONDS("jD.png"),
        QUEEN_OF_DIAMONDS("qD.png"),
        KING_OF_DIAMONDS("kD.png"),
        ACE_OF_HEARTS("aH.png"),
        TWO_OF_HEARTS("2H.png"),
        THREE_OF_HEARTS("3H.png"),
        FOUR_OF_HEARTS("4H.png"),
        FIVE_OF_HEARTS("5H.png"),
        SIX_OF_HEARTS("6H.png"),
        SEVEN_OF_HEARTS("7H.png"),
        EIGHT_OF_HEARTS("8H.png"),
        NINE_OF_HEARTS("9H.png"),
        TEN_OF_HEARTS("10H.png"),
        JACK_OF_HEARTS("jH.png"),
        QUEEN_OF_HEARTS("qH.png"),
        KING_OF_HEARTS("kH.png");

        /** The filename of the card image resource. */
        private final String file;

        CardImage(String file) {
            this.file = file;
        }

        /**
         * Loads the image file as a JavaFX {@link ImageView} with fixed sizes.
         *
         * @return an {@link ImageView} representing the card image.
         */
        public ImageView getImage() {
            Image img = new Image("/cards/" + file);
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(80);
            imgView.setFitHeight(120);
            return imgView;
        }

        /**
         * Creates a {@code CardImage} instance based on the given rank and suit.
         * <p> The ranks can be: </p>
         * <ul>
         *     <li>ace</li>
         *     <li>numeric values 2-10</li>
         *     <li>jack</li>
         *     <li>queen</li>
         *     <li>king</li>
         * </ul>
         *
         * <p> The suits can be: "spades", "clubs", "diamonds", "hearts". </p>
         *
         * @param rank The rank of the card.
         * @param suit The suit of the card.
         * @return The corresponding {@code CardImage} enum constant.
         */
        public static CardImage create(String rank, String suit) {
            String rankCode = switch (rank.toLowerCase()) {
                case "ace" -> "a";
                case "jack" -> "j";
                case "queen" -> "q";
                case "king" -> "k";
                default -> rank;
            };

            String suitCode = switch (suit.toLowerCase()) {
                case "spades" -> "S";
                case "clubs" -> "C";
                case "diamonds" -> "D";
                case "hearts" -> "H";
                default -> throw new IllegalStateException("Unexpected value: " + suit);
            };

            String fileName = rankCode + suitCode + ".png";

            for (CardImage c : values()) {
                if(c.file.equals(fileName)) return c;
            }

            throw new IllegalArgumentException("No image found for rank = " + rank + ", suit = " + suit);
        }


    }
}
