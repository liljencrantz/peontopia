package org.peontopia.models;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by axel on 2016-11-10.
 */
public abstract class MutableCompany extends MutableActor implements Company {
  private long id;
  private int x;
  private int y;
  private int money;
  private Set<Peon> employees = new HashSet<>();

/*  public MutableCompany(MutableWorld world, MutableCompany b) {
    super(b.world());
    this.id = b.id();
    this.x = b.x();
    this.y = b.y();
    this.money = b.money();

    world.actors.put(id(), this);
    world.tile(x, y).building = Optional.of(this);
  }
*/
  public MutableCompany(MutableWorld world, int x, int y) {
    super(world);
    this.id = world.nextId();
    this.x = x;
    this.y = y;
    this.money = Company.START_MONEY;
    world.add(this);
    world.tile(x, y).building = Optional.of(this);
  }

  @Override
  public long id() {
    return id;
  }

  @Override
  public int x() {
    return x;
  }

  @Override
  public int y() {
    return y;
  }

  @Override
  public int level() {
    return 1;
  }

  @Override
  public int money() {
    return money;
  }

  @Override
  public Collection<Peon> employees() {
    return ImmutableSet.copyOf(employees);
  }

  public MutableCompany addEmployee(Peon p) {
    employees.add(p);
    p.employer(this);
    return this;
  }

  public MutableCompany addMoney(int v) {
    money += v;
    return this;
  }

}
