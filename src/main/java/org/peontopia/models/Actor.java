package org.peontopia.models;

/**
 * Created by axel on 2016-11-10.
 */
public class Actor {


  private final World world;
  private final long id;

  public Actor(World world) {
    this.world = world;
    id = world.nextId();
  }

  public World world() {
    return world;
  }

  long id(){
    return id;
  }
}
