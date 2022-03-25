package com.thegreatapi.vertx_download_file_demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

public class MainVerticle extends AbstractVerticle {

    private static System.Logger LOGGER = System.getLogger(MainVerticle.class.getName());

    private static final String FILE_NAME = "/dummy.pdf";

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.createHttpServer().requestHandler(req -> {
            try (InputStream file = getClass().getResourceAsStream(FILE_NAME)) {
                if (file == null) {
                    LOGGER.log(ERROR, "File not found");
                    req.response().setStatusCode(404).end();
                } else {
                    req.response()
                            .putHeader("Content-Disposition", "inline; filename=\"dummy.pdf\"")
                            .putHeader("Content-Type", "application/octet-stream")
                            .end(Buffer.buffer(file.readAllBytes()));
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
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
