package beelivery.user.controller;

import beelivery.Application;
import beelivery.order.service.OrderService;
import beelivery.restaurant.dto.ManagerRestaurantResponse;
import beelivery.restaurant.service.RestaurantService;
import beelivery.user.dto.ArticleRequest;
import beelivery.user.model.DeliveryRequest;
import beelivery.user.model.ERole;
import beelivery.user.model.Manager;
import beelivery.user.model.User;
import beelivery.user.service.UserService;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static beelivery.misc.Responses.*;
import static beelivery.Application.gson;
import static spark.Spark.*;

public class ManagerController {
    private UserService service;
    private RestaurantService restaurantService;
    private OrderService orderService;

    public ManagerController(UserService service, RestaurantService restaurantService, OrderService orderService) {
        this.service = service;
        this.restaurantService = restaurantService;
        this.orderService = orderService;

        delete("/manager/restaurant/article/:name", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String name = req.params(":name");
                if(name == null || name.isBlank()) {
                    return badRequest("Empty name", res);
                }
                Manager m = (Manager) u.get();
                return restaurantService.deleteArticleFromRestaurant(m, name)
                        && service.updateUser(m)
                    ? ok("Deleted", res)
                    : badRequest("Not deleted", res);
            } catch (Exception e) {
                return internal(res);
            }
        });

        put("/manager/requests", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                DeliveryRequest dReq = gson.fromJson(req.body(), DeliveryRequest.class);

                return service.declineRequest((Manager) u.get(), dReq)
                    ? ok("Declined", res)
                    : badRequest("Not declined", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        put("/manager/order/wait", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String id = req.body();
                return orderService.toWait(id)
                    ? ok("Changed to WAITING", res)
                    : badRequest("Not changed", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        put("/manager/order/prepare", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String id = req.body();
                return orderService.toPrepare(id)
                    ? ok("Changed to PREPARING", res)
                    : badRequest("Not changed", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        put("/manager/restaurant/article", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                ArticleRequest areq = gson.fromJson(req.body(), ArticleRequest.class);
                Manager m = (Manager) u.get();
                return restaurantService.updateArticleInRestaurant(m, areq)
                        && service.updateUser(m)
                    ? ok("Updated", res)
                    : badRequest("Not updated", res);

            } catch(Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        post("/manager/comment/approve", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String idstr = req.body();
                if(idstr == null || idstr.isBlank()) {
                    return badRequest("Invalid comment", res);
                }
                Integer id = Integer.parseInt(idstr);
                return service.approveComment((Manager) u.get(), id)
                        ? ok("Approved", res)
                        : badRequest("Not approved", res);
            } catch (Exception e) {
                return internal(res);
            }
        });

        post("/manager/comment/decline", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String idstr = req.body();
                if(idstr == null || idstr.isBlank()) {
                    return badRequest("Invalid comment", res);
                }
                Integer id = Integer.parseInt(idstr);
                return service.declineComment(id)
                        ? ok("Declined", res)
                        : badRequest("Not declined", res);
            } catch (Exception e) {
                return internal(res);
            }
        });

        post("/manager/restaurant/article", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
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
                    if(!Files.exists(out))
                        Files.copy(in, out);
                }
                ArticleRequest areq = gson.fromJson(req.raw().getParameter("request"), ArticleRequest.class);
                areq.setImageFilepath(fname);

                Manager m = (Manager) u.get();
                return restaurantService.addArticleToRestaurant(m, areq)
                        && service.updateUser(m)
                    ? ok("Added", res)
                    : badRequest("Failed to add", res);

            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        post("/manager/requests", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if (!u.isPresent()) {
                    return forbidden(res);
                }
                DeliveryRequest dReq = gson.fromJson(req.body(), DeliveryRequest.class);
                return service.approveOrder((Manager) u.get(), dReq)
                    ? ok("Approved", res)
                    : badRequest("Not approved", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/manager/customer", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if (!u.isPresent()) {
                    return forbidden(res);
                }
                return gson.toJson(service.getRestaurantCustomers((Manager) u.get()));
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/manager/info", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if (!u.isPresent()) {
                    return forbidden(res);
                }
                return gson.toJson(service.getRestaurantInfo((Manager) u.get()));
            } catch(Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        get("/manager/requests", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if (!u.isPresent()) {
                    return forbidden(res);
                }
                Manager m = (Manager) u.get();
                return gson.toJson(m.getRequests());
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/manager/isowner/:name", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                Manager m = (Manager) u.get();
                if(m.getRestaurant() == null) {
                    return badRequest("Not owner", res);
                }
                String name = req.params(":name");
                if(name.isBlank()) {
                    return badRequest("Invalid name", res);
                }
                return m.getRestaurant().getName().equals(name)
                    ? ok("Is owner", res)
                    : badRequest("Not owner", res);
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

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
