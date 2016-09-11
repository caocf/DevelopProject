package com.moge.gege.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class AlbumFolderModel implements Parcelable
{
    private String path;
    private String name;
    private int count;
    private int firstResId;
    private ArrayList<AlbumItemModel> albumList = new ArrayList<AlbumItemModel>();

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public ArrayList<AlbumItemModel> getAlbumList()
    {
        return albumList;
    }

    public void setAlbumList(ArrayList<AlbumItemModel> albumList)
    {
        this.albumList = albumList;
    }

    public int getFirstResId()
    {
        return firstResId;
    }

    public void setFirstResId(int firstResId)
    {
        this.firstResId = firstResId;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeInt(count);
        dest.writeInt(firstResId);
        dest.writeList(albumList);
    }

    public static final Parcelable.Creator<AlbumFolderModel> CREATOR = new Creator<AlbumFolderModel>()
    {
        @Override
        public AlbumFolderModel createFromParcel(Parcel source)
        {
            AlbumFolderModel model = new AlbumFolderModel();
            model.path = source.readString();
            model.name = source.readString();
            model.count = source.readInt();
            model.firstResId = source.readInt();
            source.readList(model.albumList,
                    AlbumItemModel.class.getClassLoader());
            return model;
        }

        @Override
        public AlbumFolderModel[] newArray(int size)
        {
            return new AlbumFolderModel[size];
        }
    };
}
