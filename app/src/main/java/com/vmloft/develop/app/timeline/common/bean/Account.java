package com.vmloft.develop.app.timeline.common.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVUser;

/**
 * Created by lzan13 on 2017/11/24.
 * 账户实体类
 */
@AVClassName("Account")
public class Account extends AVUser {

    public Account() {}

    public String getAvatar() {
        return this.getString("avatar");
    }

    public void setAvatar(String avatar) {
        this.put("avatar", avatar);
    }

    public String getCover() {
        return this.getString("cover");
    }

    public void setCover(String cover) {
        this.put("cover", cover);
    }

    public int getGender() {
        return this.getInt("gender");
    }

    public void setGender(int gender) {
        this.put("gender", gender);
    }

    public String getAddress() {
        return this.getString("address");
    }

    public void setAddress(String address) {
        this.put("address", address);
    }

    public String getNickname() {
        return this.getString("nickname");
    }

    public void setNickname(String nickname) {
        this.put("nickname", nickname);
    }

    public String getDescription() {
        return this.getString("description");
    }

    public void setDescription(String description) {
        this.put("description", description);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tobjectId:" + getObjectId());
        builder.append("\n\tusername:" + getUsername());
        builder.append("\n\temail:" + getEmail());
        builder.append("\n\tphones:" + getMobilePhoneNumber());
        builder.append("\n\tavatar:" + getAvatar());
        builder.append("\n\tcover:" + getCover());
        builder.append("\n\tnickname:" + getNickname());
        builder.append("\n\tgender:" + getGender());
        builder.append("\n\taddress:" + getAddress());
        builder.append("\n\tdescription:" + getDescription());
        builder.append("\n\tcreateAt:" + getCreatedAt());
        builder.append("\n\tupdateAt:" + getUpdatedAt());
        return builder.toString();
    }
}
