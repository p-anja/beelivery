package beelivery.restaurant.service;

import beelivery.restaurant.dto.RestaurantRequest;
import beelivery.restaurant.model.Article;
import beelivery.restaurant.model.ERestStatus;
import beelivery.restaurant.model.Restaurant;
import beelivery.restaurant.repository.RestaurantRepository;
import beelivery.user.dto.ArticleRequest;
import beelivery.user.model.Manager;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestaurantService {

    private RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public boolean update(Restaurant r) {
        return repository.update(r);
    }

    public boolean addArticleToRestaurant(Manager m, ArticleRequest req) {
        Restaurant r = m.getRestaurant();
        if(r == null) {
            return false;
        }

        Optional<Article> a = r.getArticles().stream().filter(ar -> ar.getName().equals(req.getName())).findFirst();
        if(a.isPresent() && !a.get().isDeleted()) {
            return false;
        }

        if(a.isPresent() && a.get().isDeleted()) {
            a.get().setDeleted(false);
            a.get().setName(req.getName());
            a.get().setArticleType(req.getArticleType());
            a.get().setAmount(req.getAmount());
            a.get().setDescription(req.getDescription());
            a.get().setPrice(req.getPrice());
            return repository.update(r);
        }

        r.addArticle(new Article(req.getName(), req.getArticleType(), r.getId(), req.getAmount(), req.getDescription(),
            req.getImageFilepath(), req.getPrice()));
        return repository.update(r);
    }

    public List<Article> getArticlesByRestaurantName(String name) {
        Optional<Restaurant> r = getByName(name);
        if(!r.isPresent()) {
            return Collections.emptyList();
        }
        return r.get().getArticles().stream().filter(a -> !a.isDeleted()).collect(Collectors.toList());
    }

    public boolean updateArticleInRestaurant(Manager m, ArticleRequest req) {
        Restaurant r = m.getRestaurant();
        if(r == null) {
            return false;
        }
        Optional<Article> a = r.getArticles().stream().filter(ar -> ar.getName().equals(req.getName()) && !ar.isDeleted()).findFirst();
        if(!a.isPresent()) {
            return false;
        }

        a.get().setName(req.getName());
        a.get().setArticleType(req.getArticleType());
        a.get().setAmount(req.getAmount());
        a.get().setDescription(req.getDescription());
        a.get().setPrice(req.getPrice());
        return repository.update(r);
    }

    public boolean deleteArticleFromRestaurant(Manager m, String name) {
        Restaurant r = m.getRestaurant();
        if(m == null) {
            return false;
        }
        Optional<Article> a = r.getArticles().stream().filter(ar -> ar.getName().equals(name) && !ar.isDeleted()).findFirst();
        if(!a.isPresent()) {
            return false;
        }
        a.get().setDeleted(true);
        return repository.update(r);
    }

    public Optional<Restaurant> create(RestaurantRequest req, String filename) throws IOException {
        // Not specified how to set open/closed
        Restaurant r = new Restaurant(req.getName(), req.getType(), req.getAddress(), filename, ERestStatus.OPEN);
        r.setManagerUsername(req.getManagerUsername());
        Integer id = repository.getNextId();
        r.setId(id);
        if(!repository.create(r)) {
            return Optional.empty();
        }
        return Optional.of(r);
    }

    public List<Restaurant> search(String name, String type, String state, String city, String avgScoreString) {
        List<Restaurant> res = repository.getAll();
        if(name != null && !name.isBlank()) {
            res = res.stream().filter(r -> r.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        if(type != null && !type.isBlank()) {
            res = res.stream().filter(r -> r.getRestType().toString().toLowerCase().contains(type.toLowerCase())).collect(Collectors.toList());
        }
        if(state != null && !state.isBlank()) {
            res = res.stream().filter(r -> r.getAddress().getState().toLowerCase().contains(state.toLowerCase())).collect(Collectors.toList());
        }
        if(city != null && !city.isBlank()) {
            res = res.stream().filter(r -> r.getAddress().getCity().toLowerCase().contains(city.toLowerCase())).collect(Collectors.toList());
        }
        // All restaurants with equal or higher average scores
        if(avgScoreString != null && !avgScoreString.isBlank()){
            final double avgScore = Double.parseDouble(avgScoreString);
            res = res.stream().filter(r -> r.getAvgScore() >= avgScore).collect(Collectors.toList());
        }
        return res;
    }

    public Optional<Restaurant> getByName(String name) {
        if(name.isBlank()) {
            return Optional.empty();
        }
        return repository.getAll().stream().filter(r -> r.getName().equals(name)).findFirst();
    }

}
