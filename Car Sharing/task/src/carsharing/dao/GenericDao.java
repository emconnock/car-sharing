package carsharing.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T, K> {

    T create(T object) throws SQLException;//CREATE
    T findById(K id) throws SQLException; // READ
    void update(T object) throws SQLException;
    void delete(K id) throws SQLException;
    void setId(T object, K id);

}
