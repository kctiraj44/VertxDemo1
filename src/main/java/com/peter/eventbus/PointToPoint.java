package com.peter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPoint {

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }


  static  class Sender extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(Sender.class);


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(3000,id ->{
        //send a message verry second
        vertx.eventBus().send(Sender.class.getName(),"Sending a message....");
      });

    }
  }

  static class Receiver extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(),message ->{
        log.debug("Received :{}",message.body());
      });
    }

  }




}
