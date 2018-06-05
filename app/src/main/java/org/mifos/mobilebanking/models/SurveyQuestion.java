package org.mifos.mobilebanking.models;

/*
 * Created by saksham on 20/May/2018
 */

import java.io.Serializable;
import java.util.List;

public class SurveyQuestion implements Serializable {

    int questionNumber;
    int selectedOption = -1;
    String question;
    List<String> options;

    public SurveyQuestion(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
