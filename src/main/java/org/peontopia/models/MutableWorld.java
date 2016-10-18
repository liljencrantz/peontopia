package org.peontopia.models;

import com.google.common.collect.ImmutableList;

import org.peontopia.collections.FreezeCheck;
import org.peontopia.collections.ModifyFrozenException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
  private final FreezeCheck check = () -> {if(frozen) throw new ModifyFrozenException();};
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

  /** Returns a mutable deep copy of the specified world
   *
   * @param template
   * @return A mutable copy of the specified game world
   */
  public static MutableWorld thaw(World template) {
    MutableWorld res = new MutableWorld(template.width(), template.height());
    res.time = template.time();

    res.actors.putAll(
        template.actors()
            .stream()
            .map(res::thawActor)
            .collect(Collectors.toMap(p -> p.id(), Function.identity())));

    return res;
  }

  public Actor thawActor(Actor a) {
    if (a instanceof Peon)
      return new MutablePeon((Peon)a);
    if (a instanceof Store)
      return new MutableStore((Store)a);
    if (a instanceof Factory)
      return new MutableFactory((Factory)a);
    throw new RuntimeException("Unknown actor type " + a.getClass().toString());
  }

  public MutableFactory thawBuilding(Factory f) {
    return new MutableFactory(f);
  }

  public MutableStore thawCompany(Store f) {
    return new MutableStore(f);
  }

  public boolean frozen() {
    return frozen;
  }

  public World freeze() {
    this.frozen = true;
    return this;
  }

  @Override
  public MutablePeon peon(long id) {
    return (MutablePeon)actors.get(id);
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
  public Collection<MutablePeon> peons() {
    return Collections.unmodifiableCollection(peons.values());
  }
*/
  public MutablePeon addPeon(int x, int y) {
    check.check();
    checkCoordinate(x, y);
    return new MutablePeon(x, y);
  }

  public MutableWorld removePeon(long id) {
    MutablePeon peon = peon(id);
    actors.remove(id);
    peon.tile().peons.remove(peon);
    return this;
  }

  public MutableStore addStore(int x, int y) {
    check.check();
    checkCoordinate(x, y);
    return new MutableStore(x, y);
  }

  public MutableFactory addFactory(int x, int y) {
    check.check();
    checkCoordinate(x, y);
    return new MutableFactory(x, y);
  }

  private void checkCoordinate(int x, int y) {
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
    if (dt < 0)
      throw new InputMismatchException();
    time += dt;

    return this;
  }

  public class MutableTile implements Tile {

    int idx;
    Set<MutablePeon> peons;
    Optional<Building> building = Optional.empty();

    MutableTile(int idx) {
      this.idx = idx;
      peons = new HashSet();
    }

    @Override
    public Optional<Building> building() {
      return building;
    }

    @Override
    public Optional<Road> road() {
      return Optional.empty();
    }

    @Override
    public Set<MutablePeon> peons() {
      return Collections.unmodifiableSet(peons);
    }
  }

  public class MutablePeon implements Peon {

    private long id;
    private String name;
    private double happiness;
    private int money;
    private int rest;
    private int food;
    private int x;
    private int y;

    public MutablePeon(Peon p) {
      this.id = p.id();
      this.name = p.name();
      this.x = p.x();
      this.y = p.y();
      this.money = p.money();
      this.rest = p.rest();
      this.food = p.food();
      this.happiness = p.happiness();

      actors.put(id(), this);
      MutableWorld.this.tile(x, y).peons.add(this);
    }

    public MutablePeon(int x, int y) {
      this.id = nextId++;
      this.name = "Bengt";
      this.x = x;
      this.y = y;
      this.money = START_MONEY;
      this.rest = MAX_REST;
      this.food = MAX_FOOD;
      this.happiness = 0;

      actors.put(id(), this);
      MutableWorld.this.tile(x, y).peons.add(this);
    }

    @Override
    public long id() {
      return id;
    }

    @Override
    public String name() {
      return name;
    }

    @Override
    public double happiness() {
      return happiness;
    }

    public MutablePeon happiness(double v) {
      check.check();
      happiness = v;
      return this;
    }

    public MutablePeon addHappiness(double v) {
      check.check();
      happiness += v;
      return this;
    }

    @Override
    public int money() {
      return money;
    }

    public MutablePeon money(int v) {
      check.check();
      money = v;
      return this;
    }

    public MutablePeon addMoney(int v) {
      check.check();
      money += v;
      return this;
    }

    @Override
    public int rest() {
      return rest;
    }

    public MutablePeon rest(int v) {
      check.check();
      if (v < 0 || v > MAX_REST)
        throw new IllegalArgumentException();
      rest = v;
      return this;
    }

    public MutablePeon addRest(int v) {
      check.check();
      rest = Math.max(0, Math.min(rest + v, MAX_REST));
      return this;
    }

    @Override
    public int food() {
      return food;
    }

    public MutablePeon food(int v) {
      check.check();
      food = v;
      return this;
    }

    public MutablePeon addFood(int v) {
      check.check();
      food += v;
      return this;
    }

    @Override
    public int x() {
      return x;
    }

    public MutablePeon x(int v) {
      checkCoordinate(v, y);
      check.check();
      MutableWorld.this.tile(x, y).peons.remove(this);
      x = v;
      MutableWorld.this.tile(x, y).peons.add(this);
      return this;
    }

    public MutablePeon addX(int v) {
      checkCoordinate(x+v, y);
      check.check();
      MutableWorld.this.tile(x,y).peons.remove(this);
      x += v;
      MutableWorld.this.tile(x, y).peons.add(this);
      return this;
    }

    @Override
    public int y() {
      return y;
    }

    @Override
    public MutableTile tile() {
      return MutableWorld.this.tile(x(), y());
    }

    public MutablePeon y(int v) {
      checkCoordinate(x, v);
      check.check();
      MutableWorld.this.tile(x, y).peons.remove(this);
      y = v;
      MutableWorld.this.tile(x, y).peons.add(this);

      return this;
    }

    public MutablePeon addY(int v) {
      checkCoordinate(x, y+v);
      check.check();
      MutableWorld.this.tile(x,y).peons.remove(this);
      y += v;
      MutableWorld.this.tile(x, y).peons.add(this);
      return this;
    }

  }

  class MutableCompany implements Company {
    private long id;
    private int x;
    private int y;
    private int money;

    public MutableCompany(Company b) {
      this.id = b.id();
      this.x = b.x();
      this.y = b.y();
      this.money = b.money();

      actors.put(id(), this);
      tile(x, y).building = Optional.of(this);
    }

    public MutableCompany(int x, int y) {
      this.id = nextId++;
      this.x = x;
      this.y = y;
      this.money = Company.START_MONEY;
      actors.put(id(), this);
      tile(x, y).building = Optional.of(this);
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
    public int money() {
      return money;
    }

    public MutableCompany addMoney(int v) {
      check.check();
      money += v;
      return this;
    }
  }

  class MutableFactory extends MutableCompany implements Factory {

    public MutableFactory(Factory b) {
      super(b);
    }

    public MutableFactory(int x, int y) {
      super(x, y);
    }
  }

  class MutableStore extends MutableCompany implements Store {

    public MutableStore(Store b) {
      super(b);
    }

    public MutableStore(int x, int y) {
      super(x, y);
    }

  }

}
