package carsharing.dao.impl;

import carsharing.CarsharingDb;
import carsharing.dao.AbstractDao;
//import carsharing.dao.CarDao;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl extends AbstractDao<Car, Integer> {

    private final Connection connection;

    public CarDaoImpl(Connection connection) {
        super();
        this.connection = connection;
        //debugTable();
    }

    @Override
    protected String getCreateQuery(Car car) {
        return "INSERT INTO car (NAME, COMPANY_ID) values (?, ?) ";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM car WHERE id = ?";
        //return "SELECT * FROM car WHERE NAME = ? AND company_id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE car SET name = ?, company_id = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "";
    }

    @Override
    protected void setIdIntoStatement(PreparedStatement statement, Integer id) throws SQLException {
        if (id == null) {
            statement.setNull(1, java.sql.Types.INTEGER);
        } else {
            statement.setInt(1, id);
        }
    }

    @Override
    protected void setObjectIntoStatement(PreparedStatement statement, Car car) throws SQLException {
        statement.setString(1, car.getCarName());
        statement.setInt(2, car.getCompanyId());
        }

    @Override
    protected Car readObject(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        try {
            car.setId((resultSet.getInt("id")));
            car.setCarName(resultSet.getString("name"));
            car.setCompanyId(resultSet.getInt("company_id"));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return car;
    }

    @Override
    public void update(Car car) throws SQLException {
        String query = getUpdateQuery();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setObjectIntoStatement(stmt, car);
            stmt.executeUpdate();
        }
    }

    @Override
    public void setId(Car car, Integer id) {
        car.setId(id);
    }

    public List<Car> findAllByCompanyId(Integer companyId) throws SQLException {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM car WHERE company_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int carId = resultSet.getInt("id");
                    String carName = resultSet.getString("name");
                    cars.add(new Car(carId, carName));
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return cars;
    }



    public void addCar(Car car) throws SQLException {

        String query = getCreateQuery(car);

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setObjectIntoStatement(statement, car);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    car.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> findAvailableCarsByCompanyId(int companyId) throws SQLException {
        String query = "SELECT * FROM car WHERE company_id = ? ORDER BY id";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("company_id")
                ));
            }
            return cars;
        }
    }

    public List<Car> getAvailableCars(int companyId) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Car c WHERE c.company_id = ? AND c.id NOT IN (SELECT rented_car_id FROM Customer WHERE rented_car_id IS NOT NULL)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("name"),
                        companyId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }




}