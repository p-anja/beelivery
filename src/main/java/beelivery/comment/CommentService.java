package beelivery.comment;

import beelivery.comment.model.Comment;
import beelivery.comment.model.ECommentStatus;
import beelivery.comment.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentService {

    private CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public boolean create(String username, String restaurantName, String body, int rating, String userProfile) {
        Comment c = new Comment(username, restaurantName, body, rating, userProfile);
        c.setId(repository.getNextId());
        return repository.create(c);
    }

    public List<Comment> getByRestaurantName(String name) {
        return repository.getAll().stream().filter(c -> c.getRestaurantName().equals(name)
            && c.getStatus().equals(ECommentStatus.APPROVED) && !c.isDeleted()).collect(Collectors.toList());
    }

    public List<Comment> getOwnerComments(String name) {
        return repository.getAll().stream().filter(c -> c.getRestaurantName().equals(name)
            && !c.getStatus().equals(ECommentStatus.APPROVED)).collect(Collectors.toList());
    }

    public boolean approve(Integer id) {
        Optional<Comment> c = repository.get(id);
        if(!c.isPresent()) {
            return false;
        }
        c.get().setStatus(ECommentStatus.APPROVED);
        return repository.update(c.get());
    }
}
