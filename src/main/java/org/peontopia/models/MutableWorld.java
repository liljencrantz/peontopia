package org.peontopia.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

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

  public MutableStore addStore(int x, int y) {
    check.check();
    checkCoordinate(x, y);
    return new MutableStore(x, y);
  }

  public MutableFactory addFactory(int x, int y, Resource resource) {
    check.check();
    checkCoordinate(x, y);
    return new MutableFactory(x, y, resource);
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

    /** We do not support time travel */
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

  public class MutableActor {
    public MutableWorld world() {
      return MutableWorld.this;
    }
  }

  public class MutablePeon extends MutableActor implements Peon {

    private long id;
    private String name;
    private double happiness;
    private int money;
    private int rest;
    private int food;
    private int x;
    private int y;

    private Education education;

    public MutablePeon employer(MutableCompany employer) {
      this.employer = Optional.of(employer);
      return this;
    }

    public MutablePeon removeEmployer() {
      this.employer = Optional.empty();
      return this;
    }

    Optional<MutableCompany> employer;

    public MutablePeon(Peon p) {
      this.id = p.id();
      this.name = p.name();
      this.x = p.x();
      this.y = p.y();
      this.money = p.money();
      this.rest = p.rest();
      this.food = p.food();
      this.happiness = p.happiness();
      this.education = p.education();

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
      this.education = Education.NONE;
      this.employer = Optional.empty();

      actors.put(id(), this);
      MutableWorld.this.tile(x, y).peons.add(this);
    }

    public void remove() {
      actors.remove(this.id());
      tile().peons.remove(this);
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
      food = Math.max(0, Math.min(food + v, MAX_FOOD));
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
    public Education education() {
      return education;
    }

    public MutablePeon education(Education education) {
      this.education = education;
      return this;
    }

    @Override
    public Optional<MutableCompany> employer() {
      return employer;
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

  public abstract class MutableCompany extends MutableActor implements Company {
    private long id;
    private int x;
    private int y;
    private int money;
    private Set<Peon> employees = new HashSet<>();

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

    public MutableCompany addEmployee(MutablePeon p) {
      employees.add(p);
      p.employer(this);
      return this;
    }

    public MutableCompany addMoney(int v) {
      check.check();
      money += v;
      return this;
    }

  }

  public class MutableFactory extends MutableCompany implements Factory {

    private final Map<String, Double> supply = new HashMap<>();
    private final Resource resource;

    public MutableFactory(Factory b) {
      super(b);
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

    public MutableFactory(int x, int y, Resource resource) {
      super(x, y);
      checkNotNull(resource);
      this.resource = resource;
      supply.put(resource.name(), 0.0);
      resource
          .ingredients()
          .stream()
          .forEach(ingredient -> supply.put(ingredient.resource().name(), 0.0));
    }

    public void remove() {
      tile(x(), y()).building = Optional.empty();
      actors.remove(this);
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
      check.check();
      double current = supply.get(r.name());
      double updated = current + amount;
//      checkArgument(updated >= 0.0);
      supply.put(r.name(), updated);
      return this;
    }

    @Override
    public int employeeOpenings(Education education) {
        return education==Education.NONE ? workerCount(resource(), level()) : 0;
    }
  }

  class MutableStore extends MutableCompany implements Store {

    public MutableStore(Store b) {
      super(b);
    }

    public MutableStore(int x, int y) {
      super(x, y);
    }

    @Override
    public int employeeOpenings(Education education) {
      return 0;
    }

  }

}
