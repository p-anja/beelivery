package beelivery.user.controller;

import beelivery.Application;
import beelivery.misc.JwtUtil;
import beelivery.restaurant.dto.RestaurantRequest;
import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.service.RestaurantService;
import beelivery.user.dto.RegisterRequest;
import beelivery.user.model.ERole;
import beelivery.user.model.User;
import beelivery.user.service.UserService;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import javax.swing.text.html.Option;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.*;
import static spark.Spark.get;
import static spark.Spark.post;

public class AdminController {
    private UserService service;
    private RestaurantService restaurantService;

    public AdminController(UserService service, RestaurantService restaurantService) {
        this.service = service;
        this.restaurantService = restaurantService;

        post("/admin/restaurant", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.ADMIN);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String location = "image";
                long maxFileSize = 100000000;
                long maxRequestSize = 100000000;
                int fileSizeThreshold = 1024;
                MultipartConfigElement mce = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
                req.raw().setAttribute("org.eclipse.jetty.multipartConfig", mce);
                Collection<Part> parts = req.raw().getParts();
                String fname = req.raw().getPart("file").getSubmittedFileName();
                Path out = Paths.get(Application.UPLOAD_DIR + File.separator + fname);
                try (final InputStream in = req.raw().getPart("file").getInputStream()) {
                    Files.copy(in, out);
                }
                RestaurantRequest restReq = gson.fromJson(req.raw().getParameter("request"), RestaurantRequest.class);
                Optional<Restaurant> r = restaurantService.create(restReq, fname);
                if(!r.isPresent()) {
                    return badRequest("Bad request", res);
                }
                return service.addRestaurantToManager(restReq.getManagerUsername(), r.get())
                        ? ok("Added", res)
                        : badRequest("Failed to add", res);

            } catch(Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        post("/admin/manager", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.ADMIN);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                return service.registerManager(gson.fromJson(req.body(), RegisterRequest.class)) ?
                        ok("Registered manager", res)
                        : badRequest("Failed to register manager", res);
            } catch (Exception e) {
                return internal(res);
            }
        });

        post("/admin/delivery", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.ADMIN);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                return service.registerDelivery(gson.fromJson(req.body(), RegisterRequest.class))
                    ? ok("Registered delivery", res)
                    : badRequest("Failed to register delivery person", res);
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        get("/admin/managers", (req, res) -> {
            try {
                if(!service.validateJWS(req, ERole.ADMIN).isPresent()) {
                    return forbidden(res);
                }
                return gson.toJson(service.getRestaurantlessManagers());
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/admin/users", (req, res) -> {
            Optional<User> u = service.validateJWS(req, ERole.ADMIN);
            if(!u.isPresent()) {
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
