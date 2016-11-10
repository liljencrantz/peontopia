package org.peontopia.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by axel on 2016-11-10.
 */
public class Tile implements Tile {

  int idx;
  Set<Peon> peons;
  Optional<Building> building = Optional.empty();

  Tile(int idx) {
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
  public Set<Peon> peons() {
    return Collections.unmodifiableSet(peons);
  }
}
