package com.peter.vertex_starter;

import com.peter.eventbus.customdoc.PingPongExample;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {

  private static final Logger log = LoggerFactory.getLogger(FuturePromiseExample.class);


  @Test
  void promise_success(Vertx vertx, VertxTestContext context){

    Promise<String> promise = Promise.promise();
    log.debug("Start");
    vertx.setTimer(500,id->{
      promise.complete("Success");
      log.debug("Success");
      context.completeNow();
    });
    log.debug("End");

  }

  @Test
  void promise_Failure(Vertx vertx, VertxTestContext context){
    Promise<String> promise = Promise.promise();
    log.debug("Start");
    vertx.setTimer(500,id->{
      promise.fail(new RuntimeException("Failed"));
      log.debug("Failed");
      context.completeNow();
    });
    log.debug("End");
  }


  @Test
  void future_success(Vertx vertx, VertxTestContext context){

    Promise<String> promise = Promise.promise();
    log.debug("Start");
    vertx.setTimer(500,id->{
      promise.complete("Success1");
      log.debug("Timer done");
      context.completeNow();
    });

    final Future<String> future = promise.future();
    future
      .onSuccess(result->{
      log.debug("result :{}",result);
      context.completeNow();
    })
      .onFailure(context::failNow)
    ;
  }

@Test
  void future_map(Vertx vertx, VertxTestContext context){

    Promise<String> promise = Promise.promise();
    log.debug("Start");
    vertx.setTimer(500,id->{
      promise.complete("Success1");
      log.debug("Timer done");

    });

    final Future<String> future = promise.future();
    future
      .map(asString ->{
        log.debug("Map string to jsonObject");
        return new JsonObject().put("key",asString);
      })
      .mapEmpty()
      .map(jsonObject -> new JsonArray().add(jsonObject))
      .onSuccess(result->{
        log.debug("result :{}",result);
        context.completeNow();
      })
      .onFailure(context::failNow)
    ;
  }


  @Test
  void futute_c0rrodintiomn(Vertx vertx, VertxTestContext context){

    vertx.createHttpServer()
      .requestHandler(request->log.debug("{}",request))
      .listen(100_00)
      .compose(httpServer -> {
        log.info("ANother task");
        return Future.succeededFuture(httpServer);
      })
      .compose(httpServer -> {
        log.info("Even more");
        return Future.succeededFuture(httpServer);
      })

      .onFailure(context::failNow)
      .onSuccess(server ->{
        log.debug("Server started on port {}",server.actualPort());
        context.completeNow();
      })

    ;
  }





}
