package beelivery;

import static spark.Spark.*;

import beelivery.user.controller.RegularController;
import beelivery.user.repository.RegularRepository;
import beelivery.user.service.RegularService;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.time.LocalDate;

public class Application {

    public static Gson gson;

    public static Route serveStaticResource = (Request req, Response res) -> {
        res.redirect("/");
        return res;
    };
    public static void main(String[] args) {
        gson = new GsonBuilder().create();
        port(8080);
        staticFiles.location("/static");
        get("/", serveStaticResource);

        RegularRepository regularRepository = new RegularRepository("regulars.json");
        RegularService regularService = new RegularService(regularRepository);
        RegularController regularController = new RegularController(regularService);

    }
}
