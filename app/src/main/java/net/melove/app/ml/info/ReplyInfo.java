package net.melove.app.ml.info;

/**
 * Created by Administrator on 2015/3/25.
 */
public class ReplyInfo {

    private String userId;
    private String noteId;
    private String replyId;
    private String content;
    private String createAt;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }



    public String getUserId() {
        return userId;
    }

    public String getNoteId() {
        return noteId;
    }

    public String getReplyId() {
        return replyId;
    }

    public String getContent() {
        return content;
    }

    public String getCreateAt() {
        return createAt;
    }
}
