package org.peontopia.models;

import java.util.Optional;

/**
 * Created by axel on 14/10/16.
 */
public interface Peon extends Actor {

  public static final int MAX_REST = World.TICKS_IN_DAY*3;
  public static final int MAX_FOOD = World.TICKS_IN_DAY*3;
  public static final int START_MONEY = 10000;

  String name();
  double happiness();
  int money();
  int rest();
  int food();
  int x();
  int y();
  Education education();

  Optional<? extends Company> employer();
  Tile tile();
}
