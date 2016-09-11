package com.moge.gege.model;

import java.io.Serializable;
import java.util.List;

public class AttachmentModel implements Serializable
{
    // private String images[];
    //
    // public String[] getImages()
    // {
    // return images;
    // }
    //
    // public void setImages(String[] images)
    // {
    // this.images = images;
    // }

    private static final long serialVersionUID = 1L;

    private List<String> images;

    public List<String> getImages()
    {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
    }

}
