package com.peter.eventbus.customdoc;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {
  private static final Logger log = LoggerFactory.getLogger(PingPongExample.class);



  public static void main(String[] args) {
    Vertx vertx  = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), ErrorHandler());
    vertx.deployVerticle(new PongVerticle(), ErrorHandler());

  }

  private static Handler<AsyncResult<String>> ErrorHandler() {
    return ar -> {
      if (ar.failed()) {
        log.error("err", ar.cause());
      }

    };
  }


  static class PingVerticle extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(PingVerticle.class);
    public static final String ADDRESS =PingVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus =vertx.eventBus();
        final Ping message = new Ping("Hello",true);
         log.debug("Sending :{}",message);
         //Register only once
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request( ADDRESS ,message, reply->{
           if(reply.failed()){
             log.error("Failed: ",reply.cause());
             return;
           }
           log.debug("Response : {}", reply.result().body());
         });
      startPromise.complete();

    }
  }

  static class PongVerticle extends  AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().consumer(PingVerticle.ADDRESS, message ->{
        log.debug("Received message :{}",message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error->{
        log.error("Error :",error);
      });
    }
  }

}
