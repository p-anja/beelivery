package beelivery.user.controller;

import beelivery.misc.JwtUtil;
import beelivery.user.dto.LoginRequest;
import beelivery.user.dto.RegisterRequest;
import beelivery.user.dto.RegularResponse;
import beelivery.user.dto.UserResponse;
import beelivery.user.model.ERole;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.service.UserService;

import java.util.Optional;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.*;
import static spark.Spark.get;
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

        post("/user/update", (req, res) -> {
            try {
                Optional<String> jws = JwtUtil.parseJws(req);
                if(!jws.isPresent()) {
                    return badRequest("Missing jws", res);
                }
                String username = JwtUtil.getUsername(jws.get());
                Optional<User> u = service.getByUsername(username);
                if(!u.isPresent()) {
                    return notFound(res);
                }
                // Contains most fields
                RegisterRequest updateRequest = gson.fromJson(req.body(), RegisterRequest.class);
                return service.updateUser(updateRequest) ?
                        ok("Updated", res)
                        : badRequest("Failed to update", res);
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        get("/user/role", (req, res) -> {
            try {
                Optional<String> jws = JwtUtil.parseJws(req);
                if(!jws.isPresent()) {
                    return badRequest("Missing jws", res);
                }
                String username = JwtUtil.getUsername(jws.get());
                Optional<User> u = service.getByUsername(username);
                if(!u.isPresent()) {
                    return notFound(res);
                }
                res.type("application/json");
                return u.get().getRole();
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        get("/user", (req, res) -> {
            try {
                Optional<String> jws = JwtUtil.parseJws(req);
                if(!jws.isPresent()) {
                    return badRequest("Missing jws", res);
                }
                String username = JwtUtil.getUsername(jws.get());
                Optional<User> u = service.getByUsername(username);
                if(!u.isPresent()) {
                    return notFound(res);
                }
                if(u.get().getRole() == ERole.REGULAR) {
                    return gson.toJson(new RegularResponse((Regular) u.get()));
                }
                return gson.toJson(new UserResponse(u.get()));
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

    }

}
