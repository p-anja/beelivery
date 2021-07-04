package beelivery.user.dto;

public class RestInfoResponse {
    private long orderCount;
    private long customerCount;
    private long commentCount;

    public RestInfoResponse(long orderCount, long customerCount, long commentCount) {
        this.orderCount = orderCount;
        this.customerCount = customerCount;
        this.commentCount = commentCount;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(long customerCount) {
        this.customerCount = customerCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }
}
