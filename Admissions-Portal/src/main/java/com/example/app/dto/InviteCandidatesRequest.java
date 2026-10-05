package com.example.app.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class InviteCandidatesRequest {

    private List<String> applicationIds;

    public List<String> getApplicationIds() {
        return this.applicationIds;
    }

    public void setApplicationIds(List<String> applicationIds) {
        this.applicationIds = applicationIds;
    }

    private String roundId;

    public String getRoundId() {
        return this.roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }
}
