package org.peontopia.models;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by axel on 2016-11-10.
 */
public abstract class Company extends Building {

  int START_MONEY = 100000;

  private long id;
  private int money;
  private Set<Peon> employees = new HashSet<>();

  public Company(World world, int x, int y) {
    super(world, x, y, 0);
    this.money = START_MONEY;
    world.add(this);
    world.tile(x, y).building = Optional.of(this);
  }

  public long id() {
    return id;
  }


  public int money() {
    return money;
  }

  public Collection<Peon> employees() {
    return ImmutableSet.copyOf(employees);
  }

  public Company addEmployee(Peon p) {
    employees.add(p);
    p.employer(this);
    return this;
  }

  public Company addMoney(int v) {
    money += v;
    return this;
  }

  public abstract int employeeOpenings(Education education);

}