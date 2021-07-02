package beelivery.restaurant.dto;

import beelivery.misc.Address;
import beelivery.restaurant.model.Article;
import beelivery.restaurant.model.ERestStatus;
import beelivery.restaurant.model.ERestType;
import beelivery.restaurant.model.Restaurant;

import java.util.List;

public class ManagerRestaurantResponse {
    private Integer id;
    private String managerUsername;
    private String name;
    private List<Article> articles;
    private ERestType restType;
    private Address address;
    private String logoFilepath;
    private ERestStatus status;
    private double avgScore;
    private int orderCount;
    private int commentCount;
    private int customerCount;

    public ManagerRestaurantResponse(Integer id, String managerUsername, String name, List<Article> articles, ERestType restType, Address address, String logoFilepath, ERestStatus status, double avgScore, int orderCount, int commentCount, int customerCount) {
        this.id = id;
        this.managerUsername = managerUsername;
        this.name = name;
        this.articles = articles;
        this.restType = restType;
        this.address = address;
        this.logoFilepath = logoFilepath;
        this.status = status;
        this.avgScore = avgScore;
        this.orderCount = orderCount;
        this.commentCount = commentCount;
        this.customerCount = customerCount;
    }

    public ManagerRestaurantResponse(Restaurant r) {
        this.id = r.getId();
        this.managerUsername = r.getManagerUsername();
        this.name = r.getName();
        this.articles = r.getArticles();
        this.restType = r.getRestType();
        this.address = r.getAddress();
        this.logoFilepath = r.getLogoFilepath();
        this.status = r.getStatus();
        this.avgScore = r.getAvgScore();
        this.orderCount = 0;
        this.commentCount = 0;
        this.customerCount = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public ERestType getRestType() {
        return restType;
    }

    public void setRestType(ERestType restType) {
        this.restType = restType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getLogoFilepath() {
        return logoFilepath;
    }

    public void setLogoFilepath(String logoFilepath) {
        this.logoFilepath = logoFilepath;
    }

    public ERestStatus getStatus() {
        return status;
    }

    public void setStatus(ERestStatus status) {
        this.status = status;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }
}
