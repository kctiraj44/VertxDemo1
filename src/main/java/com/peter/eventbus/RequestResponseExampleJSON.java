package com.peter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExampleJSON {

  public static void main(String[] args) {
    Vertx vertx  = Vertx.vertx();
    vertx.deployVerticle(new RequestVerrticle());
    vertx.deployVerticle(new ResponseVerticle());

  }


  static class RequestVerrticle extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(RequestVerrticle.class);
    public static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        startPromise.complete();
        final var message = new JsonObject()
          .put("message","Hello world")
          .put("version","1");
         EventBus eventBus =vertx.eventBus();
         log.debug("Sending :{}",message);
         eventBus.<JsonArray>request( ADDRESS ,message, reply->{
           log.debug("Response : {}", reply.result().body());
         });

    }
  }

  static class ResponseVerticle extends  AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<JsonObject>consumer(RequestVerrticle.ADDRESS, message ->{
        log.debug("Received message :{}",message.body());
        message.reply(new JsonArray().add("One").add("Two").add("Three"));
      });
    }
  }

}
