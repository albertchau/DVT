package com.intuit.idea.chopsticks.query.copies;

import com.intuit.idea.app.globals.Literals;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * Generates a sql server query given proper input builder parameters
 */
public class SqlServerQueryBuilder extends QueryBuilder {

    private final String schema;

    private SqlServerQueryBuilder(Builder<?> builder) throws Exception {
        super(builder);
        if (builder.schema == null)
            throw new Exception("Cannot create Sql Server query because there is no specified schema.");
        this.schema = builder.schema;
        this.fromClause = schema + "." + table.getName();
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
        if (fetchAmount > 0)
            query.append("TOP ").append(fetchAmount).append(" ");
        query.append(dataSelect("%s")); //"CONVERT(varchar,%s,101)"
        query.append(" FROM ");
        query.append(getFrom());

        if (!getWhere().isEmpty())
            query.append(" WHERE ").append(getWhere());
        if (hasPks())
            query.append(" ORDER BY ").append(getOrderBy());

        return query.toString();
    }

    /**
     * Generates the existence query based on test type
     *
     * @return String for the existence query for the specified database
     */
    public String generateExistenceQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        if (fetchAmount > 0)
            query.append("TOP ").append(fetchAmount).append(" ");
        query.append(StringUtils.join(columnsUsedInExistenceQuery(), ","));
        query.append(" FROM ");
        query.append(getFrom());

        if (!testType.equals(Literals.FULL)) {
            if (!getWhere().isEmpty())
                query.append(" WHERE ").append(getWhere());
        }
        if (hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        return query.toString();
    }

    //Override this method!!
    protected String getDateRangeString(DateTime start, DateTime end) {
        if(start == null && end == null)
            return null; //case shouldn't happen. just in case...

        if(end == null) {
            String startStr = dateTimeFormat.print(start);
            return String.format("%s >= '%s'"
                    , getQueryableDateColumn()
                    , startStr);
        }
        if (start == null) {
            String endStr = dateTimeFormat.print(end);
            return String.format("%s <= '%s'"
                    , getQueryableDateColumn()
                    , endStr);
        }

        String startStr = dateTimeFormat.print(start);
        String endStr = dateTimeFormat.print(end);
        return String.format("%s between '%s' and '%s'"
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

        public SqlServerQueryBuilder build() throws Exception {
            return new SqlServerQueryBuilder(this);
        }

        public String getQuery() throws Exception {
            SqlServerQueryBuilder query = new SqlServerQueryBuilder(this);
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
