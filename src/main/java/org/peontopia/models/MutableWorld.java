package org.peontopia.models;

import com.google.common.collect.ImmutableList;

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
public class MutableWorld implements World {

  private long nextId = 0;

  private final Map<Long, Actor> actors;

  private final int width;
  private final int height;
  private final List<MutableTile> tiles;

  private boolean frozen = false;
  private long time = 0;

  public MutableWorld(int width, int height) {
    this.width = width;
    this.height = height;
    this.tiles = ImmutableList.copyOf(IntStream.range(0, width*height)
        .boxed()
        .map(MutableTile::new)
        .collect(Collectors.toList()));

    actors = new HashMap<>();
  }

  public boolean frozen() {
    return frozen;
  }

  public World freeze() {
    this.frozen = true;
    return this;
  }

  @Override
  public Peon peon(long id) {
    return (Peon)actors.get(id);
  }

  @Override
  public MutableStore store(long id) {
    return (MutableStore)actors.get(id);
  }

  @Override
  public MutableFactory factory(long id) {
    return (MutableFactory)actors.get(id);
  }

  @Override
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

  public MutableStore addStore(int x, int y) {
    checkCoordinate(x, y);
    return new MutableStore(this, x, y);
  }

  public MutableFactory addFactory(int x, int y, Resource resource) {
    checkCoordinate(x, y);
    return new MutableFactory(this, x, y, resource);
  }

  public void checkCoordinate(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
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
  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  @Override
  public List<MutableTile> tiles() {
    return tiles;
  }

  public MutableTile tile(int x, int y) {
    checkCoordinate(x, y);
    return tiles().get(x + width()*y);
  }

  public MutableWorld addTime(int dt) {

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
}
