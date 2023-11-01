package com.peter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class MainVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);




  @Override
  public void start(Promise<Void> promise) {
    vertx
      .createHttpServer()
      .requestHandler(r -> {
        r.response().end("<h1>Hello from my first " +
          "Vert.x 3 application</h1>");
      })
      .listen(8888, result -> {
        if (result.succeeded()) {
         promise.complete(); ;
         log.info("HTTP server started on port  88888");
        } else {
          promise.fail(result.cause());
        }
      });

    vertx.setPeriodic(100, id->log.info(String.valueOf(new Random().nextDouble())));
  }
}
