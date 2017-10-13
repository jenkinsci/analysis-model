package edu.hm.balz;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * todo
 *  2 Wochen abgabe
 *  Isue ca ein test und alle variablen Testen mit IsueBuilder
 *
 *  Test für IsueBilder und Iseues
 */
public class AssertJTest {

    @Test
    public void test1(){
        assertThat("abcd").as("fängt mit a und b an").contains("a","b");
    }
}
