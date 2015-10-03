package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.query.QueryService;
import com.intuit.idea.chopsticks.query.QueryServiceBuilder;
import com.intuit.idea.chopsticks.query.TestType;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.services.ExistenceComparisonService;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.testng.annotations.Test;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class StructuredJdbcDataProviderTest {

    @Test
    public void testName() throws Exception {

    }

    @Test
    public void MySqlConnectionIT() throws DataProviderException, SQLException {

        QueryService tqf = new QueryServiceBuilder().build("employees", VendorType.MYSQL, TestType.FULL);
        StructuredJdbcDataProvider target = new StructuredJdbcDataProvider(
                VendorType.MYSQL,
                "host",
                "port",
                "jdbc:mysql://localhost:3306/test",
                "root",
                "admin",
                "",
                "",
                "employees",
                null,
                tqf
        );
        ExistenceComparisonService existenceComparisonService = new ExistenceComparisonService(null);

        target.openConnections();
        ResultSets data = target.getData("Select * from test.employees");

        ResultSetMetaData metaData = data.getMetaData();

//        metaData.getColumnCount()

//        Metadata[] metadatas = existenceComparisonService.columnLabelsFromResultSet(data);
//        List<String> columnNames = Stream.of(metadatas)
//                .map(Metadata::getColumn)
//                .collect(Collectors.toList());
//        List<Object[]> comparables = existenceComparisonService.rowsToList(data, columnNames);
//
//        Arrays.stream(metadatas).forEach(System.out::println);
//
//        comparables.stream().flatMap(Arrays::stream).forEach((x) -> System.out.println(x + " :: " + x.getClass()));

        target.closeConnections();
    }
}