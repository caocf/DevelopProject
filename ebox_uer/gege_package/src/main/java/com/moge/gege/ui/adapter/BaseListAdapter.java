package com.moge.gege.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

/**
 * The Class BaseListAdapter.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class BaseListAdapter<T> extends BaseAdapter
{

    protected final List<T> list;

    public BaseListAdapter()
    {
        super();
        this.list = new ArrayList<T>();
    }

    public List<T> getAll()
    {
        return this.list;
    }

    public boolean addAll(List<? extends T> list)
    {
        return this.list.addAll(list);
    }

    public void clear()
    {
        if (list != null)
        {
            this.list.clear();
        }
    }

    public int getCount()
    {
        return list == null ? 0 : list.size();
    }

    public int getItemChildCount()
    {
        return 1;
    }

    public T getItem(int i)
    {
        return this.list.get(i);
    }

    public long getItemId(int id)
    {
        return id;
    }

    public boolean isEmpty()
    {
        return list == null ? true : list.isEmpty();
    }

    public T remove(int i)
    {
        return list.remove(i);
    }

    public boolean remove(T t)
    {
        return list.remove(t);
    }

    public void addItem(T t)
    {
        this.list.add(t);
    }

    public void addItem(int location, T t)
    {
        this.list.add(location, t);
    }
}
