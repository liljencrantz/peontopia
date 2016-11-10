package org.peontopia.simulation;

import org.peontopia.Game;
import org.peontopia.models.Peon;
import org.peontopia.simulation.actions.Action;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.peontopia.simulation.actions.Action.action;

/**
 * Class to keep state for the peon AI as well as wire together all different AI components into
 * a whole simulation.
 */
public class PeonSimulator {

  private final Map<Long, Data> allData = new HashMap<>();
  private final Action.PeonActions actions;

  private static final int FOOD_HUNGRY = 60;

  public PeonSimulator(Action.PeonActions actions) {
    this.actions = actions;
  }

  public void simulate(Peon p, Game g) {
    g.scheduler().schedule(action(() -> step(p)));
  }

  private Action step(Peon peon) {

    //System.err.println("Step");
    //  System.err.println("Non-fatal step");
    if (!allData.containsKey(peon.id())) {
      //System.err.println("New peon!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      allData.put(peon.id(), new Data(peon.world().time()));
    }
    Data data = allData.get(peon.id());

    age(peon, data);

    /* First check if we should be dead */
    if (peon.food() <= 0) {
      System.err.println("Die");
      return actions.die(peon);
    }

    if (peon.rest() <= 0) {
      System.err.println("Die");
      return actions.die(peon);
    }

    return Action.then(nonFatalStep(peon, data), action((() -> step(peon))));
  }

  private void age(Peon peon, Data data) {
    int diff = (int)(peon.world().time() - data.lastEvalTick);
    peon.addFood(-diff);
    peon.addRest(-diff);
    data.lastEvalTick = peon.world().time();
  }

  private Action nonFatalStep(Peon peon, Data data) {

    int day = peon.world().day();
//    System.err.println("Day " + day + " tick " + peon.world().time());

    /* If we're hungry, eat */
    if (peon.food() < FOOD_HUNGRY) {
      //System.err.println("Eat");
      return actions.eat(peon);
    }

    /* If we haven't slept today, go to sleep */
    if ( day != data.lastSleepDay) {
//      System.err.println(format("Sleep, previous sleep was on %d, today is day %d", data.lastSleepDay, day));
      data.lastSleepDay = day;
      return actions.sleep(peon);
    }


    /* If we haven't worked today, work */
    if ( day != data.lastWorkDay) {
  //    System.err.println("Work");
      data.lastWorkDay = day;
      return actions.work(peon);
    }

    /* If we haven't done any chores today, do them */
    if ( day != data.lastChoresDay) {
    //  System.err.println("Chores");
      data.lastChoresDay = day;
      return actions.chores(peon);
    }

    //System.err.println("Play");
    /* Have some fun until it's time for bed */
    return actions.play(peon);
  }

  static class Data {
    int lastWorkDay = -1;
    int lastSleepDay = -1;
    int lastChoresDay = -1;
    long lastEvalTick;

    Data(long lastEvalTick) {
      this.lastEvalTick = lastEvalTick;
    }
  }

}
