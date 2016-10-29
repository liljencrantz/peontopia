package org.peontopia.simulation.actions;

import org.peontopia.models.Factory;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.Peon;
import org.peontopia.models.Resource;
import org.peontopia.simulation.MarketSimulator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;

/**
 * These specific actions, like moving from A to B, buying some goods,
 * performing some work, etc are the basic building blocks that make up
 * the game AI.
 */
public interface Action {
  boolean apply();

  /**
   * Create single action object that executes multiple actions in order
   */
  static Action compose(Action... actions) {
    return compose(newArrayList(actions));
  }

  static Action compose(List<Action> actions) {
    if (actions.isEmpty())
      throw new IllegalArgumentException();
    List<Action> a = newArrayList(actions);
    return () -> {
      if (a.get(0).apply()) {
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
    return () -> {
      boolean done = true;
      for (Action a: all)
        done &= a.apply();
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
    return () -> {
      if (count.get() == delayBy) {
        return action.apply();
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
    public Action age(MutableWorld.MutablePeon peon) {
      return () -> {
        peon.addRest(-1).addFood(-1);
        return true;
      };
    }

    public Action move(MutableWorld.MutablePeon p, int dx, int dy) {
      return new PeonMove(p, dx, dy, true);
    }

    public Action setCoord(MutableWorld.MutablePeon p, int x, int y) {
      return new PeonMove(p, x, y, false);
    }

    /**
     * End the life of this poor soul.
     *
     * @param peon the peon that should perform the action
     * @return true
     */
    public Action die(MutableWorld.MutablePeon peon) {
      return () -> { peon.remove(); return true;};
    }

    /**
     * Sleep until fully rested
     * @param peon the peon that should perform the action
     * @return true if this peon is fully rested and this action need not be applied again
     */
    public Action sleep(MutableWorld.MutablePeon peon) {
      return () -> peon.addRest(3).rest() == Peon.MAX_REST;
    }

    /**
     * Eat until either full or no more food left
     * @param peon the peon that should perform the action
     * @return true if this peon cannot eat any more
     */
    public Action eat(MutableWorld.MutablePeon peon) {
      return () -> {
        double price = 3;
        if(peon.money() < price)
          return true;
        peon.addMoney((int)-price);
        System.err.println("MONEY " + peon.money());
        return peon.addFood(10).food() == Peon.MAX_FOOD;
      };
    }

    /**
     * Wrapper for PeonWork
     *
     * @param p
     * @return
     */
    public Action work(MutableWorld.MutablePeon p) {
      return new PeonWork(p);
    }

    public Action chores(Peon p) {
      return () -> true;
    }

    public Action play(Peon p) {
      return () -> true;
    }
  }

  class FactoryActions {

    public Action bankrupt(MutableWorld.MutableFactory f) {
      return () -> {f.remove(); return true;};
    }

    public Action purchase(MarketSimulator market, MutableWorld.MutableFactory factory, Resource r, double amount){
      checkState(amount > 0);
      return () -> {
        double price = market.buyingPrice(r)*amount;
        factory.addMoney(-(int)Math.ceil(price));
        factory.addToSupply(r, amount);
        return true;
      };
    }

    public Action sell(MarketSimulator market, MutableWorld.MutableFactory factory, double amount) {
      checkState(amount > 0);
      return () -> {
        double price = market.sellingPrice(factory.resource())*amount;
        factory.addMoney((int)Math.ceil(price));
        factory.addToSupply(factory.resource(), -amount);
        return true;
      };
    }
  }
}
