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
        blocked = false;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void addRequest(DeliveryRequest r) {
        if(!requests.stream().filter(re -> re.getUsername().equals(r.getUsername()) && re.getOrderId().equals(r.getOrderId())).findFirst().isPresent()) {
            requests.add(r);
        }
    }

    public boolean removeRequest(DeliveryRequest r) {
        return requests.removeIf(re -> re.getOrderId().equals(r.getOrderId()) && re.getUsername().equals(r.getUsername()));
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
