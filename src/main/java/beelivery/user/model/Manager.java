package beelivery.user.model;

import beelivery.restaurant.model.Restaurant;

import java.util.Date;

public class Manager extends User{
    private Restaurant restaurant;

    public Manager(String username, String password, String firstName, String lastName, ESex sex, Date birthDate) {
        super(username, password, firstName, lastName, sex, ERole.MANAGER, birthDate);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        restaurant.setManagerUsername(username);
        this.restaurant = restaurant;
    }
}
