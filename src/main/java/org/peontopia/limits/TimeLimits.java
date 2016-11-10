package org.peontopia.limits;

/**
 * Created by axel on 2016-11-10.
 */
public class TimeLimits {
  public static final int TICKS_IN_DAY = 24*6;
  public static final int DAYS_IN_MONTH = 4;
  public static final int MONTHS_IN_YEAR = 12;

  public static int month(long tick) {
    long day = tick/TICKS_IN_DAY;
    return (int)(day/DAYS_IN_MONTH)%MONTHS_IN_YEAR;
  }

  public static int year(long tick) {
    long day = tick/TICKS_IN_DAY;
    return (int)(day/DAYS_IN_MONTH)/MONTHS_IN_YEAR;
  }
}
