package com.example.app.dto;

import com.example.app.entity.Application;
import com.example.app.entity.ApplicationDocument;
import com.example.app.entity.ApplicationEducation;
import com.example.app.entity.ApplicationExamScore;
import com.example.app.entity.ApplicationPreference;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class SaveDraftApplicationRequest {

    private Application application;

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    private List<ApplicationEducation> educations;

    public List<ApplicationEducation> getEducations() {
        return this.educations;
    }

    public void setEducations(List<ApplicationEducation> educations) {
        this.educations = educations;
    }

    private List<ApplicationExamScore> examScores;

    public List<ApplicationExamScore> getExamScores() {
        return this.examScores;
    }

    public void setExamScores(List<ApplicationExamScore> examScores) {
        this.examScores = examScores;
    }

    private List<ApplicationPreference> preferences;

    public List<ApplicationPreference> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(List<ApplicationPreference> preferences) {
        this.preferences = preferences;
    }

    private List<ApplicationDocument> documents;

    public List<ApplicationDocument> getDocuments() {
        return this.documents;
    }

    public void setDocuments(List<ApplicationDocument> documents) {
        this.documents = documents;
    }
}
