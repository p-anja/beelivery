package beelivery.user.repository;

import beelivery.misc.JSONRepository;
import beelivery.user.model.User;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class UserRepository extends JSONRepository<User, String> {

    public UserRepository(String filename) {
        super(filename, new TypeToken<List<User>>(){}.getType());
    }

}
