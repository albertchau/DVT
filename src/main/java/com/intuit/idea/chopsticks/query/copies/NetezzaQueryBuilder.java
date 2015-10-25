package com.intuit.idea.chopsticks.query.copies;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * Generates a netezza query given proper input builder parameters
 */
public class NetezzaQueryBuilder extends QueryBuilder {

    private NetezzaQueryBuilder(Builder<?> builder) throws Exception {
        super(builder);
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
        query.append(StringUtils.join(columnsUsedInDataQuery(), ","));//query.append(dataSelect("TO_CHAR(%s, 'MM/DD/YYYY')"));
        query.append(" FROM ");
        query.append(getFrom());

        if (!getWhere().isEmpty())
            query.append(" WHERE ").append(getWhere());
        if (hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        if (fetchAmount > 0)
            query.append(" LIMIT ").append(fetchAmount);

        return query.toString();
    }

    /**
     * Generates the existence query based on test type
     *
     * @return String for the existence query for the specified database
     */
    public String generateExistenceQuery() {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(StringUtils.join(columnsUsedInExistenceQuery(), ","));//query.append(StringUtils.join(existenceSelect("TO_CHAR(%s, 'MM/DD/YYYY')"),","));
        query.append(" FROM ");
        query.append(getFrom());

        if (!getWhere().isEmpty())
            query.append(" WHERE ").append(getWhere());
        if (hasPks())
            query.append(" ORDER BY ").append(getOrderBy());
        if (fetchAmount > 0)
            query.append(" LIMIT ").append(fetchAmount);

        return query.toString();
    }

    //Override this method!!
    protected String getDateRangeString(DateTime start, DateTime end) {
        if(start == null && end == null)
            return null; //case shouldn't happen. just in case...

        if(end == null) {
            String startStr = dateTimeFormat.print(start);
            return String.format("%s >= to_char(DATE '%s','MM-DD-YYYY HH24:MI:SS')"
                    , getQueryableDateColumn()
                    , startStr);
        }
        if (start == null) {
            String endStr = dateTimeFormat.print(end);
            return String.format("%s <= to_char(DATE '%s','MM-DD-YYYY HH24:MI:SS')"
                    , getQueryableDateColumn()
                    , endStr);
        }

        String startStr = dateTimeFormat.print(start);
        String endStr = dateTimeFormat.print(end);
        return String.format("%s between to_char(DATE '%s','MM-DD-YYYY HH24:MI:SS') and to_char(DATE '%s','MM-DD-YYYY HH24:MI:SS')"
                , getQueryableDateColumn()
                , startStr
                , endStr);
    }

    public static abstract class Builder<T extends Builder<T>> extends QueryBuilder.Builder<T> {

        public NetezzaQueryBuilder build() throws Exception {
            return new NetezzaQueryBuilder(this);
        }

        public String getQuery() throws Exception {
            NetezzaQueryBuilder query = new NetezzaQueryBuilder(this);
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
