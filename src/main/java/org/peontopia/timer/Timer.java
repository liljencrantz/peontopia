package org.peontopia.timer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by axel on 2016-11-04.
 */
public class Timer {

  private static final Map<String, Timer> timers = new HashMap<>();
  private long tm = 0;
  private final String name;

  private Timer(String name) {
    this.name = name;
  }

  public void time(Runnable r) {
    long t1 = System.nanoTime();
    r.run();
    tm += (System.nanoTime()-t1);
  }

  public static void print() {
    timers.values().forEach(timer -> System.err.println(timer));
  }

  @Override
  public String toString() {
    return String.format("Timer %s has used %.2f seconds of time", name, 0.000000001 * tm);
  }

  public static Timer timer(String name) {
    if (!timers.containsKey(name))
      timers.put(name, new Timer(name));
    return timers.get(name);
  }
}
