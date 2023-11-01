package com.peter.eventbus.customdoc;

public class Pong {

  private Integer id;

  public Pong(){

  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }

  public Pong(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
