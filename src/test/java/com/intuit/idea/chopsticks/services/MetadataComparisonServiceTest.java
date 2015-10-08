package com.intuit.idea.chopsticks.services;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.services.ComparisonUtils.findLeftNotInRight;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/1/15
 * ************************************
 */
@Test(groups = "comparison")
public class MetadataComparisonServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MetadataComparisonServiceTest.class);
    private MetadataComparisonService metadataComparisonService;

    @BeforeMethod
    public void setup() {
        metadataComparisonService = new MetadataComparisonService(null);
    }

    @Test
    public void testMetadataCompare() throws Exception {
        List<String> sPks = Arrays.asList("employeeId", "firstName");
        List<String> tPks = Arrays.asList("employeeId", "firstName");
        MockResultSet sMd = new MockResultSet("sourceMetadataResultSetMock");
        sMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId", "firstName", "lsdName", "lastName", "createDate"});
        sMd.addColumn("DATA_TYPE", new Integer[]{4, 4, 12, 12, 12, 91});
        MockResultSet tMd = new MockResultSet("targetMetadataResultSetMock");
        tMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId", "firstName", "lsdName", "lastName", "createDate"});
        tMd.addColumn("DATA_TYPE", new Integer[]{4, 4, 12, 12, 12, 91});
        metadataComparisonService.metadataCompare(sMd, tMd, sPks, tPks);
    }

    @Test
    public void testCheckPrimaryKeys() throws Exception {
        List<String> sPks = Arrays.asList("employeeId", "firstName");
        List<String> tPks = Arrays.asList("employeeId", "firstName");
//        metadataComparisonService.comparePrimaryKeys(sPks, tPks);
    }

    @Test
    public void testFindLeftNotInRight() throws Exception {
        List<Integer> left = IntStream.range(1, 20).boxed().collect(Collectors.toList());
        List<Integer> right = IntStream.range(10, 30).boxed().collect(Collectors.toList());
        List<Integer> leftNotInRight = findLeftNotInRight(left, right, Integer::equals);
        List<Integer> rightNotInLeft = findLeftNotInRight(right, left, Integer::equals);
        logger.info("[" + leftNotInRight.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]");
        logger.info("[" + rightNotInLeft.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]");

    }
}