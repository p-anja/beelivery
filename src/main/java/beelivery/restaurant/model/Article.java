package beelivery.restaurant.model;

import beelivery.misc.IIdentifiable;

public class Article implements IIdentifiable<Integer> {
    private Integer id;
    private String name;
    private EArticleType articleType;
    private Integer restaurantId;
    private double amount;
    private String description;
    private String imageFilepath;
    private boolean deleted;

    public Article(Integer id, String name, EArticleType articleType, Integer restaurantId, double amount, String description, String imageFilepath) {
        this.id = id;
        this.name = name;
        this.articleType = articleType;
        this.restaurantId = restaurantId;
        this.amount = amount;
        this.description = description;
        this.imageFilepath = imageFilepath;
    }

    public Article(String name, EArticleType articleType, Integer restaurantId, double amount, String description, String imageFilepath) {
        this.id = id;
        this.name = name;
        this.articleType = articleType;
        this.restaurantId = restaurantId;
        this.amount = amount;
        this.description = description;
        this.imageFilepath = imageFilepath;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
