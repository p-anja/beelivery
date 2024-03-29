package beelivery.restaurant.model;

import beelivery.misc.IIdentifiable;

public class Article implements IIdentifiable<String> {
    private String name;
    private EArticleType articleType;
    private Integer restaurantId;
    private double amount;
    private double price;
    private String description;
    private String imageFilepath;
    private boolean deleted;

    public Article(String name, EArticleType articleType, Integer restaurantId, double amount, String description, String imageFilepath, double price) {
        this.name = name;
        this.articleType = articleType;
        this.restaurantId = restaurantId;
        this.amount = amount;
        this.description = description;
        this.imageFilepath = imageFilepath;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        this.name = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(EArticleType articleType) {
        this.articleType = articleType;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFilepath() {
        return imageFilepath;
    }

    public void setImageFilepath(String imageFilepath) {
        this.imageFilepath = imageFilepath;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
