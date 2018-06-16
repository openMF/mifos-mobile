package org.mifos.mobilebanking.models.survey;

/*
 * Created by saksham on 17/June/2018
 */

import java.io.Serializable;

public class ResponseDatas implements Serializable {

    private String id;

    private String text;

    private String value;

    private String sequenceNo;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

}
