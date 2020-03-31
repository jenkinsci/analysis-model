package edu.hm.mschober;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Michael Schober, mschober@hm.edu
 * @version 2020-31-03
 */

public abstract class ListTest{

    /* a new list instance */
    abstract List<Integer> create(int numberOfInitialElements);

    @Test public void listShouldBeEmpty(){
        List list = this.create(0);
        Assert.assertEquals(0, list.size());
    }

    @Test public void addWorks1(){
        List list = this.create(4);
        list.add(3);
        Assert.assertTrue(list.contains(3));
        Assert.assertEquals(1, list.size());
    }

    @Test public void addWorks2(){
        List list = this.create(3);
        for(int index = 1; index <= 4; index++){
            list.add(index);
        }
        Assert.assertEquals(4, list.size());
        Assert.assertTrue(list.contains(1));
        Assert.assertTrue(list.contains(2));
        Assert.assertTrue(list.contains(3));
        Assert.assertTrue(list.contains(4));
    }

    @Test public void removeWorks1(){
        List list = this.create(3);
        for(int index = 1; index <= 3; index++){
            list.add(index);
        }
        Assert.assertTrue(list.remove(Integer.valueOf(3)));
        Assert.assertTrue(list.remove(Integer.valueOf(2)));
        Assert.assertTrue(list.remove(Integer.valueOf(1)));
        Assert.assertFalse(list.remove(Integer.valueOf(3)));
    }

    @Test public void removeWorks2() {
        List list = this.create(3);
        for (int index = 1; index <= 3; index++) {
            list.add(index);
        }
        Assert.assertEquals(Integer.valueOf(3), list.remove(2));
        Assert.assertEquals(Integer.valueOf(2), list.remove(1));
        Assert.assertEquals(Integer.valueOf(1), list.remove(0));
    }

    @Test public void getWorks() {
        List list = this.create(3);
        for (int index = 1; index <= 3; index++) {
            list.add(index);
        }
        Assert.assertEquals(Integer.valueOf(3), list.get(2));
        Assert.assertEquals(Integer.valueOf(2), list.get(1));
        Assert.assertEquals(Integer.valueOf(1), list.get(0));
    }


}
