package org.peontopia.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.peontopia.limits.FactoryLimits.workerCount;

/**
 * Created by axel on 2016-11-10.
 */
public class MutableFactory extends MutableCompany implements Factory {

  private final Map<String, Double> supply = new HashMap<>();
  private final Resource resource;
/*
  public MutableFactory(MutableWorld world, Factory b) {
    super(world, b);
    resource = b.resource();
    supply.put(resource.name(), b.supply(resource));
    resource
        .ingredients()
        .stream()
        .forEach(
            ingredient -> supply.put(
                ingredient.resource().name(),
                b.supply(ingredient.resource())));
  }
*/
  public MutableFactory(MutableWorld world, int x, int y, Resource resource) {
    super(world, x, y);
    checkNotNull(resource);
    this.resource = resource;
    supply.put(resource.name(), 0.0);
    resource
        .ingredients()
        .stream()
        .forEach(ingredient -> supply.put(ingredient.resource().name(), 0.0));
  }

  public void remove() {
    world().tile(x(), y()).building = Optional.empty();
    world().remove(this);
  }

  @Override
  public Resource resource() {
    return resource;
  }

  @Override
  public double supply(Resource r) {
    return supply.get(r.name());
  }

  public MutableFactory addToSupply(Resource r, double amount) {
    double current = supply.get(r.name());
    double updated = current + amount;
//      checkArgument(updated >= 0.0);
    supply.put(r.name(), updated);
    return this;
  }

  @Override
  public int employeeOpenings(Education education) {
    return education == Education.NONE ? workerCount(resource(), level()) : 0;
  }
}
