package org.peontopia.simulation.analysis;

import org.peontopia.models.Resource;
import org.peontopia.models.World;

import static java.lang.Math.pow;

/**
 * Analyse various factors pertaining to operation of a factory
 */
public class FactoryAnalysis {

  /** For every increment of speed in resource extraction/manufacturing, throughput goes up by this factor */
  public static final double RESOURCE_SPEED_EXPONENT = 5;
  /** For every factory level, manufacturing throughput goes up by this factor */
  private static final double FACTORY_LEVEL_EXPONENT = 2;
  /** If an imaginary worker worked every tick of an entire day in a low level (1) factory
   * manufacturing an average (3) speed resource, this is how much he would end up with */
  private static final double BASE_MANUFACTURING_RATE = 1;
  private static final int BASE_WORKER_COUNT = 4;
  private static final double EXPONENT_WORKER = 3;

  /** Manufacturing throughput in units per tick and worker */
  public double calculateWorkerThroughput(Resource resource, double factoryLevel) {
    return
        BASE_MANUFACTURING_RATE
            * pow(RESOURCE_SPEED_EXPONENT, resource.speed())
            * pow(FACTORY_LEVEL_EXPONENT, factoryLevel)
            / World.TICKS_IN_DAY / pow(RESOURCE_SPEED_EXPONENT, 3) / pow(FACTORY_LEVEL_EXPONENT, 1);
  }

  public int workerCount(Resource resource, int factoryLevel) {
    return (int)workerCount(resource, (double)factoryLevel);
  }

  public double workerCount(Resource resource, double factoryLevel) {
    return pow(EXPONENT_WORKER, factoryLevel)* BASE_WORKER_COUNT;
  }

  public double calculateFactoryThroughput(Resource resource, double factoryLevel) {
    return calculateWorkerThroughput(resource, factoryLevel) * workerCount(resource, factoryLevel);
  }
}
