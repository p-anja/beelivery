package beelivery.user.dto;

import beelivery.restaurant.model.EArticleType;

public class ArticleRequest {
    private String name;
    private EArticleType articleType;
    private double amount;
    private String description;
    private String imageFilepath;
    private double price;

    public ArticleRequest(String name, EArticleType articleType, double amount, String description, String imageFilepath, double price) {
        this.name = name;
        this.articleType = articleType;
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
}
