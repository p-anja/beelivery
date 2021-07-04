package beelivery.order.model;

import beelivery.misc.IIdentifiable;
import beelivery.restaurant.model.Article;
import beelivery.restaurant.model.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order implements IIdentifiable<String> {

    private String id;
    private List<Article> articles;
    private Restaurant restaurant;
    private LocalDateTime date;
    private double price;
    private String username;
    private String firstName;
    private String lastName;
    private EOrderStatus status;
    private boolean deleted;
    private String deliveryUsername;

    public Order(String id, Restaurant restaurant, LocalDateTime date, double price, String username, String firstName, String lastName, EOrderStatus status) {
        this.id = id;
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.articles = new ArrayList<Article>();
        this.deleted = false;
    }

    public Order(Restaurant restaurant, LocalDateTime date, double price, String username, String firstName, String lastName, EOrderStatus status) {
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.articles = new ArrayList<Article>();
        this.deleted = false;
    }

    public Order(Restaurant restaurant, LocalDateTime date, double price, String username, String firstName, String lastName) {
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.articles = new ArrayList<Article>();
        this.deleted = false;
        this.status = EOrderStatus.PENDING;
    }

    public String getDeliveryUsername() {
        return deliveryUsername;
    }

    public void setDeliveryUsername(String deliveryUsername) {
        this.deliveryUsername = deliveryUsername;
    }

    public void addArticle(Article a) {
        Optional<Article> article = articles.stream().filter(ar -> ar.getName().equals(a.getName())).findFirst();
        if(!article.isPresent()) {
            articles.add(a);
        }
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean removeArticle(String articleName) {
        return articles.removeIf(a -> a.getName().equals(articleName));
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public void setStatus(EOrderStatus status) {
        this.status = status;
    }
}
