package beelivery.restaurant.service;

import beelivery.restaurant.dto.RestaurantRequest;
import beelivery.restaurant.model.ERestStatus;
import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.repository.RestaurantRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestaurantService {

    private RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Optional<Restaurant> create(RestaurantRequest req, String filename) throws IOException {
        // Not specified how to set open/closed
        Restaurant r = new Restaurant(req.getName(), req.getType(), req.getAddress(), filename, ERestStatus.OPEN);
        r.setManagerUsername(req.getManagerUsername());
        Integer id = repository.getNextId();
        r.setId(id);
        if(!repository.create(r)) {
            return Optional.empty();
        }
        return Optional.of(r);
    }

    public List<Restaurant> search(String name, String type, String state, String city, double avgScore) {
        List<Restaurant> res = repository.getAll();
        if(!name.isBlank()) {
            res = res.stream().filter(r -> r.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        if(!type.isBlank()) {
            res = res.stream().filter(r -> r.getRestType().toString().toLowerCase().contains(type.toLowerCase())).collect(Collectors.toList());
        }
        if(!state.isBlank()) {
            res = res.stream().filter(r -> r.getAddress().getState().toLowerCase().contains(state.toLowerCase())).collect(Collectors.toList());
        }
        if(!city.isBlank()) {
            res = res.stream().filter(r -> r.getAddress().getCity().toLowerCase().contains(city.toLowerCase())).collect(Collectors.toList());
        }
        // All restaurants with equal or higher average scores
        res = res.stream().filter(r -> r.getAvgScore() >= avgScore).collect(Collectors.toList());
        return res;
    }

}
