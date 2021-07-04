package beelivery;

import static spark.Spark.*;

import beelivery.misc.ImageController;
import beelivery.misc.LocalDateTimeDeserializer;
import beelivery.misc.LocalDateTimeSerializer;
import beelivery.misc.RuntimeTypeAdapterFactory;
import beelivery.order.repository.OrderRepository;
import beelivery.order.service.OrderService;
import beelivery.restaurant.controller.RestaurantController;
import beelivery.restaurant.repository.RestaurantRepository;
import beelivery.restaurant.service.RestaurantService;
import beelivery.user.controller.AdminController;
import beelivery.user.controller.ManagerController;
import beelivery.user.controller.UserController;
import beelivery.user.model.*;
import beelivery.user.repository.UserRepository;
import beelivery.user.service.UserService;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
                .registerSubtype(Manager.class)
                .registerSubtype(Delivery.class);

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(userAdapterFactory)
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        port(8080);
        staticFiles.location("/static");
        get("/", serveStaticResource);

        ImageController imageController = new ImageController(UPLOAD_DIR);

        OrderRepository orderRepository = new OrderRepository("orders.json");
        OrderService orderService = new OrderService(orderRepository);

        RestaurantRepository restaurantRepository = new RestaurantRepository("restaurants.json");
        RestaurantService restaurantService = new RestaurantService(restaurantRepository);
        RestaurantController restaurantController = new RestaurantController(restaurantService);

        UserRepository userRepository = new UserRepository("users.json");
        UserService userService = new UserService(userRepository, restaurantService, orderService);
        UserController userController = new UserController(userService);

        ManagerController managerController = new ManagerController(userService, restaurantService);
        AdminController adminController = new AdminController(userService, restaurantService);

    }
}
