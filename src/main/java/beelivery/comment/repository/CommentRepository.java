package beelivery.comment.repository;

import beelivery.comment.model.Comment;
import beelivery.misc.JSONRepository;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommentRepository extends JSONRepository<Comment, Integer> {
    public CommentRepository(String filename) {
        super(filename, new TypeToken<List<Comment>>(){}.getType());
    }

    public Integer getNextId() {
        return getAll().size() + 1;
    }
}
