package com.intuit.idea.chopsticks.stuff.reporters;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Copyright 2015
 *
 * @author albert
 */
public class StatisticsReporter {

    Map<String, IRes> colResults;

    private int totalPassRows;

    private int totalRows;

    public StatisticsReporter(List<String> colNames) {
        colResults = new HashMap<>();
        totalPassRows = totalRows = 0;
        colNames.stream().forEach(c -> colResults.put(c, new IRes()));
    }

    public void log(List<SimpleResults> row) {
        row.forEach(r -> colResults.get(r.colName).save(r.result));
        totalPassRows += row.stream().allMatch(r -> r.result) ? 1 : 0;
        totalRows += 1;
    }

    public void generateReport() {

        System.out.println("Generating Statistics Report");
        colResults.forEach((k, v) -> System.out.printf("Column: %s \tquality: %s\n", k, v.prettyPassReport()));

        int totalFail = colResults.entrySet().stream().mapToInt(e -> e.getValue().fail).sum();
        int totalPass = colResults.entrySet().stream().mapToInt(e -> e.getValue().pass).sum();

        System.out.printf("Column Totals = Pass: %d Fail: %d Total: %d\n", totalPass, totalFail, totalPass + totalFail);
        System.out.printf("Row Totals = Pass: %d Fail: %d Total: %d\n", totalPassRows, totalRows - totalPassRows, totalRows + totalPassRows);

    }

    class IRes {

        private int pass;

        private int fail;

        IRes() {
            pass = fail = 0;
        }

        public void save(Boolean result) {
            if (result) pass++;
            else fail++;
        }

        public String passPercent() {
            return String.format("%2.02f", (float) pass / (pass + fail));
        }

        public String failPercent() {
            return String.format("%2.02f", (float) fail / (pass + fail));
        }

        public String prettyPassReport() {
            return String.format("%d/%d (%s%%)", pass, pass + fail, passPercent());
        }

        public String prettyFailReport() {
            return String.format("%d/%d (%s%%)", fail, pass + fail, failPercent());
        }
    }
}