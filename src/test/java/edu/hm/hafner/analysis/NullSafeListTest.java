package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
public class NullSafeListTest extends ListTest{
    @Override
    List<Integer> create(final int numberOfInitialElements) {
        List<Integer> sut = new NullSafeList<>();
        IntStream.range(1, numberOfInitialElements + 1).forEach(sut::add);
        return sut;
    }
    @Test
    void addingNullBombsNPE(){
        //arrange + act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> create(0).add(null));
    }
    @Test
    void addingNullAtIndexBombsNPE(){
        //arrange + act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> create(4).add(2, null));
    }
    @Test
    void addingAnotherListContaingNullBombsNPE(){
        //arrange
        List<Integer> sut = new ArrayList<>();
        sut.add(null);
        //act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> create(0).addAll(sut));
    }
    @Test
    void addingAnotherListWhichIsNullBombsNPE(){
        //arrange
        List<Integer> sut = null;
        //act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> create(0).addAll(sut));
    }
    @Test
    void settingNullAtIndexBombsNPE(){
        //arrange + act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> create(4).set(2, null));
    }
    @Test
    void copyFromCtorWhichIsNullBombsNPE(){
        //arrange
        List<Integer> sut = null;
        //act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new NullSafeList(sut));
    }
    @Test
    void copyFromCtorWhichContainsNullBombsNPE(){
        //arrange
        List<Integer> sut = new ArrayList<>();
        sut.add(null);

        //act + assert
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> create(0).addAll(sut));
    }
}
