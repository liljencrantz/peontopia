package org.peontopia;

import org.peontopia.loaders.ResourceLoader;
import org.peontopia.models.ActorMapper;
import org.peontopia.models.MutableWorld;
import org.peontopia.models.Resource;
import org.peontopia.simulation.ActorSimulator;
import org.peontopia.simulation.FactorySimulator;
import org.peontopia.simulation.MarketSimulator;
import org.peontopia.simulation.PeonSimulator;
import org.peontopia.simulation.Simulation;
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
  public static Simulation create(int w, int h) throws IOException {
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
    MarketSimulator marketSimulator = new MarketSimulator(resourceAnalysis);

    MutableWorld world = new MutableWorld(w, h);

    ActorMapper<ActorSimulator> actorSimulator = ActorMapper.<ActorSimulator>builder()
        .factory(new FactorySimulator(new Action.FactoryActions(), factoryAnalysis, marketSimulator))
        .peon(new PeonSimulator(new Action.PeonActions()))
        .store((a) -> () -> true)
        .build();

    world.addPeon(3,3);
    world.addFactory(5,5, resources.get("Glass"));

    return new Simulation(
        world,
        actorSimulator,
        marketSimulator);
  }

}
