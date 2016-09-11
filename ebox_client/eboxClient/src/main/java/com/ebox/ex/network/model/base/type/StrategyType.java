package com.ebox.ex.network.model.base.type;

/**
 * Created by Android on 2015/9/18.
 */
public class StrategyType {

    /*"strategy": {
                "pic_playtime": 10,
                "play_starttime": "0:00:00",
                "play_endtime": "23:59:59",
                "video_count": 1,
                "html_playtime": 10
            }
*/

    private Integer pic_playtime;
    private Integer html_playtime;
    private Integer video_count;
    private String play_starttime;
    private String play_endtime;

    public Integer getPic_playtime() {
        return pic_playtime;
    }

    public void setPic_playtime(Integer pic_playtime) {
        this.pic_playtime = pic_playtime;
    }

    public Integer getHtml_playtime() {
        return html_playtime;
    }

    public void setHtml_playtime(Integer html_playtime) {
        this.html_playtime = html_playtime;
    }

    public Integer getVide_count() {
        return video_count;
    }

    public void setVide_count(Integer vide_count) {
        this.video_count = vide_count;
    }

    public String getPlay_starttime() {
        return play_starttime;
    }

    public void setPlay_starttime(String play_starttime) {
        this.play_starttime = play_starttime;
    }

    public String getPlay_endtime() {
        return play_endtime;
    }

    public void setPlay_endtime(String play_endtime) {
        this.play_endtime = play_endtime;
    }
}
