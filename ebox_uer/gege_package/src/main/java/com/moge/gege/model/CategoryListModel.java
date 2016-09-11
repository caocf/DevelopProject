package com.moge.gege.model;

import java.util.List;

public class CategoryListModel
{
    private List<BaseOptionModel> breeds;
    private List<BaseOptionModel> classifys;
    private List<BaseOptionModel> categorys;

    public List<BaseOptionModel> getBreeds()
    {
        return breeds;
    }

    public void setBreeds(List<BaseOptionModel> breeds)
    {
        this.breeds = breeds;
    }

    public List<BaseOptionModel> getClassifys()
    {
        return classifys;
    }

    public void setClassifys(List<BaseOptionModel> classifys)
    {
        this.classifys = classifys;
    }

    public List<BaseOptionModel> getCategorys()
    {
        return categorys;
    }

    public void setCategorys(List<BaseOptionModel> categorys)
    {
        this.categorys = categorys;
    }

}
