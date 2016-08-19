package com.xhl.bqlh.data;

import java.util.List;

/**
 * Created by Sum on 15/12/29.
 */
public class SearchData {

    private List<String> search_history;
    private List<String> default_hot;

    public List<String> getDefault_hot() {
        return default_hot;
    }

    public void setDefault_hot(List<String> default_hot) {
        this.default_hot = default_hot;
    }

    public List<String> getSearch_history() {
        return search_history;
    }

    public void setSearch_history(List<String> search_history) {
        this.search_history = search_history;
    }
}

