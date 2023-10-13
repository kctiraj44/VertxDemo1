package com.redhat.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVericle1  extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        Vertx vertx = Vertx.vertx();

        Router router =Router.router(vertx);
        router.get("/api/v1/hello").handler(this::getHello);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(9022);
    }

    private void getHello(RoutingContext routingContext) {
        routingContext
                .request()
                .response()
                .end("This is from the Hello world");
    }



}

