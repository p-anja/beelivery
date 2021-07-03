package beelivery;

import static spark.Spark.*;

import beelivery.misc.ImageController;
import beelivery.misc.RuntimeTypeAdapterFactory;
import beelivery.restaurant.controller.RestaurantController;
import beelivery.restaurant.repository.RestaurantRepository;
import beelivery.restaurant.service.RestaurantService;
import beelivery.user.controller.AdminController;
import beelivery.user.controller.ManagerController;
import beelivery.user.controller.UserController;
import beelivery.user.model.Admin;
import beelivery.user.model.Manager;
import beelivery.user.model.Regular;
import beelivery.user.model.User;
import beelivery.user.repository.UserRepository;
import beelivery.user.service.UserService;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.io.File;

public class Application {

    public static Gson gson;
    public static String UPLOAD_DIR = "uploads";

    public static Route serveStaticResource = (Request req, Response res) -> {
        res.redirect("/");
        return res;
    };
    public static void main(String[] args) {
        new File(UPLOAD_DIR).mkdir();

        RuntimeTypeAdapterFactory<User> userAdapterFactory = RuntimeTypeAdapterFactory.of(User.class)
                .registerSubtype(Regular.class)
                .registerSubtype(Admin.class)
                .registerSubtype(Manager.class);

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(userAdapterFactory)
                .create();
        port(8080);
        staticFiles.location("/static");
        get("/", serveStaticResource);

        ImageController imageController = new ImageController(UPLOAD_DIR);

        RestaurantRepository restaurantRepository = new RestaurantRepository("restaurants.json");
        RestaurantService restaurantService = new RestaurantService(restaurantRepository);
        RestaurantController restaurantController = new RestaurantController(restaurantService);

        UserRepository userRepository = new UserRepository("users.json");
        UserService userService = new UserService(userRepository, restaurantService);
        UserController userController = new UserController(userService);

        ManagerController managerController = new ManagerController(userService, restaurantService);
        AdminController adminController = new AdminController(userService, restaurantService);

    }
}
