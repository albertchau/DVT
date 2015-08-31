
import javax.management.Query;
import java.sql.*;
import java.util.List;

public class Main {

    private static final String DBURL =
            "jdbc:mysql://localhost:3306/test?user=root&password=admin"+
                    "&useUnicode=true&characterEncoding=UTF-8";
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";

    static {
        try {
            Class.forName(DBDRIVER).newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Connection getConnection()
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createEmployees()
    {
        Connection con = getConnection();
        Statement stmt =null;
        String createString;
        createString = "CREATE TABLE  `test`.`employees` ("+
                "`EmployeeID` int(10) unsigned NOT NULL default '0',"+
                "`Name` varchar(45) collate utf8_unicode_ci NOT NULL default '',"+
                "`Office` varchar(10) collate utf8_unicode_ci NOT NULL default '',"+
                "`CreateTime` timestamp NOT NULL default CURRENT_TIMESTAMP,"+
                "PRIMARY KEY  (`EmployeeID`)"+
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }
    }
    private static void dropEmployees()
    {
        Connection con = getConnection();
        Statement stmt =null;
        String createString;
        createString = "DROP TABLE IF EXISTS `test`.`employees`;";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }
    }

    private static void insertEmployee()
    {
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();

            stmt.addBatch("INSERT INTO employees(EmployeeID, Name, Office) "
                    + "VALUES(1001, 'David Walker', 'HQ101')");

            stmt.addBatch("INSERT INTO employees(EmployeeID, Name, Office) "
                    + "VALUES(1002, 'Paul Walker', 'HQ202')");

            stmt.addBatch("INSERT INTO employees(EmployeeID, Name, Office) "
                    + "VALUES(1003, 'Scott Warner', 'HQ201')");

            int [] updateCounts = stmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }
    }
    public static void showEmployeeInfo()
    {
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet employees = dbmd.getColumns(null, null, "Employees", null);

            ResultSetMetaData metaData = employees.getMetaData();
            int cce = metaData.getColumnCount();

            while(employees.next()) {
                for (int i = 1; i < cce-1; i++) {
                    System.out.print(employees.getObject(i) + " ");
                }
                System.out.println();
            }
            ResultSet rs = stmt.executeQuery("SELECT EmployeeID eid, Name nm, Office as oo from Employees");
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            System.out.println("Column Count is " + cols);
            for (int i = 1; i <= cols; i++) {
                System.out.println("\nNAME: " + rsmd.getColumnName(i) + "\n" +
                        "TYPE: " + rsmd.getColumnTypeName(i) + "\n" +
                        "TABLE: " + rsmd.getTableName(i)+"\n" +
                        "Schema: " + rsmd.getSchemaName(i) + "\n" +
                        "Scale: " + rsmd.getScale(i) + "\n" +
                        "Length: " + rsmd.getColumnDisplaySize(i) + "\n" +
                        "Precision: " + rsmd.getPrecision(i));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }

    }
    public static void main(String[] args) {

        QueryInfo qi = new StructuredQueryInfo();
        QueryInfoBase.QueryInfoBuilder.idk();

        if (qi instanceof AdHocQueryInfo)
        System.out.println("qi.getClass() = " + qi.getClass());

    }

    public static void sidk(StructuredQueryInfo sqi) {
        System.out.println("Main.sqi");
    }

    public static void sidk(AdHocQueryInfo qhqi) {
        System.out.println("Main.qhqi");
    }

    public static void sidk(QueryInfo qhqi) {
        System.out.println("Main.qhqi");
    }

    public static QueryInfo idkk() {
        return new StructuredQueryInfo();
    }
}

interface QueryInfo {
}

class StructuredQueryInfo implements QueryInfo {
}

class AdHocQueryInfo implements QueryInfo {
}

abstract class QueryInfoBase implements QueryInfo {

    static class QueryInfoBuilder {
        static void idk() {
            System.out.println("QueryInfoBuilder.idk");

        }
    }
}

