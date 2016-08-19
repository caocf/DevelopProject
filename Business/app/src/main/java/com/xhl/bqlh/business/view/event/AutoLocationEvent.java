package com.xhl.bqlh.business.view.event;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Sum on 16/3/21.
 */
public class AutoLocationEvent {

    public BDLocation location;

    public LatLng latLng;
    //116.4321,38.76623
    public String stLatLng;

    public String address;

    public String nearBy;

}
