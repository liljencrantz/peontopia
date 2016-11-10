package org.peontopia.models;

import com.google.common.collect.ImmutableList;

import org.peontopia.limits.TimeLimits;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.peontopia.limits.FactoryLimits.workerCount;

/**
 * Created by axel on 15/10/16.
 */
public class World {

  private long nextId = 0;

  private final Map<Long, Actor> actors;

  private final int width;
  private final int height;
  private final List<Tile> tiles;

  private boolean frozen = false;
  private long time = 0;

  public World(int width, int height) {
    this.width = width;
    this.height = height;
    this.tiles = ImmutableList.copyOf(IntStream.range(0, width*height)
        .boxed()
        .map(Tile::new)
        .collect(Collectors.toList()));

    actors = new HashMap<>();
  }

  public Peon peon(long id) {
    return (Peon)actors.get(id);
  }

  public Store store(long id) {
    return (Store)actors.get(id);
  }

  public Factory factory(long id) {
    return (Factory)actors.get(id);
  }

  public long time() {
    return time;
  }

/*  @Override
  public Collection<Peon> peons() {
    return Collections.unmodifiableCollection(peons.values());
  }
*/
  public Peon addPeon(int x, int y) {
    checkCoordinate(x, y);
    return new Peon(this, x, y);
  }

  public Store addStore(int x, int y) {
    checkCoordinate(x, y);
    return new Store(this, x, y);
  }

  public Factory addFactory(int x, int y, Resource resource) {
    checkCoordinate(x, y);
    return new Factory(this, x, y, resource);
  }

  public void checkCoordinate(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      throw new IndexOutOfBoundsException();
    }
  }

  public Collection<Actor> actors() {
    return Collections.unmodifiableCollection(actors.values());
  }
/*
  @Override
  public Collection<Store> companies() {
    return Collections.unmodifiableCollection(companies.values());
  }

  @Override
  public Collection<Factory> buildings() {
    return Collections.unmodifiableCollection(buildings.values());
  }
*/
  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public List<Tile> tiles() {
    return tiles;
  }

  public Tile tile(int x, int y) {
    checkCoordinate(x, y);
    return tiles().get(x + width()*y);
  }

  public World addTime(int dt) {

    /** We do not support time travel */
    if (dt < 0)
      throw new InputMismatchException();
    time += dt;

    return this;
  }

  public boolean remove(Actor actor) {
    return actors.remove(actor.id()) != null;
  }

  public long nextId() {
    return nextId++;
  }

  public void add(Actor actor) {
    actors.put(actor.id(), actor);
  }

  public int day() {
    return (int)(time() / TimeLimits.TICKS_IN_DAY);
  }
}
