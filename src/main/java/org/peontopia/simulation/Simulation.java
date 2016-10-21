package org.peontopia.simulation;

import com.google.common.collect.Lists;

import org.peontopia.models.ActorMapper;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main glue that binds all the pieces of the simulation (peon ai, company ai, water, power,
 * etc) together
 */
public class Simulation {

  private World world;
  private final ActorMapper<ActorSimulator> actorSimulator;
  private final Map<Long, Action> currentAction = new HashMap<>();
  private final MarketSimulator marketSimulator;

  public Simulation(World world, ActorMapper<ActorSimulator> actorSimulator, MarketSimulator marketSimulator) {
    this.world = world;
    this.actorSimulator = actorSimulator;
    this.marketSimulator = marketSimulator;
  }

  public World step() {
    marketSimulator.step();
    MutableWorld nextWorld = MutableWorld.thaw(world);

    world.actors().stream()
        .filter(p -> !currentAction.containsKey(p.id()))
        .forEach(actor -> {
            Action a = actorSimulator.get(actor).step(world, actor);
            if (a == null) {
              throw new RuntimeException("PeonSimulator returned null");
            }
            currentAction.put(actor.id(), a);
        });

    List<Map.Entry<Long, Action>> actions = Lists.newArrayList(currentAction.entrySet());

    Collections.shuffle(actions);

    actions.stream()
        .forEach(a -> {
          if(a.getValue().apply(nextWorld))
            currentAction.remove(a.getKey());
        });

    nextWorld.addTime(1);
    world = nextWorld;
    return nextWorld.freeze();
  }

}
