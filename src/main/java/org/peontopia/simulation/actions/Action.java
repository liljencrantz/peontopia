package org.peontopia.simulation.actions;

import org.peontopia.models.Factory;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.Peon;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Lists.newArrayList;

/**
 * These specific actions, like moving from A to B, buying some goods,
 * performing some work, etc are the basic building blocks that make up
 * the game AI.
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

  /**
   * Create single action object that executes multiple actions in a single step.
   * Warning: All actions are executed every time, even ones that have previously returned true.
   * Returns true once all actions return true.
   */
  static Action combine(Action... actions) {
    List<Action> all = newArrayList(actions);
    return world -> {
      boolean done = true;
      for (Action a: all)
        done &= a.apply(world);
      return done;
    };
  }

  /**
   * Create an action object that will do nothing for the specified number of steps and then begin
   * performing the specified action.
   */
  static Action delay(Action action, int delayBy) {
    if (delayBy < 0)
      throw new IllegalArgumentException();
    AtomicInteger count = new AtomicInteger();
    return world -> {
      if (count.get() == delayBy) {
        return action.apply(world);
      }
      count.addAndGet(1);
      return false;
    };
  }

  /**
   * The building blocks for building the Peon AI
   */
  class PeonActions {

    /* Basic passage of time. Become more tired, more hungry, etc. */
    public Action age(Peon orig) {
      return world -> {
        MutableWorld.MutablePeon peon = world.peon(orig.id());
        peon.addRest(-1).addFood(-1);
        return true;
      };
    }

    public Action move(Peon p, int dx, int dy) {
      return new PeonMove(p.id(), dx, dy, true);
    }

    public Action setCoord(Peon p, int x, int y) {
      return new PeonMove(p.id(), x, y, false);
    }

    /**
     * End the life of this poor soul.
     *
     * @param p the peon that should perform the action
     * @return true
     */
    public Action die(Peon p) {
      return world -> { world.removePeon(p.id()); return true;};
    }

    /**
     * Sleep until fully rested
     * @param p the peon that should perform the action
     * @return true if this peon is fully rested and this action need not be applied again
     */
    public Action sleep(Peon p) {
      return world -> world.peon(p.id()).addRest(3).rest() == Peon.MAX_REST;
    }

    /**
     * Eat until either full or no more food left
     * @param p the peon that should perform the action
     * @return true if this peon cannot eat any more
     */
    public Action eat(Peon p) {
      return world -> world.peon(p.id()).addFood(10).food() == Peon.MAX_FOOD;
    }

    /**
     * Wrapper for PeonWork
     *
     * @param p
     * @return
     */
    public Action work(Peon p) {
      return new PeonWork(p.id());
    }

    public Action chores(Peon p) {
      return world -> true;
    }

    public Action play(Peon p) {
      return world -> true;
    }
  }

  class FactoryActions {

    public Action bankrupt(Factory f) {
      return world -> {world.removeFactory(f.id()); return true;};
    }
  }
}
