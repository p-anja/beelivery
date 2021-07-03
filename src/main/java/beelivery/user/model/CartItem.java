package beelivery.user.model;

import beelivery.restaurant.model.Article;

public class CartItem {
    private Article article;
    private int amount;

    public CartItem(Article article, int amount) {
        this.article = article;
        this.amount = amount;
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
