package beelivery.user.controller;

import beelivery.misc.JwtUtil;
import beelivery.user.dto.*;
import beelivery.user.model.ERole;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.service.UserService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.*;
import static spark.Spark.*;

public class UserController {
    private UserService service;

    public UserController(UserService service) {
        this.service = service;

        delete("/user/cart/:aname", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if (!u.isPresent()) {
                    return forbidden(res);
                }
                String articleName = req.params(":aname");
                if (articleName == null || articleName.isBlank()) {
                    return badRequest("Not deleted", res);
                }
                return service.removeFromCart((Regular) u.get(), articleName)
                        ? ok("Deleted", res)
                        : badRequest("Not deleted", res);
            } catch (Exception e) {
                return internal(res);
            }
        });

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

        post("/user/cart/:restname", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String restName = req.params(":restname");
                if(restName == null || restName.isBlank()) {
                    return badRequest("Bad request", res);
                }
                Type type = new TypeToken<List<CartItemRequest>>(){}.getType();
                List<CartItemRequest> items = gson.fromJson(req.body(), type);
                return service.addToCart((Regular)u.get(), items, restName)
                    ? ok("Added", res)
                    : badRequest("Not added", res);
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        get("/user/cart", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                Regular r = (Regular) u.get();
                return gson.toJson(r.getCart().getItems());
            } catch (Exception e) {
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
