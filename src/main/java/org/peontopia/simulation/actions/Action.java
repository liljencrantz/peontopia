package org.peontopia.simulation.actions;

import org.peontopia.models.MutableWorld;
import org.peontopia.models.Peon;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by axel on 15/10/16.
 */
public interface Action {
  boolean apply(MutableWorld world);

  /**
   * Create single action object that executes multiple actions in order
   */
  static Action compose(Action... actions) {
    List<Action> a = newArrayList(actions);
    return world -> {
      if (a.get(0).apply(world)) {
        a.remove(0);
      }
      return a.size() == 0;
    };
  }

  class PeonActions {

    public static Action move(Peon p, int dx, int dy) {
      return new PeonMove(p.id(), dx, dy, true);
    }

    public static Action setCoord(Peon p, int x, int y) {
      return new PeonMove(p.id(), x, y, false);
    }

    /**
     * End the life of this poor soul.
     *
     * @param p the peon that should perform the action
     * @return true
     */
    public static Action die(Peon p) {
      return world -> { world.removePeon(p.id()); return true;};
    }

    /**
     * Sleep until fully rested
     * @param p the peon that should perform the action
     * @return true if this peon is fully rested and this action need not be applied again
     */
    public static Action sleep(Peon p) {
      return world -> world.peon(p.id()).addRest(3).rest() == Peon.MAX_REST;
    }

    /**
     * Eat until either full or no more food left
     * @param p the peon that should perform the action
     * @return true if this peon cannot eat any more
     */
    public static Action eat(Peon p) {
      return world -> world.peon(p.id()).addFood(10).food() == Peon.MAX_FOOD;
    }

    /**
     * Wrapper for PeonWork
     *
     * @param p
     * @return
     */
    public static Action work(Peon p) {
      return new PeonWork(p.id());
    }

    public static Action play(Peon p) {
      return world -> true;
    }
  }
}
