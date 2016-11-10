package org.peontopia.simulation.actions;

import org.peontopia.limits.PeonLimits;
import org.peontopia.limits.TimeLimits;
import org.peontopia.models.Factory;
import org.peontopia.models.Peon;
import org.peontopia.models.Resource;
import org.peontopia.simulation.MarketSimulator;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;

/**
 * These specific actions, like moving from A to B, buying some goods,
 * performing some work, etc are the basic building blocks that make up
 * the game AI.
 */
public interface Action {

  int delay();
  Optional<Action> run();

  static Action once(Runnable r) {
    return new SimpleAction(() -> {r.run(); return Optional.empty();}, 0);
  }

  static Action once(Runnable r, int steps) {
    return new SimpleAction(() -> {r.run(); return Optional.empty();}, steps);
  }

  static Action action(Supplier<Action> r) {
    return new SimpleAction(() -> Optional.of(r.get()), 0);
  }

  static Action action(Supplier<Action> r, int steps) {
    return new SimpleAction(() -> Optional.of(r.get()), steps);
  }

  static Action maybe(Supplier<Optional<Action>> r) {
    return new SimpleAction(r, 0);
  }

  static Action maybe(Supplier<Optional<Action>> r, int steps) {
    return new SimpleAction(r, steps);
  }

  /**
   * Create single action object that executes multiple actions in order
   */
  static Action then(Action first, Action second) {
    return new CombinedAction(first, second);
  }

  static Action noop() {
    return noop(0);
  }

  static Action noop(int steps) {
    return once(() -> {
    }, steps);
  }

  /**
   * Create single action object that executes multiple actions in a single step.
   * Warning: All actions are executed every time, even ones that have previously returned true.
   * Returns true once all actions return true.
   */
/*  static Action combine(Action... actions) {
    List<Action> all = newArrayList(actions);
    return () -> {
      boolean done = true;
      for (Action a: all)
        done &= a.apply();
      return done;
    };
  }
*/
  /**
   * Create an action object that will do nothing for the specified number of steps and then begin
   * performing the specified action.
   */
/*  static Action delay(Action action, int delayBy) {
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
*/
  /**
   * The building blocks for building the Peon AI
   */
  class PeonActions {

    private Random random = new Random();

    /* Basic passage of time. Become more tired, more hungry, etc. */
    public Action age(Peon peon) {
      return once(() -> peon.addRest(-1).addFood(-1));
    }

    public Action move(Peon p, int dx, int dy) {
      return PeonMove.move(p, dx, dy, true);
    }

    public Action setCoord(Peon p, int x, int y) {
      return PeonMove.move(p, x, y, false);
    }

    /**
     * End the life of this poor soul.
     *
     * @param peon the peon that should perform the action
     * @return true
     */
    public Action die(Peon peon) {
      return once(() -> peon.remove());
    }

    /**
     * Sleep until fully rested
     * @param peon the peon that should perform the action
     * @return true if this peon is fully rested and this action need not be applied again
     */
    public Action sleep(Peon peon) {
      final int SLEEP_REST_TICK = 3;
      int steps = (PeonLimits.MAX_REST-peon.rest())/SLEEP_REST_TICK + 1 + random.nextInt(5);
      System.out.println(format("Sleep for %d ticks in day %d", steps, peon.world().day()));
      return once(() -> peon.rest(PeonLimits.MAX_REST), steps);
    }

    /**
     * Eat until either full or no more food left
     * @param peon the peon that should perform the action
     * @return true if this peon cannot eat any more
     */
    public Action eat(Peon peon) {
        double price = 3;
        int EAT_SPEED = 10;
        int time = + random.nextInt(2) + peon.food()/EAT_SPEED;

        if(peon.money() < price)
          return noop();

//        System.out.println("Eat for " + time + " ticks");
        return once(() -> peon.addMoney((int) -price).food(PeonLimits.MAX_FOOD), time);
    }

    /**
     * Wrapper for PeonWork
     *
     * @param p
     * @return
     */
    public Action work(Peon p) {
      return PeonWork.create(p);
    }

    public Action chores(Peon p) {
      int time = TimeLimits.TICKS_IN_DAY/10;
  //    System.out.println("Chores for  " + time + " ticks");
      return noop(time);
    }

    public Action play(Peon p) {
      int time = TimeLimits.TICKS_IN_DAY/10;
//      System.out.println("Play for  " + time + " ticks");
      return noop(time);
    }
  }

  class FactoryActions {

    public Action bankrupt(Factory f) {
      return once(() -> f.remove());
    }

    public Action purchase(MarketSimulator market, Factory factory, Resource r, double amount){
      checkState(amount > 0);
      return once(() -> {
        double price = market.buyingPrice(r) * amount;
        factory.addMoney(-(int) Math.ceil(price));
        factory.addToSupply(r, amount);
      });
    }

    public Action sell(MarketSimulator market, Factory factory, double amount) {
      checkState(amount > 0);
      return once(() -> {
        double price = market.sellingPrice(factory.resource()) * amount;
        factory.addMoney((int) Math.ceil(price));
        factory.addToSupply(factory.resource(), -amount);
      });
    }
  }
}
