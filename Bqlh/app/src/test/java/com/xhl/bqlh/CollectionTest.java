package com.xhl.bqlh;

import org.junit.Test;

/**
 * Created by Sum on 16/6/30.
 */
public class CollectionTest {

    public static class A{
        private A next;

        public A(A next) {
            this.next = next;
        }

        public A getNext() {
            return next;
        }
    }

    @Test
    public void testArray() throws Exception {

        A[] table = new A[5];

        table[1] = new A(table[1]);

        A a = table[1];


        A next = a.getNext();
    }
}
