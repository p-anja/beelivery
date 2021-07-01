package beelivery.user.service;

import beelivery.user.dto.RegisterRequest;
import beelivery.user.model.EType;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.repository.UserRepository;

public class RegularService {
    private UserRepository repository;

    public RegularService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean registerUser(RegisterRequest req) {
        User reg = repository.get(req.getUsername());
        if (reg != null) {
            return false;
        }
        Regular newRegular = new Regular(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate(), 0, EType.BRONZE);
        return repository.create(newRegular);
    }
}
