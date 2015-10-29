package com.intuit.idea.ziplock.core.query;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public final class HiveQueryService extends QueryServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(HiveQueryService.class);
    private String dateFormatForQuery = "yyyy-MM-dd HH:mm:ss";

    public HiveQueryService(String tableName,
                            String schema,
                            List<String> includedColumns,
                            List<String> excludedColumns,
                            Integer fetchAmount,
                            TestType testType,
                            List<WhereClause> whereClauses,
                            OrderDirection orderDirection,
                            DateTimeFormatter dateTimeFormat) {
        super(tableName,
                schema,
                includedColumns,
                excludedColumns,
                fetchAmount,
                testType,
                whereClauses,
                orderDirection,
                dateTimeFormat);
    }

    @Override
    public String getDateRange(String dateColumn, DateTime startDate, DateTime endDate) {
        if (testType.equals(TestType.FULL)) {
            return null; // is handled when filtering whereclauses by nulls
        }
        if (endDate == null) {
            String startStr = dateTimeFormat.print(startDate);
            return String.format("unix_timestamp(%s, '%s') >= unix_timestamp('%s','%s')"
                    , dateColumn
                    , dateFormatForQuery
                    , startStr
                    , dateFormatForQuery);
        }
        if (startDate == null) {
            String endStr = dateTimeFormat.print(endDate);
            return String.format("unix_timestamp(%s, '%s') >= unix_timestamp('%s','%s')"
                    , dateColumn
                    , dateFormatForQuery
                    , endStr
                    , dateFormatForQuery);
        }
        String startStr = dateTimeFormat.print(startDate);
        String endStr = dateTimeFormat.print(endDate);
        return String.format("unix_timestamp(%s, '%s') between unix_timestamp('%s','%s') and unix_timestamp('%s','%s')"
                , dateColumn
                , dateFormatForQuery
                , startStr
                , dateFormatForQuery
                , endStr
                , dateFormatForQuery);
    }

}
