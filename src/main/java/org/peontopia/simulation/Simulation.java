package org.peontopia.simulation;

import com.google.common.collect.Lists;

import org.peontopia.models.MutableWorld;
import org.peontopia.models.Peon;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by axel on 14/10/16.
 */
public class Simulation {

  private World world;
  private final PeonSimulator peonSimulator;
  private final Map<Long, Action> currentAction = new HashMap<>();

  public Simulation(World world, PeonSimulator peonSimulator) {
    this.world = world;
    this.peonSimulator = peonSimulator;
  }

  public World step() {
    MutableWorld nextWorld = MutableWorld.thaw(world);

    world.actors().stream()
        .filter(p -> !currentAction.containsKey(p.id()))
        .forEach(actor -> {
          if (actor instanceof Peon) {
            Peon p = (Peon) actor;

            Action a = peonSimulator.step(world, p);
            if (a == null) {
              throw new RuntimeException("PeonSimulator returned null");
            }
            currentAction.put(p.id(), a);
          }
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
