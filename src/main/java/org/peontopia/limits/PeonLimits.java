package org.peontopia.limits;

/**
 * Created by axel on 26/10/16.
 */
public class PeonLimits {
  public static final int MAX_REST = TimeLimits.TICKS_IN_DAY * 3;
  public static final int MAX_FOOD = TimeLimits.TICKS_IN_DAY * 3;

  public static final int START_MONEY = 10000;
  public static int WORK_TICKS_IN_DAY = TimeLimits.TICKS_IN_DAY / 3;

  public static double EAT_AMOUNT_PER_TICK = 0.01;
}
