package org.peontopia.models;

/**
 * Created by axel on 18/10/16.
 */
public class Building extends Actor {

  private int x;
  private int y;
  private int l;

  public Building(World w, int x, int y, int level) {
    super(w);
    this.x = x;
    this.y = y;
    l = level;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  public int level() {
    return 1;
  }
}
