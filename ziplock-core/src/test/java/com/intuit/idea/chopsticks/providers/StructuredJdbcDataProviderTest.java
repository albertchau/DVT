package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class StructuredJdbcDataProviderTest {

    @Test
    public void testName() throws Exception {

    }

    @Test
    public void MySqlConnectionIT() throws DataProviderException, SQLException {
//        QueryService tqf = new QueryServiceBuilder().build("employees", VendorType.MYSQL, TestType.FULL);
//        StructuredJdbcDataProvider target = new StructuredJdbcDataProvider(
//                VendorType.MYSQL,
//                "host",
//                "port",
//                "jdbc:mysql://localhost:3306/test",
//                "root",
//                "admin",
//                "",
//                "",
//                "employees",
//                null,
//                tqf
//        );
////        ExistenceComparisonService existenceComparisonService = new ExistenceComparisonService(null,null,null,null);
//
//        target.openConnections();
//        ResultSet data = target.getData("Select * from test.employees");
//
//        ResultSetMetaData metaData = data.getMetaData();
//
////        metaData.getColumnCount()
//
////        Metadata[] metadatas = existenceComparisonService.metadataFromResultSet(data);
////        List<String> columnNames = Stream.of(metadatas)
////                .map(Metadata::getColumnLabel)
////                .collect(Collectors.toList());
////        List<Object[]> comparables = existenceComparisonService.rowsToList(data, columnNames);
////
////        Arrays.stream(metadatas).forEach(System.out::println);
////
////        comparables.stream().flatMap(Arrays::stream).forEach((x) -> System.out.println(x + " :: " + x.getClass()));
//
//        target.closeConnections();
    }
}