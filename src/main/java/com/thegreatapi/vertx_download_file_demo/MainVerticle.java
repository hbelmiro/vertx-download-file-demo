package com.thegreatapi.vertx_download_file_demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.net.URL;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

public class MainVerticle extends AbstractVerticle {

    private static final System.Logger LOGGER = System.getLogger(MainVerticle.class.getName());

    private static final String FILE_NAME = "/dummy.pdf";

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.createHttpServer().requestHandler(req -> {
            URL file = getClass().getResource(FILE_NAME);
            if (file == null) {
                LOGGER.log(ERROR, "File not found");
                req.response().setStatusCode(404).end();
            } else {
                req.response()
                        .putHeader("Content-Disposition", "inline; filename=\"dummy.pdf\"")
                        .putHeader("Content-Type", "application/octet-stream") // force download (instead of open in browser)
                        .sendFile(file.getPath());
            }
        }).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                LOGGER.log(INFO, "HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }
}
