package com.intuit.idea.chopsticks.query.copies;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates a oracle query given proper input builder parameters
 */
public class OracleQueryBuilder extends QueryBuilder {
    private final static Logger log = LoggerFactory.getLogger(OracleQueryBuilder.class);

    private final String schema;

    private OracleQueryBuilder(Builder<?> builder) throws Exception {
        super(builder);
        if (builder.schema == null) {
            log.warn("Oracle table has no specified database.");
            this.schema = "";
            setFrom(table.getName());
        } else {
            this.schema = builder.schema;
            setFrom(schema + "." + table.getName());
        }
        if (builder.metadatas(metadatas) == null) {

        }
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    /**
     * Generates the existence query based on test type because oracle db, then we have to generate a nested from statement
     *
     * @return String for the existence query for the specified database
     */
    public String generateExistenceQuery() {
        if (!hasPks())
            return null;
        StringBuilder s = new StringBuilder("SELECT ");
        s.append(StringUtils.join(columnsUsedInExistenceQuery(), ","));
        s.append(" FROM ");
        s.append(getExistenceFrom());
        if (fetchAmount > 0)
            s.append(" WHERE ROWNUM <= ").append(fetchAmount);
        return s.toString();
    }

    private String getExistenceFrom() {
        StringBuilder query = new StringBuilder("(SELECT ");
        query.append(StringUtils.join(columnsUsedInExistenceQuery(), ","));//query.append(select);
        query.append(" FROM ");
        query.append(getFrom());
        if (!getWhere().isEmpty()) {
            query.append(" WHERE ").append(getWhere());
        }
        if(hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        query.append(")");
        return query.toString();
    }

    /**
     * Generates the data query based on test type because oracle db, then we have to generate a nested from statement
     *
     * @return String for the data query for the specified database
     */
    public String generateDataQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        if (columnsUsedInDataQuery() != null) {
        	query.append(StringUtils.join(columnsUsedInDataQuery(), ","));//query.append(dataSelect("TO_CHAR(%s, 'MM/DD/YYYY') AS %s"));
        }
        else {
        	query.append(" * "); // use it in case when data source is an mview as opposite to a table. mview doesn't have metadata and it cannot be described.
        }
        query.append(" FROM ");
        query.append(getNestedFrom());
        if (fetchAmount > 0)
            query.append(" WHERE ROWNUM <= ").append(fetchAmount);
        return query.toString();
    }

    private String getNestedFrom() {
        StringBuilder query = new StringBuilder("(SELECT ");
        if (metadatasUsedInDataQuery != null) {
        	String select = StringUtils.join(metadatasUsedInDataQuery, ",");
        	query.append(select);
        }
        else {
        	String select = " * ";
        	query.append(select);
        }

        query.append(" FROM ");
        query.append(getFrom());
        if (!getWhere().isEmpty()) {
            query.append(" WHERE ").append(getWhere());
        }
        if(hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        query.append(")");
        return query.toString();
    }

    //Override this method!!
    protected String getDateRangeString(DateTime start, DateTime end) {
        if(start == null && end == null)
            return null; //case shouldn't happen. just in case...

        if(end == null) {
            String startStr = dateTimeFormat.print(start);
            return String.format("%s >= to_date('%s','MM-dd-yyyy hh24:mi:ss')"
                    , getQueryableDateColumn()
                    , startStr);
        }
        if (start == null) {
            String endStr = dateTimeFormat.print(end);
            return String.format("%s <= to_date('%s','MM-dd-yyyy hh24:mi:ss')"
                    , getQueryableDateColumn()
                    , endStr);
        }

        String startStr = dateTimeFormat.print(start);
        String endStr = dateTimeFormat.print(end);
        return String.format("%s between to_date('%s','MM-dd-yyyy hh24:mi:ss') and to_date('%s','MM-dd-yyyy hh24:mi:ss')"
                , getQueryableDateColumn()
                , startStr
                , endStr);
    }

    public static abstract class Builder<T extends Builder<T>> extends QueryBuilder.Builder<T> {
        private String schema;

        public T schema(String schema) {
            this.schema = schema;
            return self();
        }

        public String getQuery() throws Exception {
            OracleQueryBuilder query = new OracleQueryBuilder(this);
            return query.generateDataQuery();
        }

        @Override
        public OracleQueryBuilder build() throws Exception {
            return new OracleQueryBuilder(this);
        }
    }

    private static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}
