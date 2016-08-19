package com.xhl.bqlh.business;

import org.junit.Test;

import java.util.Calendar;

/**
 * Created by Sum on 16/5/13.
 */
public class TimeTest {

    @Test
    public void startTime() throws Exception {

        Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.DAY_OF_MONTH, 1);
//        instance.add(Calendar.MONTH,1);

        int i = instance.get(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = instance.getFirstDayOfWeek();
        int actualMinimum = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
        int i1 = instance.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        int i2 = instance.get(Calendar.DAY_OF_YEAR);
        int i3 = instance.get(Calendar.DAY_OF_WEEK);
        print(i3 + "");
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

}
