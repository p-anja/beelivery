package beelivery;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class Application {

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
