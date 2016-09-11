package com.moge.gege.model;

public class ShareParam
{
    private int topicType;
    private String topicId;
    private String boardId;
    private String title;
    private String content;
    private String imageUrl;
    private boolean isShowDelete;
    private String promotionId;

    public ShareParam()
    {
        boardId = "";
        isShowDelete = false;
    }

    public int getTopicType()
    {
        return topicType;
    }

    public void setTopicType(int topicType)
    {
        this.topicType = topicType;
    }

    public String getTopicId()
    {
        return topicId;
    }

    public void setTopicId(String topicId)
    {
        this.topicId = topicId;
    }

    public String getBoardId()
    {
        return boardId;
    }

    public void setBoardId(String boardId)
    {
        this.boardId = boardId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public boolean isShowDelete()
    {
        return isShowDelete;
    }

    public void setShowDelete(boolean isShowDelete)
    {
        this.isShowDelete = isShowDelete;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}
