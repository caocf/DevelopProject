package com.xhl.bqlh.business;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 16/5/25.
 */
public class TimeComplexTest {

    private static List<TestObj> list1;
    private static List<TestObj> list2;

    @BeforeClass()
    public static void setUp() throws Exception {
        list1 = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            String id = "id" + i;
            String attr = "attr" + i;
            list1.add(new TestObj(id, attr));
        }

        list2 = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            String id = "id" + i;
            if (i % 3 == 0) {
                id = "ids" + i;
            }
            String attr = "attr2" + i;
            list2.add(new TestObj(id, attr));
        }
    }

    @Test
    public void testHashMap() throws Exception {
        long millis = System.currentTimeMillis();
        HashMap<String, TestObj> map = new HashMap<>();
        for (TestObj test : list1) {
            map.put(test.getId(), test);
        }
        for (TestObj test : list2) {
            boolean containsKey = map.containsKey(test.getId());
            if (containsKey) {
                TestObj testObj = map.get(test.getId());
                testObj.setAttr1(test.getAttr1());
            } else {
                map.put(test.getId(), test);
            }
        }
        long milli2 = System.currentTimeMillis();
        long TIme = milli2 - millis;
        printTime("MAP 耗时:" + TIme);
    }

    @Test
    public void testTowList() throws Exception {
        long millis = System.currentTimeMillis();
        for (TestObj test : list1) {
            String id = test.getId();
            for (TestObj test2 : list2) {
                if (id.equals(test2.getId())) {
                    test.setAttr1(test2.getAttr1());
                }
            }
        }
        long milli2 = System.currentTimeMillis();
        long TIme = milli2 - millis;
        printTime("List 耗时:" + TIme);
    }

    @Test
    public void testTowList2() throws Exception {
        long millis = System.currentTimeMillis();
//        list1.addAll(list2);
        list1.removeAll(list2);

        long milli2 = System.currentTimeMillis();
        long TIme = milli2 - millis;
        printTime("List2 耗时:" + TIme);
    }


    @Test
    public void testSort() throws Exception {
//
//        HashMap<String, String> map = new HashMap<>();
//        String key = map.put("key", "1");
//        String key1 = map.put("key", "2");
//        String key2 = map.get("key");
//        printTime(key2);
        Object i = new Object();
        Object a = new Object();
        boolean equals = i.equals(a);
//        printTime(equals + " " + (i == a));
//        printTime(i.toString());
//        printTime(a.toString());
//        printTime(new TestObj().toString());
    }


    public static void printTime(String mst) {
        System.out.println(mst);
    }


    static class TestObj {
        private String id;
        private String attr1;

        public TestObj() {
        }

        public TestObj(String id, String attr1) {
            this.id = id;
            this.attr1 = attr1;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof TestObj) {
                return ((TestObj) o).id.equals(this.id);
            }
            return super.equals(o);
        }
    }
}
