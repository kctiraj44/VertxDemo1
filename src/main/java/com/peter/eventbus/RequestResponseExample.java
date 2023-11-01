package com.peter.eventbus;

import com.peter.workers.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseExample {

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
        final String message = "Hello world";
         EventBus eventBus =vertx.eventBus();
         log.debug("Sending :{}",message);
         eventBus.request( ADDRESS ,message, reply->{
           log.debug("Response : {}", reply.result().body());
         });

    }
  }

  static class ResponseVerticle extends  AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(RequestVerrticle.ADDRESS,message ->{
        log.debug("Received message :{}",message.body());
        message.reply("Received your message. Thanks");
      });
    }
  }

}
