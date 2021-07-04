package beelivery.user.service;

import beelivery.comment.CommentService;
import beelivery.comment.dto.CommentRequest;
import beelivery.comment.model.Comment;
import beelivery.misc.JwtUtil;
import beelivery.order.model.EOrderStatus;
import beelivery.order.model.Order;
import beelivery.order.service.OrderService;
import beelivery.restaurant.model.Article;
import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.service.RestaurantService;
import beelivery.user.dto.CartItemRequest;
import beelivery.user.dto.LoginRequest;
import beelivery.user.dto.RegisterRequest;
import beelivery.user.model.*;
import beelivery.user.repository.UserRepository;
import spark.Request;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private UserRepository repository;
    private RestaurantService restaurantService;
    private OrderService orderService;
    private CommentService commentService;

    public UserService(UserRepository repository, RestaurantService restaurantService, OrderService orderService, CommentService commentService) {
        this.repository = repository;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.commentService = commentService;
    }

    public boolean deleteUser(String username) {
        return repository.delete(username);
    }

    public boolean addComment(Regular r, CommentRequest req, String restName) {
        if(!orderService.hasDeliveredOrder(r.getUsername(), restName)) {
            return false;
        }
        Optional<Restaurant> rest = restaurantService.getByName(restName);
        if(!rest.isPresent()) {
            return false;
        }

        return commentService.create(r.getUsername(), restName, req.getBody(), req.getRating(), r.getProfileImg());
    }

    public boolean approveComment(Manager m, Integer id) {
        double newAvg = commentService.approve(id, m.getRestaurant().getName());
        if(newAvg < 0) {
            return false;
        }
        m.getRestaurant().setAvgScore(newAvg);
        Optional<Restaurant> r = restaurantService.getByName(m.getRestaurant().getName());
        r.get().setAvgScore(newAvg);

        return updateUser(m) && restaurantService.update(r.get());
    }

    public boolean declineComment(Integer id) {
        return commentService.decline(id);
    }

    public boolean deleteComment(Integer id) {
        return commentService.delete(id);
    }

    public Optional<String> login(LoginRequest req) {
        Optional<User> u = repository.get(req.getUsername());
        if (!u.isPresent() || !u.get().getPassword().equals(req.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(JwtUtil.generateJws(u.get()));
    }

    public boolean updateUser(User user) {
        return repository.update(user);
    }

    public boolean updateUser(RegisterRequest req) {
        Optional<User> u = getByUsername(req.getUsername());
        if (!u.isPresent()) {
            return false;
        }

        switch(u.get().getRole()) {
            case REGULAR: {
                Regular r = (Regular) u.get();
                r.setBirthDate(req.getBirthDate());
                r.setSex(req.getSex());
                r.setFirstName(req.getFirstName());
                r.setLastName(req.getLastName());
                r.setPassword(req.getPassword());
                return repository.update(r);
            }
        }
        return false;
    }

    public Optional<User> validateJWS(Request req) {
        Optional<String> jws = JwtUtil.parseJws(req);
        if(!jws.isPresent()) {
            return Optional.empty();
        }
        String username = JwtUtil.getUsername(jws.get());
        Optional<User> u = getByUsername(username);
        return u;
    }

    public Optional<User> validateJWS(Request req, ERole role) {
        Optional<String> jws = JwtUtil.parseJws(req);
        if(!jws.isPresent()) {
            return Optional.empty();
        }
        String username = JwtUtil.getUsername(jws.get());
        Optional<User> u = getByUsername(username);
        if(!u.isPresent() || !u.get().getRole().equals(role)) {
            return Optional.empty();
        }
        return u;
    }

    public boolean createOrder(Regular r) {
        String restName = r.getCart().getRestaurantName();
        if(restName == null || restName.isBlank()) {
            return false;
        }
        Optional<Restaurant> rest = restaurantService.getByName(restName);
        if(!rest.isPresent()) {
            return false;
        }

        r.addOrder(orderService.createOrder(r.getCart(), rest.get(), r.getUsername(),
            r.getFirstName(), r.getLastName()));

        r.addPoints(r.getCart().getPrice() / 1000 * 133);
        r.getCart().clear();

        return repository.update(r);
    }

    public boolean cancelOrder(Regular r, String orderId) {
        double orderPrice = orderService.cancel(orderId);
        if(orderPrice <= 0.0) {
            return false;
        }

        r.subtractPoints(orderPrice / 1000 * 133 * 4);
        return updateUser(r);
    }

    public boolean requestOrder(Delivery d, String orderId) {
        Optional<Order> o = orderService.get(orderId);
        if(!o.isPresent() || !o.get().getStatus().equals(EOrderStatus.WAITING)) {
            return false;
        }

        Optional<User> u = getByUsername(o.get().getRestaurant().getManagerUsername());
        if(!u.isPresent() || !u.get().getRole().equals(ERole.MANAGER)) {
            return false;
        }
        Manager m = (Manager) u.get();
        m.addRequest(new DeliveryRequest(orderId, d.getUsername()));
        return updateUser(m);
    }

    public boolean approveOrder(Manager m, DeliveryRequest req) {
        Optional<Order> o = orderService.get(req.getOrderId());
        if(!o.isPresent() || !o.get().getStatus().equals(EOrderStatus.WAITING)) {
            return false;
        }
        Optional<User> u = getByUsername(req.getUsername());
        if(!u.isPresent()) {
            return false;
        }
        Delivery d = (Delivery) u.get();
        o.get().setStatus(EOrderStatus.TRANSPORT);
        o.get().setDeliveryUsername(d.getUsername());
        d.addOrder(o.get().getId());
        m.removeRequest(req);
        return updateUser(m) && updateUser(d) && orderService.update(o.get());
    }

    public boolean declineRequest(Manager m, DeliveryRequest req) {
        Optional<User> u = getByUsername(req.getUsername());
        if(!u.isPresent()) {
            return false;
        }
        return m.removeRequest(req) && updateUser(m);
    }

    public List<Order> getUserOrders(User user) {
        switch (user.getRole()) {
            case REGULAR: {
                return orderService.getByUsername(user.getUsername());
            }

            case MANAGER: {
                return orderService.getByManagerUsername(user.getUsername());
            }

            case DELIVERY: {
                return orderService.getByDeliveryUsername(user.getUsername());
            }
            // ADMIN ?
        }
        return Collections.emptyList();
    }

    public boolean addToCart(Regular r, List<CartItemRequest> itemsReq, String restName) {
        List<Article> articles = restaurantService.getArticlesByRestaurantName(restName);
        List<CartItem> newItems;
        newItems = itemsReq.stream().flatMap(i -> articles.stream()
            .filter(a -> a.getName().equals(i.getArticleName()))
            .map(ci -> new CartItem(ci, i.getAmount())))
            .collect(Collectors.toList());
        newItems.forEach(ni -> r.getCart().addArticle(ni));
        r.getCart().setRestaurantName(restName);
        return updateUser(r);
    }

    public boolean removeFromCart(Regular r, String articleName) {
        return r.getCart().removeArticle(articleName) && updateUser(r);
    }

    public boolean addRestaurantToManager(String username, Restaurant r) {
        Optional<User> u = getByUsername(username);
        if(!u.isPresent() || !u.get().getRole().equals(ERole.MANAGER)) {
            return false;
        }
        Manager m = (Manager) u.get();
        m.setRestaurant(r);
        return repository.update(m);
    }

    public boolean registerUser(RegisterRequest req) {
        Optional<User> reg = repository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Regular newRegular = new Regular(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate(), 0, EMemberType.BRONZE);
        return repository.create(newRegular);
    }

    public boolean registerManager(RegisterRequest req) {
        if(getByUsername(req.getUsername()).isPresent()) {
            return false;
        }
        Manager manager = new Manager(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate());
        return repository.create(manager);
    }

    public boolean registerDelivery(RegisterRequest req) {
        if(getByUsername(req.getUsername()).isPresent()) {
            return false;
        }
        Delivery delivery = new Delivery(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate());
        return repository.create(delivery);
    }

    public Optional<User> getByUsername(String username) {
        return repository.get(username);
    }

    public List<User> search(String username, String fname, String lname) {
        List<User> res = getAll().stream().filter(u -> !u.isDeleted()).collect(Collectors.toList());
        if(!username.isBlank()) {
            res = res.stream().filter(u -> u.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(!fname.isBlank()) {
            res = res.stream().filter(u -> u.getFirstName().toLowerCase().contains(fname.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(!lname.isBlank()) {
            res = res.stream().filter(u -> u.getLastName().toLowerCase().contains(lname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return res;
    }

    public List<Manager> getRestaurantlessManagers() {
        return getManagers().stream().filter(m -> m.getRestaurant() == null)
                .collect(Collectors.toList());
    }

    public List<Manager> getManagers() {
        return getAll().stream().filter(u -> u.getRole().equals(ERole.MANAGER)  && u instanceof Manager)
            .map(u -> (Manager) u)
            .collect(Collectors.toList());
    }

    public List<User> getAll() {
        return repository.getAll();
    }

}
