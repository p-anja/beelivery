package beelivery.misc;

import spark.Response;

public class Responses {

    public static Response ok(String message, Response res) {
        res.body(message);
        res.status(200);
        return res;
    }

    public static Response badRequest(String message, Response res) {
        res.body(message);
        res.status(400);
        return res;
    }

    public static Response forbidden(Response res) {
        res.body("Forbidden");
        res.status(403);
        return res;
    }

    public static Response notFound(Response res) {
        res.body("Not found");
        res.status(404);
        return res;
    }

    public static Response internal(Response res) {
        res.body("Internal server error");
        res.status(500);
        return res;
    }
}
