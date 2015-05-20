package net.melove.app.ml.info;


import android.content.ContentValues;
import android.database.Cursor;

import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.utils.MLDate;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2014/12/27.
 */
public class UserInfo {

    private String loveId;
    private String userId;
    private String spouseId;
    private String signinname;
    private String nickname;
    private String email;
    private String avatar;
    private String cover;
    private int gender;
    private String location;
    private String signature;
    private int replyCount;
    private int noteCount;
    private String createAt;
    private String updateAt;
    private String accessToken;


    public UserInfo() {

    }

    public UserInfo(JSONObject user) throws JSONException {
        this.loveId = user.getString(MLDBConstants.COL_LOVE_ID);
        this.spouseId = user.getString(MLDBConstants.COL_SPOUSE_ID);
        this.userId = user.getString(MLDBConstants.COL_USER_ID);
        this.signinname = user.getString(MLDBConstants.COL_SIGNINNAME);
        this.email = user.getString(MLDBConstants.COL_EMAIL);
        this.nickname = user.getString(MLDBConstants.COL_NICKNAME);
        this.avatar = user.getString(MLDBConstants.COL_AVATAR);
        this.cover = user.getString(MLDBConstants.COL_COVER);
        this.gender = user.getInt(MLDBConstants.COL_GENDER);
        this.location = user.getString(MLDBConstants.COL_LOCATION);
        this.signature = user.getString(MLDBConstants.COL_SIGNATURE);
        this.noteCount = user.getInt(MLDBConstants.COL_NOTE_COUNT);
        this.replyCount = user.getInt(MLDBConstants.COL_REPLY_COUNT);
        this.createAt = MLDate.formatDate(user.getString(MLDBConstants.COL_CREATE_AT));
        this.updateAt = MLDate.formatDate(user.getString(MLDBConstants.COL_UPDATE_AT));
        this.accessToken = user.getString(MLDBConstants.COL_ACCESS_TOKEN);

    }

    public UserInfo(Cursor cursor) {
        this.loveId = cursor.getString(0);
        this.spouseId = cursor.getString(1);
        this.userId = cursor.getString(2);
        this.signinname = cursor.getString(3);
        this.email = cursor.getString(4);
        this.nickname = cursor.getString(5);
        this.avatar = cursor.getString(6);
        this.cover = cursor.getString(7);
        this.gender = cursor.getInt(8);
        this.location = cursor.getString(9);
        this.signature = cursor.getString(10);
        this.noteCount = cursor.getInt(11);
        this.replyCount = cursor.getInt(12);
        this.createAt = cursor.getString(13);
        this.updateAt = cursor.getString(14);
        this.accessToken = cursor.getString(15);
    }

    public void changeInfo() {
        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        ContentValues values = new ContentValues();
        values.put(MLDBConstants.COL_LOVE_ID, this.getLoveId());
        values.put(MLDBConstants.COL_SPOUSE_ID, this.getSpouseId());
        values.put(MLDBConstants.COL_USER_ID, this.getUserId());
        values.put(MLDBConstants.COL_SIGNINNAME, this.getSigninname());
        values.put(MLDBConstants.COL_EMAIL, this.getEmail());
        values.put(MLDBConstants.COL_NICKNAME, this.getNickname());
        values.put(MLDBConstants.COL_AVATAR, this.getAvatar());
        values.put(MLDBConstants.COL_COVER, this.getCover());
        values.put(MLDBConstants.COL_GENDER, this.getGender());
        values.put(MLDBConstants.COL_LOCATION, this.getLocation());
        values.put(MLDBConstants.COL_SIGNATURE, this.getSignature());
        values.put(MLDBConstants.COL_UPDATE_AT, this.getUpdateAt());
        values.put(MLDBConstants.COL_ACCESS_TOKEN, this.getAccessToken());
        String whereClause = MLDBConstants.COL_USER_ID + "=?";
        String args[] = {this.getUserId()};

        long result = mldbHelper.updateData(MLDBConstants.TB_USER, values, whereClause, args);
        if (result == 0) {
            mldbHelper.insterData(MLDBConstants.TB_USER, values);
        }
        mldbHelper.closeDatabase();
    }

    public void setLoveId(String loveId) {
        this.loveId = loveId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseId = spouseId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSigninname(String signinname) {
        this.signinname = signinname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getLoveId() {
        return loveId;
    }

    public String getSpouseId() {
        return spouseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSigninname() {
        return signinname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCover() {
        return cover;
    }

    public int getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getSignature() {
        return signature;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
