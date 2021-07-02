package beelivery.restaurant.service;

import beelivery.restaurant.repository.RestaurantRepository;

public class RestaurantService {

    private RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public boolean create() {
        return false;
    }

}
