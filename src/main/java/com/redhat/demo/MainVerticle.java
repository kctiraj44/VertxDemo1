package com.redhat.demo;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> start) {



        vertx.deployVerticle(new HelloVerticle());
       Router router =  Router.router(vertx);

        //-------------------------------------------------------------------------------------------------------

       router.route().handler(ctx->{
           String authToken = ctx.request().getHeader("AUTH_TOKEN");
           if(authToken!=null && "mySuoerSeceretAuthToken".contentEquals(authToken)){
               ctx.next();                                                                               //--apple
           }
           else {
               ctx.response().setStatusCode(401).setStatusMessage("UNAUTHORIZED").end();
           }
       });
       //-------------------------------------------------------------------------------------------------------
       router.get("/api/v1/hello").handler(this::helloVertx);
     router.get("/api/v1/hello/:name").handler(this::helloName);

        //-------------------------------------------------------------------------------------------------------
        router.route().handler(StaticHandler.create("web"));                                              //--apple
        //-------------------------------------------------------------------------------------------------------


        ConfigStoreOptions defaultConfig = new ConfigStoreOptions()
                .setType("file")
                .setFormat("json")
                .setConfig(new JsonObject().put("path","config.json"));

        ConfigRetrieverOptions opts = new ConfigRetrieverOptions()
                .addStore(defaultConfig);

//        int httpPort;
//        try{
//            httpPort = Integer.parseInt(System.getProperty("http.port","9092"));
//        }
//        catch (NumberFormatException nfe){
//            httpPort =9092;
//        }

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx,opts);
        Handler<AsyncResult<JsonObject>> handler  =asyncResult -> this.handleConfigResults(start,router,asyncResult);
        configRetriever.getConfig(handler);

    }


    void handleConfigResults(Promise<Void> start,Router router ,  AsyncResult<JsonObject> asyncResult){
        if(asyncResult.succeeded()){
            JsonObject config =asyncResult.result();
            JsonObject http = config.getJsonObject("http");
            int httpPort = http.getInteger("port");
            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(httpPort);
            start.complete();
        }
        else {
            start.fail("Unable to load configurations");
        }

    }



    private void helloName(RoutingContext routingContext) {
        String  name = routingContext.pathParam("name");
        vertx.eventBus().request("hello.name.addr","name",reply->{
            routingContext.request().response().end((String) reply.result().body());
        });
    }

    private void helloVertx(RoutingContext ctx) {
       vertx.eventBus().request("hello.vertx.addr","",reply ->{
           ctx.request().response().end((String) reply.result().body());
       });
    }



}
