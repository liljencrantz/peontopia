package org.peontopia.simulation;

import org.peontopia.models.Actor;
import org.peontopia.models.Peon;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import java.util.HashMap;
import java.util.Map;

import static org.peontopia.simulation.actions.Action.combine;

/**
 * Class to keep state for the peon AI as well as wire together all different AI components into
 * a whole simulation.
 */
public class PeonSimulator implements ActorSimulator{

  private final Map<Long, Data> allData = new HashMap<>();
  private final Action.PeonActions actions;

  private static final int FOOD_HUNGRY = 60;

  public PeonSimulator(Action.PeonActions actions) {
    this.actions = actions;
  }

  public Action step(World world, Actor peon) {
    return combine(actions.age((Peon)peon), action(world, (Peon)peon));
  }

  public Action action(World world, Peon peon) {

    /* First check if we should be dead */
    if (peon.food() <= 0) {
      return actions.die(peon);
    }

    if (peon.rest() <= 0) {
      return actions.die(peon);
    }

    Data data = allData.getOrDefault(peon.id(), new Data());
    allData.put(peon.id(), data);

    int day = world.day();

    /* If we're hungry, eat */
    if (peon.food() < FOOD_HUNGRY) {
      return actions.eat(peon);
    }

    /* If we haven't slept today, go to sleep */
    if ( day != data.lastSleepDay) {
      data.lastSleepDay = day;
      return actions.sleep(peon);
    }


    /* If we haven't worked today, work */
    if ( day != data.lastWorkDay) {
      data.lastWorkDay = day;
      return actions.work(peon);
    }

    /* If we haven't done any chores today, do them */
    if ( day != data.lastChoresDay) {
      data.lastChoresDay = day;
      return actions.chores(peon);
    }

    /* Have some fun until it's time for bed */
    return actions.play(peon);
  }

  static class Data {
    int lastWorkDay = -1;
    int lastSleepDay = -1;
    int lastChoresDay = -1;

  }

}
