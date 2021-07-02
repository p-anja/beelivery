package beelivery.restaurant.controller;

import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.service.RestaurantService;

import java.util.Optional;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.notFound;
import static spark.Spark.get;

public class RestaurantController {

    private RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;

        get("/restaurant/:name", (req, res) -> {
            res.type("application/json");
            Optional<Restaurant> r = service.getByName(req.params(":name"));
            if(!r.isPresent()) {
                return notFound(res);
            }
            return gson.toJson(r.get());
        });

        get("/restaurant", (req, res) -> {
              String name = req.queryParams("name");
              String type = req.queryParams("type");
              String state = req.queryParams("state");
              String city = req.queryParams("city");
              double avgScore = Double.parseDouble(req.queryParams("score"));
              return gson.toJson(service.search(name, type, state, city, avgScore));
        });
    }
}
