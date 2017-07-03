package lecture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.hafner.java2.lecture.Pixel.PixelXComparator;
import edu.hm.hafner.java2.lecture.Pixel.PixelYComparator;

public class Pixels {
    public static void main(final String[] args) {
        List<Pixel> pixels = new ArrayList<>();
        Collections.sort(pixels, new PixelXComparator());
        pixels.sort(new PixelYComparator());
        pixels.sort(new Comparator<Pixel>() {
            @Override
            public int compare(final Pixel o1, final Pixel o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        pixels.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
        pixels.sort(Comparator.comparing(Pixel::toString));
    }
}
