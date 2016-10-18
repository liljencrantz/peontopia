package org.peontopia.models;

import java.util.Collection;

/**
 * Created by axel on 18/10/16.
 */
public class Resource {

  private final Collection<Ingredient> ingredients;
  private final String name;

  public Resource(Collection<Ingredient> ingredients, String name) {
    this.ingredients = ingredients;
    this.name = name;
  }

  public String name() {
    return name;
  }

  public Collection<Ingredient> ingredients() {
    return ingredients;
  }

  class Ingredient {
    private final int amount;
    private final Resource resource;

    Ingredient(int amount, Resource resource) {
      this.amount = amount;
      this.resource = resource;
    }

    public int amount() {
      return amount;
    }

    public Resource resource() {
      return resource;
    }
  }
}
