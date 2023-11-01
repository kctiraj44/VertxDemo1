package com.peter.vertex_starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleN extends AbstractVerticle {


  private static final Logger LOG = LoggerFactory.getLogger(VerticleB.class);


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug(" Start {}: ", getClass().getName());
    startPromise.complete();
}
}
