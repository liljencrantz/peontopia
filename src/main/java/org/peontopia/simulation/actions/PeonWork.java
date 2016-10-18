package org.peontopia.simulation.actions;

import org.peontopia.models.MutableWorld;

/**
 * Created by axel on 17/10/16.
 */
public class PeonWork implements Action{

  private final long id;

  public PeonWork(long id) {
    this.id = id;
  }

  @Override
  public boolean apply(MutableWorld world) {
    return true;
  }
}
