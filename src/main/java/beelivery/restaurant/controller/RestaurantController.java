package beelivery.restaurant.controller;

import beelivery.comment.CommentService;
import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.service.RestaurantService;

import java.util.Collections;
import java.util.Optional;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.notFound;
import static spark.Spark.get;

public class RestaurantController {

    private RestaurantService service;
    private CommentService commentService;

    public RestaurantController(RestaurantService service, CommentService commentService) {
        this.service = service;
        this.commentService = commentService;

        get("/restaurant/:name/comment", (req, res) -> {
            res.type("application/json");
            String name = req.params(":name");
            if(name == null || name.isBlank()) {
                return gson.toJson(Collections.emptyList());
            }
            return gson.toJson(commentService.getByRestaurantName(name));
        });

        get("/restaurant/:name/article", (req, res) -> {
            res.type("application/json");
            String name = req.params(":name");
            if(name == null || name.isBlank()) {
                return gson.toJson(Collections.emptyList());
            }
            return gson.toJson(service.getArticlesByRestaurantName(name));
        });

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
              String avgScoreString = req.queryParams("score");
              String onlyOpen = req.queryParams("open");
              return gson.toJson(service.search(name, type, state, city, avgScoreString, onlyOpen));
        });
    }
}
