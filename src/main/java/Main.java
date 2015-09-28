import java.sql.*;

public class Main {
    private static final String DBURL =
            "jdbc:h2:tcp://localhost/~/test;MODE=MYSQL";
    private static final String DBDRIVER = "org.h2.Driver";

    static {
        try {
            Class.forName(DBDRIVER).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, "sa", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createEmployees() {
        Connection con = getConnection();
        Statement stmt = null;
        String createString;
        createString = "CREATE TABLE  employees (" +
                "EmployeeID INT(10) NOT NULL DEFAULT '0'," +
                "Name VARCHAR(45) NOT NULL DEFAULT ''," +
                "Office VARCHAR(10)  NOT NULL DEFAULT ''," +
                "CreateTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY  (EmployeeID)" +
                ");";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        } finally {
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

    private static void dropEmployees() {
        Connection con = getConnection();
        Statement stmt = null;
        String createString;
        createString = "DROP TABLE IF EXISTS `employees`;";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(createString);
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        } finally {
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

    private static void insertEmployee() {
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

            int[] updateCounts = stmt.executeBatch();

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } finally {
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

    public static void showEmployeeInfo() {
        Connection con = getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet employees = dbmd.getColumns(null, null, "Employees", null);

            ResultSetMetaData metaData = employees.getMetaData();
            int cce = metaData.getColumnCount();

            while (employees.next()) {
                for (int i = 1; i < cce - 1; i++) {
                    System.out.print(employees.getObject(i) + " ");
                }
                System.out.println();
            }
            ResultSet rs = stmt.executeQuery("SELECT EmployeeID eid, Name nm, Office as oo from Employees");
            while (rs.next()) {
                System.out.println(rs.getObject(1));
                System.out.println(rs.getObject(2));
            }
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            System.out.println("Column Count is " + cols);
            for (int i = 1; i <= cols; i++) {
                System.out.println("\nNAME: " + rsmd.getColumnName(i) + "\n" +
                        "TYPE: " + rsmd.getColumnTypeName(i) + "\n" +
                        "TABLE: " + rsmd.getTableName(i) + "\n" +
                        "Schema: " + rsmd.getSchemaName(i) + "\n" +
                        "Scale: " + rsmd.getScale(i) + "\n" +
                        "Length: " + rsmd.getColumnDisplaySize(i) + "\n" +
                        "Catalog: " + rsmd.getCatalogName(i) + "\n" +
                        "ColumnClassName: " + rsmd.getColumnClassName(i) + "\n" +
                        "ColumnLabel: " + rsmd.getColumnLabel(i) + "\n" +
                        "ColumnType: " + rsmd.getColumnType(i) + "\n" +
                        "Precision: " + rsmd.getPrecision(i));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } finally {
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

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("org.h2.Driver");
//            Connection conn = DriverManager.
//                    getConnections("jdbc:h2:tcp://localhost/~/test", "sa", "");
//            System.out.println("Connection Established: "
//                    + conn.getMetaData().getDatabaseProductName() + "/" + conn.getCatalog());

//            dropEmployees();
//            createEmployees();
//            insertEmployee();
            showEmployeeInfo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
