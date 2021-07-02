package beelivery;

import static spark.Spark.*;

import beelivery.misc.RuntimeTypeAdapterFactory;
import beelivery.user.controller.AdminController;
import beelivery.user.controller.UserController;
import beelivery.user.model.Admin;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.repository.UserRepository;
import beelivery.user.service.UserService;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class Application {

    public static Gson gson;

    public static Route serveStaticResource = (Request req, Response res) -> {
        res.redirect("/");
        return res;
    };
    public static void main(String[] args) {
        RuntimeTypeAdapterFactory<User> userAdapterFactory = RuntimeTypeAdapterFactory.of(User.class)
                .registerSubtype(Regular.class)
                .registerSubtype(Admin.class);

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(userAdapterFactory)
                .create();
        port(8080);
        staticFiles.location("/static");
        get("/", serveStaticResource);

        UserRepository userRepository = new UserRepository("regulars.json");
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        AdminController adminController = new AdminController(userService);

    }
}
