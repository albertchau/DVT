package com.intuit.idea.ziplock.core.query;

/*
count, existence, data
count, existence, data with schema
count, existence, data with schema, boundDate
count, existence, data with schema, lowerDate
count, existence, data with schema, upperDate
count, existence, data with schema, boundDate, where
count, existence, data with schema, boundDate, whereEquals
existence, data with schema, boundDate, where, fetchAmount
existence, data with schema, boundDate, where, orderDirections
data with schema, boundDate, where, orderDirections, includedColumns
data with schema, boundDate, where, orderDirections, excludedColumns,
data with schema, boundDate, where, orderDirections, includedColumns, excludedColumns
 */
public class QueryServiceBaseTest {
//    public static final List<Metadata> SINGLE_INT_PK_METADATA = Arrays.asList(
//            createWithNoAliasing("employeeId", true, Integer.class),
//            createWithNoAliasing("companyId", false, Integer.class),
//            createWithNoAliasing("firstName", false, String.class),
//            createWithNoAliasing("lastName", false, String.class),
//            createWithNoAliasing("createDate", false, DateTime.class),
//            createWithNoAliasing("lastModifiedDate", false, DateTime.class)
//    );
//    public static final List<Metadata> SINGLE_STR_PK_METADATA = Arrays.asList(
//            createWithNoAliasing("employeeId", false, Integer.class),
//            createWithNoAliasing("companyId", false, Integer.class),
//            createWithNoAliasing("firstName", true, String.class),
//            createWithNoAliasing("lastName", false, String.class),
//            createWithNoAliasing("createDate", false, DateTime.class),
//            createWithNoAliasing("lastModifiedDate", false, DateTime.class)
//    );
//    public static final List<Metadata> COMPOSITE_PK_METADATA = Arrays.asList(
//            createWithNoAliasing("employeeId", true, Integer.class),
//            createWithNoAliasing("companyId", false, Integer.class),
//            createWithNoAliasing("firstName", true, String.class),
//            createWithNoAliasing("lastName", false, String.class),
//            createWithNoAliasing("createDate", false, DateTime.class),
//            createWithNoAliasing("lastModifiedDate", false, DateTime.class)
//    );
//    public static final Map<String, List<String>> SAMPLED_MAP_SINGLE_INT = new HashMap<String, List<String>>() {{
//        put("employeeId", Arrays.asList("2", "3"));
//    }};
//    public static final Map<String, List<String>> SAMPLED_MAP_SINGLE_STR = new HashMap<String, List<String>>() {{
//        put("firstName", Arrays.asList("Bob", "Albert"));
//    }};
//    public static final Map<String, List<String>> SAMPLED_MAP_COMPOSITE = new HashMap<String, List<String>>() {{
//        put("employeeId", Arrays.asList("2", "3"));
//        put("firstName", Arrays.asList("Bob", "Albert"));
//    }};
//    public static final List<TestType> TEST_TYPES = Arrays.asList(TestType.FULL, TestType.HISTORIC, TestType.INCREMENTAL);
//    public static Logger logger = LoggerFactory.getLogger(MySqlQueryServiceTest.class);
//    QueryService singleIntPkQs;
//    QueryService compositeIntStrPkQs;
//    QueryService singleStrPkQs;
//
//    protected List<QueryService> getQueryServices(TestType testType, VendorType vendorType) {
//        List<QueryService> qss = new ArrayList<>();
//        QueryServiceBuilder queryServiceBuilder = new QueryServiceBuilder();
//        QueryService base = queryServiceBuilder
//                .build("employees", vendorType, testType);
//        QueryService withSchema = queryServiceBuilder
//                .setSchema("test")
//                .build("employees", vendorType, testType);
//        QueryService withExcludedColumns = queryServiceBuilder
//                .setExcludedColumns(Arrays.asList("companyId", "createDate"))
//                .build("employees", vendorType, testType);
//        QueryService withIncludedColumns = queryServiceBuilder
//                .setExcludedColumns(new ArrayList<>())
//                .setIncludedColumns(Arrays.asList("employeeId", "firstName"))
//                .build("employees", vendorType, testType);
//        QueryService withExcludedIncludedColumns = queryServiceBuilder
//                .setExcludedColumns(Arrays.asList("companyId", "createDate"))
//                .build("employees", vendorType, testType);
//        QueryService withFetchAmount = queryServiceBuilder
//                .setFetchAmount(10)
//                .build("employees", vendorType, testType);
//        QueryService withDateBound = queryServiceBuilder
//                .setWhereClauses(Arrays.asList(WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate")))
//                .build("employees", vendorType, testType);
//        QueryService withDateLowerBound = queryServiceBuilder
//                .setWhereClauses(Arrays.asList(WhereClause.createLowerBounded(DateTime.now(), "createDate")))
//                .build("employees", vendorType, testType);
//        QueryService withDateUpperBound = queryServiceBuilder
//                .setWhereClauses(Arrays.asList(WhereClause.createUpperBounded(DateTime.now(), "createDate")))
//                .build("employees", vendorType, testType);
//        QueryService withAnotherBound = queryServiceBuilder
//                .setWhereClauses(Arrays.asList(
//                        WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate"),
//                        WhereClause.createBounded(10, 20, "employeeId")))
//                .build("employees", vendorType, testType);
//        QueryService withSpecifiedDatePattern = queryServiceBuilder
//                .setDateTimeFormatter(DateTimeFormat.forPattern("MM, yyyy, dd"))
//                .build("employees", vendorType, testType);
//        QueryService withResevoirRandom = queryServiceBuilder
//                .setOrderDirection(OrderDirection.RESERVOIR_RANDOM)
//                .build("employees", vendorType, testType);
//        QueryService withSQLRandom = queryServiceBuilder
//                .setOrderDirection(OrderDirection.SQL_RANDOM)
//                .build("employees", vendorType, testType);
//        QueryService withDescendingOrder = queryServiceBuilder
//                .setOrderDirection(OrderDirection.DESCENDING)
//                .build("employees", vendorType, testType);
//        qss.add(base);
//        qss.add(withSchema);
//        qss.add(withIncludedColumns);
//        qss.add(withExcludedColumns);
//        qss.add(withExcludedIncludedColumns);
//        qss.add(withFetchAmount);
//        qss.add(withDateBound);
//        qss.add(withDateLowerBound);
//        qss.add(withDateUpperBound);
//        qss.add(withAnotherBound);
//        qss.add(withSpecifiedDatePattern);
//        qss.add(withResevoirRandom);
//        qss.add(withSQLRandom);
//        qss.add(withDescendingOrder);
//        return qss;
//    }
//
//    @BeforeTest
//    public void setup() {
//        singleIntPkQs = new QueryServiceBuilder()
//                .setSchema("test")
//                .setFetchAmount(10)
//                .setWhereClauses(Arrays.asList(WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate")))
//                .setOrderDirection(OrderDirection.DESCENDING)
//                .build("employees", VendorType.MYSQL, TestType.FULL);
//
//        compositeIntStrPkQs = new QueryServiceBuilder()
//                .setSchema("test")
//                .setFetchAmount(10)
//                .setWhereClauses(Arrays.asList(WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate")))
//                .setOrderDirection(OrderDirection.DESCENDING)
//                .build("employees", VendorType.MYSQL, TestType.FULL);
//
//        singleStrPkQs = new QueryServiceBuilder()
//                .setSchema("test")
//                .setFetchAmount(10)
//                .setWhereClauses(Arrays.asList(WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate")))
//                .setOrderDirection(OrderDirection.DESCENDING)
//                .build("employees", VendorType.MYSQL, TestType.FULL);
//
//    }
//
//    @Test(expectedExceptions = QueryCreationError.class, expectedExceptionsMessageRegExp = ".*registered primary keys.*")
//    public void testPkSizeNotMatching() throws Exception {
//        Map<String, List<String>> pkSizeNotMatching = new HashMap<String, List<String>>() {{
//            put("employeeId", Arrays.asList("2", "3"));
//        }};
//        compositeIntStrPkQs.createDataQueryWithInputSamples(null, pkSizeNotMatching);
//    }
//
//    @Test(expectedExceptions = QueryCreationError.class, expectedExceptionsMessageRegExp = ".*At least one of the.*")
//    public void testPkEmptySample() throws Exception {
//        Map<String, List<String>> notEnoughPkToSample = new HashMap<String, List<String>>() {{
//            put("employeeId", Arrays.asList());
//            put("firstName", Arrays.asList("Bob", "Albert"));
//        }};
//        compositeIntStrPkQs.createDataQueryWithInputSamples(null, notEnoughPkToSample);
//    }
//
//    @Test(expectedExceptions = QueryCreationError.class, expectedExceptionsMessageRegExp = ".*Did not pass in.*")
//    public void testNullSamplePks() throws Exception {
//        Map<String, List<String>> nullSamplePks = new HashMap<>();
//        compositeIntStrPkQs.createDataQueryWithInputSamples(null, nullSamplePks);
//    }

}