package lecture;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Tests the class {@link GiftBox}.
 *
 * @author Ullrich Hafner
 */
public class GiftBoxTest {
    /**
     * A Gift box test.
     */
    @Test
    public void shouldPutAndGet() {
        GiftBox wineBox = new GiftBox();
        WineBottle wineBottle = new WineBottle();
        wineBox.put(wineBottle);

        GiftBox bookBox = new GiftBox();
        Book book = new Book();
        bookBox.put(book);

        // Object as return type is flagged as compile error
//      wineBottle = wineBox.get();
//      book = bookBox.get();

        // Wrong types can be put into the boxes
        Assertions.assertThatThrownBy(() -> wineBox.put(book)).isInstanceOf(IllegalStateException.class);
        Assertions.assertThatThrownBy(() -> bookBox.put(wineBottle)).isInstanceOf(IllegalStateException.class);
    }
}