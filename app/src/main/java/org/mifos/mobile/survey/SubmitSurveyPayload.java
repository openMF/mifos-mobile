package org.mifos.mobile.survey;

/*
 * Created by saksham on 18/June/2018
 */

import java.io.Serializable;
import java.util.List;

public class SubmitSurveyPayload implements Serializable {

    private String id;

    private String username;

    private List<ScorecardValues> scorecardValues;

    private String userId;

    private String surveyName;

    private String surveyId;

    private String clientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ScorecardValues> getScorecardValues() {
        return scorecardValues;
    }

    public void setScorecardValues(List<ScorecardValues> scorecardValues) {
        this.scorecardValues = scorecardValues;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
