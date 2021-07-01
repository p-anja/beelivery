package beelivery;

import static spark.Spark.*;

import beelivery.user.model.ESex;
import beelivery.user.model.EType;
import beelivery.user.model.Regular;
import beelivery.user.repository.RegularRepository;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.time.LocalDateTime;

public class Application {

    public static Gson gson = new Gson();
    public static RegularRepository regularRepository = new RegularRepository("regulars.json");

    public static Route serveStaticResource = (Request req, Response res) -> {
        res.redirect("/");
        return res;
    };
    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/static");

        get("/", serveStaticResource);
        get("hello", (req, res) -> "WASSAP");

    }
}
