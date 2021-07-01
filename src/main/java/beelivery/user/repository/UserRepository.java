package beelivery.user.repository;

import static beelivery.Application.gson;

import beelivery.misc.JSONRepository;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserRepository extends JSONRepository<User, String> {

    public UserRepository(String filename) {
        super(filename, User.class);
    }

}
