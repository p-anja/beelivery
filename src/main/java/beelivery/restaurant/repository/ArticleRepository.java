package beelivery.restaurant.repository;

import beelivery.misc.JSONRepository;
import beelivery.restaurant.model.Article;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class ArticleRepository extends JSONRepository<Article, Integer> {
    public ArticleRepository(String filename) {
        super(filename, new TypeToken<List<Article>>(){}.getType());
    }

    public Integer getNextId() throws JsonSyntaxException, IOException {
        return getAll().size() + 1;
    }
}
