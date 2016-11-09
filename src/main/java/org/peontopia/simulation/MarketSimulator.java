package org.peontopia.simulation;

import org.peontopia.models.Resource;
import org.peontopia.simulation.analysis.ResourceAnalysis;

/**
 * This class simulates the price point for various resources on the open market.
 */
public class MarketSimulator {

  private final ResourceAnalysis resourceAnalysis;

  public MarketSimulator(ResourceAnalysis resourceAnalysis, Scheduler scheduler) {
    this.resourceAnalysis = resourceAnalysis;
  }

  public double buyingPrice(Resource r) {
    return resourceAnalysis.getValue(r)*1.1;
  }

  public double sellingPrice(Resource r) {
    return resourceAnalysis.getValue(r)*0.9;
  }

  public void step() {
  }

}
