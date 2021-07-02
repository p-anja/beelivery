package beelivery.user.service;

import beelivery.misc.JwtUtil;
import beelivery.user.dto.LoginRequest;
import beelivery.user.dto.RegisterRequest;
import beelivery.user.model.*;
import beelivery.user.repository.UserRepository;
import spark.Request;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static beelivery.misc.Responses.badRequest;
import static beelivery.misc.Responses.forbidden;

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

    public Optional<User> validateJWS(Request req) {
        Optional<String> jws = JwtUtil.parseJws(req);
        if(!jws.isPresent()) {
            return Optional.empty();
        }
        String username = JwtUtil.getUsername(jws.get());
        Optional<User> u = getByUsername(username);
        return u;
    }

    public Optional<User> validateJWS(Request req, ERole role) {
        Optional<String> jws = JwtUtil.parseJws(req);
        if(!jws.isPresent()) {
            return Optional.empty();
        }
        String username = JwtUtil.getUsername(jws.get());
        Optional<User> u = getByUsername(username);
        if(!u.isPresent() || !u.get().getRole().equals(role)) {
            return Optional.empty();
        }
        return u;
    }

    public boolean registerUser(RegisterRequest req) {
        Optional<User> reg = repository.get(req.getUsername());
        if (reg.isPresent()) {
            return false;
        }
        Regular newRegular = new Regular(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate(), 0, EMemberType.BRONZE);
        return repository.create(newRegular);
    }

    public boolean registerManager(RegisterRequest req) {
        if(getByUsername(req.getUsername()).isPresent()) {
            return false;
        }
        Manager manager = new Manager(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate());
        return repository.create(manager);
    }

    public Optional<User> getByUsername(String username) {
        return repository.get(username);
    }

    public List<User> search(String username, String fname, String lname) {
        List<User> res = getAll();
        if(!username.isBlank()) {
            res = res.stream().filter(u -> u.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(!fname.isBlank()) {
            res = res.stream().filter(u -> u.getFirstName().toLowerCase().contains(fname.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if(!lname.isBlank()) {
            res = res.stream().filter(u -> u.getLastName().toLowerCase().contains(lname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return res;
    }

    public List<Manager> getRestaurantlessManagers() {
        return getManagers().stream().filter(m -> m.getRestaurant() == null)
                .collect(Collectors.toList());
    }

    public List<Manager> getManagers() {
        return getAll().stream().filter(u -> u.getRole().equals(ERole.MANAGER)  && u instanceof Manager)
            .map(u -> (Manager) u)
            .collect(Collectors.toList());
    }

    public List<User> getAll() {
        return repository.getAll();
    }

}
