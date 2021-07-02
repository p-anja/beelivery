package beelivery.user.controller;

import beelivery.misc.JwtUtil;
import beelivery.user.model.ERole;
import beelivery.user.model.User;
import beelivery.user.service.UserService;

import java.util.Optional;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.badRequest;
import static beelivery.misc.Responses.forbidden;
import static spark.Spark.get;

public class AdminController {
    private UserService service;

    public AdminController(UserService service) {
        this.service = service;

        get("/admin/users", (req, res) -> {
            Optional<String> jws = JwtUtil.parseJws(req);
            if(!jws.isPresent()) {
                return badRequest("Missing jws", res);
            }
            String username = JwtUtil.getUsername(jws.get());
            Optional<User> u = service.getByUsername(username);
            if(!u.isPresent() || u.get().getRole() != ERole.ADMIN) {
                return forbidden(res);
            }
            res.type("application/json");
            String searchUsername = req.queryParams("username");
            String searchFName = req.queryParams("fname");
            String searchLName = req.queryParams("lname");

            return gson.toJson(service.search(searchUsername, searchFName, searchLName));
        });
    }

}
