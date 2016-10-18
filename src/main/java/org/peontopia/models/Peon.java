package org.peontopia.models;

/**
 * Created by axel on 14/10/16.
 */
public interface Peon extends Actor {

  public static final int MAX_REST = 100;
  public static final int MAX_FOOD = 100;
  public static final int START_MONEY = 10000;

  String name();
  double happiness();
  int money();
  int rest();
  int food();
  int x();
  int y();
  Tile tile();
}
