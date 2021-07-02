package beelivery.misc;

public class Address {
    private String state;
    private String city;
    private String street;
    private String streetNo;
    private double lat;
    private double lon;

    public Address(String state, String city, String street, String streetNo, double lat, double lon) {
        this.state = state;
        this.city = city;
        this.street = street;
        this.streetNo = streetNo;
        this.lat = lat;
        this.lon = lon;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
