package beelivery.user.dto;

public class CartItemRequest {
    private String articleName;
    private int amount;
    public CartItemRequest(String articleName, int amount) {
        this.articleName = articleName;
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int a) {
        this.amount = a;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
}
