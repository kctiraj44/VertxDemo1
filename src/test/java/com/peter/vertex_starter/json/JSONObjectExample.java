package com.peter.vertex_starter.json;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONObjectExample {



  @Test
  void jsonObjectCanBeMapped(){

    final  JsonObject myJsonObject = new JsonObject();
    myJsonObject.put("id",1);
    myJsonObject.put("name", "Alice");
    myJsonObject.put("Love_vertx",true);
    final String encoded = myJsonObject.encode();

    assertEquals("{\"id\":1,\"name\":\"Alice\",\"Love_vertx\":true}",encoded);


    final JsonObject decodedJsonObject =   new JsonObject(encoded);
    assertEquals(myJsonObject, decodedJsonObject);



  }

  @Test
  void jsonObjectCanBeCreatedFromMap(){
    final Map<String ,Object> mymap = new HashMap<>();
    mymap.put("id",1);
    mymap.put("name", "Alice");
    mymap.put("Love_vertx",true);
     JsonObject asJsonObejct = new JsonObject(mymap);
     assertEquals(mymap,asJsonObejct.getMap());
     assertEquals(1,asJsonObejct.getInteger("id"));
     assertEquals("Alice", asJsonObejct.getString("name"));
     assertEquals(true,asJsonObejct.getBoolean("Love_vertx"));
  }

  @Test
  void JsonArrayCanBeMapped(){
    final JsonArray myJsonArray = new JsonArray();
    myJsonArray
      .add(new JsonObject().put("id",1))
      .add(new JsonObject().put("id",2))
      .add(new JsonObject().put("id",3))
      .add("ransomValue");

    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"ransomValue\"]",myJsonArray.encode());
  }


  @Test
  void  CanMapJavaObjects(){
    final Person person = new Person(1,"ALice",true);
   final JsonObject alice =  JsonObject.mapFrom(person);
    assertEquals(person.getId(),alice.getInteger("id"));
    assertEquals(person.getName(),alice.getString("name"));


    Person person1 = alice.mapTo(Person.class);
    assertEquals(person.getId(),person1.getId());
  }
}
