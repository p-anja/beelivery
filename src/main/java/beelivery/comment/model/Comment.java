package beelivery.comment.model;

import beelivery.misc.IIdentifiable;

public class Comment implements IIdentifiable<Integer> {
    private Integer id;
    private String username;
    private String restaurantName;
    private String body;
    private int rating;
    private String userProfile;
    private ECommentStatus status;
    private boolean deleted;

    public Comment(Integer id, String username, String restaurantName, String body, int rating, boolean deleted, String userProfile) {
        this.id = id;
        this.username = username;
        this.restaurantName = restaurantName;
        this.body = body;
        this.rating = rating;
        this.deleted = deleted;
        this.userProfile = userProfile;
        this.status = ECommentStatus.PENDING;
    }

    public Comment(String username, String restaurantName, String body, int rating, String userProfile) {
        this.username = username;
        this.restaurantName = restaurantName;
        this.body = body;
        this.rating = rating;
        this.userProfile = userProfile;
        this.status = ECommentStatus.PENDING;
    }

    public ECommentStatus getStatus() {
        return status;
    }

    public void setStatus(ECommentStatus status) {
        this.status = status;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
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

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
