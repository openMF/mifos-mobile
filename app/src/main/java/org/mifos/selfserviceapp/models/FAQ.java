package org.mifos.selfserviceapp.models;

/**
 * Created by dilpreet on 12/8/17.
 */

public class FAQ {

    private String question;

    private String answer;

    private boolean selected;

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
        selected = false;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
