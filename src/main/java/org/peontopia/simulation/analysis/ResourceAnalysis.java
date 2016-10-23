package org.peontopia.simulation.analysis;

import org.peontopia.models.Resource;
import org.peontopia.models.World;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.pow;

/**
 * Analyse Resources, including estimating fair market value of a resource given current salaries
 * and current level of technological advancement.
 */
public class ResourceAnalysis {

  private static final double OVERHEAD = 0.2;
  private static final double PROFIT_MARGIN = 0.2;

  private static final double AVAILABILITY_EXPONENT = 4;
  private static final double BASE_AVAILIBILITY_COST = 10;

  private final Map<String, Resource> resources;
  private final Map<String, Analysis> data = new HashMap<>();
  private final double level;
  private final FactoryAnalysis factoryAnalysis;
  private final SalaryAnalysis salaryAnalysis;

  public ResourceAnalysis(
      Map<String, Resource> resources, double level,
      FactoryAnalysis factoryAnalysis, SalaryAnalysis salaryAnalysis) {
    this.resources = resources;
    this.level = level;
    this.factoryAnalysis = factoryAnalysis;
    this.salaryAnalysis = salaryAnalysis;
    analyse();
  }

  public double getValue(Resource r) {
    return data.get(r.name()).value;
  }

  private void analyse() {
    resources.values().stream().forEach(r -> analyse(r));
  }

  private void analyse(Resource r) {
    if (data.containsKey(r.name()))
      return;
    r.ingredients().stream().forEach(ingredient -> analyse(ingredient.resource()));
    double ingredientCost = 0;
    double salaryCost = 0;
    double scarcityCost = 0;

    if (r.ingredients().size() > 0) {
      ingredientCost = r.ingredients().stream().mapToDouble(ingredient -> {
        analyse(ingredient.resource());
        return data.get(ingredient.resource().name()).value * ingredient.amount();
      }).sum();
    } else {
     scarcityCost = BASE_AVAILIBILITY_COST
         * pow(AVAILABILITY_EXPONENT, Resource.MAX_AVAILABILITY-r.availability());
    }

    double timePerUnit = 1.0 / factoryAnalysis.calculateWorkerThroughput(r, level);
    salaryCost = timePerUnit * salaryAnalysis.baseDailySalary(level)/ World.TICKS_IN_DAY;

    double value = (ingredientCost + salaryCost + scarcityCost) * (1.0 + OVERHEAD + PROFIT_MARGIN);
    data.put(r.name(), new Analysis(value));
  }

  private static class Analysis {
    private final double value;

    private Analysis(double value) {
      this.value = value;
    }
  }
}
