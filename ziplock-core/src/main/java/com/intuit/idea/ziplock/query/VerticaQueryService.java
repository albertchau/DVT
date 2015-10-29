package com.intuit.idea.ziplock.query;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TODO
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public final class VerticaQueryService extends QueryServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(VerticaQueryService.class);

    public VerticaQueryService(String tableName,
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
            return null;
        }
        if (endDate == null) {
            String startStr = dateTimeFormat.print(startDate);
            return String.format("%s >= TO_DATE('%s','MM-DD-YYYY HH24:MI:SS')"
                    , dateColumn
                    , startStr);
        }
        if (startDate == null) {
            String endStr = dateTimeFormat.print(endDate);
            return String.format("%s <= TO_DATE('%s','MM-DD-YYYY HH24:MI:SS')"
                    , dateColumn
                    , endStr);
        }
        String startStr = dateTimeFormat.print(startDate);
        String endStr = dateTimeFormat.print(endDate);
        return String.format("%s between TO_DATE('%s','MM-DD-YYYY HH24:MI:SS') and TO_DATE('%s','MM-DD-YYYY HH24:MI:SS')"
                , dateColumn
                , startStr
                , endStr);
    }

}
