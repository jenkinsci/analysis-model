package lecture;

import java.util.Iterator;

public class DeckIterator implements Iterator<Integer> {
    private final Deck deck;

    public DeckIterator(final Deck integers) {
        deck = integers;
    }

    @Override
    public boolean hasNext() {
        return !deck.isEmpty();
    }

    @Override
    public Integer next() {
        return deck.drawCard();
    }
}
