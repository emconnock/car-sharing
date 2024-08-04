package carsharing.model;

public class Car {

    private int id;
    private String carName;
    private int companyId;

    public Car(int id, String carName, int companyId) {
        this.id = id;
        this.carName = carName;
        this.companyId = companyId;
    }

    public Car(int id, String carName) {
        this.id = id;
        this.carName = carName;
    }

    public Car() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carName='" + carName + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
