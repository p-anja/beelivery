package beelivery.user.controller;

import beelivery.restaurant.dto.ManagerRestaurantResponse;
import beelivery.user.model.ERole;
import beelivery.user.model.Manager;
import beelivery.user.model.User;
import beelivery.user.service.UserService;

import java.util.Optional;

import static beelivery.misc.Responses.*;
import static beelivery.Application.gson;

import static spark.Spark.get;

public class ManagerController {
    private UserService service;

    public ManagerController(UserService service) {
        this.service = service;

        get("/manager/restaurant", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                Manager m = (Manager) u.get();
                res.type("application/json");
                if(m.getRestaurant() == null) {
                    return notFound(res);
                }

                return gson.toJson(new ManagerRestaurantResponse(m.getRestaurant()));

             } catch(Exception e) {
                return internal(res);
            }
        });
    }
}
