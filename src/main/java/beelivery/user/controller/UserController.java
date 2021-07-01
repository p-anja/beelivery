package beelivery.user.controller;

import beelivery.user.dto.LoginRequest;
import beelivery.user.dto.RegisterRequest;
import beelivery.user.service.UserService;

import java.util.Optional;

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

        post("/user/login", (req, res) -> {
            try {
                Optional<String> jws = service.login(gson.fromJson(req.body(), LoginRequest.class));
                if(!jws.isPresent()) {
                    return badRequest("Bad login", res);
                }
                res.type("application/json");
                return jws.get();
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

    }

}
