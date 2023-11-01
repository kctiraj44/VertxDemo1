package com.peter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PublishSubscribe {


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscribe1());
    vertx.deployVerticle(new Subscribe2());
  }


  static class Publish extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(Publish.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(Duration.ofSeconds(3).toMillis(), id->
        vertx.eventBus().publish(Publish.class.getName(),"A message for everyone")
      );

    }
  }


  static class Subscribe1 extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(Subscribe1.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Publish.class.getName(), message ->{
        log.debug("Received : {}", message.body());
      });
    }
  }




  static class Subscribe2 extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(Subscribe2.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Publish.class.getName(), message ->{
        log.debug("Received : {}", message.body());
      });

    }
  }






}
