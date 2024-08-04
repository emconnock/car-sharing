package carsharing.model;

public class Customer {

    private int id;
    private String customerName;
    private Integer rentedCarId;

    public Customer(int id, String customerName, Integer rentedCarId) {
        this.id = id;
        this.customerName = customerName;
        this.rentedCarId = rentedCarId;
    }

    public Customer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", rentedCarId=" + rentedCarId +
                '}';
    }
}
