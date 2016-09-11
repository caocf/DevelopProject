package com.ebox.ex.network.model.base.type;

import java.io.Serializable;

public class TerminalActivity implements Serializable {
    private static final long serialVersionUID = -2635295507752168253L;
    private Long page_id;
    private String name;
    private String content;
    private Integer type;
    private String url;

    public Long getPage_id() {
        return page_id;
    }

    public void setPage_id(Long page_id) {
        this.page_id = page_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
