package org.peontopia.models;

import java.util.Optional;
import java.util.Set;

/**
 * Created by axel on 15/10/16.
 */
public interface Tile {
  Optional<Building> building();
  Optional<Road> road();
  Set<? extends Peon> peons();
}
