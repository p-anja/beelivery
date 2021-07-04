package beelivery.user.controller;

import beelivery.order.service.OrderService;
import beelivery.user.model.Delivery;
import beelivery.user.model.ERole;
import beelivery.user.model.User;
import beelivery.user.service.UserService;

import java.util.Optional;

import static beelivery.misc.Responses.*;
import static spark.Spark.post;
import static spark.Spark.put;

public class DeliveryController {

    private UserService service;
    private OrderService orderService;

    public DeliveryController(UserService service, OrderService orderService) {
        this.service = service;
        this.orderService = orderService;

        put("/delivery/delivered", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.DELIVERY);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String id = req.body();
                return orderService.toDelivered(id)
                    ? ok("Changed to DELIVERED", res)
                    : badRequest("Not changed", res);
            } catch (Exception e) {
                return internal(res);
            }
        });

        post("/delivery/request", (req, res) -> {
            try {
                Optional<User> u = service.validateJWS(req, ERole.DELIVERY);
                if(!u.isPresent()) {
                    return forbidden(res);
                }
                String id = req.body();
                return service.requestOrder((Delivery) u.get(), id)
                    ? ok("Requested", res)
                    : badRequest("Not requested", res);
            } catch (Exception e) {
                return internal(res);
            }
        });
    }
}
