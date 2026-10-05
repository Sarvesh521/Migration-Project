package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class UpdateQueryMessageRequest {

    private String queryId;

    public String getQueryId() {
        return this.queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    private String senderId;

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    private String messageText;

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    private String attachmentDocId;

    public String getAttachmentDocId() {
        return this.attachmentDocId;
    }

    public void setAttachmentDocId(String attachmentDocId) {
        this.attachmentDocId = attachmentDocId;
    }
}
