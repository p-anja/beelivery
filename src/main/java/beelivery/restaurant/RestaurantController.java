package beelivery.restaurant;

import beelivery.restaurant.service.RestaurantService;

import static beelivery.Application.gson;
import static spark.Spark.get;

public class RestaurantController {

    private RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;

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
