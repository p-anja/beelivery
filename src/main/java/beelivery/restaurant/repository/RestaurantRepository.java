package beelivery.restaurant.repository;

import beelivery.misc.JSONRepository;
import beelivery.restaurant.model.Restaurant;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class RestaurantRepository extends JSONRepository<Restaurant, Integer> {

    public RestaurantRepository(String filename) {
        super(filename, new TypeToken<List<Restaurant>>(){}.getType());
    }

    public Integer getNextId() throws JsonSyntaxException, IOException {
        return getAll().size() + 1;
    }
}
