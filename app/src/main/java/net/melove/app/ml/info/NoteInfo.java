package net.melove.app.ml.info;

import android.database.Cursor;

import net.melove.app.ml.db.MLDBConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/3/25.
 */
public class NoteInfo {
    /**
     * noteType:
     *  1.text
     *  2.image
     *  3.system
     *      a.sleep
     *      b.wake up
     */
    private String loveId;
    private String userId;
    private UserInfo userInfo;
    private String noteId;
    private String noteType;
    private String image;
    private String content;
    private String createAt;

    public NoteInfo() {

    }

    public NoteInfo(String loveId, String userId, String noteId, String noteType, String image, String content, String createAt) {
        this.loveId = loveId;
        this.userId = userId;
        this.noteId = noteId;
        this.noteType = noteType;
        this.image = image;
        this.content = content;
        this.createAt = createAt;
    }

    public NoteInfo(JSONObject note) throws JSONException {
        this.loveId = note.getString(MLDBConstants.COL_LOVE_ID);
        this.userId = note.getString(MLDBConstants.COL_USER_ID);
        this.noteId = note.getString(MLDBConstants.COL_NOTE_ID);
        this.noteType = note.getString(MLDBConstants.COL_NOTE_TYPE);
        this.image = note.getString(MLDBConstants.COL_IMAGE);
        this.content = note.getString(MLDBConstants.COL_CONTENT);
        this.createAt = note.getString(MLDBConstants.COL_CREATE_AT);
    }

    public NoteInfo(Cursor cursor) {
        this.setLoveId(cursor.getString(0));
        this.setUserId(cursor.getString(1));
        this.setNoteId(cursor.getString(2));
        this.setNoteType(cursor.getString(3));
        this.setImage(cursor.getString(4));
        this.setContent(cursor.getString(5));
        this.setCreateAt(cursor.getString(6));
    }

    public void setLoveId(String loveId) {
        this.loveId = loveId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserInfo(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }


    public String getLoveId() {
        return loveId;
    }

    public String getUserId() {
        return userId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getNoteId() {
        return noteId;
    }

    public String getNoteType() {
        return noteType;
    }

    public String getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public String getCreateAt() {
        return createAt;
    }
}
