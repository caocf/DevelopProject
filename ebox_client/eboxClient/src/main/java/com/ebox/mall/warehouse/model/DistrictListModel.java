package com.ebox.mall.warehouse.model;

import java.util.List;

public class DistrictListModel
{
    private List<DistrictModel> provinces;
    private List<DistrictModel> citys;
    private List<DistrictModel> districts;

    public List<DistrictModel> getProvinces()
    {
        return provinces;
    }

    public void setProvinces(List<DistrictModel> provinces)
    {
        this.provinces = provinces;
    }

    public List<DistrictModel> getCitys()
    {
        return citys;
    }

    public void setCitys(List<DistrictModel> citys)
    {
        this.citys = citys;
    }

    public List<DistrictModel> getDistricts()
    {
        return districts;
    }

    public void setDistricts(List<DistrictModel> districts)
    {
        this.districts = districts;
    }

}
