package carsharing.dao.impl;

import carsharing.CarsharingDb;
import carsharing.dao.AbstractDao;
//import carsharing.dao.CustomerDao;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl extends AbstractDao<Customer, Integer> {

    private final Connection connection;

    public CustomerDaoImpl(Connection connection) {
        super();
        this.connection = connection;
    }

    @Override
    protected String getCreateQuery(Customer customer) {
        return "INSERT INTO customer(NAME, RENTED_CAR_ID) values(?, NULL)  ";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM customer";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE customer SET RENTED_CAR_ID = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "";
    }

    public void setIdIntoStatement(PreparedStatement statement, Integer id) throws SQLException {
        if (id == null) {
            statement.setNull(1, java.sql.Types.INTEGER);
        } else {
            statement.setInt(1, id);
        }
    }


    @Override
    protected void setObjectIntoStatement(PreparedStatement statement, Customer customer) throws SQLException {
        try {
            if (customer.getId() < 0) {
                //statement.setObject(1, customer.getId());
                statement.setString(1, customer.getCustomerName());
            } else {
                //statement.setObject(1, customer.getId());
                statement.setString(1, customer.getCustomerName());
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    protected Customer readObject(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        try {
            customer.setId((resultSet.getInt("id")));
            customer.setCustomerName(resultSet.getString("name"));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return customer;
    }

    @Override
    public void setId(Customer customer, Integer id) {
        customer.setId(id);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";

        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Customer customer = readObject(resultSet);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        String query = "SELECT id, name, rented_car_id FROM Customer WHERE id = ?";
        Customer customer = null;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Integer rentedCarId = (Integer) rs.getObject("rented_car_id");
                    customer = new Customer(id, name, rentedCarId);
                }
            }
        }
        return customer;
    }

    public void addCustomer(Customer customer) {

        String query = getCreateQuery(customer);

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setObjectIntoStatement(statement, customer);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rentACar(int customerId, int carId) {
        String updateQuery = "UPDATE Customer SET rented_car_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setInt(1, carId);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRentedCar(int customerId, Integer carId) throws SQLException {
        String query = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            if (carId == null) {
                statement.setNull(1, java.sql.Types.INTEGER); // Set to null if carId is null
            } else {
                statement.setInt(1, carId); // Otherwise, set the car ID
            }
            statement.setInt(2, customerId); // Set the customer ID

            statement.executeUpdate();
        }
    }

    public boolean hasRentedCar(int customerId) {
        String query = "SELECT rented_car_id FROM customer WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("rented_car_id") != 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void returnCar(int customerId) {
        String updateQuery = "UPDATE Customer SET rented_car_id = NULL WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
