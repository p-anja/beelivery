package beelivery.misc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static beelivery.misc.Responses.internal;
import static spark.Spark.get;

public class ImageController {

    private String uploadDir;

    public ImageController(String uploadDir) {
        this.uploadDir = uploadDir;
        get("image/:filename", (req, res) -> {
           String filename = req.params(":filename");
           try {
               return Files.readAllBytes(Paths.get(uploadDir + File.separator + filename));
           } catch (Exception e) {
               e.printStackTrace();
               return internal(res);
           }
        });
    }
}
