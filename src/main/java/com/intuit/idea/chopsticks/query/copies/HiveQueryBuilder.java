package com.intuit.idea.chopsticks.query.copies;

import com.intuit.idea.app.globals.Literals;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Metadata class gets the metadata for the table Created by achau1 on 5/25/14.
 */
public class HiveQueryBuilder extends QueryBuilder {

    private static Logger log = LoggerFactory.getLogger(HiveQueryBuilder.class);

    private HiveQueryBuilder(Builder<?> builder) throws Exception {
        super(builder);
        if (builder.database == null && !testType.equals(Literals.ADHOC))
            throw new Exception("Cannot create Hive query because there is no specified database.");
        String database = builder.database;
        setFrom(database + "." + table.getName());
        if (this.hiveDateFormat != null && !this.hiveDateFormat.isEmpty()) {
        	this.dateFormatForQuery = this.hiveDateFormat;
        	this.dateTimeFormat = DateTimeFormat.forPattern(dateFormatForQuery);
        } else {
        	this.dateFormatForQuery = Literals.DATE_FORMAT_HIVE_2;
        	this.dateTimeFormat = DateTimeFormat.forPattern(dateFormatForQuery);
        }
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    /**
     * Generates the data query based on test type
     *
     * @return String for the data query for the specified database
     */
    public String generateDataQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(StringUtils.join(columnsUsedInDataQuery(), ","));
        query.append(" FROM ");
        query.append(getFrom());
        if (!getWhere().isEmpty())
            query.append(" WHERE ").append(getWhere());
        if (hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        if (fetchAmount > 0) {
            query.append(" LIMIT ").append(fetchAmount);
        }
        return query.toString();
    }

    /**
     * Generates the existence query based on test type
     *
     * @return String for the existence query for the specified database
     */
    public String generateExistenceQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(StringUtils.join(columnsUsedInExistenceQuery(), ","));
        query.append(" FROM ");
        query.append(getFrom());
        if (!getWhere().isEmpty())
            query.append(" WHERE ").append(getWhere());
        if (hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        if (fetchAmount > 0) {
            query.append(" LIMIT ").append(fetchAmount);
        }
        return query.toString();
    }

    @Override
    protected String getDateRangeString(DateTime start, DateTime end) {
        if(start == null && end == null)
            return null; //case shouldn't happen. just in case...

        if(end == null) {
            String startStr = dateTimeFormat.print(start);
            return String.format("unix_timestamp(%s, '%s') >= unix_timestamp('%s','%s')"
                    , getQueryableDateColumn()
                    , dateFormatForQuery
                    , startStr
                    , dateFormatForQuery);
        }
        if (start == null) {
            String endStr = dateTimeFormat.print(end);
            return String.format("unix_timestamp(%s, '%s') >= unix_timestamp('%s','%s')"
                    , getQueryableDateColumn()
                    , dateFormatForQuery
                    , endStr
                    , dateFormatForQuery);
        }

        String startStr = dateTimeFormat.print(start);
        String endStr = dateTimeFormat.print(end);
        return String.format("unix_timestamp(%s, '%s') between unix_timestamp('%s','%s') and unix_timestamp('%s','%s')"
                , getQueryableDateColumn()
                , dateFormatForQuery
                , startStr
                , dateFormatForQuery
                , endStr
                , dateFormatForQuery);
    }

    public static abstract class Builder<T extends Builder<T>> extends QueryBuilder.Builder<T> {
        private String database;

        public T database(String schema) {
            this.database = schema;
            return self();
        }

        public HiveQueryBuilder build() throws Exception {
            return new HiveQueryBuilder(this);
        }

        public String getQuery() throws Exception {
            HiveQueryBuilder query = new HiveQueryBuilder(this);
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
