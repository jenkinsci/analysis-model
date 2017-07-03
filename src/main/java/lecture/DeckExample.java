package lecture;

public class DeckExample {
    public static void main(final String[] args) {
        Deck deck = new Deck();

        for (Integer card : deck) {
            System.out.println(card);
        }

        System.out.println(deck.isEmpty());
    }
}
