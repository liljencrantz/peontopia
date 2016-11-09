package org.peontopia.simulation;

import org.peontopia.simulation.actions.Action;
import org.peontopia.timer.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.peontopia.timer.Timer.timer;

/**
 * The coordination that locks all the pieces of the simulation (peon ai, company ai, water, power,
 * etc) together in the correct way
 */
public class Scheduler implements ActionScheduler {

  public static int SCHEDULE_MAX = 1024;

  private List<Action>[] schedule = new List[SCHEDULE_MAX];
  private int scheduleIndex = 0;
  private long steps = 0;
  private long actionCount = 0;

  private List<Runnable> mutations = new ArrayList<>();

  public Scheduler() {
    for(int i=0; i<SCHEDULE_MAX; i++) {
      schedule[i] = new ArrayList<>();
    }
  }

  @Override
  public void schedule(Action action) {
//    System.err.println("Schedule action in " + action.delay() + " steps");
    checkNotNull(action);
    checkArgument(action.delay() < SCHEDULE_MAX);
    schedule[(scheduleIndex+action.delay()) % SCHEDULE_MAX].add(action);
  }

  public Scheduler step() {
    steps++;
    mutations.clear();
    List<Action> actions = schedule[scheduleIndex];
  //  System.err.println("We have " + actions.size() + " actions lined up for this step");
    schedule[scheduleIndex] = new ArrayList<>();
    scheduleIndex = (scheduleIndex + 1) % SCHEDULE_MAX;
    timer("shuffle").time(() -> Collections.shuffle(actions));
//    timer("actions").time(() -> actions.forEach(action ->
  //      action.run().ifPresent(nextAction -> schedule(nextAction))));
    timer("actions").time(() -> actions.forEach(action ->
    {
      actionCount++;
      Optional<Action> next = action.run();
//      System.err.println("Ran one action. Do we have more? " + next.isPresent());
      next.ifPresent(nextAction -> schedule(nextAction));
    }));

    System.err.println(String.format("We do %.2f actions per step", (double)actionCount/steps));
        //Timer.print();
    return this;
  }

}
