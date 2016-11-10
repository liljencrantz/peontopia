package org.peontopia.models;

import org.peontopia.limits.PeonLimits;

import java.util.Optional;

/**
 * Created by axel on 2016-11-10.
 */
public class Peon extends Actor {

  private long id;
  private String name;
  private double happiness;
  private int money;
  private int rest;
  private int food;
  private int x;
  private int y;

  private Education education;
  Optional<Company> employer;

  public Peon employer(Company employer) {
    this.employer = Optional.of(employer);
    return this;
  }

  public Peon removeEmployer() {
    this.employer = Optional.empty();
    return this;
  }


  public Peon(World world, int x, int y) {
    super(world);
    this.id = world.nextId();
    this.name = "Bengt";
    this.x = x;
    this.y = y;
    this.money = PeonLimits.START_MONEY;
    this.rest = PeonLimits.MAX_REST;
    this.food = PeonLimits.MAX_FOOD;
    this.happiness = 0;
    this.education = Education.NONE;
    this.employer = Optional.empty();

    world().add(this);
    world().tile(x, y).peons.add(this);
  }

  public void remove() {
    world().remove(this);
    tile().peons.remove(this);
  }

  public long id() {
    return id;
  }

  public String name() {
    return name;
  }

  public double happiness() {
    return happiness;
  }

  public Peon happiness(double v) {
    happiness = v;
    return this;
  }

  public Peon addHappiness(double v) {
    happiness += v;
    return this;
  }

  public int money() {
    return money;
  }

  public Peon money(int v) {
    money = v;
    return this;
  }

  public Peon addMoney(int v) {
    money += v;
    return this;
  }

  public int rest() {
    return rest;
  }

  public Peon rest(int v) {
    if (v < 0 || v > PeonLimits.MAX_REST)
      throw new IllegalArgumentException();
    rest = v;
    return this;
  }

  public Peon addRest(int v) {
    rest = Math.max(0, Math.min(rest + v, PeonLimits.MAX_REST));
    return this;
  }

  public int food() {
    return food;
  }

  public Peon food(int v) {
    food = v;
    return this;
  }

  public Peon addFood(int v) {
    food = Math.max(0, Math.min(food + v, PeonLimits.MAX_FOOD));
    return this;
  }

  public int x() {
    return x;
  }

  public Peon x(int v) {
    world().checkCoordinate(v, y);
    world().tile(x, y).peons.remove(this);
    x = v;
    world().tile(x, y).peons.add(this);
    return this;
  }

  public Peon addX(int v) {
    world().checkCoordinate(x + v, y);
    world().tile(x, y).peons.remove(this);
    x += v;
    world().tile(x, y).peons.add(this);
    return this;
  }

  public int y() {
    return y;
  }

  public Education education() {
    return education;
  }

  public Peon education(Education education) {
    this.education = education;
    return this;
  }

  public Optional<Company> employer() {
    return employer;
  }

  public Tile tile() {
    return world().tile(x(), y());
  }

  public Peon y(int v) {
    world().checkCoordinate(x, v);
    world().tile(x, y).peons.remove(this);
    y = v;
    world().tile(x, y).peons.add(this);

    return this;
  }

  public Peon addY(int v) {
    world().checkCoordinate(x, y + v);
    world().tile(x, y).peons.remove(this);
    y += v;
    world().tile(x, y).peons.add(this);
    return this;
  }

}
