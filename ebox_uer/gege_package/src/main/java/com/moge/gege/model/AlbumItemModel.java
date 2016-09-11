package com.moge.gege.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumItemModel implements Parcelable, Serializable
{
    private int id;
    private String path;
    private boolean selected;

    public static Parcelable.Creator<AlbumItemModel> getCreator()
    {
        return CREATOR;
    }

    public AlbumItemModel()
    {
    }

    public AlbumItemModel(int id, String path)
    {
        this.id = id;
        this.path = path;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeBooleanArray(new boolean[] { selected });
    }

    public static final Parcelable.Creator<AlbumItemModel> CREATOR = new Creator<AlbumItemModel>()
    {
        @Override
        public AlbumItemModel createFromParcel(Parcel source)
        {
            AlbumItemModel model = new AlbumItemModel();
            model.id = source.readInt();
            model.path = source.readString();

            boolean[] myBooleanArr = new boolean[1];
            source.readBooleanArray(myBooleanArr);
            model.selected = myBooleanArr[0];

            return model;
        }

        @Override
        public AlbumItemModel[] newArray(int size)
        {
            return new AlbumItemModel[size];
        }
    };

}
