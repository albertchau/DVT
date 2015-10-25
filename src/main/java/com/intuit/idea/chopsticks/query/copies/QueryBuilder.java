package com.intuit.idea.chopsticks.query.copies;

import com.intuit.idea.app.globals.DateFunctions;
import com.intuit.idea.app.globals.DateType;
import com.intuit.idea.app.globals.Literals;
import com.intuit.idea.app.json_bindings.Table;
import com.intuit.idea.app.json_bindings.WhereClause;
import com.intuit.idea.app.metadata.Metadata;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.cookie.DateParseException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.intuit.idea.app.globals.Methods.isNull;
import static com.intuit.idea.app.globals.Methods.nonNull;
import static java.util.Collections.sort;

/**
 * QueryBuilder base class that other query builders inherits from
 * The classes that inherit from this need to inherit the internal builder too
 * for more info go to
 * https://weblogs.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses
 */
@SuppressWarnings("Duplicates")
public class QueryBuilder {

    private static Logger log = LoggerFactory.getLogger(QueryBuilder.class);
    protected List<Metadata> metadatasUsedInDataQuery = new ArrayList<>();
    protected List<Metadata> metadatas = new ArrayList<>();
    protected List<String> excludedColumns;
    protected List<String> includedColumns;
    protected int fetchAmount;
    protected String startDate;
    protected String endDate;
    protected String testType;
    protected Table table;
    protected String fromClause;
    protected List<WhereClause> whereClauses;

    protected DateType dateType;
    protected String dateFormatForQuery;
    //Formatter used
    protected DateTimeFormatter dateTimeFormat;
    protected String hiveDateFormat;
    private boolean ascending;

    QueryBuilder(Builder<?> builder) throws Exception {
        if (!builder.validParams().isEmpty()) {
            throw new Exception("Cannot generateDataQuery query: " + builder.validParams());
        }
        this.endDate = builder.eDate;
        this.startDate = builder.sDate;
        this.testType = builder.testType;
        this.fetchAmount = builder.fetchAmount;
        this.table = builder.table;
        this.includedColumns = builder.table.getIncludeColumns();
        this.excludedColumns = builder.table.getExcludeColumns();

        setMetadatas(builder.metadatas);
        setColumnsToQuery();
        this.dateFormatForQuery = Literals.DATE_FORMAT_2;
        this.dateType = DateType.NOT_SET_YET;
        this.dateTimeFormat = DateTimeFormat.forPattern(dateFormatForQuery);
        this.ascending = builder.ascending;
        this.hiveDateFormat = builder.hDateFormat;
        this.whereClauses = builder.whereClauses;
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    private void setColumnsToQuery() {
        if (includedColumns.isEmpty() && excludedColumns.isEmpty()) {
            metadatasUsedInDataQuery = new ArrayList<>(metadatas);
            return;
        }
        if (!includedColumns.isEmpty()) {
            for (Metadata metadata : metadatas) {
                String column = metadata.getColumn().toUpperCase();
                if (includedColumns.contains(column) && !excludedColumns.contains(column))
                    metadatasUsedInDataQuery.add(metadata);
                else if (metadata.isPk()) metadatasUsedInDataQuery.add(metadata);
            }
        } else {
            for (Metadata metadata : metadatas) {
                String column = metadata.getColumn();
                if (!excludedColumns.contains(column)) metadatasUsedInDataQuery.add(metadata);
                else if (metadata.isPk()) metadatasUsedInDataQuery.add(metadata);
            }
        }
    }

    /**
     * Generates the count query based on test type
     *
     * @param useJanusMetadata should we use janus metadata?
     * @return String for the count query for the specified database
     */
    public String generateCountQuery(boolean useJanusMetadata) {
        //make sure there is a table to query from
        if (getFrom() == null) return null;

        StringBuilder s = new StringBuilder("SELECT ");
        s.append("count(*)");
        s.append(" FROM ");
        s.append(getFrom());
        if (!getWhere(true, useJanusMetadata).isEmpty()) {
            s.append(" WHERE ").append(getWhere(true, useJanusMetadata));
        }
        return s.toString();
    }

    /**
     * Generates the existence query based on test type
     *
     * @return String for the existence query for the specified database
     */
    public String generateExistenceQuery() {
        StringBuilder s = new StringBuilder("SELECT ");
        String selectClause = StringUtils.join(columnsUsedInExistenceQuery(), ",");
        if (!testType.equals(Literals.FULL)) {
            if (!StringUtils.containsIgnoreCase(selectClause, getQueryableDateColumn())) {
                selectClause += "," + getQueryableDateColumn();
            }
        }
        s.append(selectClause);
        s.append(" FROM ");
        s.append(getFrom());
        if (!testType.equals(Literals.FULL)) {
            if (!getWhere().isEmpty()) {
                s.append(" WHERE ").append(getWhere());
            }
        }
        if (hasPks()) {
            s.append(" ORDER BY ").append(getOrderBy());
        }
        if (fetchAmount > 0) {
            s.append(" LIMIT ").append(fetchAmount);
        }
        return s.toString();
    }

    /**
     * Generates the data query based on test type
     *
     * @return String for the data query for the specified database
     */
    public String generateDataQuery() {
        StringBuilder query = new StringBuilder();

        query.append("SELECT ");
        query.append(StringUtils.join(columnsUsedInDataQuery(), ","));
        query.append(" FROM ").append(getFrom());

        if (testType.equals(Literals.INCREMENTAL)) {
            query.append(" WHERE ").append(getWhere());
        }

        if (getOrderBy() != null) query.append(" ORDER BY ").append(getOrderBy());

        if (fetchAmount > 0) query.append(" LIMIT ").append(fetchAmount);

        return query.toString();
    }

    protected List<String> existenceSelect(String formatForDate) {
        List<String> rtn = new ArrayList<>();
        for (Metadata md : metadatas) {
            if (md.isPk()) {
                if (QueryUtils.isDate(md)) {
                    int sum = StringUtils.countMatches(formatForDate, "%s");
                    String[] x = new String[sum];
                    for (int i = 0; i < sum; i++) x[i] = md.getColumn();
                    rtn.add(String.format(formatForDate, x));
                } else rtn.add(md.getColumn());
            } else break;
        }
        if (testType == null || !testType.equals(Literals.FULL)) {
            rtn.add(getQueryableDateColumn());
        }
        return rtn;
    }

    protected boolean hasPks() {
        return metadatas.get(0).isPk();
    }

    protected String dataSelect(String formatForDate) {
        List<String> selectList = new ArrayList<>();
        for (Metadata md : metadatasUsedInDataQuery) {
            if (QueryUtils.isDate(md)) {
                int sum = StringUtils.countMatches(formatForDate, "%s");
                String[] x = new String[sum];
                for (int i = 0; i < sum; i++) x[i] = md.getColumn();
                selectList.add(String.format(formatForDate, x));
            } else selectList.add(md.getColumn());
        }
        return StringUtils.join(selectList, ",");
    }

    /**
     * depending on type of database (database or schemaless) we will generate the proper source from clause
     *
     * @return clause
     */
    protected String getFrom() {
        if (fromClause == null) return table.getName();
        return fromClause;
    }

    protected void setFrom(String fromClause) {
        this.fromClause = fromClause;
    }

    String getOrderBy() {
        String ordering;
        if (ascending) ordering = "asc";
        else ordering = "desc";
        int idx;
        for (idx = 0; idx < metadatas.size(); idx++) {
            Metadata md = metadatas.get(idx);
            if (!md.isPk()) break;
        }
        return StringUtils.join(metadatas.subList(0, idx), " " + ordering + ",") + " " + ordering;
    }

    /**
     * Depending on what fields were passed in the JSON file, and what kind of test we are running, we will generate
     * the source where clause
     *
     * @return the where clause string
     */
    protected String getWhere(boolean isCountQuery, boolean isUsingJanusMetadata) {
        List<String> toBeJoined = new ArrayList<>();
        if ((!isCountQuery && !isUsingJanusMetadata) || (isCountQuery && !isUsingJanusMetadata)) { //date where clause
            if (testType.equals(Literals.INCREMENTAL)) { //if incremental: no dates specified; or one of the dates specified; if both specified use start date
                toBeJoined.add(getIncrementalDateString());
            } else if (testType.equals(Literals.HISTORIC)) { //if Historic either: both dates specified; or one of the dates specified.
                toBeJoined.add(getHistoricDateString());
            }
        }
        if (nonNull(whereClauses)) {
            for (WhereClause whereClause : whereClauses) {
                toBeJoined.add(whereClause.stringify());
            }
        }
        return StringUtils.join(toBeJoined, " AND ");
    }

    protected String getWhere() {
        return getWhere(false, false);
    }

    protected String getHistoricDateString() {
        try { //both dates specified
            if (getStartDate() != null && getEndDate() != null) {
                DateTime startHistoricDateTime = toDateTime(getStartDate()); //exception thrown here
                DateTime endHistoricDateTime = toDateTime(getEndDate()); //exception thrown here
                return getDateRangeString(startHistoricDateTime, endHistoricDateTime);
            }
        } catch (DateParseException e) {
            log.error(e.getMessage());
            log.error("Invalid parameters for Historic type with both parameters specified, returning null.");
            return null;
        }

        try { //only start date specified
            if (getStartDate() != null) {
                DateTime startHistoricDateTime = toDateTime(getStartDate()); //exception thrown here
                return getDateRangeString(startHistoricDateTime, null);
            }
        } catch (DateParseException e) {
            log.error(e.getMessage());
            log.error("Invalid parameters for Historic type with only start date specified, returning null.");
            return null;
        }

        try { //only end date specified
            if (getEndDate() != null) {
                DateTime endHistoricDateTime = toDateTime(getEndDate()); //exception thrown here
                return getDateRangeString(null, endHistoricDateTime);
            }
        } catch (DateParseException e) {
            log.error(e.getMessage());
            log.error("Invalid parameters for Historic type with only end date specified, returning null.");
            return null;
        }
        return null;
    }

    protected String getIncrementalDateString() {
        try { //no dates specified
            if (getStartDate() == null && getEndDate() == null) {
                DateTime todayDateTime = new DateTime();
                DateTime yesterdayDateTime = new DateTime(todayDateTime.minusDays(1));
                return getDateRangeString(yesterdayDateTime, todayDateTime);
            }
        } catch (DateParseException ignored) {
            return null;
        }

        try { //both dates specified
            if (getStartDate() != null && getEndDate() != null) {
                DateTime startIncrementalDateTime = toDateTime(getStartDate()); //exception thrown here
                DateTime endIncrementalDateTime = toDateTime(getEndDate());
                return getDateRangeString(startIncrementalDateTime, endIncrementalDateTime);
            }
        } catch (DateParseException e) {
            log.error(e.getMessage());
            log.error("Invalid parameters for Incremental type with both parameters specified, returning null.");
            return null;
        }

        try { //only start date specified - use start date, ignore end date, set end date = start date + one day
            if (getStartDate() != null) {
                DateTime startIncrementalDateTime = toDateTime(getStartDate()); //exception thrown here
                DateTime endIncrementalDateTime = new DateTime(startIncrementalDateTime.plusDays(1)); //add a day
                return getDateRangeString(startIncrementalDateTime, endIncrementalDateTime);
            }
        } catch (DateParseException e) {
            log.error(e.getMessage());
            log.error("Invalid parameters for Incremental type with only start date specified, returning null.");
            return null;
        }

        try { //only end date specified, use end date as ending date and endingDate - one day as start date
            DateTime startIncrementalDateTime = toDateTime(getEndDate()); //exception thrown here
            DateTime endIncrementalDateTime = new DateTime(startIncrementalDateTime.plusDays(1));
            return getDateRangeString(startIncrementalDateTime, endIncrementalDateTime);
        } catch (DateParseException e) {
            log.error(e.getMessage());
            log.error("Invalid parameters for Incremental type with only end date specified, returning null.");
            return null;
        }
    }

    //Override this method!!
    protected String getDateRangeString(DateTime start, DateTime end) {
        if (start == null && end == null) return null; //case shouldn't happen. just in case...

        if (end == null) {
            String startStr = dateTimeFormat.print(start);
            return String.format("date_column(%s) > = timestamp(%s)", getQueryableDateColumn(), startStr);
        }
        if (start == null) {
            String endStr = dateTimeFormat.print(end);
            return String.format("date_column(%s) <= timestamp(%s)", getQueryableDateColumn(), endStr);
        }

        String startStr = dateTimeFormat.print(start);
        String endStr = dateTimeFormat.print(end);
        return String.format("date_column(%s) between timestamp(%s) and timestamp(%s)", getQueryableDateColumn(), startStr, endStr);
    }

    protected boolean isValidDateFormat(String date) {
        if (DateFunctions.isCorrectTimeStamp(date)) dateType = DateType.CORRECT_TIMESTAMP;
        else if (DateFunctions.isAmericanDateSpaceTime(date)) dateType = DateType.AMERICAN_DATE_SPACE_TIME;
        else if (DateFunctions.isJustDate(date)) dateType = DateType.JUST_DATE;
        else if (DateFunctions.isTimestampWithSpace(date)) dateType = DateType.TIMESTAMP_WITH_SPACE;
        else if (DateFunctions.isJustDateSlashes(date)) dateType = DateType.JUST_DATE_SLASHES;
        else {
            dateType = DateType.TYPE_UNDETERMINED;
            return false;
        }
        return true;
    }

    protected DateTime toDateTime(String date) {
        switch (dateType) {
            case CORRECT_TIMESTAMP:
                return new DateTime(DateFunctions.getCorrectTimeStamp(date) * 1000);
            case AMERICAN_DATE_SPACE_TIME:
                return new DateTime(DateFunctions.getAmericanDateSpaceTime(date) * 1000);
            case TIMESTAMP_WITH_SPACE:
                return new DateTime(DateFunctions.getTimeStampWithSpace(date) * 1000);
            case JUST_DATE:
                return new DateTime(DateFunctions.getJustDate(date) * 1000);
            case JUST_DATE_SLASHES:
                return new DateTime(DateFunctions.getJustDateSlashes(date) * 1000);
            default:
                throw new IllegalArgumentException("Somehow illegal date.. changed from when we validated it to now.");
        }
    }

    /**
     * returns the end date which depends on the type of test to be go
     *
     * @return end date in string format
     */
    String getEndDate() throws DateParseException {
        //checks to see if end date exists
        if (endDate == null || endDate.isEmpty()) return null;

        //check to see if it is a valid date format
        if (!isValidDateFormat(endDate))
            throw new DateParseException("Invalid/Malformed End Date Format. Caused by: " + endDate);

        return endDate;
    }

    /**
     * returns the start date which depends on the type of test to be go Also sets objects' DateType
     *
     * @return start date in string format
     */
    String getStartDate() throws DateParseException {
        //checks to see if start date exists
        if (startDate == null || startDate.isEmpty()) {
            return null;
        }
        //check to see if it is a valid date format
        if (!isValidDateFormat(startDate)) {
            throw new DateParseException("Invalid/Malformed Start Date Format. Caused by: " + startDate);
        }
        return startDate;
    }

    String getHiveDateFormat() throws DateParseException {
        //checks to see if hive date format exists
        if (hiveDateFormat == null || hiveDateFormat.isEmpty()) return null;

        //check to see if it is a valid date format
        if (!isValidDateFormat(hiveDateFormat))
            throw new DateParseException("Invalid/Malformed Start Date Format. Caused by: " + startDate);

        return hiveDateFormat;
    }

    String getQueryableDateColumn() {
        return table.getQueryableDateColumn();
    }

    public List<Metadata> metadatasUsedInDataQuery() {
        return metadatasUsedInDataQuery;
    }

    /**
     * Generates a List of Strings the specify which columns are queried in the data query
     *
     * @return a list of strings used in data query
     */
    public List<String> columnsUsedInDataQuery() {
        List<String> rtn = new ArrayList<>();
        for (Metadata metadata : metadatasUsedInDataQuery) {
            rtn.add(metadata.getColumn());
        }
        return rtn;
    }

    /**
     * Generates a List of Strings the specify which columns are queried in the existence query
     *
     * @return a list of strings used in existence query
     */
    public List<String> columnsUsedInExistenceQuery() {
        List<String> rtn = new ArrayList<>();
        for (Metadata md : metadatas) {
            if (md.isPk()) {
                rtn.add(md.getColumn());
            } else break;
        }
        if (testType == null || !testType.equals(Literals.FULL)) {
            rtn.add(getQueryableDateColumn());
        }
        return rtn;
    }

    /**
     * Generates the list of metadatas that were used in the existence query. Does this by finding the Primary Keys
     *
     * @return a list of metadatas that were used in existence query
     */
    public List<Metadata> metadatasUsedInExistenceQuery() {
        List<Metadata> existenceColumns = new ArrayList<>();
        for (Metadata metadata : metadatas) {
            if (metadata.isPk()) {
                existenceColumns.add(metadata);
            } else break;
        }
        if (testType == null || !testType.equals(Literals.FULL)) {
            for (Metadata md : metadatas) {
                if (md.getColumn().equals(getQueryableDateColumn())) existenceColumns.add(md);
            }
        }
        return existenceColumns;

    }

    /**
     * Sets this objects metadatas to use in the data and existence queries.
     * This method sanitizes the input by alphabetizing the input and moving primary keys to the
     *
     * @param metadatas the metadata list to set as the metadatas for this class.
     */
    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = new ArrayList<>();
        sort(metadatas);
        int placeHolder = 0;
        for (Metadata md : metadatas) {
            if (md.isPk()) {
                this.metadatas.add(placeHolder++, md);
            } else {
                this.metadatas.add(md);
            }
        }
    }

    public void addWhereClause(WhereClause whereClause) {
        if (isNull(whereClauses)) {
            whereClauses = new ArrayList<>();
        }
        whereClauses.add(whereClause);
    }

    public void removeWhereClause(WhereClause whereClause) {
        if (nonNull(whereClause)) {
            whereClauses.remove(whereClause);
        }
    }

    public static abstract class Builder<T extends Builder<T>> {
        private int fetchAmount;
        private String sDate;
        private String eDate;
        private String testType;
        private Table table;
        private List<Metadata> metadatas = new ArrayList<>();
        private List<WhereClause> whereClauses = new ArrayList<>();
        private boolean ascending = true;
        private String hDateFormat;

        protected abstract T self();

        public T table(Table table) {
            this.table = table;
            return self();
        }

        public T testType(String testType) {
            this.testType = testType;
            return self();
        }

        public T fetchAmount(int fetchAmount) {
            this.fetchAmount = fetchAmount;
            return self();
        }

        public T hiveDateFormat(String hDateFormat) {
            this.hDateFormat = hDateFormat;
            return self();
        }

        public T startDate(String sDate) {
            this.sDate = sDate;
            return self();
        }

        public T endDate(String eDate) {
            this.eDate = eDate;
            return self();
        }

        public T ascending(boolean ascending) {
            this.ascending = ascending;
            return self();
        }

        public T metadatas(List<Metadata> metadatas) {
            this.metadatas = metadatas;
            return self();
        }

        public T whereClauses(List<WhereClause> whereClauses) {
            this.whereClauses = whereClauses;
            return self();
        }

        private String validParams() {
            String rtn = "";
            if (metadatas.isEmpty()) {
                rtn += "Column metadata is null. ";
            }
            if (table == null) {
                rtn += "There is no table. ";
            }
            if (testType == null) {
                rtn += "Test type is null. ";
            }
            return rtn;
        }

        public QueryBuilder build() throws Exception {
            return new QueryBuilder(this);
        }

        public String getQuery() throws Exception {
            QueryBuilder query = new QueryBuilder(this);
            return query.generateDataQuery();
        }
    }

    private static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}