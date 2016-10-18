package org.peontopia.simulation;

import org.peontopia.models.Peon;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import java.util.HashMap;
import java.util.Map;

import static org.peontopia.simulation.actions.Action.PeonActions.*;

/**
 * Created by axel on 15/10/16.
 */
public class PeonSimulator {

  Map<Long, Data> allData = new HashMap<>();

  private static final int FOOD_HUNGRY = 60;

  public Action step(World world, Peon peon) {

    /* First check if we should be dead */
    if (peon.food() <= 0) {
      return die(peon);
    }

    if (peon.rest() <= 0) {
      return die(peon);
    }

    Data data = allData.getOrDefault(peon.id(), new Data());
    allData.put(peon.id(), data);

    int day = world.day();

    /* If we haven't slept today, go to sleep */
    if ( day != data.lastSleepDay) {
      data.lastSleepDay = day;
      return sleep(peon);
    }

    /* If we're hungry, eat */
    if (peon.food() < FOOD_HUNGRY) {
      return eat(peon);
    }

    /* If we haven't worked today, work */
    if ( day != data.lastWorkDay) {
      data.lastWorkDay = day;
      return work(peon);
    }

    /* Have some fun until it's time for bed */
    return play(peon);
  }

  static class Data {
    int lastWorkDay = -1;
    int lastSleepDay = -1;
  }

}
