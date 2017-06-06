package lecture;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ArrayList#toString()}.
 */
public class ArrayListHelper {
    /**
     * Implementation of {@link ArrayList#toString()}.
     *
     * @param args not used
     */
    public static void main(final String[] args) {
        printElementsOfArray();
    }

    private static void printElementsOfArray() {
        List<String> elements = new ArrayList<String>();
        elements.add("Hello");
        elements.add("World");
        elements.add("!");

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            builder.append(elements.get(i));
            if (i < elements.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(']');
        System.out.println(builder);
    }
}
