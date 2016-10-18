package org.peontopia.models;

import java.util.Collection;
import java.util.List;

public interface World {

  int TICKS_IN_DAY = 24*6;

  Peon peon(long id);
  Store store(long id);
  Factory factory(long id);

  long time();
  default int timeOfDay() {
    return (int)(time() % TICKS_IN_DAY);
  }
  default int day() {
    return (int)(time() / TICKS_IN_DAY);
  }

  Collection<? extends Actor> actors();
/*  Collection<? extends Peon> peons();
  Collection<? extends Store> companies();
  Collection<? extends Factory> buildings();
*/
  int width();
  int height();
  List<? extends Tile> tiles();

  Tile tile(int x, int y);
}
