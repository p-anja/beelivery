package beelivery.user.service;

import beelivery.misc.JwtUtil;
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

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<String> login(LoginRequest req) {
        Optional<User> u = repository.get(req.getUsername());
        if (!u.isPresent() || !u.get().getPassword().equals(req.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(JwtUtil.generateJws(u.get()));
    }

    public boolean updateUser(RegisterRequest req) {
        Optional<User> u = getByUsername(req.getUsername());
        if (!u.isPresent()) {
            return false;
        }

        switch(u.get().getRole()) {
            case REGULAR: {
                Regular r = (Regular) u.get();
                r.setBirthDate(req.getBirthDate());
                r.setSex(req.getSex());
                r.setFirstName(req.getFirstName());
                r.setLastName(req.getLastName());
                r.setPassword(req.getPassword());
                return repository.update(r);
            }
        }
        return false;
    }

    public boolean registerUser(RegisterRequest req) {
        Optional<User> reg = repository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Regular newRegular = new Regular(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate(), 0, EType.BRONZE);
        return repository.create(newRegular);
    }

    public Optional<User> getByUsername(String username) {
        return repository.get(username);
    }

}
