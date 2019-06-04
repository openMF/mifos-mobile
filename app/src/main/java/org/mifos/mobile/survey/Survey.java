package org.mifos.mobile.survey;

/*
 * Created by saksham on 16/June/2018
 */

import java.io.Serializable;
import java.util.List;

public class Survey implements Serializable {

    private String validTo;

    private String id;

    private List<QuestionDatas> questionDatas;

    private String description;

    private String name;

    private String validFrom;

    private String countryCode;

    private String key;

    private String[] componentDatas;

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<QuestionDatas> getQuestionDatas() {
        return questionDatas;
    }

    public void setQuestionDatas(List<QuestionDatas> questionDatas) {
        this.questionDatas = questionDatas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getComponentDatas() {
        return componentDatas;
    }

    public void setComponentDatas(String[] componentDatas) {
        this.componentDatas = componentDatas;
    }
}
