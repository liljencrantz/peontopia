package org.peontopia.models;

import java.util.Collection;

/**
 * Created by axel on 18/10/16.
 */
public class Resource {

  private final Collection<Ingredient> ingredients;
  private final String name;
  private final int availability;
  private final int speed;
  private final boolean renewable;
  private final String resourceClass;


  public Resource(
      String name,
      int availability,
      int speed,
      boolean renewable,
      String resourceClass,
      Collection<Ingredient> ingredients) {
    this.availability = availability;
    this.speed = speed;
    this.renewable = renewable;
    this.resourceClass = resourceClass;
    this.ingredients = ingredients;
    this.name = name;
  }

  public String name() {
    return name;
  }

  public Collection<Ingredient> ingredients() {
    return ingredients;
  }

  public int availability() {
    return availability;
  }

  public int speed() {
    return speed;
  }

  public boolean renewable() {
    return renewable;
  }

  public String resourceClass() {
    return resourceClass;
  }

  public static class Ingredient {
    private final double amount;
    private final Resource resource;

    public Ingredient(Resource resource, double amount) {
      this.amount = amount;
      this.resource = resource;
    }

    public double amount() {
      return amount;
    }

    public Resource resource() {
      return resource;
    }
  }
}
