package lecture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeckRunner {
    public static void main(final String[] args) {
        List<Deck> list = new ArrayList<>();
        Set<Deck> set = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            list.add(new Deck());
            set.add(new Deck());
        }

        System.out.println(list.size());
        System.out.println(set.size());
    }
}
