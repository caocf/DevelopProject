package com.xhl.world.ui.event;

/**
 * Created by Sum on 15/12/29.
 */
public class SearchItemEvent {
    public static final int Type_Search = 1;
    public static final int Type_Classify = 2;
    public static final int Type_Scan= 3;
    public static final int Type_Fast_Search= 4;
    public int search_type;
    public String search_content;
}
