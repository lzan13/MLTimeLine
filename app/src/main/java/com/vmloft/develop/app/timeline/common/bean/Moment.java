package com.vmloft.develop.app.timeline.common.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by lzan13 on 2018/4/22.
 * 时刻信息实体类
 */
@AVClassName("Moment")
public class Moment extends AVObject {

    public Moment() {}

    public Account getAuthor() {
        return this.getAVUser("author", Account.class);
    }

    public void setAuthor(Account author) {
        this.put("author", author);
    }

    public String getImage() {
        return this.getString("image");
    }

    public void setImage(String image) {
        this.put("imageUrl", image);
    }

    public String getVideo() {
        return this.getString("video");
    }

    public void setVideo(String video) {
        this.put("video", video);
    }

    public String getContent() {
        return this.getString("content");
    }

    public void setContent(String content) {
        this.put("content", content);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n\tobjectId:" + getObjectId());
        buffer.append("\n\tauthor:" + getAuthor());
        buffer.append("\n\timage:" + getImage());
        buffer.append("\n\tvideo:" + getVideo());
        buffer.append("\n\tcreatedAt:" + getCreatedAt());
        buffer.append("\n\tupdatedAt:" + getUpdatedAt());
        return buffer.toString();
    }
}
