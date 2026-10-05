package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class RespondToQueryRequest {

    private String replyText;

    public String getReplyText() {
        return this.replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    private String newStatus;

    public String getNewStatus() {
        return this.newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
