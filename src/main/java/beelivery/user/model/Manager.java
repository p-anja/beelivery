package beelivery.user.model;

import beelivery.restaurant.model.Restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Manager extends User{
    private Restaurant restaurant;
    private List<DeliveryRequest> requests;

    public Manager(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.MANAGER, birthDate);
        requests = new ArrayList<DeliveryRequest>();
    }

    public void addRequest(DeliveryRequest r) {
        if(!requests.contains(r)) {
            requests.add(r);
        }
    }

    public boolean removeRequest(DeliveryRequest r) {
        return requests.remove(r);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        restaurant.setManagerUsername(username);
        this.restaurant = restaurant;
    }

    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<DeliveryRequest> requests) {
        this.requests = requests;
    }
}
