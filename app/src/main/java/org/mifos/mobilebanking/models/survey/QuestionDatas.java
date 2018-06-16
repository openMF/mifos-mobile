package org.mifos.mobilebanking.models.survey;

/*
 * Created by saksham on 17/June/2018
 */

import java.io.Serializable;
import java.util.List;

public class QuestionDatas implements Serializable {

    private String id;

    private String text;

    private String componentKey;

    private String description;

    private List<ResponseDatas> responseDatas;

    private String sequenceNo;

    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComponentKey() {
        return componentKey;
    }

    public void setComponentKey(String componentKey) {
        this.componentKey = componentKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ResponseDatas> getResponseDatas() {
        return responseDatas;
    }

    public void setResponseDatas(List<ResponseDatas> responseDatas) {
        this.responseDatas = responseDatas;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
