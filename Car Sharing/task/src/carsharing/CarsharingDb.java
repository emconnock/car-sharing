package carsharing;

import java.sql.*;

public class CarsharingDb {

    private Connection connection;
    private Statement statement;
    private static final String dbName = "carsharing";
    private static final String url = "jdbc:h2:./src/carsharing/db/" + dbName;

    public CarsharingDb() throws SQLException {

        try {
            connection = getConnection();

            createCompanyTable();
            createCarTable();
            createCustomerTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        connection.setAutoCommit(true);
        return connection;
    }

    public void createCompanyTable() throws SQLException {

        try (Statement stmt = getConnection().createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) UNIQUE NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCarTable() throws SQLException {

        try (Statement stmt = getConnection().createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY , " +
                    "name VARCHAR(50) UNIQUE NOT NULL, " +
                    "company_id INT NOT NULL, " +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCustomerTable() throws SQLException {

        try (Statement stmt = getConnection().createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50) UNIQUE NOT NULL, " +
                    "rented_car_id INT, " +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void closeStatement(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }


}
