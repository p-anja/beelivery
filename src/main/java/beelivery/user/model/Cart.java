package beelivery.user.model;

import beelivery.restaurant.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Cart {
    private String username;
    private double price;
    private List<CartItem> items;
    private String restaurantName;

    public Cart(String username) {
        this.username = username;
        this.price = 0.0;
        items = new ArrayList<CartItem>();
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void clear() {
        items.clear();
        restaurantName = "";
        recalculatePrice();
    }

    public List<Article> getArticles() {
        return items.stream()
            .map(ci -> ci.getArticle())
            .collect(Collectors.toList());
    }

    private void recalculatePrice() {
        price = 0.0;
        for(CartItem i : items) {
            price += i.getPrice();
        }
    }

    public void addArticle(Article a, int amount) {
        Optional<CartItem> item = items.stream().filter(i -> i.getArticle().getName().equals(a.getName())).findFirst();
        if (item.isPresent()) {
            item.get().setAmount(item.get().getAmount() + amount);
            recalculatePrice();
            return;
        }
        items.add(new CartItem(a, amount));
        recalculatePrice();
    }

    public void addArticle(CartItem ci) {
        Optional<CartItem> item = items.stream().filter(i -> i.getArticle().getName().equals(ci.getArticle().getName()))
            .findFirst();
        if(item.isPresent()) {
            item.get().setAmount(item.get().getAmount() + ci.getAmount());
            recalculatePrice();
            return;
        }
        items.add(ci);
        recalculatePrice();
    }

    public boolean removeArticle(String aName) {
        Optional<CartItem> item = items.stream().filter(i -> i.getArticle().getName().equals(aName)).findFirst();
        if(!item.isPresent()) {
            return false;
        }
        items.removeIf(a -> a.getArticle().getName().equals(aName));
        recalculatePrice();
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
