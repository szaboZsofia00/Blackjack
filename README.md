# Blackjack
This is a Blackjack game implemented in Java with JavaFX graphical user interface. The game is based on standard Blackjack casino rules.
The project was built for learning JavaFX elements.

## Features
 * standard Blackjack rules (dealer hits until 17, Blackjack = 21 on first two cards)
 * player can:
   * hit, stand
   * split hands
   * double down
   * surrender
 * each hand keeps its own bet
 * JavaFX GUI

## Project Structure
 * `Card` - represents a playing card.
 * `Deck` - manages the deck of cards.
 * `Hand` - represents a participant's hand.
 * `Player` - represents a player in the game.
 * `Dealer` - represents the dealer in the game.
 * `ChipStack` - represents the player's chips (money) in the game.
 * `Game` - controls the game overall flow.
 * GUI classes - handle the user interface.

## How to Play
1. Start the game.
2. Place your bet. Without placing the bet the round cannot be started.
3. After the first two cards were dealt you can choose your moves.
    * Hit to draw a card.
    * Stand to finish your round.
    * Split if you have a pair (same value).
    * Double down to double your bet and draw exactly one card.
    * Surrender to give up half your bet, and finish the round immediately.
4. After your turn, the dealer plays automatically.
5. The result are shown at the end of the round, and the payout happens depending on it.

## Requirements
 * Java 24
 * JavaFX SDK 21.0.8 (installed and configured)