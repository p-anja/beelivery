package beelivery.user.controller;

import beelivery.Application;
import beelivery.comment.CommentService;
import beelivery.comment.dto.CommentRequest;
import beelivery.misc.JwtUtil;
import beelivery.order.model.Order;
import beelivery.order.service.OrderService;
import beelivery.user.dto.*;
import beelivery.user.model.ERole;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.service.UserService;
import com.google.gson.reflect.TypeToken;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static beelivery.Application.gson;
import static beelivery.misc.Responses.*;
import static spark.Spark.*;

public class UserController {
    private UserService service;
    private OrderService orderService;
    private CommentService commentService;

    public UserController(UserService service, OrderService orderService, CommentService commentService) {
        this.service = service;
        this.orderService = orderService;
        this.commentService = commentService;

        delete("/user/order/:id", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String id = req.params(":id");
                if(id == null || id.isBlank()) {
                    return badRequest("Not canceled", res);
                }

                return service.cancelOrder((Regular) u.get(), id)
                    ? ok("Canceled", res)
                    : badRequest("Not canceled", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

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

        post("/user/profile", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req);
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
                u.get().setProfileImg(fname);
                return service.updateUser(u.get())
                    ? ok("Updated", res)
                    : badRequest("Not updated", res);
            } catch(Exception e) {
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

        post("/user/order", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                Regular r = (Regular) u.get();
                return service.createOrder(r)
                    ? ok("Added", res)
                    : badRequest("Not added", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/user/discount", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                Regular r = (Regular) u.get();
                switch (r.getMemberType()) {
                    case BRONZE: {
                        return 0.0;
                    }
                    case SILVER: {
                        return 3.0;
                    }
                    case GOLD: {
                        return 5.0;
                    }
                }
                return 0.0;
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/user/order", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String restName = req.queryParams("rest");
                String priceFrom = req.queryParams("pricefrom");
                String priceTo = req.queryParams("priceto");
                String dateFrom = req.queryParams("datefrom");
                String dateTo = req.queryParams("dateto");

                List<Order> orders = service.getUserOrders(u.get());

                if(restName != null && !restName.isBlank()) {
                    orders = orders.stream().filter(o -> o.getRestaurant().getName().toLowerCase()
                            .contains(restName.toLowerCase()))
                        .collect(Collectors.toList());
                }

                if(priceFrom != null && !priceFrom.isBlank()) {
                    final double price = Double.parseDouble(priceFrom);
                    orders = orders.stream().filter(o -> o.getPrice() >= price).collect(Collectors.toList());
                }

                if(priceTo != null && !priceTo.isBlank()) {
                    final double price = Double.parseDouble(priceTo);
                    orders = orders.stream().filter(o -> o.getPrice() <= price).collect(Collectors.toList());
                }

                if(dateFrom != null && !dateFrom.isBlank()) {
                    final LocalDate date = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    orders = orders.stream().filter(o -> {
                        LocalDate oDate = o.getDate().toLocalDate();
                        if(oDate.compareTo(date) >= 0) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                }

                if(dateTo != null && !dateTo.isBlank()) {
                    final LocalDate date = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    orders = orders.stream().filter(o -> {
                        LocalDate oDate = o.getDate().toLocalDate();
                        if(oDate.compareTo(date) <= 0) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
                }

                return gson.toJson(orders);
            } catch (Exception e) {
                e.printStackTrace();
                return internal(res);
            }
        });

        post("/user/comment/:restname", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String restName = req.params(":restname");
                if(restName == null || restName.isBlank()) {
                    return badRequest("Invalid restname", res);
                }
                CommentRequest cReq = gson.fromJson(req.body(), CommentRequest.class);
                return service.addComment((Regular) u.get(), cReq, restName)
                    ? ok("Posted", res)
                    : badRequest("Not posted", res);
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/owner/:restname/comment", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.MANAGER);
                if(!u.isPresent()) {
                    u = service.validateJWS(req, ERole.ADMIN);
                    if(!u.isPresent()) {
                        return forbidden(res);
                    }
                }

                String restName = req.params(":restname");
                if(restName == null || restName.isBlank()) {
                    return badRequest("Bad request", res);
                }

                return gson.toJson(commentService.getOwnerComments(restName));
            } catch(Exception e) {
                return internal(res);
            }
        });

        get("/user/cancomment/:restname", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.REGULAR);
                if(!u.isPresent()) {
                    return forbidden(res);
                }

                String restName = req.params(":restname");
                if(restName == null || restName.isBlank()) {
                    return forbidden(res);
                }

                return orderService.hasDeliveredOrder(u.get().getUsername(), restName)
                    ? ok("Can comment", res)
                    : badRequest("Cannot comment", res);
            } catch(Exception e) {
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
                return gson.toJson(r.getCart());
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
