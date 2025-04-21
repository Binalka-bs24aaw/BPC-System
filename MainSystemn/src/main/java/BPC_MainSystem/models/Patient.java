package BPC_MainSystem.models;

public class Patient {
    public Patient(int id, String pName, String address, String phone) {
        this.id = id;
        this.pName = pName;
        this.address = address;
        this.phone = phone;
    }

    private int id;
    private String pName;
    private String address;
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
