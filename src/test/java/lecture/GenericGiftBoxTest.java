package lecture;

import org.junit.Test;

/**
 * Tests the class {@link GenericGiftBox}.
 *
 * @author Ullrich Hafner
 */
public class GenericGiftBoxTest {
    /**
     * A Gift box test.
     */
    @Test
    public void shouldPutAndGet() {
        GenericGiftBox<WineBottle> wineBox
                = new GenericGiftBox<WineBottle>();
        WineBottle wineBottle
                = new WineBottle();
        wineBox.put(wineBottle);

        GenericGiftBox<Book> bookBox
                = new GenericGiftBox<>();
        Book book = new Book();
        bookBox.put(book);

        // Get returns the expected type
        wineBottle = wineBox.get();
        book = bookBox.get();

        // Wrong types are flagged as compile error
//      wineBox.put(book);
//      bookBox.put(wineBottle);
    }
}