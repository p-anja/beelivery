package beelivery.restaurant.model;

import beelivery.misc.Address;
import beelivery.misc.IIdentifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restaurant implements IIdentifiable<Integer> {
    private Integer id;
    private String managerUsername;
    private String name;
    private List<Article> articles;
    private ERestType restType;
    private Address address;
    private String logoFilepath;
    private boolean deleted;

    public Restaurant(Integer id, String name, List<Article> articles, ERestType restType, Address address, String logoFilepath) {
        this.id = id;
        this.name = name;
        this.articles = articles;
        this.restType = restType;
        this.address = address;
        this.logoFilepath = logoFilepath;
    }

    public Restaurant(String name, List<Article> articles, ERestType restType, Address address, String logoFilepath) {
        this.name = name;
        this.articles = articles;
        this.restType = restType;
        this.address = address;
        this.logoFilepath = logoFilepath;
    }

    public Restaurant(String name, ERestType restType, Address address, String logoFilepath) {
        this.name = name;
        this.articles = new ArrayList<Article>();
        this.restType = restType;
        this.address = address;
        this.logoFilepath = logoFilepath;
    }

    public void addArticle(Article a) {
        if(!articles.stream().filter(ar -> ar.getId().equals(a.getId())).findFirst().isPresent()) {
            a.setRestaurantId(id);
            articles.add(a);
        }
    }

    public void removeArticle(Integer articleId) {
        articles.removeIf(a -> a.getId().equals(articleId));
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

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }
}
