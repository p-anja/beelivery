package beelivery.restaurant.service;

import beelivery.restaurant.dto.RestaurantRequest;
import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.repository.RestaurantRepository;

import java.io.IOException;
import java.util.Optional;

public class RestaurantService {

    private RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Optional<Restaurant> create(RestaurantRequest req, String filename) throws IOException {
        Restaurant r = new Restaurant(req.getName(), req.getType(), req.getAddress(), filename);
        r.setManagerUsername(req.getManagerUsername());
        Integer id = repository.getNextId();
        r.setId(id);
        if(!repository.create(r)) {
            return Optional.empty();
        }
        return Optional.of(r);
    }

}
