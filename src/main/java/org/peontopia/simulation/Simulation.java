package org.peontopia.simulation;

import com.google.common.collect.Lists;

import org.peontopia.models.ActorMapper;
import org.peontopia.models.MutableWorld;
import org.peontopia.simulation.actions.Action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The coordination that locks all the pieces of the simulation (peon ai, company ai, water, power,
 * etc) together in the correct way
 */
public class Simulation {

  private MutableWorld world;
  private final ActorMapper<ActorSimulator> actorSimulator;
  private final Map<Long, Action> currentAction = new HashMap<>();
  private final MarketSimulator marketSimulator;

  public Simulation(
      MutableWorld world,
      ActorMapper<ActorSimulator> actorSimulator,
      MarketSimulator marketSimulator) {
    this.world = world;
    this.actorSimulator = actorSimulator;
    this.marketSimulator = marketSimulator;
  }

  public Simulation step() {
    marketSimulator.step();

    world.actors().stream()
        .filter(p -> !currentAction.containsKey(p.id()))
        .forEach(actor -> {
          ActorSimulator sim = actorSimulator.get(actor);
            Action a = sim.step(actor);
            if (a == null) {
              throw new RuntimeException("Simulator " + sim + " returned null");
            }
            currentAction.put(actor.id(), a);
        });

    List<Map.Entry<Long, Action>> actions = Lists.newArrayList(currentAction.entrySet());
    Collections.shuffle(actions);

    /* Let the mutations begin! */

    world.addTime(1);
    actions.stream()
        .forEach(a -> {
          if(a.getValue().apply())
            currentAction.remove(a.getKey());
        });

    return this;
  }

  public MutableWorld world() {
    return world;
  }
}
