package org.mifos.mobilebanking.models.survey;

/*
 * Created by saksham on 18/June/2018
 */

import java.io.Serializable;

public class ScorecardValues implements Serializable {

    private String createdOn;

    private String responseId;

    private String questionId;

    private String value;

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
