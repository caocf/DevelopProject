package com.moge.gege.model;

import java.util.List;

public class ApplyListModel
{
    private boolean applyed;
    private List<ApplyModel> applys;

    public boolean isApplyed()
    {
        return applyed;
    }

    public void setApplyed(boolean applyed)
    {
        this.applyed = applyed;
    }

    public List<ApplyModel> getApplys()
    {
        return applys;
    }

    public void setApplys(List<ApplyModel> applys)
    {
        this.applys = applys;
    }

}
