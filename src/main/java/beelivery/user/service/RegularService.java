package beelivery.user.service;

import beelivery.user.dto.RegisterRequest;
import beelivery.user.model.EType;
import beelivery.user.model.Regular;
import beelivery.user.repository.RegularRepository;

public class RegularService {
    private RegularRepository repository;

    public RegularService(RegularRepository repository) {
        this.repository = repository;
    }

    public boolean registerUser(RegisterRequest req) {
        Regular reg = repository.get(req.getUsername());
        if (reg != null) {
            return false;
        }
        Regular newRegular = new Regular(req.getUsername(), req.getPassword(), req.getFirstName(), req.getLastName(), req.getSex(), req.getBirthDate(), 0, EType.BRONZE);
        return repository.create(newRegular);
    }
}
