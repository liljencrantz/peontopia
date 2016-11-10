package org.peontopia;

import org.peontopia.loaders.ResourceLoader;
import org.peontopia.models.World;
import org.peontopia.models.Resource;
import org.peontopia.simulation.FactorySimulator;
import org.peontopia.simulation.MarketSimulator;
import org.peontopia.simulation.PeonSimulator;
import org.peontopia.simulation.Scheduler;
import org.peontopia.simulation.actions.Action;
import org.peontopia.simulation.analysis.FactoryAnalysis;
import org.peontopia.simulation.analysis.ResourceAnalysis;
import org.peontopia.simulation.analysis.SalaryAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Construct all of the simulation pieces and pass them along to the main simulation class.
 */
public class Game {

  private final World world;
  private final Scheduler scheduler;
  private final Map<String, Resource> resources;
  private final MarketSimulator market;

  private final PeonSimulator peonSimulator;
  private final FactorySimulator factorySimulator;

  public Game(World world, Scheduler scheduler, Map<String, Resource> resources, MarketSimulator market, PeonSimulator peonSimulator, FactorySimulator factorySimulator) {
    this.world = world;
    this.scheduler = scheduler;
    this.resources = resources;
    this.market = market;
    this.peonSimulator = peonSimulator;
    this.factorySimulator = factorySimulator;
  }

  public static Game create(int w, int h) throws IOException {
    Scheduler scheduler = new Scheduler();
    ResourceLoader loader = new ResourceLoader();

    InputStream in = Game.class.getResourceAsStream("/resources.json");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    Map<String, Resource> resources = loader.load(reader);
    reader.close();

    FactoryAnalysis factoryAnalysis = new FactoryAnalysis();
    SalaryAnalysis salaryAnalysis = new SalaryAnalysis();

    ResourceAnalysis resourceAnalysis = new ResourceAnalysis(
        resources, 1.0,
        factoryAnalysis,
        salaryAnalysis);

    MarketSimulator marketSimulator = new MarketSimulator(resourceAnalysis, scheduler);
    PeonSimulator peonSimulator = new PeonSimulator(new Action.PeonActions());
    FactorySimulator factorySimulator = new FactorySimulator(new Action.FactoryActions(), factoryAnalysis, marketSimulator);

    World world = new World(w, h);

    return new Game(world, scheduler, resources, marketSimulator, peonSimulator, factorySimulator);
  }


  public World world() {
    return world;
  }

  public Scheduler scheduler() {
    return scheduler;
  }

  public Map<String, Resource> resources() {
    return resources;
  }

  public MarketSimulator market() {
    return market;
  }

  public PeonSimulator peonSimulator() {
    return peonSimulator;
  }

  public FactorySimulator factorySimulator() {
    return factorySimulator;
  }

  public void step() {
    world.addTime(1);
    scheduler.step();
  }
}
