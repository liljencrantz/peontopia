package org.peontopia.simulation;

import org.peontopia.models.Actor;
import org.peontopia.models.Factory;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.Resource;
import org.peontopia.models.World;
import org.peontopia.simulation.actions.Action;
import org.peontopia.simulation.analysis.FactoryAnalysis;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.peontopia.limits.FactoryLimits.calculateFactoryThroughput;
import static org.peontopia.simulation.actions.Action.delay;

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
  private final FactoryAnalysis analysis;
  private final MarketSimulator marketSimulator;

  public FactorySimulator(Action.FactoryActions actions, FactoryAnalysis analysis, MarketSimulator marketSimulator) {
    this.actions = actions;
    this.analysis = analysis;
    this.marketSimulator = marketSimulator;
  }

  @Override
  public Action step(Actor a) {
    return step((MutableWorld.MutableFactory) a);
  }

  private double weeklyProduction(Factory factory) {
    return calculateFactoryThroughput(factory.resource(), factory.level()) *
        World.TICKS_IN_DAY * 7;
  }

  private double weeklySupply(Factory factory, Resource.Ingredient ingredient) {
    return weeklyProduction(factory) * ingredient.amount();
  }

  public Action step(MutableWorld.MutableFactory f) {
    if (f.money() < 0)
      return actions.bankrupt(f);

    /* Whenever a factory has too little of an input good to run for one week at full capacity, two
       weeks worth of that good will be purchased on the open market. */
    List<Action> missingResources = f.resource()
        .ingredients()
        .stream()
        .filter(ingredient -> f.supply(ingredient.resource()) < weeklySupply(f, ingredient))
        .map(ingredient -> actions.purchase(marketSimulator, f, ingredient.resource(), 2.0 *
            weeklySupply(f, ingredient)))
        .collect(toList());

    if (!missingResources.isEmpty()) {
      return Action.compose(missingResources);
    }

    /* Whenever a factory has more than one week of output at full capacity, it is sold on the open
       market. */
    if (f.supply(f.resource()) > 2.0 * weeklyProduction(f)) {
      return actions.sell(marketSimulator, f, f.supply(f.resource()));
    }

    // Do nothing for one day
    return delay(() -> true, World.TICKS_IN_DAY);
  }

}
