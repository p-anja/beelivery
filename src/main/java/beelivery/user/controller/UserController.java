package beelivery.user.controller;

import beelivery.user.dto.RegisterRequest;
import beelivery.user.service.UserService;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.*;
import static spark.Spark.post;

public class UserController {
    private UserService service;

    public UserController(UserService service) {
        this.service = service;

        post("/user/register", (req, res) -> {
            try {
                if(service.registerUser(gson.fromJson(req.body(), RegisterRequest.class))) {
                    return ok("Registered", res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
            return badRequest("Failed to register", res);
        });

    }

}
