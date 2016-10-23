package org.peontopia.simulation;

import org.peontopia.models.Actor;
import org.peontopia.models.Factory;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;

/**
 * Simulate the actions of a factory. A fair bit of the simulation of a factory is actually
 * performed by the peons working in the factory, that is the entire manufacturing process as
 * well as finding available positions within the company.
 *
 * What's left for the factory simulation is the procurement of goods, selling of manufactured goods
 * and administrative tasks like upgrading the factory, firing underperforming workers, going
 * bankrupt, etc.
 */
public class FactorySimulator implements ActorSimulator {

  private final Action.FactoryActions actions;

  public FactorySimulator(Action.FactoryActions actions) {
    this.actions = actions;
  }

  @Override
  public Action step(World w, Actor a) {
    return step(w, (Factory) a);
  }

  public Action step(World w, Factory f) {
    if (f.money() < 0)
      return actions.bankrupt(f);
    return world -> true;
  }

}
