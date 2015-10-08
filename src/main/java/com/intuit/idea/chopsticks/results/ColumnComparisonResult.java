package com.intuit.idea.chopsticks.results;

import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;

/**
 * TODO - make this a message class. subclass the messages for data/exist...meta...
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public class ColumnComparisonResult {
    private final boolean outcome;
    private final boolean isPk;
    private final String sField;
    private final String tField;
    private final Object sVal;
    private final Object tVal;

    private ColumnComparisonResult(Boolean outcome, String sField, String tField, Object sVal, Object tVal, boolean isPk) {
        this.outcome = outcome;
        this.sField = sField;
        this.tField = tField;
        this.sVal = sVal;
        this.tVal = tVal;
        this.isPk = isPk;
    }

    public static ColumnComparisonResult createMates(Boolean outcome, CombinedMetadata metadata, Object sVal, Object tVal, boolean isPk) {
        return new ColumnComparisonResult(outcome, metadata.getSrcColumnLabel(), metadata.getTarColumnLabel(), sVal, tVal, isPk);
    }

    public static ColumnComparisonResult createMates(Boolean outcome, String sField, String tField, Object sVal, Object tVal, boolean isPk) {
        return new ColumnComparisonResult(outcome, sField, tField, sVal, tVal, isPk);
    }

    public static ColumnComparisonResult createMatesFromMeta(Boolean outcome, CombinedMetadata metadata, Object sVal, Object tVal) {
        return new ColumnComparisonResult(outcome, metadata.getSrcColumnLabel(), metadata.getTarColumnLabel(), sVal, tVal, metadata.isPk());
    }

    public static ColumnComparisonResult createOnlySource(String sField, Object sVal, boolean isPk) {
        return new ColumnComparisonResult(false, sField, null, sVal, null, isPk);
    }

    public static ColumnComparisonResult createOnlyTarget(String tField, Object tVal, boolean isPk) {
        return new ColumnComparisonResult(false, null, tField, null, tVal, isPk);
    }

    public static ColumnComparisonResult createOnlySource(CombinedMetadata metadata, Object sVal) {
        return new ColumnComparisonResult(false, metadata.getSrcColumnLabel(), null, sVal, null, metadata.isPk());
    }

    public static ColumnComparisonResult createOnlyTarget(CombinedMetadata metadata, Object tVal) {
        return new ColumnComparisonResult(false, null, metadata.getTarColumnLabel(), null, tVal, metadata.isPk());
    }

    public static ColumnComparisonResult createOnlySourceField(String sField, boolean isPk) {
        return new ColumnComparisonResult(false, sField, null, null, null, isPk);
    }

    public static ColumnComparisonResult createOnlyTargetField(String tField, boolean isPk) {
        return new ColumnComparisonResult(false, null, tField, null, null, isPk);
    }

    public boolean isPk() {
        return isPk;
    }

    public Boolean getOutcome() {
        return outcome;
    }


    public String getField() {
        return getsField();
    }

    public String getsField() {
        return sField;
    }


    public String gettField() {
        return tField;
    }

    public Object getsVal() {
        return sVal;
    }


    public Object gettVal() {
        return tVal;
    }

}
