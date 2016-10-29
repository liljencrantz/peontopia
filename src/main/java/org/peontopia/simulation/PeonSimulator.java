package org.peontopia.simulation;

import org.peontopia.models.Actor;
import org.peontopia.models.MutableWorld;
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

  @Override
  public Action step(Actor peon) {
    return combine(actions.age((MutableWorld.MutablePeon) peon), action((MutableWorld.MutablePeon) peon));
  }

  public Action action(MutableWorld.MutablePeon peon) {


    /* First check if we should be dead */
    if (peon.food() <= 0) {
      System.err.println("Die");
      return actions.die(peon);
    }

    if (peon.rest() <= 0) {
      System.err.println("Die");
      return actions.die(peon);
    }

    Data data = allData.getOrDefault(peon.id(), new Data());
    allData.put(peon.id(), data);

    int day = peon.world().day();
//    System.err.println("Day " + day + " tick " + peon.world().time());

    /* If we're hungry, eat */
    if (peon.food() < FOOD_HUNGRY) {
//      System.err.println("Eat");
      return actions.eat(peon);
    }

    /* If we haven't slept today, go to sleep */
    if ( day != data.lastSleepDay) {
//      System.err.println("Sleep");
      data.lastSleepDay = day;
      return actions.sleep(peon);
    }


    /* If we haven't worked today, work */
    if ( day != data.lastWorkDay) {
//      System.err.println("Work");
      data.lastWorkDay = day;
      return actions.work(peon);
    }

    /* If we haven't done any chores today, do them */
    if ( day != data.lastChoresDay) {
//      System.err.println("Chores");
      data.lastChoresDay = day;
      return actions.chores(peon);
    }

//    System.err.println("Play");
    /* Have some fun until it's time for bed */
    return actions.play(peon);
  }

  static class Data {
    int lastWorkDay = -1;
    int lastSleepDay = -1;
    int lastChoresDay = -1;

  }

}
