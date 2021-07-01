package beelivery.user.service;

import beelivery.user.dto.LoginRequest;
import beelivery.user.dto.RegisterRequest;
import beelivery.user.model.EType;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Optional;

public class UserService {
    private UserRepository repository;
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<String> login(LoginRequest req) {
        Optional<User> u = repository.get(req.getUsername());
        if (!u.isPresent() || !u.get().getPassword().equals(req.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(generateJws(u.get()));
    }

    public boolean registerUser(RegisterRequest req) {
        Optional<User> reg = repository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Regular newRegular = new Regular(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate(), 0, EType.BRONZE);
        return repository.create(newRegular);
    }

    private String generateJws(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .signWith(KEY)
                .compact();
    }
}
