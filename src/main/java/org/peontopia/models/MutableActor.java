package org.peontopia.models;

/**
 * Created by axel on 2016-11-10.
 */
public class MutableActor {
  private MutableWorld world;

  public MutableActor(MutableWorld world) {
    this.world = world;
  }

  public MutableWorld world() {
    return world;
  }
}
