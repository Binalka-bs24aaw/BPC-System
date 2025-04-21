package BPC_MainSystem.models;

import java.util.List;
import java.util.Map;

public class Physiotherapists {

    private int id;
    private String phyName;
    private String address;
    private String phone;
    private Map<String, List<String>> expertiseAreas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhyName() {
        return phyName;
    }

    public void setFullName(String phyName) {
        this.phyName = phyName;
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

    public Map<String, List<String>> getExpertiseAreas() {
        return expertiseAreas;
    }

    public void setExpertiseAreas(Map<String, List<String>> expertiseAreas) {
        this.expertiseAreas = expertiseAreas;
    }

    public Physiotherapists() {}

    public Physiotherapists(int id, String phyName, String address, String phone, Map<String, List<String>> expertiseAreas) {
        this.id = id;
        this.phyName = phyName;
        this.address = address;
        this.phone = phone;
        this.expertiseAreas = expertiseAreas;
    }

}
