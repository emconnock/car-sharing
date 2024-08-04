package carsharing.dao;

import carsharing.CarsharingDb;

import java.sql.*;
import java.util.List;

public abstract class AbstractDao<T, K> implements GenericDao<T, K> {


    private static final int UPDATE_EXECUTED_SUCCESSFULLY = 1;
    public static final String COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID = "Could not find an object with such id";


    protected abstract String getCreateQuery(T object);
    protected abstract String getSelectByIdQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();

    protected abstract void setIdIntoStatement(PreparedStatement preparedStatement, K id)
            throws SQLException;

    protected abstract void setObjectIntoStatement(PreparedStatement preparedStatement, T object)
        throws SQLException;

    protected  abstract T readObject(ResultSet resultSet) throws SQLException;

    public T create(T object) throws SQLException {
        String createQuery = getCreateQuery(object);
        try (Connection connection = CarsharingDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {

            setObjectIntoStatement(statement, object);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setId(object, (K) generatedKeys.getObject(1));
                } else {
                    throw new SQLException("Problem with creating the object");
                }
            }
            return object;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public T findById(K id) throws SQLException {
        if (id == null) {
            return null; // or throw an exception, depending on your requirements
        }

        String selectByIdQuery = getSelectByIdQuery();
        try (Connection connection = CarsharingDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectByIdQuery)) {

            setIdIntoStatement(statement, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return readObject(resultSet);
            } else {
                throw new SQLException(COULD_NOT_FIND_AN_OBJECT_WITH_SUCH_ID);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void update(T object) throws SQLException {
        String updateQuery = getUpdateQuery();

        try (Connection connection = CarsharingDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            setObjectIntoStatement(statement, object);

            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new SQLException("Problem with updating the object");
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(K id) throws SQLException {
        String deleteQuery = getDeleteQuery();

        try (Connection connection = CarsharingDb.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            setIdIntoStatement(statement, id);

            if (statement.executeUpdate() < UPDATE_EXECUTED_SUCCESSFULLY) {
                throw new SQLException("Problem with deleting the object");
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
