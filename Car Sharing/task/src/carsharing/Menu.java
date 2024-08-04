package carsharing;

import carsharing.dao.impl.CarDaoImpl;
import carsharing.dao.impl.CompanyDaoImpl;
import carsharing.dao.impl.CustomerDaoImpl;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Menu {

    static Scanner sc = new Scanner(System.in);

    private final CompanyDaoImpl companyDaoImpl;
    private final CarDaoImpl carDaoImpl;
    private final CustomerDaoImpl customerDaoImpl;
    private Connection connection;

    public Menu(CompanyDaoImpl companyDaoImpl, CarDaoImpl carDaoImpl, CustomerDaoImpl customerDaoImpl, Connection connection) {
        this.companyDaoImpl = companyDaoImpl;
        this.carDaoImpl = carDaoImpl;
        this.customerDaoImpl = customerDaoImpl;
        //this.sc = new Scanner(System.in);
        this.connection = connection;
    }

    public void logInMenu() throws SQLException {

        System.out.println("\n1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");

        if (!sc.hasNextLine()) {
            return;
        }

        String input = sc.nextLine();

        switch (input) {
            case "1" -> managerMenu();
            case "2" -> viewCustomerList();
            case "3" -> addCustomer();
            case "0" -> System.exit(0);
        }
    }

    public boolean managerMenu() throws SQLException {

        while (true) {
            System.out.println("\n1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            if (!sc.hasNextLine()) {
                return true;
            }

            String input = sc.nextLine();

            switch (input) {
                case "1" -> viewCompanyListManager();
                case "2" -> addCompany();
                case "0" -> {
                    logInMenu();
                    return false;
                }
            }
        }
    }

    public boolean companyMenu(int companyId) throws SQLException {

        while (true) {
            System.out.println("\n1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            if (!sc.hasNextLine()) {
                return true;
            }

            String input = sc.nextLine();

            switch (input) {
                case "1" -> viewCarListManager(companyId);
                case "2" -> addCar(companyId);
                case "0" -> {
                    managerMenu();
                    return false;
                }
            }
        }
    }

    public boolean customerMenu(Customer customer, int customerId) throws SQLException {

        while (true) {
            System.out.println("\n1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            if (!sc.hasNextLine()) {
                return true;
            }

            String input = sc.nextLine();

            switch (input) {
                case "1" -> chooseACompany(customerId);
                case "2" -> returnRentedCar(customerId);
                case "3" -> rentedCarDetails(customer, customerId);
                case "0" -> {
                    logInMenu();
                    return false;
                }
            }
            return false;
        }
    }

    public void rentACar(int customerId, Company company) throws SQLException {
        Customer customer = customerDaoImpl.getCustomerById(customerId);



        List<Car> cars = carDaoImpl.getAvailableCars(company.getId());
        if (cars.isEmpty()) {
            System.out.println("No available cars in the company.");
            return;
        }

        System.out.println("Choose a car:");
        for (int i = 0; i < cars.size(); i++) {
            System.out.println((i + 1) + ". " + cars.get(i).getCarName());
        }
        System.out.println("0. Back");
        int carChoice = sc.nextInt();
        sc.nextLine(); // Consume the newline character

        if (carChoice == 0) {
            //Customer customer = customerDaoImpl.getCustomerById(customerId);
            customerMenu(customer, customerId); // User chose to go back
            return;
        }

        if (carChoice > 0 && carChoice <= cars.size()) {
            Car car = cars.get(carChoice - 1);

            // Set the rented car in the customer record
            customerDaoImpl.rentACar(customerId, car.getId());
            System.out.println("You rented '" + car.getCarName() + "'");

            // Retrieve the updated customer information
            Customer updatedCustomer = customerDaoImpl.getCustomerById(customerId);
            customerMenu(updatedCustomer, customerId);
        } else {
            System.out.println("Invalid choice. Try again.");
            rentACar(customerId, company); // Optionally, you can prompt the user again to choose a car
        }
    }

    public void returnRentedCar(int customerId) throws SQLException {
        Customer customer = customerDaoImpl.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            customerMenu(customer, customerId);
        }

        customerDaoImpl.returnCar(customerId);
        System.out.println("You've returned a rented car!");
        customerMenu(customer, customerId);
    }

    public void rentedCarDetails(Customer customer, int customerId) throws SQLException {

        Customer updatedCustomer = customerDaoImpl.getCustomerById(customerId);
        Integer rentedCarId = updatedCustomer.getRentedCarId();

        if (rentedCarId == null) {
            System.out.println("You didn't rent a car!");
            customerMenu(customer, customerId);
            return;
        }

        try {
            Car rentedCar = carDaoImpl.findById(rentedCarId);
            if (rentedCar != null) {
                // Print details of the rented car
                System.out.println("Your rented car:");
                System.out.println(rentedCar.getCarName());

                // Retrieve the company name associated with the car
                Company company = companyDaoImpl.findById(rentedCar.getCompanyId());
                if (company != null) {
                    System.out.println("Company:");
                    System.out.println(company.getCompanyName());
                } else {
                    System.out.println("Company details not found.");
                }
            } else {
                System.out.println("Car details not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            customerMenu(customer, customerId); // Ensure the menu is shown after operation
        }
    }

    public boolean viewCustomerList() throws SQLException {

        List<Customer> customers = customerDaoImpl.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!");
            logInMenu();
        } else {
            System.out.println("\nCustomer list:");
            for (Customer customer : customers) {
                System.out.println(customer.getId() + ". " + customer.getCustomerName());
            }
            System.out.println("0. Back");
            String input = sc.nextLine();
            int customerId = Integer.parseInt(input);

            if (customerId == 0) {
                logInMenu();
                return false;
            }

            Customer selectedCustomer = customers.stream()
                    .filter(customer -> customer.getId() == customerId)
                    .findFirst()
                    .orElse(null);

            if (selectedCustomer != null) {
                customerMenu(selectedCustomer, customerId);
            }
        }
        return true;
    }


    public void viewCarListManager(int companyId) throws SQLException {
        List<Car> cars = carDaoImpl.findAllByCompanyId(companyId);

        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
            companyMenu(companyId);
        } else {

            System.out.println("\nCar list: ");
            int counter = 1;
            for (Car car : cars) {
                System.out.println(counter + ". " + car.getCarName());
                counter++;

            }
        }
        companyMenu(companyId);
    }

    public void viewCarListCustomer(int companyId, Customer customer, int customerId) throws SQLException {

        List<Car> cars = carDaoImpl.findAllByCompanyId(companyId);

        if (cars.isEmpty()) {
            Company company = companyDaoImpl.getCompanyById(companyId);
            System.out.printf("\nNo available cars in the '%s' company", company.getCompanyName());
            viewCompanyListCustomer(customer, customerId);
            return;
        } else {
            cars.sort(Comparator.comparing(Car::getId));

            System.out.println("\nChoose a car: ");
            int counter = 1;
            for (Car car : cars) {
                System.out.println(counter + ". " + car.getCarName());
                counter++;
            }
            System.out.println("0. Back");
            String input = sc.nextLine();
            int carChoice = Integer.parseInt(input);

            if (carChoice == 0) {
                logInMenu(); // Return to main menu
                return;
            }
            if (carChoice > 0 && carChoice <= cars.size()) {
                // Get the car from the list based on the user choice
                Car selectedCar = cars.get(carChoice - 1);
                //System.out.println("You have chosen the car: " + selectedCar.getCarName());
                Company company = companyDaoImpl.getCompanyById(companyId);
                rentACar(customerId, company); // Pass the companyId and customer to the rentACar method
            } else {
                viewCarListCustomer(companyId, customer, customerId); // Re-run the method to prompt again
            }
        }
    }

    public void addCustomer() throws SQLException {

        System.out.println("\nEnter the customer name: ");
        String customerName = sc.nextLine();

        Customer newCustomer = new Customer();
        newCustomer.setCustomerName(customerName);
        newCustomer.setRentedCarId(null);
        customerDaoImpl.addCustomer(newCustomer);
        System.out.println("The customer was added!");
        logInMenu();
    }

    public void addCar(int companyId) throws SQLException {

        System.out.println("\nEnter the car name: ");
        String carName = sc.nextLine();

        Car newCar = new Car();
        newCar.setCarName(carName);
        newCar.setCompanyId(companyId);
        carDaoImpl.addCar(newCar);
        System.out.println("The car was added!");
    }

    private void chooseACompany(int customerId) throws SQLException {

        Customer customer = customerDaoImpl.getCustomerById(customerId);
        if (customerDaoImpl.hasRentedCar(customerId)) {
            System.out.println("You've already rented a car!");
            customerMenu(customer, customerId);
        }
        List<Company> companies = companyDaoImpl.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("\nChoose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getCompanyName());
        }
        System.out.println("0. Back");

        int companyChoice = sc.nextInt();
        sc.nextLine(); // Consume the newline character

        if (companyChoice == 0) {
            Customer updatedCustomer = customerDaoImpl.getCustomerById(customerId);
            customerMenu(updatedCustomer, customerId); // User chose to go back
        }

        if (companyChoice > 0 && companyChoice <= companies.size()) {
            Company company = companies.get(companyChoice - 1);
            rentACar(customerId, company); // Pass selected company to rentACar
        } else {
            System.out.println("Invalid choice. Try again.");
            chooseACompany(customerId); // Recursively prompt user for valid choice
        }
    }

    public boolean viewCompanyListManager() throws SQLException {
        List<Company> companies = companyDaoImpl.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
            return false;
        } else {
            System.out.println("\nChoose the company:");
            for (Company company : companies) {
                System.out.println(company.getId() + ". " + company.getCompanyName());
            }
            System.out.println("0. Back");
            String input = sc.nextLine();
            int companyId;
            try {
                companyId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return false;
            }

            if (companyId == 0) {
                managerMenu();
                return false;
            }

            Company selectedCompany = companies.stream()
                    .filter(company -> company.getId() == companyId)
                    .findFirst()
                    .orElse(null);

            if (selectedCompany != null) {
                Company company = companyDaoImpl.getCompanyById(companyId);
                System.out.printf("\n'%s' company", company.getCompanyName());
                companyMenu(companyId);
            } else {
                System.out.println("No company found with the given ID.");
                return false;
            }
        }
        return true;
    }

    public boolean viewCompanyListCustomer(Customer customer, int customerId) throws SQLException {
        List<Company> companies = companyDaoImpl.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
            return false;
        } else {
            System.out.println("\nChoose a company:");
            for (Company company : companies) {
                System.out.println(company.getId() + ". " + company.getCompanyName());
            }
            System.out.println("0. Back");
            String input = sc.nextLine();
            int companyId;
            try {
                companyId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                return false;
            }

            if (companyId == 0) {
                managerMenu();
                return false;
            }

            Company selectedCompany = companies.stream()
                    .filter(company -> company.getId() == companyId)
                    .findFirst()
                    .orElse(null);

            if (selectedCompany != null) {
                viewCarListCustomer(companyId, customer, customerId);
            } else {
                System.out.println("No company found with the given ID.");
                return false;
            }
        }
        return true;
    }

    public void addCompany() throws SQLException {

        System.out.println("\nEnter the company name: ");
        String companyName = sc.nextLine();

        Company newCompany = new Company();
        newCompany.setCompanyName(companyName);
        companyDaoImpl.addCompany(newCompany);
        System.out.println("The company was created!");
    }

}
