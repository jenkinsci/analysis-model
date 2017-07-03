package lecture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The deck contains the 98 remaining cards that need to be laid on one of the four piles.
 *
 * @author Ullrich Hafner
 */
public class Deck implements Iterable<Integer> {
    private List<Integer> cards = new ArrayList<>();

    /**
     * Creates the deck with 98 cards [2, ..., 99].
     */
    public Deck() {
        for (int i = 2; i < 100; i++) {
            cards.add(i);
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new DeckIterator(this);
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Returns whether this deck is empty.
     *
     * @return {@code true} if this deck is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return cards.size() == 0;
    }

    /**
     * Draws the next card from this deck.
     *
     * @return the new card
     * @throws IndexOutOfBoundsException if there is no card left
     */
    public int drawCard() {
        return cards.remove(0).intValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Deck deck = (Deck)o;

        return cards != null ? cards.equals(deck.cards) : deck.cards == null;
    }

    @Override
    public int hashCode() {
        return cards != null ? cards.hashCode() : 0;
    }
}
