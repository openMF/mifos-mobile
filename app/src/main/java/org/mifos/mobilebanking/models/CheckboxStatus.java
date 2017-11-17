package org.mifos.mobilebanking.models;

/**
 * Created by dilpreet on 3/7/17.
 */

public class CheckboxStatus {
    private String status;
    private int color;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public CheckboxStatus(String status, int color) {
        this.status = status;
        this.color = color;
    }
}
