package org.peontopia.limits;

import org.peontopia.models.Resource;

import static java.lang.Math.pow;

/**
 * Created by axel on 26/10/16.
 */
public class FactoryLimits {

  private static final int BASE_WORKER_COUNT = 40;
  private static final double EXPONENT_WORKER = 3;

  /** For every increment of speed in resource extraction/manufacturing, throughput goes up by this factor */
  public static final double RESOURCE_SPEED_EXPONENT = 5;
  /** For every factory level, manufacturing throughput goes up by this factor */
  private static final double FACTORY_LEVEL_EXPONENT = 2;
  /** If an imaginary worker worked every tick of an entire day in a low level (1) factory
   * manufacturing an average (3) speed resource, this is how much he would end up with */
  private static final double BASE_MANUFACTURING_RATE = 1;

  /** Manufacturing throughput in units per tick and worker */
  public static double calculateWorkerThroughput(Resource resource, double factoryLevel) {
    return
        BASE_MANUFACTURING_RATE
            * pow(RESOURCE_SPEED_EXPONENT, resource.speed())
            * pow(FACTORY_LEVEL_EXPONENT, factoryLevel)
            / TimeLimits.TICKS_IN_DAY / pow(RESOURCE_SPEED_EXPONENT, 3) / pow(FACTORY_LEVEL_EXPONENT, 1);
  }

  public static double calculateFactoryThroughput(Resource resource, double factoryLevel) {
    return calculateWorkerThroughput(resource, factoryLevel) * workerCount(resource, factoryLevel);
  }

  public static int workerCount(Resource resource, int factoryLevel) {
    return (int)workerCount(resource, (double)factoryLevel);
  }

  public static double workerCount(Resource resource, double factoryLevel) {
    return pow(EXPONENT_WORKER, factoryLevel)* BASE_WORKER_COUNT;
  }
}
