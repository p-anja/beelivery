package beelivery.order.service;

import beelivery.order.model.EOrderStatus;
import beelivery.order.model.Order;
import beelivery.order.repository.OrderRepository;
import beelivery.restaurant.model.Article;
import beelivery.restaurant.model.Restaurant;
import beelivery.user.model.Cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService {

    private OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public String createOrder(Cart cart, Restaurant restaurant, String username, String fName, String lName) {
        Order order = new Order(restaurant, LocalDateTime.now(), cart.getPrice(), username, fName, lName);
        String id = repository.getNextId();
        order.setId(id);
        order.setArticles(cart.getArticles());
        repository.create(order);
        return id;
    }

    public Optional<Order> get(String id) {
        return repository.get(id);
    }

    public double cancel(String id) {
        Optional<Order> order = repository.get(id);
        if(!order.isPresent()) {
            return -1.0;
        }
        order.get().setStatus(EOrderStatus.CANCELED);
        return repository.update(order.get())
            ? order.get().getPrice()
            : -1.0;
    }

    public boolean toWait(String id) {
        Optional<Order> order = repository.get(id);
        if(!order.isPresent() || !order.get().getStatus().equals(EOrderStatus.PENDING)) {
            return false;
        }
        order.get().setStatus(EOrderStatus.WAITING);
        return repository.update(order.get());
    }

    public boolean toDelivered(String id) {
        Optional<Order> order = repository.get(id);
        if(!order.isPresent() || !order.get().getStatus().equals(EOrderStatus.TRANSPORT)) {
            return false;
        }
        order.get().setStatus(EOrderStatus.DELIVERED);
        return repository.update(order.get());
    }

    public List<Order> getByUsername(String username) {
        List<Order> all = repository.getAll();
        return all.stream().filter(o -> o.getUsername().equals(username))
            .collect(Collectors.toList());
    }

    public List<Order> getByManagerUsername(String username) {
        return repository.getAll().stream()
            .filter(o -> o.getRestaurant().getManagerUsername().equals(username))
            .collect(Collectors.toList());
    }

    public List<Order> getByDeliveryUsername(String username) {
        return repository.getAll().stream()
            .filter(o -> o.getDeliveryUsername() != null && o.getDeliveryUsername().equals(username)
                || o.getStatus().equals(EOrderStatus.WAITING))
            .collect(Collectors.toList());
    }
}
