package beelivery.user.model;

import beelivery.restaurant.model.Article;

public class CartItem {
    private Article article;
    private int amount;
    private double price;

    public CartItem(Article article, int amount) {
        this.article = article;
        this.amount = amount;
        this.price = article.getPrice() * amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
