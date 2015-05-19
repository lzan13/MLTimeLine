package net.melove.app.ml.info;

import net.melove.app.ml.db.MLDBConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2015/3/25.
 */
public class MessageInfo {
    /**
     * type:
     * 1.reply 回复
     * 2.spouse 添加另一半
     * 3.other 其他类型
     * state: 是否已读
     */
    private String sendUserId;
    private String receiveUserId;
    private String messageId;
    private String messageType;
    private String content;
    private int state;
    private String createAt;

    public MessageInfo() {

    }

    public MessageInfo(JSONObject note) throws JSONException {
        this.sendUserId = note.getString(MLDBConstants.COL_SEND_USER_ID);
        this.receiveUserId = note.getString(MLDBConstants.COL_RECEIVE_USER_ID);
        this.messageId = note.getString(MLDBConstants.COL_MESSAGE_ID);
        this.messageType = note.getString(MLDBConstants.COL_MESSAGE_TYPE);
        this.content = note.getString(MLDBConstants.COL_CONTENT);
        this.state = note.getInt(MLDBConstants.COL_STATE);
        this.createAt = note.getString(MLDBConstants.COL_CREATE_AT);

    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }


    public String getSendUserId() {
        return sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public int getState() {
        return state;
    }

    public String getCreateAt() {
        return createAt;
    }
}
