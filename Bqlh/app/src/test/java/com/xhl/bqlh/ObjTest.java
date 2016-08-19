package com.xhl.bqlh;

import org.junit.Test;

/**
 * Created by Summer on 2016/8/4.
 */
public class ObjTest {


    @Test
    public void equalsTest() throws Exception {
        String l1 = "LOVE";
        String l2 = "LOVE";
        boolean b = l1 == l2;
        boolean equals = l1.equals(l2);
       int MAXIMUM_CAPACITY = 1 << 30;
        print(equals);
        print(MAXIMUM_CAPACITY);
    }

    @Test
    public void equalsTest2() throws Exception {



    }


    public static void print(Object msg) {
        System.out.println(msg);
    }
}
