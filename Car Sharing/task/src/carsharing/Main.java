package carsharing;
import carsharing.dao.impl.CarDaoImpl;
import carsharing.dao.impl.CompanyDaoImpl;
import carsharing.dao.impl.CustomerDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        try {
            CarsharingDb db = new CarsharingDb();
            Connection connection = CarsharingDb.getConnection();

            CompanyDaoImpl companyDaoImpl = new CompanyDaoImpl(connection);
            CarDaoImpl carDaoImpl = new CarDaoImpl(connection);
            CustomerDaoImpl customerDaoImpl = new CustomerDaoImpl(connection);
            Scanner sc = new Scanner(System.in);

            Menu menu = new Menu(companyDaoImpl, carDaoImpl, customerDaoImpl, connection);
            menu.logInMenu();
            Menu.sc.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
   }
}