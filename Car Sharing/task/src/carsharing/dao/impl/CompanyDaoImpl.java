package carsharing.dao.impl;

import carsharing.CarsharingDb;
import carsharing.dao.AbstractDao;
//import carsharing.dao.CompanyDao;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class CompanyDaoImpl extends AbstractDao<Company, Integer> {

    private final Connection connection;

    public CompanyDaoImpl(Connection connection) {
        super();
        this.connection = connection;
        //debugTable();
    }

    @Override
    protected String getCreateQuery(Company company) {
        return "INSERT INTO company (name) VALUES (?)";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM company WHERE id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "";
    }

    @Override
    protected String getDeleteQuery() {
        return "";
    }

    @Override
    protected void setIdIntoStatement(PreparedStatement statement, Integer id) throws SQLException {
        //try {
            statement.setInt(1, id);
//        } catch (SQLException e) {
//            throw new SQLException(e);
//        }
    }

    @Override
    protected void setObjectIntoStatement(PreparedStatement statement, Company company) throws SQLException {
        try {
            statement.setString(1, company.getCompanyName());
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    @Override
    protected Company readObject(ResultSet resultSet) throws SQLException {
        Company company = new Company();
        try {
            company.setId((resultSet.getInt("id")));
            company.setCompanyName(resultSet.getString("name"));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return company;
    }

    @Override
    public void setId(Company company, Integer id) {
        company.setId(id);
    }

    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM company";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Company company = readObject(resultSet);
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    public void addCompany(Company company) {

        String query = getCreateQuery(company);

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setObjectIntoStatement(statement, company);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Company getCompanyById(int companyId) {
        String query = "SELECT * FROM company WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return readObject(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
