package beelivery.restaurant.dto;

import beelivery.misc.Address;
import beelivery.restaurant.model.ERestType;

public class RestaurantRequest {
    private String name;
    private ERestType type;
    private String managerUsername;
    private Address address;

    public RestaurantRequest(String name, ERestType type, String managerUsername, Address address) {
        this.name = name;
        this.type = type;
        this.managerUsername = managerUsername;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ERestType getType() {
        return type;
    }

    public void setType(ERestType type) {
        this.type = type;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
