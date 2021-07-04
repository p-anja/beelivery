package beelivery.comment.dto;

public class CommentRequest {
    private String body;
    private int rating;

    public CommentRequest(String body, int rating) {
        this.body = body;
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
