package com.moge.gege.model;

public class ConfigModel
{
    private String API_URL_PREFIX;
    private String POLL_URL_PREFIX;
    private String IMG_URL_PREFIX;
    private String MSG_URL_PREFIX;
    private String PAY_URL_PREFIX;
    private int DELIVERY_FEE;
    private int DELIVERY_FEE_START;

    public String getAPI_URL_PREFIX()
    {
        return API_URL_PREFIX;
    }

    public void setAPI_URL_PREFIX(String aPI_URL_PREFIX)
    {
        API_URL_PREFIX = aPI_URL_PREFIX;
    }

    public String getPOLL_URL_PREFIX()
    {
        return POLL_URL_PREFIX;
    }

    public void setPOLL_URL_PREFIX(String pOLL_URL_PREFIX)
    {
        POLL_URL_PREFIX = pOLL_URL_PREFIX;
    }

    public String getIMG_URL_PREFIX()
    {
        return IMG_URL_PREFIX;
    }

    public void setIMG_URL_PREFIX(String iMG_URL_PREFIX)
    {
        IMG_URL_PREFIX = iMG_URL_PREFIX;
    }

    public String getMSG_URL_PREFIX()
    {
        return MSG_URL_PREFIX;
    }

    public void setMSG_URL_PREFIX(String mSG_URL_PREFIX)
    {
        MSG_URL_PREFIX = mSG_URL_PREFIX;
    }

    public String getPAY_URL_PREFIX()
    {
        return PAY_URL_PREFIX;
    }

    public void setPAY_URL_PREFIX(String pAY_URL_PREFIX)
    {
        PAY_URL_PREFIX = pAY_URL_PREFIX;
    }

    public int getDELIVERY_FEE()
    {
        return DELIVERY_FEE;
    }

    public void setDELIVERY_FEE(int dELIVERY_FEE)
    {
        DELIVERY_FEE = dELIVERY_FEE;
    }

    public int getDELIVERY_FEE_START()
    {
        return DELIVERY_FEE_START;
    }

    public void setDELIVERY_FEE_START(int dELIVERY_FEE_START)
    {
        DELIVERY_FEE_START = dELIVERY_FEE_START;
    }

}
