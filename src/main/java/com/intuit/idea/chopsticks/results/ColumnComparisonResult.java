package com.intuit.idea.chopsticks.results;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public class ColumnComparisonResult {
    private Boolean outcome;
    private String sField;
    private String tField;
    private String sVal;
    private String tVal;

    public ColumnComparisonResult(Boolean outcome, String sField, String tField, String sVal, String tVal) {
        this.setOutcome(outcome);
        this.setsField(sField);
        this.settField(tField);
        this.setsVal(sVal);
        this.settVal(tVal);
    }

    public Boolean getOutcome() {
        return outcome;
    }

    public void setOutcome(Boolean outcome) {
        this.outcome = outcome;
    }

    public String getField() {
        return getsField();
    }

    public String getsField() {
        return sField;
    }

    public void setsField(String sField) {
        this.sField = sField;
    }

    public String gettField() {
        return tField;
    }

    public void settField(String tField) {
        this.tField = tField;
    }

    public String getsVal() {
        return sVal;
    }

    public void setsVal(String sVal) {
        this.sVal = sVal;
    }

    public String gettVal() {
        return tVal;
    }

    public void settVal(String tVal) {
        this.tVal = tVal;
    }
}
